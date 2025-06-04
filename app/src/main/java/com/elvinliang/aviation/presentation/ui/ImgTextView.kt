package com.elvinliang.aviation.presentation.ui

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.elvinliang.aviation.R

class ImgTextView : ConstraintLayout {
    var imgsize = resources.getDimensionPixelSize(R.dimen.default_imgsize)
    var imgsrc = ResourcesCompat.getDrawable(resources, android.R.drawable.alert_dark_frame, null)
    var txtstring = "Title"
    var txtsize = resources.getDimensionPixelSize(R.dimen.default_txtsize)

    constructor(context: Context) : super(context) {
        initView()
    }
    constructor(context: Context, attrs: AttributeSet, def: Int) : super(context, attrs, def) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.imgtextview)
        getvalues(typedArray)
        initView()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.imgtextview)
        getvalues(typedArray)
        initView()
    }

    fun getvalues(typedArray: TypedArray) {
        imgsrc = typedArray.getDrawable(R.styleable.imgtextview_imgsrc)
        txtstring = typedArray.getString(R.styleable.imgtextview_txtstring) ?: "nonono"
        typedArray.recycle()
    }

    private fun initView() {
        val c: View = inflate(context, R.layout.component_imgtextview, this)

        val img = c.findViewById<ImageView>(R.id.img)
        val text = c.findViewById<TextView>(R.id.txv_title)
        val imgLayoutParams = ConstraintLayout.LayoutParams(imgsize, imgsize)

        img.layoutParams = imgLayoutParams
        img.setImageDrawable(imgsrc)

        text.text = txtstring
        text.layoutParams = ConstraintLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

}
