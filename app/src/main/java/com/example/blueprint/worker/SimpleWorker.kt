package com.example.blueprint.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.blueprint.network.SimpleNetwork
import com.example.blueprint.network.model.NetworkUser
import com.example.blueprint.notification.SimpleNotification
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class SimpleWorker(private val appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = coroutineScope {
        withContext(Dispatchers.IO) {
            // delay 3 second
            delay(3000L)

            val result = try {
                val sample = SimpleNetwork.simpleService.getSample()
                if (sample.isSuccessful && sample.body() != null) {
                    sample.body()
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }

            val message = result?.run {
                Moshi.Builder().build().adapter(NetworkUser::class.java).toJson(this)
            } ?: "Hello"

            SimpleNotification.getInstance(appContext).createNotification(message)
        }
        Result.success()
    }
}
