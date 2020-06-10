package com.example.blueprint.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import com.example.blueprint.R

class PairDetailTable @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val leftTextView: TextView
    private val rightTextView: TextView
    var leftText: String = ""
        set(value) {
            field = value
            leftTextView.text = value
            invalidate()
            requestLayout()
        }

    var rightText: String = ""
        set(value) {
            field = value
            rightTextView.text = value
            invalidate()
            requestLayout()
        }

    init {
        View.inflate(context, R.layout.view_pair_detail_table, this)

        leftTextView = findViewById(R.id.leftText)
        rightTextView = findViewById(R.id.rightText)
        context.withStyledAttributes(attrs, R.styleable.PairDetailTable, defStyleAttr, 0) {
            leftText = getString(R.styleable.PairDetailTable_leftText) ?: ""
            rightText = getString(R.styleable.PairDetailTable_rightText) ?: ""
        }
    }
}
