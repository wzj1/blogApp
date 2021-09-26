package com.wzj.lib_common.wigde
import android.animation.ValueAnimator
import android.content.ContentValues
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import com.ydph.lib_common.log.Logs
import kotlin.math.max
import kotlin.math.sin


class LoadingView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    var paint: Paint = Paint()

    private val count = 4

    var defaultSize = 200

    var lineHeight = 150f

    var defaultHeight = 150f

    var lineWidth = 0f

    private val space = 20f


    init {
        paint.color = Color.RED
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //view根据xml中layout_width和layout_height测量出对应的宽度和高度值，
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        when (widthSpecMode) {
            MeasureSpec.UNSPECIFIED -> defaultSize = widthSpecSize
            MeasureSpec.AT_MOST -> defaultSize = getContentWidth()
            MeasureSpec.EXACTLY ->
                defaultSize = max(getContentWidth(), widthSpecSize)
        }
        lineHeight = max(0f, defaultSize - 80f)

        lineWidth = (defaultSize - (count + 1) * space) * 1f / count
        Logs.d("宽度:$defaultSize")
        setMeasuredDimension(defaultSize, defaultSize)
    }

    open fun getContentHeight(): Int {
        val contentHeight = height + paddingTop + paddingBottom.toFloat()
        Log.d(ContentValues.TAG, "getContentWidth: contentHeight=$contentHeight")
        return contentHeight.toInt()
    }

    private  fun getContentWidth(): Int {
        val contentWidth = width + paddingLeft + paddingRight.toFloat()
        Log.d(ContentValues.TAG, "getContentWidth: contentWidth=$contentWidth")
        return contentWidth.toInt()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.translate(0f, defaultSize * 1.0f)
        paint.strokeWidth = lineWidth
        val wave = lineHeight
        for (i in 0 until count) {
            val startX = i * (lineWidth + space) + space
            val endY = wave * sin(progress * 360 + startX / defaultSize * 360)
            canvas?.drawLine(startX, 0f, startX, endY.toFloat(), paint)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAnimation()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (valueAnimator != null) {
            valueAnimator.cancel()
        }
    }

    var progress = 0f
    lateinit var valueAnimator: ValueAnimator
    private fun startAnimation() {
        valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator?.duration = 60000
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.repeatCount = -1
        valueAnimator.repeatMode = ValueAnimator.REVERSE
        valueAnimator.addUpdateListener {
            progress = it.animatedValue as Float
            postInvalidate()
        }
        valueAnimator.start()
    }

}

