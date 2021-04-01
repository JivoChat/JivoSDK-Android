package com.jivosite.sdk.support.coil

import android.graphics.*
import androidx.annotation.Px
import androidx.core.graphics.applyCanvas
import coil.bitmap.BitmapPool
import coil.size.Size
import coil.transform.Transformation
import com.jivosite.sdk.support.ext.dp
import kotlin.math.roundToInt

/**
 * Created on 24.12.2020.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
class ImageTransformation(
    @Px private val topLeft: Float = 0f,
    @Px private val topRight: Float = 0f,
    @Px private val bottomLeft: Float = 0f,
    @Px private val bottomRight: Float = 0f
) : Transformation {

    constructor(@Px radius: Float) : this(radius, radius, radius, radius)

    init {
        require(topLeft >= 0 && topRight >= 0 && bottomLeft >= 0 && bottomRight >= 0) { "All radii must be >= 0." }
    }

    override fun key(): String = ImageTransformation::class.java.name

    override suspend fun transform(pool: BitmapPool, input: Bitmap, size: Size): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)

        val inputWidth = input.width
        val inputHeight = input.height

        val ratio: Float = 1.0f * inputWidth / inputHeight

        val outputWidth: Int
        val outputHeight: Int

        outputHeight = when {
            inputHeight > IMAGE_MAX_HEIGHT -> IMAGE_MAX_HEIGHT
            inputHeight < IMAGE_MIN_HEIGHT -> IMAGE_MIN_HEIGHT
            else -> inputHeight
        }

        val calculatedWidth = (outputHeight * ratio).roundToInt()

        outputWidth = when {
            calculatedWidth > IMAGE_MAX_WIDTH -> IMAGE_MAX_WIDTH
            calculatedWidth < IMAGE_MIN_WIDTH -> IMAGE_MIN_WIDTH
            else -> calculatedWidth
        }

        val output = pool.get(outputWidth, outputHeight, input.config ?: Bitmap.Config.ARGB_8888)
        output.applyCanvas {
            drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

            val matrix = Matrix()

            when {
                calculatedWidth > IMAGE_MAX_WIDTH -> {
                    matrix.setScale(
                        1.0f * calculatedWidth / inputWidth,
                        1.0f * outputHeight / inputHeight
                    )
                    matrix.postTranslate((outputWidth - calculatedWidth) / 2.0f, 0f)
                }
                calculatedWidth < IMAGE_MIN_WIDTH && inputHeight < IMAGE_MIN_HEIGHT -> {
                    matrix.setScale(
                        1.0f * outputWidth / inputWidth,
                        1.0f * outputHeight / inputHeight,
                    )
                }

                calculatedWidth < IMAGE_MIN_WIDTH -> {
                    matrix.postTranslate(0f, (outputHeight - inputHeight) / 2.0f)
                }
                else -> {
                    matrix.setScale(
                        1.0f * outputWidth / inputWidth,
                        1.0f * outputHeight / inputHeight,
                    )
                }
            }

            val shader = BitmapShader(input, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            shader.setLocalMatrix(matrix)
            paint.shader = shader

            val radii = floatArrayOf(topLeft, topLeft, topRight, topRight, bottomRight, bottomRight, bottomLeft, bottomLeft)

            val rect = RectF(0f, 0f, outputWidth.toFloat(), outputHeight.toFloat())

            val path = Path().apply {
                addRoundRect(rect, radii, Path.Direction.CW)
            }
            drawPath(path, paint)
        }
        return output
    }

    override fun equals(other: Any?) = other is ImageTransformation

    override fun hashCode() = javaClass.hashCode()

    override fun toString() = "ImageTransformation()"

    private companion object {
        val IMAGE_MAX_WIDTH = 276.dp
        val IMAGE_MIN_WIDTH = 60.dp
        val IMAGE_MAX_HEIGHT = 220.dp
        val IMAGE_MIN_HEIGHT = 60.dp
    }

}