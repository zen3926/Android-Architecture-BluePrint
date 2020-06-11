package com.example.blueprint.widget

import android.content.Context
import com.example.blueprint.repository.FundUpdateObserver

class WidgetFundUpdateObserver(private val context: Context) : FundUpdateObserver {
    override fun act() {
        MyWidgetProvider.updateWidget(context)
    }
}
