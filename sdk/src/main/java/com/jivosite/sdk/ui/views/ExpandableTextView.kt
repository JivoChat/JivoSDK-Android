package com.jivosite.sdk.ui.views

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.UnderlineSpan
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView.BufferType.NORMAL
import androidx.appcompat.widget.AppCompatTextView
import com.jivosite.sdk.R

class ExpandableTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    AppCompatTextView(context, attrs, defStyleAttr), View.OnClickListener {

    companion object {
        private const val COLLAPSED_MAX_LINES = 2
    }

    private lateinit var animator: ValueAnimator
    private var isCollapsing: Boolean = false
    private lateinit var originalText: CharSequence
    private var postfix: String

    init {
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView)
            postfix = context.getString(
                ta.getResourceId(
                    R.styleable.ExpandableTextView_postfix,
                    R.string.unsupported_message_postfix
                )
            )
            ta.recycle()
        } else {
            postfix = context.getString(R.string.unsupported_message_postfix)
        }

        maxLines = COLLAPSED_MAX_LINES
        setOnClickListener(this)
        initAnimator()
    }

    private fun initAnimator() {
        animator = ValueAnimator.ofInt(-1, -1)
            .setDuration(450)
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.addUpdateListener { animation -> updateHeight(animation.animatedValue as Int) }
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                if (isCollapsed()) {
                    isCollapsing = false
                    maxLines = Integer.MAX_VALUE
                    deEllipsize()
                } else {
                    isCollapsing = true
                }
            }

            override fun onAnimationEnd(animation: Animator) {
                if (!isCollapsed() && isCollapsing) {
                    maxLines = COLLAPSED_MAX_LINES
                    ellipsizeColored()
                    isCollapsing = false
                }
                setWrapContent()
            }
        })
    }

    override fun setText(text: CharSequence, type: BufferType) {
        originalText = text
        super.setText(text, type)
    }

    private fun ellipsizeColored() {
        val end = layout.getLineEnd(COLLAPSED_MAX_LINES - 1)
        val text = text

        val chars = layout.getLineEnd(COLLAPSED_MAX_LINES - 1) - layout.getLineStart(COLLAPSED_MAX_LINES - 1);

        val additionalGap = 4
        if (chars + additionalGap < postfix.length) {
            return
        }
        val builder = SpannableStringBuilder(text)
        builder.replace(end - postfix.length, end, postfix)
        builder.setSpan(
            UnderlineSpan(),
            end - postfix.length, end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        setTextNoCaching(builder)
    }

    private fun deEllipsize() {
        super.setText(originalText)
    }

    private fun setTextNoCaching(text: CharSequence) {
        super.setText(text, NORMAL)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (lineCount <= COLLAPSED_MAX_LINES) {
            deEllipsize()
            isClickable = false
        } else {
            isClickable = true
            if (!animator.isRunning && isCollapsed()) {
                ellipsizeColored()
            }
        }
    }

    override fun onClick(v: View?) {
        if (animator.isRunning) {
            animatorReverse()
            return
        }

        val endPosition = animateTo()
        val startPosition = height

        animator.setIntValues(startPosition, endPosition);
        animatorStart();
    }

    private fun animatorReverse() {
        isCollapsing = !isCollapsing
        animator.reverse()
    }

    private fun animatorStart() {
        animator.start()
    }

    private fun animateTo(): Int {
        return if (isCollapsed()) {
            layout.height + getPaddingHeight()
        } else {
            layout.getLineBottom(COLLAPSED_MAX_LINES - 1) + layout.bottomPadding + getPaddingHeight()
        }
    }

    private fun getPaddingHeight(): Int {
        return compoundPaddingBottom + compoundPaddingTop
    }

    private fun isCollapsed(): Boolean {
        return Int.MAX_VALUE != maxLines
    }

    private fun updateHeight(animatedValue: Int) {
        val layoutParams = layoutParams
        layoutParams.height = animatedValue
        setLayoutParams(layoutParams)
    }

    private fun setWrapContent() {
        val layoutParams = layoutParams
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        setLayoutParams(layoutParams);
    }
}