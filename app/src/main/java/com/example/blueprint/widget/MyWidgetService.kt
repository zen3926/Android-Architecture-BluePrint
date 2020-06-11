package com.example.blueprint.widget

import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.blueprint.MainActivity
import com.example.blueprint.R
import com.example.blueprint.repository.FundRepo
import com.example.blueprint.repository.UserManager
import kotlinx.coroutines.*

class MyWidgetService : JobService() {
    private val scope = CoroutineScope(Job() + Dispatchers.Main)
    override fun onStartJob(params: JobParameters?): Boolean {
        scope.launch {
            val updateView: RemoteViews = buildRemoteView(this@MyWidgetService)
            val widget = ComponentName(this@MyWidgetService, MyWidgetProvider::class.java)
            val manager = AppWidgetManager.getInstance(this@MyWidgetService)
            manager.updateAppWidget(widget, updateView)
            jobFinished(params, false)
        }
        return true
    }

    private suspend fun buildRemoteView(context: Context): RemoteViews {
        return withContext(Dispatchers.IO) {
            val user = UserManager.getRepo(context).getSignedUser()
            val fund = FundRepo.getRepo(context).checkAvailableFund()
            val userName = user?.userName ?: context.resources.getString(R.string.app_name)

            RemoteViews(context.packageName, R.layout.widget_app).apply {
                setTextViewText(R.id.widget_userNameText, userName)
                setTextViewText(R.id.widget_fundText, "$${fund}")
                setOnClickPendingIntent(R.id.widget_read, buildIntent(context, REQUEST_READ))
                setOnClickPendingIntent(R.id.widget_welcome, buildIntent(context, REQUEST_GREET))
            }
        }
    }

    private fun buildIntent(context: Context, requestCode: Int): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra(KEY_WIDGET_ACTION, requestCode)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            action = Intent.ACTION_MAIN
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        return PendingIntent.getActivity(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
    }

    override fun onStopJob(params: JobParameters?): Boolean = true

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    companion object {
        const val REQUEST_READ = 100
        const val REQUEST_GREET = 200
        const val KEY_WIDGET_ACTION = "widgetAction"
        const val WIDGET_SERVICE_ID = 3333
    }
}
