package com.mad.mad_bookworms.profile.spinWheel

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.profile.LuckyItem
import com.mad.mad_bookworms.profile.spinWheel.LuckyWheelUtils.convertDpToPixel
import com.mad.mad_bookworms.profile.spinWheel.PielView.PieRotateListener

/**
 * Created by kiennguyen on 11/5/16.
 */
class LuckyWheelView : RelativeLayout, PieRotateListener {
    private var mBackgroundColor = 0
    private var mTextColor = 0
    private var mTopTextSize = 0
    private var mSecondaryTextSize = 0
    private var mBorderColor = 0
    private var mTopTextPadding = 0
    private var mEdgeWidth = 0
    private var mCenterImage: Drawable? = null
    private var mCursorImage: Drawable? = null
    var pielView: PielView? = null
    private var ivCursorView: ImageView? = null
    private var mLuckyRoundItemSelectedListener: LuckyRoundItemSelectedListener? = null
    override fun rotateDone(index: Int) {
        if (mLuckyRoundItemSelectedListener != null) {
            mLuckyRoundItemSelectedListener!!.LuckyRoundItemSelected(index)
        }
    }

    interface LuckyRoundItemSelectedListener {
        fun LuckyRoundItemSelected(index: Int)
    }

    fun setLuckyRoundItemSelectedListener(listener: LuckyRoundItemSelectedListener?) {
        mLuckyRoundItemSelectedListener = listener
    }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    /**
     * @param ctx
     * @param attrs
     */
    private fun init(ctx: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val typedArray = ctx.obtainStyledAttributes(attrs, R.styleable.LuckyWheelView)
            mBackgroundColor =
                typedArray.getColor(R.styleable.LuckyWheelView_lkwBackgroundColor, -0x340000)
            mTopTextSize = typedArray.getDimensionPixelSize(
                R.styleable.LuckyWheelView_lkwTopTextSize,
                convertDpToPixel(25f, context).toInt()
            )
            mSecondaryTextSize = typedArray.getDimensionPixelSize(
                R.styleable.LuckyWheelView_lkwSecondaryTextSize,
                convertDpToPixel(10f, context).toInt()
            )
            mTextColor = typedArray.getColor(R.styleable.LuckyWheelView_lkwTopTextColor, 0)
            mTopTextPadding = typedArray.getDimensionPixelSize(
                R.styleable.LuckyWheelView_lkwTopTextPadding,
                convertDpToPixel(22f, context).toInt()
            ) + convertDpToPixel(15f, context).toInt()
            mCursorImage = typedArray.getDrawable(R.styleable.LuckyWheelView_lkwCursor)
            mCenterImage = typedArray.getDrawable(R.styleable.LuckyWheelView_lkwCenterImage)
            mEdgeWidth = typedArray.getInt(R.styleable.LuckyWheelView_lkwEdgeWidth, 10)
            mBorderColor = typedArray.getColor(R.styleable.LuckyWheelView_lkwEdgeColor, 0)
            typedArray.recycle()
        }
        val inflater = LayoutInflater.from(context)
        val frameLayout = inflater.inflate(R.layout.lucky_wheel_layout, this, false) as FrameLayout
        pielView = frameLayout.findViewById(R.id.pieView)
        ivCursorView = frameLayout.findViewById(R.id.cursorView)

        pielView!!.setPieRotateListener(this);
        pielView!!.setPieBackgroundColor(mBackgroundColor);
        pielView!!.setTopTextPadding(mTopTextPadding);
        pielView!!.setTopTextSize(mTopTextSize);
        pielView!!.setSecondaryTextSizeSize(mSecondaryTextSize);
        pielView!!.setPieCenterImage(mCenterImage);
        pielView!!.setBorderColor(mBorderColor);
        pielView!!.setBorderWidth(mEdgeWidth);


        if (mTextColor != 0)
            pielView!!.setPieTextColor(mTextColor);

        ivCursorView!!.setImageDrawable(mCursorImage)
        addView(frameLayout)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        //This is to control that the touch events triggered are only going to the PieView
        for (i in 0 until childCount) {
            if (isPielView(getChildAt(i))) {
                return super.dispatchTouchEvent(ev)
            }
        }
        return false
    }

    private fun isPielView(view: View): Boolean {
        if (view is ViewGroup) {
            for (i in 0 until childCount) {
                if (isPielView(view.getChildAt(i))) {
                    return true
                }
            }
        }
        return view is PielView
    }

    fun setLuckyWheelBackgrouldColor(color: Int) {
        pielView!!.setPieBackgroundColor(color)
    }

    fun setLuckyWheelCursorImage(drawable: Int) {
        ivCursorView!!.setBackgroundResource(drawable)
    }

    fun setLuckyWheelCenterImage(drawable: Drawable?) {
        pielView!!.setPieCenterImage(drawable)
    }

    fun setBorderColor(color: Int) {
        pielView!!.setBorderColor(color)
    }

    fun setLuckyWheelTextColor(color: Int) {
        pielView!!.setPieTextColor(color)
    }

    /**
     * @param data
     */
    fun setData(data: List<LuckyItem?>?) {
        pielView!!.setData(data as List<LuckyItem>?)
    }

    /**
     * @param numberOfRound
     */
    fun setRound(numberOfRound: Int) {
        pielView!!.setRound(numberOfRound)
    }

    /**
     * @param fixedNumber
     */
    fun setPredeterminedNumber(fixedNumber: Int) {
        pielView!!.setPredeterminedNumber(fixedNumber)
    }

    fun startLuckyWheelWithTargetIndex(index: Int) {
        pielView!!.rotateTo(index)
    }
}
