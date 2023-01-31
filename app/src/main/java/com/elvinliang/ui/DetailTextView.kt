package com.elvinliang.ui

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.elvinliang.aviation.R
import kotlinx.android.synthetic.main.component_detailtextview.view.*

class DetailTextView : ConstraintLayout {
    val TAG = "ev_" + javaClass.simpleName
    var txtTitle = "Title"
    var txtTitleSize = resources.getDimensionPixelSize(R.dimen.default_txtTitleSize)
    var txtContent = "Content"
    var txtContentSize = resources.getDimensionPixelSize(R.dimen.default_txtContentSize)

    constructor(context: Context) : super(context) {
        initView()
    }
    constructor(context: Context, attrs: AttributeSet, def: Int) : super(context, attrs, def) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Detailtextview)
        getvalues(typedArray)
        initView()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Detailtextview)
        getvalues(typedArray)
        initView()
    }

    fun getvalues(typedArray: TypedArray) {
        txtTitle = typedArray.getString(R.styleable.Detailtextview_txtTitle) ?: "Title"
        txtContent = typedArray.getString(R.styleable.Detailtextview_txtContent) ?: "Content"
        typedArray.recycle()
    }

    private fun initView() {
        inflate(context, R.layout.component_detailtextview, this)
        txv_title.text = txtTitle
        txv_title.typeface = Typeface.DEFAULT_BOLD
        txv_title.layoutParams = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        txv_content.text = txtContent
        txv_content.layoutParams = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }
}
