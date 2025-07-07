package com.example.madcamp1.ui.custom

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatCheckBox

class NoToggleCheckBox @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatCheckBox(context, attrs) {

    override fun toggle() {
        if (!isChecked) {
            super.toggle()
        }
    }

    override fun performClick(): Boolean {
        return super.performClick() // ripple 효과 유지
    }
}

