package com.jivosite.sdk.ui.logs.items

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jivosite.sdk.R
import com.jivosite.sdk.support.ext.dp

/**
 * Created on 2020-01-14.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class LogsItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val dividerSize: Int = 1.dp
    private val paint: Paint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.grey)
        style = Paint.Style.STROKE
        strokeWidth = dividerSize.toFloat()
        pathEffect = DashPathEffect(floatArrayOf(5.0f, 10.0f), 0.0f)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val rect = Rect()
        for (i in 1 until parent.childCount) {
            val view = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view)
            if (position != RecyclerView.NO_POSITION) {
                parent.getDecoratedBoundsWithMargins(view, rect)
                val top = rect.top.toFloat() + dividerSize / 2.0f + view.translationY
                c.drawLine(rect.left.toFloat(), top, rect.right.toFloat(), top, paint)
            }
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val itemPosition = parent.getChildAdapterPosition(view)
        if (itemPosition != RecyclerView.NO_POSITION) {
            outRect.top = dividerSize
        }
    }
}