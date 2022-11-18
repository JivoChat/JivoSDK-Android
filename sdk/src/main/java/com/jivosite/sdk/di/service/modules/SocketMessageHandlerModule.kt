package com.jivosite.sdk.di.service.modules

import com.jivosite.sdk.BuildConfig
import com.jivosite.sdk.di.service.ServiceScope
import com.jivosite.sdk.model.repository.unsupported.UnsupportedRepository
import com.jivosite.sdk.socket.handler.SocketMessageDelegate
import com.jivosite.sdk.socket.handler.SocketMessageHandler
import com.jivosite.sdk.socket.handler.delegates.*
import com.jivosite.sdk.socket.obfuscate.DebugMessageObfuscator
import com.jivosite.sdk.socket.obfuscate.MessageObfuscator
import com.jivosite.sdk.socket.obfuscate.ReleaseMessageObfuscator
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

/**
 * Created on 08.09.2020.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
@Module(includes = [SocketMessageHandlerModule.Bindings::class])
class SocketMessageHandlerModule {

    @Module
    interface Bindings {

        @Binds
        @IntoMap
        @StringKey(AtomMeIdDelegate.TYPE)
        fun provideAtomMeIdDelegate(delegate: AtomMeIdDelegate): SocketMessageDelegate

        @Binds
        @IntoMap
        @StringKey(AtomMeUrlPathDelegate.TYPE)
        fun provideAtomMeUrlPathDelegate(delegate: AtomMeUrlPathDelegate): SocketMessageDelegate

        @Binds
        @IntoMap
        @StringKey(StatusMeUrlPathDelegate.TYPE)
        fun provideStatusMeUrlPathDelegate(delegate: StatusMeUrlPathDelegate): SocketMessageDelegate

        @Binds
        @IntoMap
        @StringKey(AtomMeHistoryDelegate.TYPE)
        fun provideAtomMeHistoryDelegate(delegate: AtomMeHistoryDelegate): SocketMessageDelegate

        @Binds
        @IntoMap
        @StringKey(AtomUserDelegate.TYPE)
        fun provideAtomUserDelegate(delegate: AtomUserDelegate): SocketMessageDelegate

        @Binds
        @IntoMap
        @StringKey(AtomUserNameDelegate.TYPE)
        fun provideAtomUserNameDelegate(delegate: AtomUserNameDelegate): SocketMessageDelegate

        @Binds
        @IntoMap
        @StringKey(AtomUserTitleDelegate.TYPE)
        fun provideAtomUserTitleDelegate(delegate: AtomUserTitleDelegate): SocketMessageDelegate

        @Binds
        @IntoMap
        @StringKey(AtomUserPhotoDelegate.TYPE)
        fun provideAtomUserPhotoDelegate(delegate: AtomUserPhotoDelegate): SocketMessageDelegate

        @Binds
        @IntoMap
        @StringKey(AtomUserTypingDelegate.TYPE)
        fun provideAtomUserTypingDelegate(delegate: AtomUserTypingDelegate): SocketMessageDelegate

        @Binds
        @IntoMap
        @StringKey(AtomMessageIdDelegate.TYPE)
        fun provideAtomMessageIdDelegate(delegate: AtomMessageIdDelegate): SocketMessageDelegate

        @Binds
        @IntoMap
        @StringKey(AtomMessageAckDelegate.TYPE)
        fun provideAtomMessageAckDelegate(delegate: AtomMessageAckDelegate): SocketMessageDelegate

        @Binds
        @IntoMap
        @StringKey(TextPlainDelegate.TYPE)
        fun provideTextPlainDelegate(delegate: TextPlainDelegate): SocketMessageDelegate

        @Binds
        @IntoMap
        @StringKey(ImageUniversalDelegate.TYPE)
        fun provideImageUniversalDelegate(delegate: ImageUniversalDelegate): SocketMessageDelegate

        @Binds
        @IntoMap
        @StringKey(ImageJpegDelegate.TYPE)
        fun provideImageJpegDelegate(delegate: ImageJpegDelegate): SocketMessageDelegate

        @Binds
        @IntoMap
        @StringKey(ImagePngDelegate.TYPE)
        fun provideImagePngDelegate(delegate: ImagePngDelegate): SocketMessageDelegate

        @Binds
        @IntoMap
        @StringKey(ImageGifDelegate.TYPE)
        fun provideImageGifDelegate(delegate: ImageGifDelegate): SocketMessageDelegate

        @Binds
        @IntoMap
        @StringKey(VideoUniversalDelegate.TYPE)
        fun provideVideoUniversalDelegate(delegate: VideoUniversalDelegate): SocketMessageDelegate

        @Binds
        @IntoMap
        @StringKey(VideoMp4Delegate.TYPE)
        fun provideVideoMp4Delegate(delegate: VideoMp4Delegate): SocketMessageDelegate

        @Binds
        @IntoMap
        @StringKey(AudioMpegDelegate.TYPE)
        fun provideAudioMpegDelegate(delegate: AudioMpegDelegate): SocketMessageDelegate

        @Binds
        @IntoMap
        @StringKey(DocumentUniversalDelegate.TYPE)
        fun provideDocumentUniversalDelegate(delegate: DocumentUniversalDelegate): SocketMessageDelegate

        @Binds
        @IntoMap
        @StringKey(DocumentZipDelegate.TYPE)
        fun provideDocumentZipDelegate(delegate: DocumentZipDelegate): SocketMessageDelegate
    }

    @ServiceScope
    @Provides
    fun provideFallbackDelegate(unsupportedRepository: UnsupportedRepository): FallbackDelegate =
        FallbackDelegate(unsupportedRepository)

    @Provides
    fun provideMessageObfuscator(moshi: Moshi): MessageObfuscator =
        if (BuildConfig.DEBUG) DebugMessageObfuscator() else ReleaseMessageObfuscator(moshi)

    @ServiceScope
    @Provides
    fun provideSocketMessageHandler(
        delegates: Map<String, @JvmSuppressWildcards SocketMessageDelegate>,
        fallbackDelegate: FallbackDelegate,
        parser: Moshi,
        messageObfuscator: MessageObfuscator
    ): SocketMessageHandler {
        return SocketMessageHandler(delegates, fallbackDelegate, parser, messageObfuscator)
    }
}