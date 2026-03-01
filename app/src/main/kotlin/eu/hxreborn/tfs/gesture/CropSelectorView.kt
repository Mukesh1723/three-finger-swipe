package eu.hxreborn.tfs.gesture

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View

internal class CropSelectorView(
    context: Context,
    private val onCropSelected: (Rect) -> Unit,
) : View(context) {
    private val dimPaint =
        Paint().apply {
            color = 0xBB000000.toInt()
            isAntiAlias = false
        }
    private val clearPaint =
        Paint().apply {
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            isAntiAlias = false
        }
    private val borderPaint =
        Paint().apply {
            color = 0xFFFFFFFF.toInt()
            style = Paint.Style.STROKE
            strokeWidth = 3f
            isAntiAlias = true
        }

    private val selection = Rect()
    private var startX = 0
    private var startY = 0

    init {
        setLayerType(LAYER_TYPE_HARDWARE, null)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x.toInt()
        val y = event.y.toInt()
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                startX = x
                startY = y
                selection.setEmpty()
                invalidate()
            }

            MotionEvent.ACTION_MOVE -> {
                selection.set(
                    minOf(startX, x),
                    minOf(startY, y),
                    maxOf(startX, x),
                    maxOf(startY, y),
                )
                invalidate()
            }

            MotionEvent.ACTION_UP -> {
                if (!selection.isEmpty) onCropSelected(Rect(selection))
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), dimPaint)
        if (!selection.isEmpty) {
            canvas.drawRect(selection, clearPaint)
            canvas.drawRect(selection, borderPaint)
        }
    }
}
