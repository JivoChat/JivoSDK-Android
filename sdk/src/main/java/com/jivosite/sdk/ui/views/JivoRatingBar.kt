package com.jivosite.sdk.ui.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isInvisible
import com.jivosite.sdk.R
import com.jivosite.sdk.model.pojo.rate.RateSettings

/**
 * Created on 08.02.2023.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
class JivoRatingBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var numIcons: Int = 5
    private var type: String? = null

    private var changeListener: ((String) -> Unit)? = null

    fun init(type: String?, icon: String?, rate: String? = null) {
        this.type = type

        prepareItems()
        val index = getIndex(rate)
        when (type) {
            RateSettings.Type.TWO.type -> setThumbIcon(index)
            RateSettings.Type.THREE.type -> {
                when (icon) {
                    RateSettings.Icon.SMILE.icon -> setSmileIcon(index)
                    RateSettings.Icon.STAR.icon -> setStarIcon(index)
                    RateSettings.Icon.UNKNOWN.icon -> setStarIcon(index)
                }
            }
            RateSettings.Type.FIVE.type -> {
                when (icon) {
                    RateSettings.Icon.SMILE.icon -> setSmileIcon(index)
                    RateSettings.Icon.STAR.icon -> setStarIcon(index)
                    RateSettings.Icon.UNKNOWN.icon -> setStarIcon(index)
                }
            }
            RateSettings.Type.UNKNOWN.type -> setStarIcon(index)
        }
    }

    private fun prepareItems() {
        numIcons = when (type) {
            RateSettings.Type.TWO.type -> 2
            RateSettings.Type.THREE.type -> 3
            RateSettings.Type.FIVE.type -> 5
            else -> 5
        }

        for (i in 0 until childCount) {
            getChildAt(i).run {
                if (i >= numIcons) {
                    isInvisible = true
                } else {
                    setOnClickListener {
                        changeListener?.invoke(getRate(i))
                    }
                }
            }
        }
    }

    fun setOnJivoRatingBarChangeListener(listener: (String) -> Unit) {
        changeListener = listener
    }

    private fun setThumbIcon(index: Int) {
        val thumbArray = resources.obtainTypedArray(R.array.jivo_sdk_thumb_icons)
        for (i in 0 until numIcons) {
            (getChildAt(i) as AppCompatImageView).run {
                setImageResource(thumbArray.getResourceId(i, 0))
                isSelected = if (index == -1) true else index != i
            }
        }
        thumbArray.recycle()
    }

    private fun setSmileIcon(index: Int) {
        val emojiArray = resources.obtainTypedArray(R.array.jivo_sdk_emoji_icons)
        for (i in 0 until numIcons) {
            (getChildAt(i) as AppCompatImageView).run {
                when (type) {
                    RateSettings.Type.THREE.type -> setImageResource(emojiArray.getResourceId(i * 2, 0))
                    RateSettings.Type.FIVE.type -> setImageResource(emojiArray.getResourceId(i, 0))
                }
                isSelected = if (index == -1) true else index == i
            }
        }
        emojiArray.recycle()
    }

    private fun setStarIcon(index: Int) {
        for (i in 0 until numIcons) {
            (getChildAt(i) as AppCompatImageView).run {
                setImageResource(R.drawable.jivo_sdk_star_rate_selector)
                isSelected = if (index == -1) false else i <= index
            }
        }
    }

    private fun getRate(index: Int): String {
        return when (type) {
            RateSettings.Type.TWO.type -> if (index == 0) RateSettings.Rate.GOOD.rate else RateSettings.Rate.BAD.rate
            RateSettings.Type.THREE.type -> {
                when (index) {
                    0 -> RateSettings.Rate.BAD.rate
                    1 -> RateSettings.Rate.NORMAL.rate
                    else -> RateSettings.Rate.GOOD.rate
                }
            }
            else -> {
                when (index) {
                    0 -> RateSettings.Rate.BAD.rate
                    1 -> RateSettings.Rate.BAD_NORMAL.rate
                    2 -> RateSettings.Rate.NORMAL.rate
                    3 -> RateSettings.Rate.GOOD_NORMAL.rate
                    else -> RateSettings.Rate.GOOD.rate
                }
            }
        }
    }

    private fun getIndex(rate: String?): Int {
        return when (type) {
            RateSettings.Type.TWO.type -> {
                when (rate) {
                    RateSettings.Rate.BAD.rate -> 0
                    RateSettings.Rate.GOOD.rate -> 1
                    else -> -1
                }
            }
            RateSettings.Type.THREE.type -> {
                when (rate) {
                    RateSettings.Rate.BAD.rate -> 0
                    RateSettings.Rate.NORMAL.rate -> 1
                    RateSettings.Rate.GOOD.rate -> 2
                    else -> -1
                }
            }
            else -> {
                when (rate) {
                    RateSettings.Rate.BAD.rate -> 0
                    RateSettings.Rate.BAD_NORMAL.rate -> 1
                    RateSettings.Rate.NORMAL.rate -> 2
                    RateSettings.Rate.GOOD_NORMAL.rate -> 3
                    RateSettings.Rate.GOOD.rate -> 4
                    else -> -1
                }
            }
        }
    }
}
