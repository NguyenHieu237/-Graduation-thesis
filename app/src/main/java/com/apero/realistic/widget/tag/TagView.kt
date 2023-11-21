package com.apero.realistic.widget.tag

import android.annotation.TargetApi
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.apero.realistic.R

/**
 * Android TagView Widget
 */
class TagView : RelativeLayout {
    private var mWidth = 0
    var lineMargin = 0
        private set
    var tagMargin = 0
        private set
    var textPaddingLeft = 0
        private set
    var textPaddingRight = 0
        private set
    var textPaddingTop = 0
        private set
    var texPaddingBottom = 0
        private set
    private val mTags: MutableList<Tag> = ArrayList()
    private var mInflater: LayoutInflater? = null
    private var mClickListener: OnTagClickListener? = null
    private var mDeleteListener: OnTagDeleteListener? = null

    constructor(context: Context) : super(context, null) {
        LogUtil.v(TAG, "[TagView]constructor 1")
        init(context, null, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        LogUtil.v(TAG, "[TagView]constructor 2")
        init(context, attrs, 0, 0)
    }

    constructor(ctx: Context, attrs: AttributeSet?, defStyle: Int) : super(ctx, attrs, defStyle) {
        LogUtil.v(TAG, "[TagView]constructor 3")
        init(ctx, attrs, defStyle, defStyle)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        LogUtil.v(TAG, "[TagView]constructor 4")
        init(context, attrs, defStyleAttr, defStyleRes)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyle: Int, defStyleRes: Int) {
        LogUtil.v(TAG, "[init]")
        Constants.DEBUG =
            context.applicationContext.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        // get AttributeSet
        val typeArray: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.TagView, defStyle, defStyleRes)
        lineMargin = typeArray.getDimension(
            R.styleable.TagView_lineMargin,
            ResolutionUtil.dpToPx(this.getContext(), Constants.DEFAULT_LINE_MARGIN).toFloat()
        ).toInt()
        tagMargin = typeArray.getDimension(
            R.styleable.TagView_tagMargin,
            ResolutionUtil.dpToPx(this.getContext(), Constants.DEFAULT_TAG_MARGIN).toFloat()
        ).toInt()
        textPaddingLeft = typeArray.getDimension(
            R.styleable.TagView_textPaddingLeft,
            ResolutionUtil.dpToPx(this.getContext(), Constants.DEFAULT_TAG_TEXT_PADDING_LEFT)
                .toFloat()
        ).toInt()
        textPaddingRight = typeArray.getDimension(
            R.styleable.TagView_textPaddingRight,
            ResolutionUtil.dpToPx(this.getContext(), Constants.DEFAULT_TAG_TEXT_PADDING_RIGHT)
                .toFloat()
        ).toInt()
        textPaddingTop = typeArray.getDimension(
            R.styleable.TagView_textPaddingTop,
            ResolutionUtil.dpToPx(this.getContext(), Constants.DEFAULT_TAG_TEXT_PADDING_TOP)
                .toFloat()
        ).toInt()
        texPaddingBottom = typeArray.getDimension(
            R.styleable.TagView_textPaddingBottom,
            ResolutionUtil.dpToPx(this.getContext(), Constants.DEFAULT_TAG_TEXT_PADDING_BOTTOM)
                .toFloat()
        ).toInt()
        typeArray.recycle()
        mWidth = ResolutionUtil.getScreenWidth(context)
        // this.setWillNotDraw(false);
    }

    protected override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        LogUtil.v(TAG, "[onSizeChanged]w = $w")
        mWidth = w
        // drawTags();
    }

    protected override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        LogUtil.v(TAG, "[onMeasure]getMeasuredWidth = " + getMeasuredWidth())
        /*int width = getMeasuredWidth();
        if (width <= 0) return;
        mWidth = getMeasuredWidth();
        drawTags();*/
    }

    protected override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // View#onDraw is disabled in view group;
        // enable View#onDraw for view group : View#setWillNotDraw(false);
        LogUtil.v(TAG, "[onDraw]")
        // drawTags();
    }

    protected override fun onVisibilityChanged(changedView: View, visibility: Int) {
        LogUtil.v(TAG, "[onVisibilityChanged]")
        /*if (changedView == this){
            if (visibility == View.VISIBLE){
                drawTags();
            }
        }*/super.onVisibilityChanged(changedView, visibility)
    }

    protected override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        LogUtil.v(TAG, "[onAttachedToWindow]")
    }

    private fun drawTags() {
        LogUtil.v(TAG, "[drawTags]visibility = " + (getVisibility() == View.VISIBLE))
        if (getVisibility() != View.VISIBLE) return
        LogUtil.v(TAG, "[drawTags]mWidth = $mWidth")
        // clear all tag
        removeAllViews()
        // layout padding left & layout padding right
        var total: Float = (getPaddingLeft() + getPaddingRight()).toFloat()
        var listIndex = 1 // List Index
        var index_bottom = 1 // The Tag to add below
        var index_header = 1 // The header tag of this line
        var tag_pre: Tag? = null
        for (item in mTags) {
            val position = listIndex - 1
            // inflate tag layout
            val tagLayout: View = mInflater?.inflate(R.layout.tagview_item, null)!!
            tagLayout.id = listIndex
            tagLayout.setBackgroundDrawable(getSelector(item))
            // tag text
            val tagView: TextView =
                tagLayout.findViewById<View>(R.id.tv_tag_item_contain) as TextView
            tagView.setText(item.text)
            //tagView.setPadding(textPaddingLeft, textPaddingTop, textPaddingRight, texPaddingBottom);
            val params: LinearLayout.LayoutParams =
                tagView.getLayoutParams() as LinearLayout.LayoutParams
            params.setMargins(textPaddingLeft, textPaddingTop, textPaddingRight, texPaddingBottom)
            tagView.setLayoutParams(params)
            tagView.setTextColor(item.tagTextColor)
            tagView.setTextSize(TypedValue.COMPLEX_UNIT_SP, item.tagTextSize)
            tagLayout.setOnClickListener {
                if (mClickListener != null) {
                    mClickListener!!.onTagClick(position, item)
                }
            }
            // calculate　of tag layout width
            var tagWidth: Float =
                tagView.getPaint().measureText(item.text) + textPaddingLeft + textPaddingRight
            // tagView padding (left & right)
            // deletable text
            val deletableView: TextView =
                tagLayout.findViewById<View>(R.id.tv_tag_item_delete) as TextView
            if (item.isDeletable) {
                deletableView.setVisibility(View.VISIBLE)
                deletableView.setText(item.deleteIcon)
                val offset = ResolutionUtil.dpToPx(getContext(), 2f)
                deletableView.setPadding(
                    offset,
                    textPaddingTop,
                    textPaddingRight + offset,
                    texPaddingBottom
                )
                /*params = (LinearLayout.LayoutParams) deletableView.getLayoutParams();
				params.setMargins(offset, textPaddingTop, textPaddingRight+offset, texPaddingBottom);
				deletableView.setLayoutParams(params);*/deletableView.setTextColor(item.deleteIndicatorColor)
                deletableView.setTextSize(TypedValue.COMPLEX_UNIT_SP, item.deleteIndicatorSize)
                deletableView.setOnClickListener(View.OnClickListener {
                    this@TagView.remove(position)
                    if (mDeleteListener != null) {
                        mDeleteListener!!.onTagDeleted(position, item)
                    }
                })
                tagWidth += deletableView.getPaint()
                    .measureText(item.deleteIcon) + deletableView.getPaddingLeft() + deletableView.getPaddingRight()
                // deletableView Padding (left & right)
            } else {
                deletableView.setVisibility(View.GONE)
            }
            val tagParams: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            //tagParams.setMargins(0, 0, 0, 0);
            //add margin of each line
            tagParams.bottomMargin = lineMargin
            if (mWidth <= total + tagMargin + tagWidth + ResolutionUtil.dpToPx(
                    this.getContext(),
                    Constants.LAYOUT_WIDTH_OFFSET
                )
            ) {
                //need to add in new line
                tagParams.addRule(RelativeLayout.BELOW, index_bottom)
                // initialize total param (layout padding left & layout padding right)
                total = (getPaddingLeft() + getPaddingRight()).toFloat()
                index_bottom = listIndex
                index_header = listIndex
            } else {
                //no need to new line
                tagParams.addRule(RelativeLayout.ALIGN_TOP, index_header)
                //not header of the line
                if (listIndex != index_header) {
                    tagParams.addRule(RelativeLayout.RIGHT_OF, listIndex - 1)
                    tagParams.leftMargin = tagMargin
                    total += tagMargin.toFloat()
                    if (tag_pre!!.tagTextSize < item.tagTextSize) {
                        index_bottom = listIndex
                    }
                }
            }
            total += tagWidth
            addView(tagLayout, tagParams)
            tag_pre = item
            listIndex++
        }
    }

    private fun getSelector(tag: Tag): Drawable? {
        if (tag.background != null) return tag.background
        val states = StateListDrawable()
        val gd_normal = GradientDrawable()
        gd_normal.setColor(tag.layoutColor)
        gd_normal.setCornerRadius(tag.radius)
        if (tag.layoutBorderSize > 0) {
            gd_normal.setStroke(
                ResolutionUtil.dpToPx(getContext(), tag.layoutBorderSize),
                tag.layoutBorderColor
            )
        }
        val gd_press = GradientDrawable()
        gd_press.setColor(tag.layoutColorPress)
        gd_press.setCornerRadius(tag.radius)
        states.addState(intArrayOf(android.R.attr.state_pressed), gd_press)
        //must add state_pressed first，or state_pressed will not take effect
        states.addState(intArrayOf(), gd_normal)
        return states
    }

    fun addTag(tag: Tag) {
        LogUtil.v(TAG, "[addTag]")
        mTags.add(tag)
        drawTags()
    }

    fun addTags(tags: ArrayList<String>) {
        LogUtil.v(TAG, "[addTags]")
        if (tags.size <= 0) return
        for (item in tags) {
            val tag = Tag(item)
            mTags.add(tag)
        }
        drawTags()
    }

    fun addTags(tagList: List<Tag>?) {
        LogUtil.v(TAG, "[addTags]")
        if (tagList == null || tagList.size <= 0) return
        mTags.addAll(tagList)
        drawTags()
    }

    val tags: List<Tag>
        get() = mTags

    fun remove(position: Int) {
        LogUtil.v(TAG, "[remove]position = $position")
        mTags.removeAt(position)
        drawTags()
    }

    fun removeAllTags() {
        LogUtil.v(TAG, "[removeAllTags]")
        mTags.clear()
        drawTags()
    }

    fun setLineMargin(lineMargin: Float) {
        this.lineMargin = ResolutionUtil.dpToPx(getContext(), lineMargin)
    }

    fun setTagMargin(tagMargin: Float) {
        this.tagMargin = ResolutionUtil.dpToPx(getContext(), tagMargin)
    }

    fun setTextPaddingLeft(textPaddingLeft: Float) {
        this.textPaddingLeft = ResolutionUtil.dpToPx(getContext(), textPaddingLeft)
    }

    fun setTextPaddingRight(textPaddingRight: Float) {
        this.textPaddingRight = ResolutionUtil.dpToPx(getContext(), textPaddingRight)
    }

    fun setTextPaddingTop(textPaddingTop: Float) {
        this.textPaddingTop = ResolutionUtil.dpToPx(getContext(), textPaddingTop)
    }

    fun setTexPaddingBottom(texPaddingBottom: Float) {
        this.texPaddingBottom = ResolutionUtil.dpToPx(getContext(), texPaddingBottom)
    }

    fun setOnTagClickListener(clickListener: OnTagClickListener?) {
        mClickListener = clickListener
    }

    fun setOnTagDeleteListener(deleteListener: OnTagDeleteListener?) {
        mDeleteListener = deleteListener
    }

    companion object {
        const val TAG = "TagView"
    }
}