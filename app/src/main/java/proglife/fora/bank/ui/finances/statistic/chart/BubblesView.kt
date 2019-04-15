package proglife.fora.bank.ui.finances.statistic.chart

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.content.ContextCompat
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.util.LruCache
import android.view.*
import proglife.fora.bank.R
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import kotlin.math.*

/**
 * Created by Evhenyi Shcherbyna on 23.07.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class BubblesView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr), SurfaceHolder.Callback {

    private val cache: LruCache<Int, Bitmap>

    private var bubbles: MutableList<Bubble> = mutableListOf()
    private var selectedBubble: Bubble? = null

    private val maxSize = resources.getDimensionPixelOffset(R.dimen._60sdp)
    private val spaceSize = resources.getDimensionPixelOffset(R.dimen._10sdp)
    private val bubblePaint = Paint()
    private val titlePaint: TextPaint = TextPaint(Paint.LINEAR_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG)
    private val textPaint: TextPaint = TextPaint(Paint.LINEAR_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG)
    private val bitmapPaint = Paint()

    private var drawThread: DrawThread? = null

    private var offsetX = 0f
    private var offsetY = 0f
    private var offsetChildX = 0f
    private var offsetChildY = 0f

    private var mScaleFactor = 1.0f
    private var mScaleChildFactor = 1.0f

    private val bitmapRect = Rect(0, 0, maxSize, maxSize)
    private val tempRect = RectF()
    private val map = mutableMapOf<Int, AnimLocation>()

    private var animatorSet: AnimatorSet? = null
//    private var xValueAnimator: ValueAnimator? = null
//    private var yValueAnimator: ValueAnimator? = null

    private var isAnimated: Boolean = false

    private val outCoords = mutableListOf<FloatArray>()

    private val clipPath = Path()
    private val arcHeight = resources.getDimensionPixelOffset(R.dimen.arc_height)

    var onItemActionListener: OnItemActionListener? = null

    interface OnItemActionListener {
        fun onOpen(bubble: Bubble)
    }

    init {
//        setZOrderOnTop(true)

        holder.setFormat(PixelFormat.OPAQUE)
        bitmapPaint.isAntiAlias = true
        bitmapPaint.isFilterBitmap = true
        bubblePaint.isAntiAlias = true
        titlePaint.color = Color.WHITE
        titlePaint.textSize = maxSize * 0.10f
        textPaint.color = Color.WHITE
        textPaint.textSize = maxSize * 0.15f
        setBackgroundColor(Color.WHITE)
        holder.addCallback(this)

        val maxMemory = Runtime.getRuntime().maxMemory()
        val cacheSize = maxMemory.toInt() / 8
        cache = object : LruCache<Int, Bitmap>(cacheSize) {
            override fun sizeOf(key: Int?, value: Bitmap?): Int {
                return value?.byteCount ?: 1
            }
        }

        val bubbles = listOf(
                Bubble("Мобильная связь", 650.0, "", Color.parseColor("#4AC1E2"), prepareBubbles(listOf(
                        Bubble("МТС", 200.0, "", Color.parseColor("#FFEB2127")),
                        Bubble("TELE2", 150.0, "", Color.parseColor("#FF181818")),
                        Bubble("Мегафон", 250.0, "", Color.parseColor("#FF18A461"))
                ))),
                Bubble("Одежда и обувь", 5600.0, "", Color.parseColor("#4AC1E2"), prepareBubbles(listOf(
                        Bubble("Nike", 200.0, "", Color.parseColor("#FFEB2127")),
                        Bubble("Adidas", 150.0, "", Color.parseColor("#FF181818")),
                        Bubble("Colins", 250.0, "", Color.parseColor("#FF18A461"))
                ))),
                Bubble("Еда, продукты питания", 14000.0, "", Color.parseColor("#2C3E50"), prepareBubbles(listOf(
                        Bubble("Mcdonalds", 200.0, "", Color.parseColor("#FFEB2127")),
                        Bubble("KFC", 150.0, "", Color.parseColor("#FF181818")),
                        Bubble("Папа джонс", 250.0, "", Color.parseColor("#FF18A461"))
                ))),
                Bubble("Переводы", 40000.0, "", Color.parseColor("#EF3A7F"), prepareBubbles(listOf(
                        Bubble("МТС", 200.0, "", Color.parseColor("#FFEB2127")),
                        Bubble("TELE2", 150.0, "", Color.parseColor("#FF181818")),
                        Bubble("Мегафон", 250.0, "", Color.parseColor("#FF18A461"))
                ))),
                Bubble("Снятие наличных", 67000.0, "", Color.parseColor("#16A085"), prepareBubbles(listOf(
                        Bubble("МТС", 200.0, "", Color.parseColor("#FFEB2127")),
                        Bubble("TELE2", 150.0, "", Color.parseColor("#FF181818")),
                        Bubble("Мегафон", 250.0, "", Color.parseColor("#FF18A461"))
                ))),
                Bubble("Коммунальные услуги", 80000.0, "", Color.parseColor("#F9C532"), prepareBubbles(listOf(
                        Bubble("МТС", 200.0, "", Color.parseColor("#FFEB2127")),
                        Bubble("TELE2", 150.0, "", Color.parseColor("#FF181818")),
                        Bubble("Мегафон", 250.0, "", Color.parseColor("#FF18A461"))
                )))
        )


        this.bubbles.addAll(prepareBubbles(bubbles))
    }

    private fun prepareBubbles(bubbles: List<Bubble>): List<Bubble> {
        val out = bubbles.sortedBy { -it.amount }
        val max = out.maxBy { it.amount }?.amount ?: 0.0
        val min = bubbles.minBy { it.amount }?.amount ?: 0.0
        out.map { it.factor = ((it.amount - min) / (max - min)).toFloat() }
        return out
    }

    private val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            if (abs((e1.y - e2.y)) > measuredHeight / 4) back()
            return true
        }

        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            if (selectedBubble != null) {
                val selectedItem = selectedItem(selectedBubble!!.bubbles ?: emptyList(), e.x, e.y)
                selectedItem?.let { onItemActionListener?.onOpen(it) }
            } else {
                selectedBubble = selectedItem(bubbles, e.x, e.y)
                offsetChildX = 0f
                offsetChildY = 0f
                mScaleChildFactor = 1f
                selectedBubble?.let { animateTransition(it, false) }
            }
            return true
        }
    })

    private val scaleGestureDetector = ScaleGestureDetector(context, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            if (selectedBubble != null) {
                mScaleChildFactor = max(0.3f, min(mScaleChildFactor * detector.scaleFactor, 2.0f))
            } else {
                mScaleFactor = max(0.3f, min(mScaleFactor * detector.scaleFactor, 2.0f))
            }
            return true
        }
    })

    private fun selectedItem(items: List<Bubble>, x: Float, y: Float): Bubble? {
        for (bubble in items) {
            if (bubble.contains(x, y)) return bubble
        }
        return null
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        cache.evictAll()
        var retry = true
        drawThread?.running = false
        while (retry) {
            try {
                drawThread?.join()
                retry = false
            } catch (e: InterruptedException) {

            }
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        drawThread = DrawThread(this, holder)
        drawThread?.running = true
        drawThread?.start()
    }

    private val cMatrix = Matrix()

    /**
     *          7
     *      1       3
     *  5       0       6
     *      2       4
     *          8
     *
     *  c - cathet
     *  h - hypotenuse
     */
    private fun calculateBubbles(bubbles: List<Bubble>, scaleFactor: Float) {
        for (i in 0 until bubbles.size) {
            val bubble = bubbles[i]
            cMatrix.reset()

            val radius = (maxSize
                    * scaleFactor
                    * (0.7f + 0.3f * bubble.factor)).toInt()
            when (i) {
                0 -> {
                    val cx = width.shr(1).toFloat() + if (this.bubbles[0] == bubble) offsetX else offsetChildX
                    val cy = height.shr(1).toFloat() + if (this.bubbles[0] == bubble) offsetY else offsetChildY
                    val left = cx - radius
                    val right = cx + radius
                    val top = cy - radius
                    val bottom = cy + radius
                    bubble.rect.set(left, top, right, bottom)

//                    val scale = getScaleFactor(bubble.factor, cx, cy)
//                    cMatrix.setScale(scale, scale, cx, cy)
//                    cMatrix.mapRect(bubble.rect)

                }
                in 1..4 -> {
                    val dep = bubbles[0]
                    var loc = processSecond(dep, i, radius.toFloat())
                    var left = loc.first - radius
                    var right = loc.first + radius
                    var top = loc.second - radius
                    var bottom = loc.second + radius
                    bubble.rect.set(left, top, right, bottom)

//                    val scale = getScaleFactor(bubble.factor, loc.first, loc.second)
//                    cMatrix.setScale(scale, scale)
//                    cMatrix.mapRect(bubble.rect)

//                    loc = processSecond(dep, i, bubble.radius())
//                    left = loc.first - bubble.radius()
//                    right = loc.first + bubble.radius()
//                    top = loc.second - bubble.radius()
//                    bottom = loc.second + bubble.radius()
//                    bubble.rect.set(left, top, right, bottom)
                }
                else -> {
                    var loc: Pair<Float, Float> = -1000f to -1000f
                    if (i == 5) loc = findCrossPoints(bubbles[1].rect, bubbles[2].rect, radius, false)
                    if (i == 6) loc = findCrossPoints(bubbles[3].rect, bubbles[4].rect, radius, true)
                    if (i == 7) loc = findCrossPoints(bubbles[1].rect, bubbles[3].rect, radius, true)
                    if (i == 8) loc = findCrossPoints(bubbles[2].rect, bubbles[4].rect, radius, false)
                    if (i == 9) loc = findCrossPoints(bubbles[1].rect, bubbles[5].rect, radius, false)
                    if (i == 10) loc = findCrossPoints(bubbles[5].rect, bubbles[2].rect, radius, false)
                    if (i == 11) loc = findCrossPoints(bubbles[1].rect, bubbles[7].rect, radius, true)
                    if (i == 12) loc = findCrossPoints(bubbles[2].rect, bubbles[8].rect, radius, false)
                    if (i == 13) loc = findCrossPoints(bubbles[10].rect, bubbles[11].rect, radius, false, bubbles[5].rect)

//                    var loc = processThird(i, radius)
                    bubble.rect.set(
                            loc.first - radius,
                            loc.second - radius,
                            loc.first + radius,
                            loc.second + radius
                    )

//                    val scale = getScaleFactor(bubble.factor, loc.first, loc.second)
//                    cMatrix.setScale(scale, scale)
//                    cMatrix.mapRect(bubble.rect)

//                    loc = processThird(i, bubble.radius().toInt())
//                    bubble.rect.set(
//                            loc.first - bubble.radius(),
//                            loc.second - bubble.radius(),
//                            loc.first + bubble.radius(),
//                            loc.second + bubble.radius()
//                    )
                }
            }
        }
    }

    private fun getStartScaleFactor(startFactor: Float): Float {
        return 0.4f + 0.2f * startFactor
    }

    private fun getScaleFactor(startFactor: Float, x: Float, y: Float): Float {
        return getStartScaleFactor(startFactor) + getScaleFactor(x, y)
    }

    private fun getScaleFactor(x: Float, y: Float): Float {
        var factorX = x / measuredWidth
        factorX = if (factorX > 0.5f) 1f - factorX else factorX
        var factorY = y / measuredHeight
        factorY = if (factorY > 0.5f) 1f - factorY else factorY
        return 0.4f * ((factorX + factorY) / 2)
    }

    /**
     * For elements with index 1-4
     */
    private fun processSecond(dep: Bubble, i: Int, radius: Float): Pair<Float, Float> {
        val h = dep.radius() + spaceSize + radius
        val c = (sin(Math.toRadians(45.0)) * h).toInt()
        val location = when (i) {
            1 -> -c to -c
            2 -> -c to +c
            3 -> +c to -c
            4 -> +c to +c
            else -> throw IllegalArgumentException("Incorrect child index. Expected 1-4, not $i")
        }
        val cx = dep.cx() + location.first
        val cy = dep.cy() + location.second

        return cx to cy
    }

    private fun distanceBetweenPoints(x1: Float, y1: Float, x2: Float, y2: Float): Int {
        val dx = x1 - x2
        val dy = y1 - y2
        return sqrt((dx * dx + dy * dy).toDouble()).toInt()
    }

    private fun getBitmap(bubble: Bubble): Bitmap {
        val key = bubble.hashCode()
        var bm = cache.get(key)
        if (bm == null) {
            bm = generateBitmap(bubble)
            cache.put(key, bm)
        }
        return bm
    }

    private fun generateBitmap(bubble: Bubble): Bitmap {
        bubblePaint.color = bubble.color
        val bitmap = Bitmap.createBitmap(maxSize, maxSize, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawCircle(
                canvas.width.shr(1).toFloat(),
                canvas.height.shr(1).toFloat(),
                canvas.width.shr(1).toFloat() - 2f,
                bubblePaint)
        canvas.save()
        canvas.translate(canvas.width / 10f, canvas.height / 4f)
        val staticLayout = StaticLayout(bubble.title, titlePaint, (canvas.width - canvas.width / 5f).toInt(),
                Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false)
        staticLayout.draw(canvas)
        canvas.translate(0f, canvas.height / 3f)
        val staticLayout2 = StaticLayout(toMoney(bubble.amount), titlePaint, (canvas.width - canvas.width / 5f).toInt(),
                Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false)
        staticLayout2.draw(canvas)
        canvas.restore()
        return bitmap
    }

    private fun toMoney(value: Double): String {
        val dfs = DecimalFormatSymbols()
        dfs.decimalSeparator = '.'
        dfs.groupingSeparator = ' '
        val df = DecimalFormat("###,###,###.##", dfs)
        return "${df.format(value)} \u20BD"
    }

    private var dX = 0f
    private var dY = 0f

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isAnimated) return true
        if (event.pointerCount == 2) {
            return scaleGestureDetector.onTouchEvent(event)
        }
        gestureDetector.onTouchEvent(event)


        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                animatorSet?.cancel()

                dX = event.rawX - if (selectedBubble != null) offsetChildX else offsetX
                dY = event.rawY - if (selectedBubble != null) offsetChildY else offsetY
            }
            MotionEvent.ACTION_MOVE -> {
                if (selectedBubble != null) {
                    offsetChildX = event.rawX - dX
                    offsetChildY = event.rawY - dY
                } else {
                    offsetX = event.rawX - dX
                    offsetY = event.rawY - dY
                }
            }
            MotionEvent.ACTION_UP -> {
                animatorSet = AnimatorSet()
                animatorSet?.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        isAnimated = false
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                        isAnimated = false
                    }

                    override fun onAnimationStart(animation: Animator?) {
                        isAnimated = true
                    }

                })
                if (selectedBubble != null) {
                    val point = findNearestPoint(offsetChildX, offsetChildY)
                    val xValueAnimator = ValueAnimator.ofFloat(offsetChildX, point.first)
                    xValueAnimator?.interpolator = FastOutSlowInInterpolator()
                    xValueAnimator?.addUpdateListener { offsetChildX = it.animatedValue as Float }
                    val yValueAnimator = ValueAnimator.ofFloat(offsetChildY, point.second)
                    yValueAnimator?.addUpdateListener { offsetChildY = it.animatedValue as Float }
                    yValueAnimator?.interpolator = FastOutSlowInInterpolator()
                    animatorSet?.playTogether(xValueAnimator, yValueAnimator)
                } else {
                    val point = findNearestPoint(offsetX, offsetY)
                    val xValueAnimator = ValueAnimator.ofFloat(offsetX, point.first)
                    xValueAnimator?.interpolator = FastOutSlowInInterpolator()
                    xValueAnimator?.addUpdateListener { offsetX = it.animatedValue as Float }
                    val yValueAnimator = ValueAnimator.ofFloat(offsetY, point.second)
                    yValueAnimator?.addUpdateListener { offsetY = it.animatedValue as Float }
                    yValueAnimator?.interpolator = FastOutSlowInInterpolator()
                    animatorSet?.playTogether(xValueAnimator, yValueAnimator)

                }
                animatorSet?.start()
            }
        }

        return true
    }

    private fun findNearestPoint(x: Float, y: Float): Pair<Float, Float> {
        val cx = measuredWidth.shr(1).toFloat()
        val cy = measuredHeight.shr(1).toFloat()
        var targetX = 0f
        val targetY = 0f
        var minDistance = Int.MAX_VALUE
        bubbles.forEach {
            val distance = distanceBetweenPoints(cx, cy, it.cx(), it.cy())
            if (!it.cx().isNaN() && distance < minDistance) {
                minDistance = distance
                targetX = cx - it.cx() + x
            }
        }
        return targetX to targetY
    }

    private enum class Direction(val x: Int, val y: Int) {
        LEFT(-1, 1),
        TOP(1, -1),
        RIGHT(1, -1),
        BOTTOM(-1, 1)
    }

    private fun animateTransition(bubble: Bubble?, isReverse: Boolean) {
        if (!isReverse || outCoords.isEmpty()) {
            outCoords.clear()
            for (i in 0 until bubbles.size) {
                val data = FloatArray(2)
                calculateOut(this, bubbles[i].rect, data, bubble?.cx()?.toInt(), bubble?.cy()?.toInt())
                outCoords.add(data)
            }
        }

        val start = if (!isReverse) 0f else 1f
        val end = if (!isReverse) 1f else 0f
        val explodeAnimator = ValueAnimator.ofFloat(start, end).apply {
            addUpdateListener { animator ->
                for (i in 0 until bubbles.size) {
                    val modBubble = bubbles[i]
                    modBubble.translationX = outCoords[i][0] * animator.animatedValue as Float
                    modBubble.translationY = outCoords[i][1] * animator.animatedValue as Float
                }
                selectedBubble?.bubbles?.let {
                    for (i in 0 until it.size) {
                        val modBubble = it[i]
                        modBubble.alpha = animator.animatedValue as Float
                    }
                }
            }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    if (isReverse) selectedBubble = null
                    isAnimated = false
                }

                override fun onAnimationCancel(animation: Animator?) {
                    isAnimated = false
                }

                override fun onAnimationStart(animation: Animator?) {
                    isAnimated = true
                }
            })
            duration = 600L
            interpolator = FastOutSlowInInterpolator()
            start()
        }
    }

    class AnimLocation(
            var translationX: Float = 0f,
            var translationY: Float = 0f
    )

    private fun calculateOut(sceneRoot: View, bounds: RectF, outVector: FloatArray, explodeCenterX: Int? = null, explodeCenterY: Int? = null) {
        val sceneRootX = 0
        val sceneRootY = 0
        val focalX: Int = explodeCenterX ?: (sceneRootX + sceneRoot.width / 2)
        val focalY: Int = explodeCenterY ?: (sceneRootY + sceneRoot.height / 2)

        val centerX = bounds.centerX()
        val centerY = bounds.centerY()
        var xVector = (centerX - focalX).toDouble()
        var yVector = (centerY - focalY).toDouble()

        if (xVector == 0.0 && yVector == 0.0) {
            xVector = Math.random() * 2 - 1
            yVector = Math.random() * 2 - 1
        }
        val vectorSize = Math.hypot(xVector, yVector)
        xVector /= vectorSize
        yVector /= vectorSize

        val maxDistance = calculateMaxDistance(sceneRoot, focalX - sceneRootX, focalY - sceneRootY)

        outVector[0] = Math.round(maxDistance * xVector).toFloat()
        outVector[1] = Math.round(maxDistance * yVector).toFloat()
    }

    private fun calculateMaxDistance(sceneRoot: View, focalX: Int, focalY: Int): Double {
        val maxX = Math.max(focalX, sceneRoot.width)
        val maxY = Math.max(focalY, sceneRoot.height)
        return Math.hypot(maxX.toDouble(), maxY.toDouble())
    }

    private fun findCrossPoints(rect1: RectF, rect2: RectF, radius: Int, isSecondPoint: Boolean, base: RectF? = null): Pair<Float, Float> {
        val x1 = rect1.centerX()
        val y1 = rect1.centerY()
        val x2 = rect2.centerX()
        val y2 = rect2.centerY()

        val abstractR1 = rect1.width() / 2f + radius + spaceSize
        val abstractR2 = rect2.width() / 2f + radius + spaceSize
        val d = distanceBetweenPoints(x1, y1, x2, y2)

        val a = (abstractR1 * abstractR1 - abstractR2 * abstractR2 + d * d) / (2 * d)
        val h = sqrt((abstractR1 * abstractR1 - a * a).toDouble())

        val x0 = x1 + a * (x2 - x1) / d
        val y0 = y1 + a * (y2 - y1) / d

        var x3 = (x0 + h * (if (!isSecondPoint) -1 else 1) * (y2 - y1) / d).toFloat()
        val y3 = (y0 + h * (if (!isSecondPoint) 1 else -1) * (x2 - x1) / d).toFloat()

        base?.let {
            x3 = if (x3 > it.left - spaceSize - radius) it.left - spaceSize - radius else x3
        }


        return x3 to y3
    }

    fun back(): Boolean {
        val back = selectedBubble == null
        if (!back) animateTransition(null, true)
        return back
    }

    fun drawCanvas(canvas: Canvas) {
        calculateBubbles(bubbles, mScaleFactor)
        selectedBubble?.bubbles?.let { it -> calculateBubbles(it, mScaleChildFactor) }

        clipPath.reset()
        clipPath.moveTo(0f, arcHeight.toFloat())
        clipPath.quadTo((width / 2).toFloat(), (-arcHeight).toFloat(), width.toFloat(), arcHeight.toFloat())
        clipPath.lineTo(width.toFloat(), height.toFloat())
        clipPath.lineTo(0f, height.toFloat())
        clipPath.close()

        canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR)
        canvas.clipPath(clipPath)

        selectedBubble?.bubbles?.let { it ->
            for (i in 0 until it.size) {
                val bubble = it[i]
                tempRect.set(bubble.rect)
                val animLocation = map.getOrPut(i) { AnimLocation() }
                bitmapPaint.alpha = (255 * bubble.alpha).toInt()
//                tempRect.offset(animLocation.translationX, animLocation.translationY)
                canvas.drawBitmap(getBitmap(bubble), bitmapRect, tempRect, bitmapPaint)
            }
        }

        for (i in 0 until bubbles.size) {
            val bubble = bubbles[i]
            tempRect.set(bubble.rect)
            tempRect.offset(bubble.translationX, bubble.translationY)
            bitmapPaint.alpha = 255
            canvas.drawBitmap(getBitmap(bubble), bitmapRect, tempRect, bitmapPaint)
        }
    }

    class DrawThread(
            private val view: BubblesView,
            private val surfaceHolder: SurfaceHolder
    ): Thread() {

        companion object {
            const val FPS = 30
        }

        @Volatile var running = false

        override fun run() {
            val ticksPS = 1000 / FPS
            while (running) {
                var canvas: Canvas? = null
                val startTime = System.currentTimeMillis()
                try {
                    canvas = surfaceHolder.lockCanvas()
                    if (canvas == null) continue
                    synchronized(surfaceHolder) {
                        view.drawCanvas(canvas)
                    }
                } finally {
                    if (canvas != null) surfaceHolder.unlockCanvasAndPost(canvas)
                }
                val sleepTime = ticksPS - (System.currentTimeMillis() - startTime)
                try {
                    if (sleepTime > 0) sleep(sleepTime)
                    else sleep(10)
                } catch (e: Exception) {}
            }
        }

    }

    override fun onSaveInstanceState(): Parcelable {
        return SavedState(super.onSaveInstanceState(), bubbles, selectedBubble,
                offsetX, offsetY, offsetChildX, offsetChildY, mScaleFactor, mScaleChildFactor)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val savedState = state as? SavedState
        super.onRestoreInstanceState(savedState?.superState)
        savedState?.let {
            bubbles = it.bubbles.toMutableList()
            selectedBubble = it.selectedBubble
            offsetX = it.offsetX
            offsetY = it.offsetY
            offsetChildX = it.offsetChildX
            offsetChildY = it.offsetChildY
            mScaleFactor = it.scaleFactor
            mScaleChildFactor = it.scaleChildFactor
        }
    }

    class SavedState : BaseSavedState {

        var bubbles: List<Bubble> = emptyList()
        var selectedBubble: Bubble? = null
        var offsetX: Float = 0f
        var offsetY: Float = 0f
        var offsetChildX: Float = 0f
        var offsetChildY: Float = 0f
        var scaleFactor: Float = 1f
        var scaleChildFactor: Float = 1f

        constructor(parcelable: Parcelable, bubbles: List<Bubble>, selectedBubble: Bubble?,
                    offsetX: Float, offsetY: Float, offsetChildX: Float, offsetChildY: Float,
                    scaleFactor: Float, scaleChildFactor: Float) : super(parcelable) {
            this.bubbles = bubbles
            this.selectedBubble = selectedBubble
            this.offsetX = offsetX
            this.offsetY = offsetY
            this.offsetChildX = offsetChildX
            this.offsetChildY = offsetChildY
            this.scaleFactor = scaleFactor
            this.scaleChildFactor = scaleChildFactor
        }

        constructor(parcel: Parcel) : super(parcel) {
            bubbles = parcel.createTypedArrayList(Bubble.CREATOR)
            selectedBubble = parcel.readParcelable(Bubble::class.java.classLoader)
            offsetX = parcel.readFloat()
            offsetY = parcel.readFloat()
            offsetChildX = parcel.readFloat()
            offsetChildY = parcel.readFloat()
            scaleFactor = parcel.readFloat()
            scaleChildFactor = parcel.readFloat()
        }

        override fun writeToParcel(out: Parcel?, flags: Int) {
            super.writeToParcel(out, flags)
            out?.apply {
                writeTypedList(bubbles)
                writeParcelable(selectedBubble, flags)
                writeFloat(offsetX)
                writeFloat(offsetY)
                writeFloat(offsetChildX)
                writeFloat(offsetChildY)
                writeFloat(scaleFactor)
                writeFloat(scaleChildFactor)
            }
        }

        companion object CREATOR : Parcelable.Creator<SavedState> {
            override fun createFromParcel(parcel: Parcel): SavedState {
                return SavedState(parcel)
            }

            override fun newArray(size: Int): Array<SavedState?> {
                return arrayOfNulls(size)
            }
        }

    }

}