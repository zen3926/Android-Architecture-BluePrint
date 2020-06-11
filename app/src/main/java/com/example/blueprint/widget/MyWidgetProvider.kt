package com.example.blueprint.widget

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent

class MyWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        val widgetService = ComponentName(context, MyWidgetService::class.java)
        val widgetJob = JobInfo.Builder(MyWidgetService.WIDGET_SERVICE_ID, widgetService)
            .setOverrideDeadline(0)
            .build()

        context.getSystemService(JobScheduler::class.java)?.schedule(widgetJob)
    }

    companion object {

        /**
         * Update all the widgets
         */
        fun updateWidget(context: Context) {
            val intent = Intent(context, MyWidgetProvider::class.java).apply {
                action = "android.appwidget.action.APPWIDGET_UPDATE"
                val name = ComponentName(context.applicationContext, MyWidgetProvider::class.java)
                val ids = AppWidgetManager.getInstance(context.applicationContext)
                    .getAppWidgetIds(name)
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
            }
            context.sendBroadcast(intent)
        }
    }
}
