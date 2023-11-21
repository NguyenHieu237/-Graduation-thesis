/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.apero.realistic.widget.myswitch

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.StateListDrawable
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.util.StateSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewConfiguration
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.CompoundButton
import com.apero.realistic.R
import com.apero.realistic.utils.ScreenUtil
import kotlin.math.roundToInt

/**
 * A Switch is a two-state toggle switch widget that can select between two
 * options. The user may drag the "thumb" back and forth to choose the selected option,
 * or simply tap to toggle as if it were a checkbox. The [text][.setText]
 * property controls the text displayed in the label for the switch, whereas the
 * [off][.setTextOff] and [on][.setTextOn] text
 * controls the text on the thumb. Similarly, the
 * [textAppearance][.setTextAppearance] and the related
 * setTypeface() methods control the typeface and style of label text, whereas the
 * [switchTextAppearance][.setSwitchTextAppearance] and
 * the related seSwitchTypeface() methods control that of the thumb.
 */
class MySwitch @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = R.attr.mySwitchStyleAttr
) : CompoundButton(context, attrs, defStyle) {
    private var mOrientation = HORIZONTAL
    private var mOnChangeAttemptListener: OnChangeAttemptListener? = null
    private val mPushStyle: Boolean
    private val mTextOnThumb: Boolean
    private val mThumbExtraMovement: Int
    private val mLeftBackground: Drawable?
    private val mRightBackground: Drawable?
    private val mMaskDrawable: Drawable?
    private var mThumbDrawable: Drawable?
    private var mTrackDrawable: Drawable?
    private val mThumbTextPadding: Int
    private val mTrackTextPadding: Int
    private val mSwitchMinWidth: Int
    private val mSwitchMinHeight: Int
    private val mSwitchPadding: Int
    private var mTextOn: CharSequence
    private var mTextOff: CharSequence
    private val mDrawableOn: Drawable?
    private val mDrawableOff: Drawable?

    /**
     * returns if the switch is fixed to one of its positions
     */
    var isFixed = false
        private set
    private var clickDisabled = false
    private var onOrOff = true
    private var pushBitmap: Bitmap? = null
    private var maskBitmap: Bitmap? = null
    private var tempBitmap: Bitmap? = null
    private var backingLayer: Canvas? = null
    private var mTouchMode = 0
    private val mTouchSlop: Int
    private var mTouchX = 0f
    private var mTouchY = 0f
    private val mVelocityTracker = VelocityTracker.obtain()
    private val mMinFlingVelocity: Int
    private var mThumbPosition = 0f
    private var mSwitchWidth = 0
    private var mSwitchHeight = 0
    private var mThumbWidth = 0
    private var mThumbHeight = 0
    private var mSwitchLeft = 0
    private var mSwitchTop = 0
    private var mSwitchRight = 0
    private var mSwitchBottom = 0
    private val mTextPaint: TextPaint
    private var mTextColors: ColorStateList? = null
    private var mOnLayout: Layout? = null
    private var mOffLayout: Layout? = null
    private val xferPaint: Paint
    private var leftBitmap: Bitmap? = null
    private var rightBitmap: Bitmap? = null
    private val mTrackPaddingRect = Rect()
    private val mThPad = Rect()
    private val canvasClipBounds = Rect()

    //Animation support
    var mStartTime: Long = 0
    var mStartPosition = 0f
    var mAnimDuration = 0f
    var mMaxAnimDuration = 250f
    var mRunning = false
    val FRAME_DURATION = 10
    var mInterpolator: Interpolator = DecelerateInterpolator()

    /**
     * Sets the switch text color, size, style, hint color, and highlight color
     * from the specified TextAppearance resource.
     */
    fun setSwitchTextAppearance(context: Context, resid: Int) {
        val appearance =
            context.obtainStyledAttributes(resid, R.styleable.mySwitchTextAppearanceAttrib)
        val colors: ColorStateList?
        val ts: Int
        colors =
            appearance.getColorStateList(R.styleable.mySwitchTextAppearanceAttrib_textColorSwitch)
        mTextColors =
            colors ?: // If no color set in TextAppearance, default to the view's textColor
                    textColors
        ts = appearance.getDimensionPixelSize(
            R.styleable.mySwitchTextAppearanceAttrib_textSizeSwitch,
            0
        )
        if (ts != 0) {
            if (ts.toFloat() != mTextPaint.textSize) {
                mTextPaint.textSize = ts.toFloat()
                requestLayout()
            }
        }
        val typefaceIndex: Int
        val styleIndex: Int
        typefaceIndex = appearance.getInt(R.styleable.mySwitchTextAppearanceAttrib_typeface, -1)
        styleIndex = appearance.getInt(R.styleable.mySwitchTextAppearanceAttrib_textStyle, -1)
        setSwitchTypefaceByIndex(typefaceIndex, styleIndex)
        appearance.recycle()
    }

    private fun setSwitchTypefaceByIndex(typefaceIndex: Int, styleIndex: Int) {
        var tf: Typeface? = null
        when (typefaceIndex) {
            SANS -> tf = Typeface.SANS_SERIF
            SERIF -> tf = Typeface.SERIF
            MONOSPACE -> tf = Typeface.MONOSPACE
        }
        setSwitchTypeface(tf, styleIndex)
    }

    /**
     * Sets the typeface and style in which the text should be displayed on the
     * switch, and turns on the fake bold and italic bits in the Paint if the
     * Typeface that you provided does not have all the bits in the
     * style that you specified.
     */
    fun setSwitchTypeface(tf: Typeface?, style: Int) {
        var tf = tf
        if (style > 0) {
            tf = if (tf == null) {
                Typeface.defaultFromStyle(style)
            } else {
                Typeface.create(tf, style)
            }
            setSwitchTypeface(tf)
            // now compute what (if any) algorithmic styling is needed
            val typefaceStyle = tf?.style ?: 0
            val need = style and typefaceStyle.inv()
            mTextPaint.isFakeBoldText = need and Typeface.BOLD != 0
            mTextPaint.setTextSkewX((if (need and Typeface.ITALIC != 0) -0.25f else 0) as Float)
        } else {
            mTextPaint.isFakeBoldText = false
            mTextPaint.textSkewX = 0f
            setSwitchTypeface(tf)
        }
    }

    /**
     * Sets the typeface in which the text should be displayed on the switch.
     * Note that not all Typeface families actually have bold and italic
     * variants, so you may need to use
     * [.setSwitchTypeface] to get the appearance
     * that you actually want.
     *
     * @attr ref android.R.styleable#TextView_typeface
     * @attr ref android.R.styleable#TextView_textStyle
     */
    fun setSwitchTypeface(tf: Typeface?) {
        if (mTextPaint.typeface !== tf) {
            mTextPaint.typeface = tf
            requestLayout()
            invalidate()
        }
    }
    /**
     * Returns the text displayed when the button is in the checked state.
     */
    /**
     * Sets the text displayed when the button is in the checked state.
     */
    var textOn: CharSequence
        get() = mTextOn
        set(textOn) {
            mTextOn = textOn
            mOnLayout = null
            requestLayout()
        }
    /**
     * Returns the text displayed when the button is not in the checked state.
     */
    /**
     * Sets the text displayed when the button is not in the checked state.
     */
    var textOff: CharSequence
        get() = mTextOff
        set(textOff) {
            mTextOff = textOff
            mOffLayout = null
            requestLayout()
        }

    /**
     * Interface definition for a callback to be invoked when the switch is
     * in a fixed state and there was an attempt to change its state either
     * via a click or drag
     */
    interface OnChangeAttemptListener {
        /**
         * Called when an attempt was made to change the checked state of the
         * switch while the switch was in a fixed state.
         *
         * @param isChecked  The current state of switch.
         */
        fun onChangeAttempted(isChecked: Boolean)
    }

    /**
     * Register a callback to be invoked when there is an attempt to change the
     * state of the switch when its in fixated
     *
     * @param listener the callback to call on checked state change
     */
    fun setOnChangeAttemptListener(listener: OnChangeAttemptListener?) {
        mOnChangeAttemptListener = listener
    }

    /**
     * fixates the switch on one of the positions ON or OFF.
     * if the switch is fixated, then it cannot be switched to the other position
     *
     * @param fixed   If true, sets the switch to fixed mode.
     * If false, sets the switch to switched mode.
     * @param onOrOff The switch position to which it will be fixed.
     * If it is true then the switch is fixed on ON.
     * If it is false then the switch is fixed on OFF
     * @Note The position is only fixed from the user interface. It can still be
     * changed through program by using [setChecked][.setChecked]
     */
    fun fixate(fixed: Boolean, onOrOff: Boolean) {
        fixate(fixed)
        this.onOrOff = onOrOff
        if (onOrOff) this.isChecked = true
    }

    /**
     * fixates the switch on one of the positions ON or OFF.
     * if the switch is fixated, then it cannot be switched to the other position
     *
     * @param fixed   if true, sets the switch to fixed mode.
     * if false, sets the switch to switched mode.
     */
    fun fixate(fixed: Boolean) {
        isFixed = fixed
    }

    private fun makeLayout(text: CharSequence): Layout {
        return StaticLayout(
            text, mTextPaint,
            Math.ceil(Layout.getDesiredWidth(text, mTextPaint).toDouble()).roundToInt(),
            Layout.Alignment.ALIGN_NORMAL, 1f, 0f, true
        )
    }

    /**
     * @return true if (x, y) is within the target area of the switch thumb
     */
    private fun hitThumb(x: Float, y: Float): Boolean {
        if (mOrientation == HORIZONTAL) {
            val thumbTop = mSwitchTop - mTouchSlop
            val thumbLeft = mSwitchLeft + (mThumbPosition + 0.5f).toInt() - mTouchSlop
            val thumbRight = thumbLeft + mThumbWidth + mTouchSlop // + mThPad.left + mThPad.right
            val thumbBottom = mSwitchBottom + mTouchSlop
            return x > thumbLeft && x < thumbRight && y > thumbTop && y < thumbBottom
        }

        //if (mOrientation == VERTICAL)
        return if (mSwitchHeight > 150) {
            val thumbLeft = mSwitchLeft - mTouchSlop
            val thumbTop = mSwitchTop + (mThumbPosition + 0.5f).toInt() - mTouchSlop
            val thumbBottom = thumbTop + mThumbHeight + mTouchSlop // + mThPad.top + mThPad.bottom
            val thumbRight = mSwitchRight + mTouchSlop
            Log.d(
                TAG,
                "returning " + (x > thumbLeft && x < thumbRight && y > thumbTop && y < thumbBottom)
            )
            x > thumbLeft && x < thumbRight && y > thumbTop && y < thumbBottom
        } else {
            x > mSwitchLeft && x < mSwitchRight && y > mSwitchTop && y < mSwitchBottom
        }
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        //if (fixed) {
        //Log.d(TAG, "the switch position is fixed to " + (onOrOff ? "On":"Off") + "position.");
        //return true;
        //}
        mVelocityTracker.addMovement(ev)
        //Log.d(TAG, "onTouchEvent(ev="+ev.toString()+")");
        //Log.d(TAG, "mTouchMode="+mTouchMode);
        val action = ev.actionMasked
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                val x = ev.x
                val y = ev.y
                if (isEnabled && hitThumb(x, y)) {
                    mTouchMode = TOUCH_MODE_DOWN
                    mTouchX = x
                    mTouchY = y
                }
            }

            MotionEvent.ACTION_MOVE -> {
                when (mTouchMode) {
                    TOUCH_MODE_IDLE -> {}
                    TOUCH_MODE_DOWN -> {
                        val x = ev.x
                        val y = ev.y
                        if (Math.abs(x - mTouchX) > mTouchSlop / 2 ||
                            Math.abs(y - mTouchY) > mTouchSlop / 2
                        ) {
                            mTouchMode = TOUCH_MODE_DRAGGING
                            if (parent != null) {
                                parent.requestDisallowInterceptTouchEvent(true)
                            }
                            mTouchX = x
                            mTouchY = y
                            return true
                        }
                    }

                    TOUCH_MODE_DRAGGING -> {
                        if (mOrientation == HORIZONTAL) {
                            val x = ev.x
                            val dx = x - mTouchX
                            val newPos = Math.max(
                                0f,
                                Math.min(
                                    mThumbPosition + dx,
                                    thumbScrollRange.toFloat()
                                )
                            )
                            if (newPos != mThumbPosition) {
                                mThumbPosition = newPos
                                mTouchX = x
                                invalidate()
                            }
                            return true
                        }
                        if (mOrientation == VERTICAL) {
                            val y = ev.y
                            val dy = y - mTouchY
                            val newPos = Math.max(
                                0f,
                                Math.min(
                                    mThumbPosition + dy,
                                    thumbScrollRange.toFloat()
                                )
                            )
                            if (newPos != mThumbPosition) {
                                mThumbPosition = newPos
                                mTouchY = y
                                invalidate()
                            }
                            return true
                        }
                    }
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (mTouchMode == TOUCH_MODE_DRAGGING) {
                    stopDrag(ev)
                    return true
                }
                mTouchMode = TOUCH_MODE_IDLE
                mVelocityTracker.clear()
            }
        }

        //Log.d(TAG, "mThumbPosition="+mThumbPosition);
        //Log.d(TAG, "super.onTouchEvent(ev) returned="+flag);
        return super.onTouchEvent(ev)
    }

    /*
        public  void toggleWithAnimation(boolean animate) {
    	toggle();
    	if (animate) {
            animateThumbToCheckedState(isChecked());
    	}
    }
    
    //@Override
    public boolean isChecked1() {
    	Log.d(TAG, "isChecked()-mTextOnThumb="+mTextOnThumb);
        if (mTextOnThumb) {
    	    Log.d(TAG, "returning = "+super.isChecked());
        	return super.isChecked();
        }
   	    Log.d(TAG, "returning="+(!(super.isChecked())));
       	return !(super.isChecked());
    }
    
	OnClickListener clickListener = new OnClickListener() {
		public void onClick(View v) {
            Log.d(TAG, "onClick()");
            animateThumbToCheckedState(isChecked());
		}
	}
	*/
    override fun performClick(): Boolean {
        return if (!clickDisabled) {
            // Log.d(TAG, "performClick(). current Value="+isChecked());
            if (mOnChangeAttemptListener != null) mOnChangeAttemptListener!!.onChangeAttempted(
                isChecked
            )
            if (!isFixed) {
                // Log.d(TAG, "after super.performClick().  Value="+isChecked());
                super.performClick()
            } else {
                false
            }
        } else {
            false
        }
    }

    fun disableClick() {
        clickDisabled = true
    }

    fun enableClick() {
        clickDisabled = false
    }

    val currentText: CharSequence
        get() = if (isChecked) mTextOn else mTextOff

    fun getText(checkedState: Boolean): CharSequence {
        return if (checkedState) mTextOn else mTextOff
    }

    private fun cancelSuperTouch(ev: MotionEvent) {
        val cancel = MotionEvent.obtain(ev)
        cancel.action = MotionEvent.ACTION_CANCEL
        super.onTouchEvent(cancel)
        cancel.recycle()
    }

    /**
     * Called from onTouchEvent to end a drag operation.
     *
     * @param ev Event that triggered the end of drag mode - ACTION_UP or ACTION_CANCEL
     */
    private fun stopDrag(ev: MotionEvent) {
        mTouchMode = TOUCH_MODE_IDLE
        // Up and not canceled, also checks the switch has not been disabled during the drag
        var commitChange = ev.action == MotionEvent.ACTION_UP && isEnabled

        //check if the swtich is fixed to a position
        commitChange = commitChange && !isFixed
        cancelSuperTouch(ev)
        if (commitChange) {
            val newState: Boolean
            mVelocityTracker.computeCurrentVelocity(1000)
            newState = if (mOrientation == HORIZONTAL) {
                val xvel = mVelocityTracker.xVelocity
                if (Math.abs(xvel) > mMinFlingVelocity) {
                    xvel > 0
                } else {
                    targetCheckedState
                }
            } else {
                val yvel = mVelocityTracker.yVelocity
                if (Math.abs(yvel) > mMinFlingVelocity) {
                    yvel > 0
                } else {
                    targetCheckedState
                }
            }
            animateThumbToCheckedState(!mTextOnThumb xor newState)
        } else {
            animateThumbToCheckedState(isChecked)
            if (isFixed) if (mOnChangeAttemptListener != null) mOnChangeAttemptListener!!.onChangeAttempted(
                isChecked
            )
        }
    }

    private fun animateThumbToCheckedState(newCheckedState: Boolean) {
        // TODO animate!
        //float targetPos = newCheckedState ? 0 : getThumbScrollRange();
        //mThumbPosition = targetPos;
        isChecked = newCheckedState
    }

    private val targetCheckedState: Boolean
        private get() = mThumbPosition >= thumbScrollRange / 2

    override fun setChecked(checked: Boolean) {
        //Log.d(TAG, "setChecked("+checked+")");
        super.setChecked(checked)
        val newPos = (if (checked xor !mTextOnThumb) thumbScrollRange else 0).toFloat()
        if (mThumbPosition != newPos) {
            startAnimation()
        }
        //mThumbPosition= (checked ^ !mTextOnThumb) ? getThumbScrollRange() : 0;
        //invalidate();
    }//mThumbPosition= (isChecked() ^ !mTextOnThumb) ? getThumbScrollRange() : 0;

    //need to evaluate this once
    /*
         protected void onLayout_orig(boolean changed, int left, int top, int right, int bottom) {
             //Log.d(TAG, "left=" + left + ",top="+top+",right="+right+",bottom="+bottom);
             super.onLayout(changed, left, top, right, bottom);
     
             mThumbPosition = isChecked() ? getThumbScrollRange() : 0;
     
             int switchRight = getWidth() - getPaddingRight();
             int switchLeft = switchRight - mSwitchWidth;
             int switchTop = 0;
             int switchBottom = 0;
             switch (getGravity() & Gravity.VERTICAL_GRAVITY_MASK) {
                 default:
                 case Gravity.TOP:
                     switchTop = getPaddingTop();
                     switchBottom = switchTop + mSwitchHeight;
                     break;
     
                 case Gravity.CENTER_VERTICAL:
                     switchTop = (getPaddingTop() + getHeight() - getPaddingBottom()) / 2 -
                             mSwitchHeight / 2;
                     switchBottom = switchTop + mSwitchHeight;
                     break;
     
                 case Gravity.BOTTOM:
                     switchBottom = getHeight() - getPaddingBottom();
                     switchTop = switchBottom - mSwitchHeight;
                     break;
             }
     
             mSwitchLeft = switchLeft;
             mSwitchTop = switchTop;
             mSwitchBottom = switchBottom;
             mSwitchRight = switchRight;
             //Log.d(TAG, "mSwitchLeft="+mSwitchLeft+" mSwitchRight="+mSwitchRight);
             //Log.d(TAG, "mSwitchTop="+mSwitchTop+" mSwitchBottom="+mSwitchBottom);
         }
         */
    private var thumbPosition: Float
        private get() {
            val sr = thumbScrollRange.toFloat()
            val chk: Float = if (isChecked xor !mTextOnThumb) sr else 0f
            val nchk = sr - chk
            return (mThumbPosition - nchk) / (chk - nchk)
            //mThumbPosition= (isChecked() ^ !mTextOnThumb) ? getThumbScrollRange() : 0;
        }
        private set(pos) {
            var sr = 0f

            //need to evaluate this once
            if (sr == 0f) sr = thumbScrollRange.toFloat()
            val chk: Float = if (isChecked xor !mTextOnThumb) sr else 0f
            val nchk = sr - chk
            mThumbPosition = nchk + (chk - nchk) * pos
            invalidate()
        }

    private fun resetAnimation() {
        mStartTime = System.currentTimeMillis()
        mStartPosition = thumbPosition
        mAnimDuration = (mMaxAnimDuration * (1f - mStartPosition)).toInt().toFloat()
    }

    private fun startAnimation() {
        if (handler != null) {
            resetAnimation()
            mRunning = true
            //getHandler().postDelayed(mUpdater, FRAME_DURATION);
            handler.post(mUpdater)
        } else {
            thumbPosition = 1f
        }
        invalidate()
    }

    private fun stopAnimation() {
        mRunning = false
        thumbPosition = 1f
        if (handler != null) {
            handler.removeCallbacks(mUpdater)
        }
        invalidate()
    }

    private val mUpdater: Runnable = object : Runnable {
        override fun run() {
            val curTime = System.currentTimeMillis()
            val progress = Math.min(1f, (curTime - mStartTime).toFloat() / mAnimDuration)
            val value = mInterpolator.getInterpolation(progress)
            thumbPosition = mStartPosition * (1 - value) + value
            if (progress == 1f) {
                stopAnimation()
            } else if (mRunning) {
                if (handler != null) {
                    //getHandler().postDelayed(mUpdater, FRAME_DURATION);
                    handler.post(this)
                } else {
                    stopAnimation()
                }
            }
        }
    }
    /**
     * Construct a new MySwitch with a default style determined by the given theme attribute,
     * overriding specific style attributes as requested.
     *
     * @param context The Context that will determine this widget's theming.
     * @param attrs Specification of attributes that should deviate from the default styling.
     * @param defStyle An attribute ID within the active theme containing a reference to the
     * default style for this widget. e.g. android.R.attr.switchStyle.
     */
    /**
     * Construct a new MySwitch with default styling, overriding specific style
     * attributes as requested.
     *
     * @param context The Context that will determine this widget's theming.
     * @param attrs Specification of attributes that should deviate from default styling.
     */
    /**
     * Construct a new MySwitch with default styling.
     *
     * @param context The Context that will determine this widget's theming.
     */
    init {

        //if (Build.VERSION.SDK_INT >= 11) {
        //setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        //}
        val res = resources
        val density = res.displayMetrics.scaledDensity
        val textNormal = -0x1000000 //res.getColor(R.color.textNormal)
        val textInvertedNormal = -0x1 //res.getColor(R.color.textInvertedNormal);
        val colorAccent = -0x3400 //res.getColor(R.color.colorAccent)
        mTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint.density = res.displayMetrics.density
        mTextPaint.setShadowLayer(0.5f, 1.0f, 1.0f, textNormal)
        mTextPaint.textSize = 16 * density
        mTextPaint.color = textInvertedNormal
        val a = context.obtainStyledAttributes(attrs, R.styleable.MySwitch, defStyle, 0)
        mLeftBackground = a.getDrawable(R.styleable.MySwitch_leftBackground)
        mRightBackground = a.getDrawable(R.styleable.MySwitch_rightBackground)
        mOrientation = a.getInteger(R.styleable.MySwitch_orientation, HORIZONTAL)
        mThumbDrawable = a.getDrawable(R.styleable.MySwitch_thumb)
        mTrackDrawable = a.getDrawable(R.styleable.MySwitch_track)
        mTextOn = a.getText(R.styleable.MySwitch_textOn)
        mTextOff = a.getText(R.styleable.MySwitch_textOff)
        //mShowText = a.getBoolean(com.android.internal.R.styleable.Switch_showText, true);
        mDrawableOn = a.getDrawable(R.styleable.MySwitch_drawableOn)
        mDrawableOff = a.getDrawable(R.styleable.MySwitch_drawableOff)
        mPushStyle = a.getBoolean(R.styleable.MySwitch_pushStyle, false)
        mTextOnThumb = a.getBoolean(R.styleable.MySwitch_textOnThumb, true)
        mThumbExtraMovement = a.getDimensionPixelSize(R.styleable.MySwitch_thumbExtraMovement, 0)
        mThumbTextPadding =
            a.getDimensionPixelSize(R.styleable.MySwitch_thumbTextPadding, (5 * density).toInt())
        mTrackTextPadding =
            a.getDimensionPixelSize(R.styleable.MySwitch_trackTextPadding, (5 * density).toInt())
        mSwitchMinWidth =
            a.getDimensionPixelSize(R.styleable.MySwitch_switchMinWidth, (60 * density).toInt())
        mSwitchMinHeight = a.getDimensionPixelSize(R.styleable.MySwitch_switchMinHeight, 0)
        mSwitchPadding = a.getDimensionPixelSize(R.styleable.MySwitch_switchPadding, 0)
        if (mThumbDrawable == null) {
            val back = StateListDrawable()
            val backn = GradientDrawable()
            backn.setColor(colorAccent)
            backn.cornerRadius = 12 * res.displayMetrics.scaledDensity
            val backu = GradientDrawable()
            backu.setColor(textNormal)
            backu.cornerRadius = 12 * res.displayMetrics.scaledDensity
            backu.alpha = 255
            val backd = GradientDrawable()
            backd.setColor(colorAccent)
            backd.cornerRadius = 12 * res.displayMetrics.scaledDensity
            backd.alpha = 96
            back.addState(intArrayOf(-android.R.attr.state_enabled), backd)
            back.addState(intArrayOf(-android.R.attr.state_checked), backu)
            back.addState(
                intArrayOf(android.R.attr.state_pressed, android.R.attr.state_checked),
                backn
            )
            back.addState(StateSet.WILD_CARD, backn)
            mThumbDrawable = back
        }
        if (mTrackDrawable == null) {
            val back = StateListDrawable()
            val trk = GradientDrawable()
            trk.setColor(colorAccent)
            trk.cornerRadius = 10 * density
            trk.alpha = 128
            val backu = GradientDrawable()
            backu.setColor(textNormal)
            backu.cornerRadius = 12 * res.displayMetrics.scaledDensity
            backu.alpha = 192
            back.addState(intArrayOf(-android.R.attr.state_checked), backu)
            back.addState(StateSet.WILD_CARD, trk)
            mTrackDrawable = InsetDrawable(back, 0, density.toInt(), 0, density.toInt())
        }
        mTrackDrawable?.getPadding(mTrackPaddingRect)
        Log.d(TAG, "mTrackPaddingRect=$mTrackPaddingRect")
        mThumbDrawable?.getPadding(mThPad)
        Log.d(TAG, "mThPad=$mThPad")
        mMaskDrawable = a.getDrawable(R.styleable.MySwitch_backgroundMask)
        var e: RuntimeException? = null
        if ((mLeftBackground != null || mRightBackground != null) && mMaskDrawable == null) {
            e = IllegalArgumentException(
                a.positionDescription
                        + " if left/right background is given, then a mask has to be there"
            )
        }
        if ((mLeftBackground != null) xor (mRightBackground != null) && mMaskDrawable == null) {
            e = IllegalArgumentException(
                a.positionDescription
                        + " left and right background both should be there. only one is not allowed "
            )
        }
        if (mTextOnThumb && mPushStyle) {
            e = IllegalArgumentException(
                a.positionDescription
                        + " Text On Thumb and Push Style are mutually exclusive. Only one can be present "
            )
        }
        xferPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        //xferPaint.setColor(Color.TRANSPARENT);
        xferPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        val appearance = a.getResourceId(R.styleable.MySwitch_switchTextAppearanceAttrib, 0)
        if (appearance != 0) {
            setSwitchTextAppearance(context, appearance)
        }
        a.recycle()
        if (e != null) {
            throw e
        }
        val config = ViewConfiguration.get(context)
        mTouchSlop = config.scaledTouchSlop
        mMinFlingVelocity = config.scaledMinimumFlingVelocity
        mInterpolator = DecelerateInterpolator()
        // Refresh display with current params
        refreshDrawableState()
        isChecked = isChecked
        this.isClickable = true
    }

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)
        if (mOnLayout == null) {
            mOnLayout = makeLayout(mTextOn)
        }
        if (mOffLayout == null) {
            mOffLayout = makeLayout(mTextOff)
        }
        val maxTextWidth = Math.max(mOnLayout!!.width, mOffLayout!!.width)
        val maxTextHeight = Math.max(mOnLayout!!.height, mOffLayout!!.height)
        mThumbWidth = maxTextWidth + mThumbTextPadding * 2 + mThPad.left + mThPad.right
        mThumbWidth = Math.max(mThumbWidth, mThumbDrawable!!.intrinsicWidth)
        if (mTextOnThumb == false) {
            mThumbWidth = mThumbDrawable!!.intrinsicWidth
            if (mThumbWidth < 15) {
                //TODO: change this to something guessed based on the other parameters.
                mThumbWidth = 15
            }
        }
        mThumbHeight = maxTextHeight + mThumbTextPadding * 2 + mThPad.bottom + mThPad.top
        mThumbHeight = Math.max(mThumbHeight, mThumbDrawable!!.intrinsicHeight)
        if (mTextOnThumb == false) {
            mThumbHeight = mThumbDrawable!!.intrinsicHeight
            if (mThumbHeight < 15) {
                //TODO: change this to something guessed based on the other parameters.
                mThumbHeight = 15
            }
        }
        Log.d(TAG, "mThumbWidth=$mThumbWidth")
        Log.d(TAG, "mThumbHeight=$mThumbHeight")
        var switchWidth: Int
        if (mOrientation == HORIZONTAL) {
            switchWidth = Math.max(
                mSwitchMinWidth, maxTextWidth * 2 + mThumbTextPadding * 2 + mTrackTextPadding * 2 +
                        mTrackPaddingRect.left + mTrackPaddingRect.right
            )
            if (mTextOnThumb == false) {
                switchWidth = Math.max(
                    (maxTextWidth
                            + mThumbWidth) + mTrackTextPadding * 2 + (mTrackPaddingRect.right + mTrackPaddingRect.left) / 2,
                    mSwitchMinWidth
                )
            }
            if (mPushStyle) {
                switchWidth = Math.max(
                    mSwitchMinWidth, maxTextWidth + mThumbWidth +
                            mTrackTextPadding + (mTrackPaddingRect.left + mTrackPaddingRect.right) / 2
                )
            }
        } else {
            switchWidth = Math.max(
                maxTextWidth + mThumbTextPadding * 2 + mThPad.left + mThPad.right,
                mThumbWidth
            )
            if (mPushStyle || mTextOnThumb == false) {
                switchWidth = Math.max(
                    maxTextWidth + mTrackTextPadding * 2 +
                            mTrackPaddingRect.left + mTrackPaddingRect.right,
                    mThumbWidth
                )
            }
        }
        switchWidth = Math.max(mSwitchMinWidth, switchWidth)
        val trackHeight = mTrackDrawable!!.intrinsicHeight
        val thumbHeight = mThumbDrawable!!.intrinsicHeight
        var switchHeight = Math.max(mSwitchMinHeight, maxTextHeight)
        switchHeight = Math.max(trackHeight, switchHeight)
        switchHeight = Math.max(switchHeight, thumbHeight)
        if (mOrientation == VERTICAL) {
            switchHeight =
                mOnLayout!!.height + mOffLayout!!.height + mThumbTextPadding * 2 + mThPad.top + mThPad.bottom +
                        mTrackPaddingRect.bottom + mTrackPaddingRect.top + mTrackTextPadding * 2
            if (mTextOnThumb == false) {
                switchHeight = Math.max(
                    mThumbHeight + maxTextHeight + (mTrackPaddingRect.bottom + mTrackPaddingRect.top) / 2 + mTrackTextPadding * 2,
                    mSwitchMinHeight
                )
            }
            if (mPushStyle) {
                switchHeight = Math.max(
                    mSwitchMinHeight, maxTextHeight + mThumbHeight +
                            mTrackTextPadding + (mTrackPaddingRect.top + mTrackPaddingRect.bottom) / 2
                )
            }
        }
        when (widthMode) {
            MeasureSpec.AT_MOST -> widthSize = Math.min(widthSize, switchWidth)
            MeasureSpec.UNSPECIFIED -> widthSize = switchWidth
            MeasureSpec.EXACTLY -> widthSize = switchWidth
        }
        when (heightMode) {
            MeasureSpec.AT_MOST -> heightSize = Math.min(heightSize, switchHeight)
            MeasureSpec.UNSPECIFIED -> heightSize = switchHeight
            MeasureSpec.EXACTLY -> heightSize = switchHeight
        }
        mSwitchWidth = switchWidth
        mSwitchHeight = switchHeight
        Log.d(TAG, "onMeasure():mSwitchWidth=$mSwitchWidth mSwitchHeight=$mSwitchHeight")
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val measuredHeight = measuredHeight
        val measuredWidth = measuredWidth
        if (measuredHeight < switchHeight) {
            setMeasuredDimension(getMeasuredWidth(), switchHeight)
        }
        if (measuredWidth < switchWidth) {
            setMeasuredDimension(switchWidth, getMeasuredHeight())
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        Log.d(TAG, "onLayout()-left=$left,top=$top,right=$right,bottom=$bottom")
        super.onLayout(changed, left, top, right, bottom)
        var switchTop = 0
        var switchBottom = 0
        when (gravity and Gravity.VERTICAL_GRAVITY_MASK) {
            Gravity.TOP -> {
                switchTop = paddingTop
                switchBottom = switchTop + mSwitchHeight
            }

            Gravity.CENTER_VERTICAL -> {
                switchTop = (paddingTop + height - paddingBottom) / 2 -
                        mSwitchHeight / 2
                switchBottom = switchTop + mSwitchHeight
            }

            Gravity.BOTTOM -> {
                switchBottom = height - paddingBottom
                switchTop = switchBottom - mSwitchHeight
            }

            else -> {
                switchTop = paddingTop
                switchBottom = switchTop + mSwitchHeight
            }
        }

        //mSwitchWidth = right - left;
        //mSwitchHeight = bottom - top;
        mSwitchBottom = mSwitchHeight - paddingBottom
        mSwitchTop = mSwitchBottom - mSwitchHeight
        mSwitchRight = mSwitchWidth - paddingRight
        mSwitchLeft = mSwitchRight - mSwitchWidth
        mThumbPosition = if (mTextOnThumb) {
            (if (isChecked) thumbScrollRange else 0).toFloat()
        } else {
            (if (isChecked) 0 else thumbScrollRange).toFloat()
        }

        /*
        Log.d(TAG, "getWidth()="+getWidth()+" getHeight()="+getHeight());
        Log.d(TAG, "getPaddingLeft()="+getPaddingLeft()+" getPaddingRight()="+getPaddingRight());
        Log.d(TAG, "getPaddingTop()="+getPaddingTop()+" getPaddingBottom()="+getPaddingBottom());

        Log.d(TAG, "mSwitchWidth="+mSwitchWidth+" mSwitchHeight="+mSwitchHeight);
        Log.d(TAG, "mSwitchLeft="+mSwitchLeft+" mSwitchRight="+mSwitchRight);
        Log.d(TAG, "mSwitchTop="+mSwitchTop+" mSwitchBottom="+mSwitchBottom);
        */

        //now that the layout is known, prepare the drawables
        mTrackDrawable!!.setBounds(mSwitchLeft, mSwitchTop, mSwitchRight, mSwitchBottom)
        mDrawableOn?.setBounds(0, 0, mDrawableOn.intrinsicWidth, mDrawableOn.intrinsicHeight)
        mDrawableOff?.setBounds(0, 0, mDrawableOff.intrinsicWidth, mDrawableOff.intrinsicHeight)
        mLeftBackground?.setBounds(mSwitchLeft, mSwitchTop, mSwitchRight, mSwitchBottom)
        mRightBackground?.setBounds(mSwitchLeft, mSwitchTop, mSwitchRight, mSwitchBottom)
        if (mMaskDrawable != null) {
            tempBitmap = Bitmap.createBitmap(
                mSwitchRight - mSwitchLeft,
                mSwitchBottom - mSwitchTop,
                Bitmap.Config.ARGB_8888
            )
            backingLayer = Canvas(tempBitmap!!)
            mMaskDrawable.setBounds(mSwitchLeft, mSwitchTop, mSwitchRight, mSwitchBottom)
            //Log.d(TAG,"bitmap width="+tempBitmap.getWidth()+" bitmap.height="+tempBitmap.getHeight());
            //Log.d(TAG,"bitmap 0,0="+String.format("%x", (tempBitmap.getPixel(0,0)))+" bitmap 40,40="+String.format("%x", (tempBitmap.getPixel(40,40))));
            //Bitmap maskBitmap = Bitmap.createBitmap(mSwitchRight - mSwitchLeft, mSwitchBottom - mSwitchTop,  Config.ARGB_8888);
            //Canvas maskLayer = new Canvas(maskBitmap);
            mMaskDrawable.draw(backingLayer!!)
            //Log.d(TAG,"mask width="+maskBitmap.getWidth()+" mask.height="+maskBitmap.getHeight());
            //Log.d(TAG,"mask 0,0="+String.format("%x", (maskBitmap.getPixel(0,0)))+" mask 40,40="+String.format("%x", (maskBitmap.getPixel(40,40))));
            maskBitmap = Bitmap.createBitmap(
                mSwitchRight - mSwitchLeft,
                mSwitchBottom - mSwitchTop,
                Bitmap.Config.ARGB_8888
            )
            val width = tempBitmap!!.getWidth()
            val height = tempBitmap!!.getHeight()
            for (x in 0 until width) {
                for (y in 0 until height) {
                    maskBitmap!!.setPixel(x, y, tempBitmap!!.getPixel(x, y) and -0x1000000)
                }
            }

            //This should work. But does not work on any of the devices I have Nexus 4, Nexus7, Nexus10
            //maskBitmap = tempBitmap.extractAlpha();

            //Log.d(TAG,"mask 0,0="+String.format("%x", (maskBitmap.getPixel(0,0)))+" mask 40,40="+String.format("%x", (maskBitmap.getPixel(40,40))));
            if (mLeftBackground != null) {
                mLeftBackground.draw(backingLayer!!)
                //leftBitmap = Bitmap.createBitmap(mSwitchRight - mSwitchLeft, mSwitchBottom - mSwitchTop,  Config.ARGB_8888);
                //Canvas backingLayer2 = new Canvas(leftBitmap);
                //backingLayer2.drawBitmap(tempBitmap, 0, 0, null);
                //backingLayer2.drawBitmap(maskBitmap, 0, 0, xferPaint);
                backingLayer!!.drawBitmap(maskBitmap!!, 0f, 0f, xferPaint)
                leftBitmap = tempBitmap!!.copy(tempBitmap!!.getConfig(), true)
            }
            if (mRightBackground != null) {
                mRightBackground.draw(backingLayer!!)
                //rightBitmap = Bitmap.createBitmap(mSwitchRight - mSwitchLeft, mSwitchBottom - mSwitchTop,  Config.ARGB_8888);
                //Canvas backingLayer3 = new Canvas(rightBitmap);
                //backingLayer3.drawBitmap(tempBitmap, 0, 0, null);
                //backingLayer3.drawBitmap(maskBitmap, 0, 0, xferPaint);
                backingLayer!!.drawBitmap(maskBitmap!!, 0f, 0f, xferPaint)
                rightBitmap = tempBitmap!!.copy(tempBitmap!!.getConfig(), true)
            }
        }
        if (mPushStyle) {
            //final int switchInnerLeft = mSwitchLeft + mTrackPaddingRect.left;
            val switchInnerTop = mSwitchTop + mTrackPaddingRect.top
            //final int switchInnerRight = mSwitchRight - mTrackPaddingRect.right;
            val switchInnerBottom = mSwitchBottom - mTrackPaddingRect.bottom
            val switchVerticalMid = (switchInnerTop + switchInnerBottom) / 2
            val maxTextWidth = Math.max(mOnLayout!!.width, mOffLayout!!.width)
            val maxTextHeight = Math.max(mOnLayout!!.height, mOffLayout!!.height)
            var width = maxTextWidth * 2 +
                    mTrackPaddingRect.left + mTrackPaddingRect.right +
                    mThumbWidth + mTrackTextPadding * 4
            var height = mSwitchBottom - mSwitchTop
            if (mOrientation == VERTICAL) {
                height = mTrackPaddingRect.top +
                        mTrackTextPadding +
                        maxTextHeight +
                        mTrackTextPadding +
                        mThumbHeight +
                        mTrackTextPadding +
                        maxTextHeight +
                        mTrackTextPadding + mTrackPaddingRect.bottom
                width = mSwitchRight - mSwitchLeft
            }
            Log.d(TAG, "pushBitmap width=$width height=$height")
            pushBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val backingLayer = Canvas(pushBitmap!!)
            mTextPaint.drawableState = drawableState
            // mTextColors should not be null, but just in case
            if (mTextColors != null) {
                mTextPaint.color = mTextColors!!.getColorForState(
                    drawableState,
                    mTextColors!!.defaultColor
                )
            }

            //for vertical orientation leftBitmap is used as top bitmap
            if (leftBitmap != null) {
                backingLayer.save()
                if (backingLayer.getClipBounds(canvasClipBounds)) {
                    if (mOrientation == HORIZONTAL) {
                        canvasClipBounds.right -= width / 2
                    }
                    if (mOrientation == VERTICAL) {
                        canvasClipBounds.bottom -= height / 2
                    }
                    backingLayer.clipRect(canvasClipBounds)
                }
                backingLayer.drawBitmap(leftBitmap!!, 0f, 0f, null)
                backingLayer.restore()
            }
            if (rightBitmap != null) {
                backingLayer.save()
                if (backingLayer.getClipBounds(canvasClipBounds)) {
                    if (mOrientation == HORIZONTAL) {
                        canvasClipBounds.left += width / 2
                    }
                    if (mOrientation == VERTICAL) {
                        canvasClipBounds.top += height / 2
                    }
                    backingLayer.clipRect(canvasClipBounds)
                }
                if (mOrientation == HORIZONTAL) {
                    backingLayer.translate((width / 2 - mTrackPaddingRect.right).toFloat(), 0f)
                }
                if (mOrientation == VERTICAL) {
                    backingLayer.translate(0f, (height / 2 - mTrackPaddingRect.bottom).toFloat())
                }
                backingLayer.drawBitmap(rightBitmap!!, 0f, 0f, null)
                backingLayer.restore()
            }

            /*
            if (mOrientation == HORIZONTAL) {
                backingLayer.translate(mTrackPaddingRect.left, 0);
                backingLayer.save(Canvas.MATRIX_SAVE_FLAG);
                backingLayer.translate((maxTextWidth - mOffLayout.getWidth())/2, switchVerticalMid - mOffLayout.getHeight() / 2);
                mOffLayout.draw(backingLayer);
                backingLayer.restore();
                backingLayer.translate(maxTextWidth+ mTrackTextPadding * 2+
            		               (maxTextWidth - mOnLayout.getWidth())/2 +
            		               mThumbWidth+ mThPad.left +
            		               mThPad.right,
            		               switchVerticalMid - mOnLayout.getHeight() / 2);
                mOnLayout.draw(backingLayer);
            }
             
            if (mOrientation == VERTICAL) {
                backingLayer.translate(0,mTrackPaddingRect.top);
                backingLayer.save(Canvas.MATRIX_SAVE_FLAG);
                backingLayer.translate((maxTextWidth - mOffLayout.getWidth())/2, switchVerticalMid - mOffLayout.getHeight() / 2);
                mOffLayout.draw(backingLayer);
                backingLayer.restore();
                backingLayer.translate((maxTextWidth - mOnLayout.getWidth())/2, maxTextHeight+ mTrackTextPadding * 2+
            		               (maxTextHeight - mOnLayout.getHeight())/2 +
            		               mThumbWidth+ mThPad.left +
            		               mThPad.top);
                mOnLayout.draw(backingLayer);
            }
            */
        }
    }

    // Draw the switch
    override fun onDraw(canvas: Canvas) {
        //Log.d(TAG, "onDraw()canvas:height="+canvas.getHeight()+" width="+canvas.getWidth());
        //Rect canvasClipBounds = canvas.getClipBounds();
        //Log.d(TAG, "onDraw()canvas:clipbounds="+canvasClipBounds);
        //super.onDraw(canvas);
        val switchInnerLeft = mSwitchLeft + mTrackPaddingRect.left
        val switchInnerTop = mSwitchTop + mTrackPaddingRect.top
        val switchInnerRight = mSwitchRight - mTrackPaddingRect.right
        val switchInnerBottom = mSwitchBottom - mTrackPaddingRect.bottom
        val thumbRange = thumbScrollRange
        val thumbPos = (mThumbPosition + 0.5f).toInt()
        val alpha = mTextPaint.alpha
        mTextPaint.drawableState = drawableState
        //Log.d(TAG, "switchInnerLeft="+switchInnerLeft+" switchInnerRight="+switchInnerRight);
        //Log.d(TAG, "switchInnerTop="+switchInnerTop+" switchInnerBottom="+switchInnerBottom);
        //Log.d(TAG, "thumbRange="+thumbRange+" thumbPos="+thumbPos);
        if (mOrientation == VERTICAL) {
            val switchHorizontalMid = (switchInnerLeft + switchInnerRight) / 2
            val thumbBoundR = mSwitchRight
            var thumbBoundT = switchInnerTop + 1 * thumbScrollRange - mThumbExtraMovement
            var thumbBoundB = thumbBoundT + mThumbHeight
            if (mPushStyle) {
                val maxTextHeight = Math.max(mOnLayout!!.height, mOffLayout!!.height)
                //tempBitmap = Bitmap.createBitmap(mSwitchRight - mSwitchLeft, mSwitchBottom - mSwitchTop,  Config.ARGB_8888);
                //backingLayer = new Canvas(tempBitmap);
                backingLayer!!.save()
                backingLayer!!.translate(0f, (-thumbRange + thumbPos).toFloat())
                backingLayer!!.drawBitmap(pushBitmap!!, 0f, 0f, null)
                backingLayer!!.restore()
                backingLayer!!.drawBitmap(maskBitmap!!, 0f, 0f, xferPaint)
                canvas.drawBitmap(tempBitmap!!, 0f, 0f, null)
                mTrackDrawable!!.draw(canvas)
                backingLayer!!.drawColor(0x01000000, PorterDuff.Mode.DST_IN)
                backingLayer!!.save()
                backingLayer!!.translate(0f, (-thumbRange + thumbPos).toFloat())
                backingLayer!!.translate(0f, mTrackPaddingRect.top.toFloat())
                backingLayer!!.save()
                backingLayer!!.translate(0f, ((maxTextHeight - mOffLayout!!.height) / 2).toFloat())
                mDrawableOff?.draw(backingLayer!!)
                backingLayer!!.translate(
                    (switchHorizontalMid - mOffLayout!!.width / 2).toFloat(),
                    0f
                )
                mOffLayout!!.draw(backingLayer)
                backingLayer!!.restore()
                backingLayer!!.translate(
                    0f,
                    (maxTextHeight + mTrackTextPadding * 2 + (maxTextHeight - mOnLayout!!.height) / 2 +
                            mThumbHeight).toFloat()
                ) //+ mThPad.left + mThPad.right,)
                mDrawableOn?.draw(backingLayer!!)
                backingLayer!!.translate(
                    (switchHorizontalMid - mOnLayout!!.width / 2).toFloat(),
                    0f
                ) //+ mThPad.left + mThPad.right,)
                mOnLayout!!.draw(backingLayer)
                backingLayer!!.restore()
                backingLayer!!.drawBitmap(maskBitmap!!, 0f, 0f, xferPaint)
                canvas.drawBitmap(tempBitmap!!, 0f, 0f, null)
            } else {
                if (rightBitmap != null) {
                    canvas.save()
                    if (canvas.getClipBounds(canvasClipBounds)) {
                        if (mOrientation == HORIZONTAL) {
                            canvasClipBounds.left += thumbPos + mThumbWidth / 2
                        }
                        if (mOrientation == VERTICAL) {
                            canvasClipBounds.top += thumbPos + mThumbHeight / 2
                        }
                        canvas.clipRect(canvasClipBounds)
                    }
                    canvas.drawBitmap(rightBitmap!!, 0f, 0f, null)
                    canvas.restore()
                }
                if (leftBitmap != null) {
                    canvas.save()
                    if (canvas.getClipBounds(canvasClipBounds)) {
                        if (mOrientation == HORIZONTAL) {
                            canvasClipBounds.right -= thumbRange - thumbPos + mThumbWidth / 2
                        }
                        if (mOrientation == VERTICAL) {
                            canvasClipBounds.bottom =
                                canvasClipBounds.top + thumbPos + mThumbHeight / 2
                        }
                        canvas.clipRect(canvasClipBounds)
                    }
                    canvas.drawBitmap(leftBitmap!!, 0f, 0f, null)
                    canvas.restore()
                }

                //draw the track
                mTrackDrawable!!.draw(canvas)
                canvas.save()
                // evaluate the coordinates for drawing the Thumb and Text
                canvas.clipRect(switchInnerLeft, mSwitchTop, switchInnerRight, mSwitchBottom)

                // mTextColors should not be null, but just in case
                if (mTextColors != null) {
                    mTextPaint.color = mTextColors!!.getColorForState(
                        drawableState,
                        mTextColors!!.defaultColor
                    )
                }
                // draw the texts for On/Off in reduced alpha mode.
                if (targetCheckedState xor mTextOnThumb) mTextPaint.alpha =
                    alpha / 4 else mTextPaint.alpha = alpha
                thumbBoundT = switchInnerTop + 1 * thumbScrollRange - mThumbExtraMovement
                thumbBoundB = thumbBoundT + mThumbHeight
                canvas.save()
                canvas.translate(
                    0f,
                    ((thumbBoundT + thumbBoundB) / 2 - mOnLayout!!.height / 2).toFloat()
                )
                if (mDrawableOn != null && mTextPaint.alpha == alpha) mDrawableOn.draw(canvas)
                canvas.translate(
                    ((mSwitchLeft + mSwitchRight) / 2 - mOnLayout!!.width / 2).toFloat(),
                    0f
                )
                mOnLayout!!.draw(canvas)
                canvas.restore()

                // mTextColors should not be null, but just in case
                if (mTextColors != null) {
                    mTextPaint.color = mTextColors!!.getColorForState(
                        drawableState,
                        mTextColors!!.defaultColor
                    )
                }
                if (targetCheckedState xor mTextOnThumb) mTextPaint.alpha =
                    alpha else mTextPaint.alpha = alpha / 4
                thumbBoundT = switchInnerTop - mThumbExtraMovement
                thumbBoundB = thumbBoundT + mThumbHeight
                canvas.save()
                canvas.translate(
                    0f,
                    ((thumbBoundT + thumbBoundB) / 2 - mOffLayout!!.height / 2).toFloat()
                )
                if (mDrawableOff != null && mTextPaint.alpha == alpha) mDrawableOff.draw(canvas)
                canvas.translate(
                    ((mSwitchLeft + mSwitchRight) / 2 - mOffLayout!!.width / 2).toFloat(),
                    0f
                )
                mOffLayout!!.draw(canvas)
                canvas.restore()
                canvas.restore()
            }
            thumbBoundT = switchInnerTop + thumbPos - mThumbExtraMovement
            thumbBoundB = switchInnerTop + thumbPos - mThumbExtraMovement + mThumbHeight
            //Draw the Thumb
            Log.d(TAG, "thumbBoundT, thumbBoundB=($thumbBoundT,$thumbBoundB)")
            Log.d(TAG, "mSwitchLeft, mSwitchRight=($mSwitchLeft,$mSwitchRight)")
            mThumbDrawable!!.setBounds(mSwitchLeft, thumbBoundT, mSwitchRight, thumbBoundB)
            mThumbDrawable!!.draw(canvas)
            mTextPaint.alpha = alpha
            //Draw the text on the Thumb
            if (mTextOnThumb) {
                val offSwitchText = if (targetCheckedState) mOnLayout else mOffLayout
                canvas.save()
                canvas.translate(
                    ((mSwitchLeft + mSwitchRight) / 2 - offSwitchText!!.width / 2).toFloat(),
                    (
                            (thumbBoundT + thumbBoundB) / 2 - offSwitchText.height / 2).toFloat()
                )
                //(switchInnerTop + switchInnerBottom) / 2 - onSwitchText.getHeight() - this.mThumbTextPadding);
                offSwitchText.draw(canvas)
                canvas.restore()
            }
        }
        if (mOrientation == HORIZONTAL) {
            var thumbL = switchInnerLeft // + mThPad.left;
            var thumbR = switchInnerLeft + mThumbWidth // - mThPad.right;
            val dxOffText = if (mTextOnThumb) ((thumbL + thumbR) / 2
                    - mOffLayout!!.width / 2 + mTrackTextPadding
                    - mThumbTextPadding //(thumbL+thumbR)/2 already has 2*mThumbTextPadding
                    // so we have to subtract it
                    ) else switchInnerLeft + mTrackTextPadding
            thumbL = thumbL + thumbRange
            thumbR = thumbR + thumbRange
            val dxOnText =
                if (mTextOnThumb) (thumbL + thumbR) / 2 - mOnLayout!!.width / 2 //(thumbL + thumbR)/2 already has the ThumbTextPadding
                //so we dont have to add it
                else switchInnerRight - mOnLayout!!.width - mTrackTextPadding
            val switchVerticalMid = (switchInnerTop + switchInnerBottom) / 2
            val thumbBoundL = switchInnerLeft + thumbPos - mThumbExtraMovement // + mThPad.left 
            val thumbBoundR =
                switchInnerLeft + thumbPos + mThumbWidth - mThumbExtraMovement // - mThPad.right
            if (mPushStyle) {
                val maxTextWidth = Math.max(mOnLayout!!.width, mOffLayout!!.width)
                //tempBitmap = Bitmap.createBitmap(mSwitchRight - mSwitchLeft, mSwitchBottom - mSwitchTop,  Config.ARGB_8888);
                //backingLayer = new Canvas(tempBitmap);
                backingLayer!!.save()
                backingLayer!!.translate((-thumbRange + thumbPos).toFloat(), 0f)
                backingLayer!!.drawBitmap(pushBitmap!!, 0f, 0f, null)
                backingLayer!!.restore()
                backingLayer!!.drawBitmap(maskBitmap!!, 0f, 0f, xferPaint)
                canvas.drawBitmap(tempBitmap!!, 0f, 0f, null)
                mTrackDrawable!!.draw(canvas)
                backingLayer!!.drawColor(0x01000000, PorterDuff.Mode.DST_IN)
                backingLayer!!.save()
                backingLayer!!.translate((-thumbRange + thumbPos).toFloat(), 0f)
                backingLayer!!.translate(mTrackPaddingRect.left.toFloat(), 0f)
                backingLayer!!.save()
                backingLayer!!.translate(
                    ((maxTextWidth - mOffLayout!!.width) / 2).toFloat(),
                    (switchVerticalMid - mOffLayout!!.height / 2).toFloat()
                )
                mOffLayout!!.draw(backingLayer)
                mDrawableOff?.draw(backingLayer!!)
                backingLayer!!.restore()
                backingLayer!!.translate(
                    (maxTextWidth + mTrackTextPadding * 2 + (maxTextWidth - mOnLayout!!.width) / 2 +
                            mThumbWidth).toFloat(),  //+ mThPad.left + mThPad.right,
                    (
                            switchVerticalMid - mOnLayout!!.height / 2).toFloat()
                )
                mOnLayout!!.draw(backingLayer)
                mDrawableOn?.draw(backingLayer!!)
                backingLayer!!.restore()
                backingLayer!!.drawBitmap(maskBitmap!!, 0f, 0f, xferPaint)
                canvas.drawBitmap(tempBitmap!!, 0f, 0f, null)
            } else {
                if (rightBitmap != null) {
                    canvas.save()
                    if (canvas.getClipBounds(canvasClipBounds)) {
                        canvasClipBounds.left += (mThumbPosition + mThumbWidth / 2).toInt()
                        canvas.clipRect(canvasClipBounds)
                    }
                    canvas.drawBitmap(rightBitmap!!, 0f, 0f, null)
                    canvas.restore()
                }
                if (leftBitmap != null) {
                    canvas.save()
                    if (canvas.getClipBounds(canvasClipBounds)) {
                        canvasClipBounds.right -= (thumbRange - mThumbPosition + mThumbWidth / 2).toInt()
                        canvas.clipRect(canvasClipBounds)
                    }
                    canvas.drawBitmap(leftBitmap!!, 0f, 0f, null)
                    canvas.restore()
                }

                //draw the track
                mTrackDrawable!!.draw(canvas)

                // evaluate the coordinates for drawing the Thumb and Text
                canvas.save()
                canvas.clipRect(switchInnerLeft, mSwitchTop, switchInnerRight, mSwitchBottom)

                // mTextColors should not be null, but just in case
                if (mTextColors != null) {
                    mTextPaint.color =
                        mTextColors!!.getColorForState(drawableState, mTextColors!!.defaultColor)
                }

                // draw the texts for On/Off in reduced alpha mode.
                mTextPaint.alpha = alpha / 4
                if (targetCheckedState) {
                    canvas.save()
                    canvas.translate(
                        dxOnText.toFloat(),
                        (switchVerticalMid - mOnLayout!!.height / 2).toFloat()
                    )
                    if (canvas.getClipBounds(canvasClipBounds)) {
                        canvasClipBounds.left += (mThumbPosition + mThumbWidth / 2).toInt()
                        canvas.clipRect(canvasClipBounds)
                    }
                    mOnLayout!!.draw(canvas)
                    mDrawableOn?.draw(canvas)
                    canvas.restore()
                    if (mTextOnThumb == false) mTextPaint.alpha = alpha
                    canvas.save()
                    canvas.translate(
                        dxOffText.toFloat(),
                        (switchVerticalMid - mOffLayout!!.height / 2).toFloat()
                    )
                    if (canvas.getClipBounds(canvasClipBounds)) {
                        canvasClipBounds.right -= (thumbRange - mThumbPosition + mThumbWidth / 2).toInt()
                        canvas.clipRect(canvasClipBounds)
                    }
                    mOffLayout!!.draw(canvas)
                    mDrawableOff?.draw(canvas)
                    canvas.restore()
                } else {
                    canvas.save()
                    canvas.translate(
                        dxOffText.toFloat(),
                        (switchVerticalMid - mOffLayout!!.height / 2).toFloat()
                    )
                    if (canvas.getClipBounds(canvasClipBounds)) {
                        canvasClipBounds.right -= (thumbRange - mThumbPosition + mThumbWidth / 2).toInt()
                        canvas.clipRect(canvasClipBounds)
                    }
                    mOffLayout!!.draw(canvas)
                    mDrawableOff?.draw(canvas)
                    canvas.restore()
                    if (mTextOnThumb == false) mTextPaint.alpha = alpha
                    canvas.save()
                    canvas.translate(
                        dxOnText.toFloat(),
                        (switchVerticalMid - mOnLayout!!.height / 2).toFloat()
                    )
                    if (canvas.getClipBounds(canvasClipBounds)) {
                        canvasClipBounds.left += (mThumbPosition + mThumbWidth / 2).toInt()
                        canvas.clipRect(canvasClipBounds)
                    }
                    mOnLayout!!.draw(canvas)
                    mDrawableOn?.draw(canvas)
                    canvas.restore()
                }
                canvas.restore()
            }

            //Draw the Thumb
            //Log.d(TAG, "thumbBoundL, thumbBoundR=("+thumbBoundL+","+thumbBoundR+")");
            mThumbDrawable!!.setBounds(thumbBoundL, mSwitchTop, thumbBoundR, mSwitchBottom)
            mThumbDrawable!!.draw(canvas)

            //Draw the text on the Thumb
            if (mTextOnThumb) {
                mTextPaint.alpha = alpha
                val onSwitchText = if (targetCheckedState) mOnLayout else mOffLayout
                canvas.save()
                canvas.translate(
                    ((thumbBoundL + thumbBoundR) / 2 - onSwitchText!!.width / 2).toFloat(),
                    (
                            (switchInnerTop + switchInnerBottom) / 2 - onSwitchText.height / 2).toFloat()
                )
                onSwitchText.draw(canvas)
                canvas.restore()
            }
        }
    }

    override fun getCompoundPaddingRight(): Int {
        var padding = super.getCompoundPaddingRight() + mSwitchWidth
        if (!TextUtils.isEmpty(text)) {
            padding += mSwitchPadding
        }
        return padding
    }

    override fun getCompoundPaddingTop(): Int {
        var padding = super.getCompoundPaddingTop() + mSwitchHeight
        if (!TextUtils.isEmpty(text)) {
            padding += mSwitchPadding
        }
        return padding
    }

    //Log.d(TAG,"getThumbScrollRange() = "+ range);
    private val thumbScrollRange: Int
        private get() {
            if (mTrackDrawable == null) {
                return 0
            }
            var range = 0
            if (mOrientation == VERTICAL) range =
                mSwitchHeight - mThumbHeight - mTrackPaddingRect.top - mTrackPaddingRect.bottom + mThumbExtraMovement * 2
            if (mOrientation == HORIZONTAL) range =
                mSwitchWidth - mThumbWidth - mTrackPaddingRect.left - mTrackPaddingRect.right + mThumbExtraMovement * 2
            if (mPushStyle) range += mTrackTextPadding * 2
            //Log.d(TAG,"getThumbScrollRange() = "+ range);
            return range
        }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (isChecked) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET)
        }
        return drawableState
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        val myDrawableState = drawableState

        // Set the state of the Drawable
        // Drawable may be null when checked state is set from XML, from super constructor
        if (mThumbDrawable != null) mThumbDrawable!!.setState(myDrawableState)
        if (mTrackDrawable != null) mTrackDrawable!!.setState(myDrawableState)
        invalidate()
    }

    override fun verifyDrawable(who: Drawable): Boolean {
        return super.verifyDrawable(who) || who === mThumbDrawable || who === mTrackDrawable
    }

    companion object {
        private const val TOUCH_MODE_IDLE = 0
        private const val TOUCH_MODE_DOWN = 1
        private const val TOUCH_MODE_DRAGGING = 2
        private const val TAG = "MySwitch"

        // Enum for the "typeface" XML parameter.
        private const val SANS = 1
        private const val SERIF = 2
        private const val MONOSPACE = 3
        private const val VERTICAL = 0
        private const val HORIZONTAL = 1
        private val CHECKED_STATE_SET = intArrayOf(
            android.R.attr.state_checked
        )
    }
}