package com.jivosite.sdk.di.modules

import android.content.Context
import com.jivosite.sdk.BuildConfig
import com.jivosite.sdk.logger.DebugLogger
import com.jivosite.sdk.logger.Logger
import com.jivosite.sdk.logger.ReleaseLogger
import com.jivosite.sdk.model.SdkContext
import com.jivosite.sdk.model.storage.SharedStorage
import com.jivosite.sdk.socket.transmitter.DefaultTransmitter
import com.jivosite.sdk.socket.transmitter.Transmitter
import com.jivosite.sdk.support.async.Schedulers
import com.jivosite.sdk.support.async.SchedulersImpl
import dagger.Module
import dagger.Provides
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonVisitor
import io.noties.markwon.linkify.LinkifyPlugin
import org.commonmark.node.Block
import org.commonmark.node.BlockQuote
import org.commonmark.node.FencedCodeBlock
import org.commonmark.node.Heading
import org.commonmark.node.HtmlBlock
import org.commonmark.node.IndentedCodeBlock
import org.commonmark.node.SoftLineBreak
import org.commonmark.node.ThematicBreak
import org.commonmark.parser.Parser
import javax.inject.Singleton

/**
 * Created on 02.09.2020.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
@Module
class SdkModule(appContext: Context) {

    private val sdkContext = SdkContext(appContext)

    @Provides
    @Singleton
    fun provideSdkContext(): SdkContext = sdkContext

    @Provides
    @Singleton
    fun provideSchedulers(): Schedulers = SchedulersImpl()

    @Provides
    @Singleton
    fun provideSharedStorage(): SharedStorage {
        return SharedStorage(sdkContext.appContext)
    }

    @Provides
    @Singleton
    fun provideLogger(debugLogger: DebugLogger): Logger {
        return if (BuildConfig.DEBUG) {
            debugLogger
        } else {
            ReleaseLogger()
        }
    }

    @Provides
    @Singleton
    fun provideTransmitter(transmitter: DefaultTransmitter): Transmitter {
        return transmitter
    }

    @Provides
    @Singleton
    fun provideMarkwon(): Markwon {
        return Markwon.builder(sdkContext.appContext)
            .usePlugin(LinkifyPlugin.create())
            .usePlugin(object : AbstractMarkwonPlugin() {
                override fun configureVisitor(builder: MarkwonVisitor.Builder) {
                    builder.on(SoftLineBreak::class.java) { visitor, _ -> visitor.forceNewLine() }
                }

                override fun configureParser(builder: Parser.Builder) {
                    builder.enabledBlockTypes(
                        mutableSetOf(
                            BlockQuote::class.java,
                            Heading::class.java,
                            FencedCodeBlock::class.java,
                            HtmlBlock::class.java,
                            ThematicBreak::class.java,
                            IndentedCodeBlock::class.java
                        ) as Set<Class<out Block>>?
                    )
                }
            }).build()
    }
}