package com.jivosite.sdk.ui.views

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.Dimension
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jivosite.sdk.Jivo
import com.jivosite.sdk.R
import com.jivosite.sdk.support.ext.dp
import com.jivosite.sdk.support.vm.ViewModelFactory
import javax.inject.Inject

/**
 * Created on 12/14/20.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class JivoChatButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr), ViewModelStoreOwner {

    companion object {
        private const val DEFAULT_CLIP_TO_PADDING = false
        private const val DEFAULT_PADDING_IN_DP = 24
    }

    private lateinit var button: FloatingActionButton
    private lateinit var badge: View

    @Dimension
    private var padding = 0
    private var optionalClipToPadding = false

    private val stateObserver = Observer<JivoChatButtonViewModel.ButtonState> {
        badge.isVisible = it.hasNewMessage
        button.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(context, if (it.isOnline) R.color.darkPastelGreen else R.color.silver))
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<JivoChatButtonViewModel>
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(JivoChatButtonViewModel::class.java)
    }

    init {
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.JivoChatButton)
            padding = ta.getInt(
                R.styleable.JivoChatButton_padding,
                DEFAULT_PADDING_IN_DP
            ).dp
            optionalClipToPadding = ta.getBoolean(
                R.styleable.JivoChatButton_clipToPadding,
                DEFAULT_CLIP_TO_PADDING
            )
            ta.recycle()
        } else {
            padding = DEFAULT_PADDING_IN_DP.dp
            optionalClipToPadding = DEFAULT_CLIP_TO_PADDING
        }

        setPadding(padding)
        clipToPadding = optionalClipToPadding

        LayoutInflater.from(context).inflate(R.layout.jivo_button, this, true)
    }

    override fun getViewModelStore(): ViewModelStore {
        return (context as AppCompatActivity).viewModelStore
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        button = findViewById(R.id.button)
        badge = findViewById(R.id.badge)

        if (!isInEditMode) {
            Jivo.jivoSdkComponent.inject(this)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (!isInEditMode) {
            viewModel.state.observeForever(stateObserver)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewModel.state.removeObserver(stateObserver)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        button.setOnClickListener(l)
    }
}