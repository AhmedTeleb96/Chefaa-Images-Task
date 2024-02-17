package com.teleb.chefaaimagestask.presentation.details.resizeService

import android.annotation.SuppressLint
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.teleb.chefaaimagestask.data.models.ResizeRequest
import com.teleb.chefaaimagestask.domain.usecases.UpdateCharacterAndResizeImageUseCase
import com.teleb.chefaaimagestask.presentation.notification.NotificationUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.lang.reflect.Type
import javax.inject.Inject

@SuppressLint("SpecifyJobSchedulerIdRange")
@AndroidEntryPoint
class ResizeImageService : JobService() {
    @Inject
    lateinit var resizeImageUC: UpdateCharacterAndResizeImageUseCase

    @Inject
    lateinit var notificationUtils: NotificationUtils

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Default + job)


    private var params: JobParameters? = null

    private lateinit var resizeRequest: ResizeRequest

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartJob(p0: JobParameters?): Boolean {
        this.params = p0
        params?.extras?.let {
            resizeRequest = it.getString("RESIZE_REQUEST", "")
                .getModelFromJSON(ResizeRequest::class.java)
            Log.i("xxx", "onStartJob: pp")
        }

        showNotification()
        resizeImage()
        return true
    }

    private fun resizeImage() {
        coroutineScope.launch {
            resizeImageUC.executeRemoteDS(resizeRequest)
                .onStart {
                    setFinalResult(WorkerResult.SHOW_LOADING)
                }
                .onCompletion {
                    setFinalResult(WorkerResult.DISMISS_LOADING)
                }
                .catch {e->
                    Log.i("xxx", "resizeImage: ${e}")
                    jobFinished(params, false)
                }
                .collect {
                    Log.i("xxx", "resizeImage: ooo")
                    setFinalResult(WorkerResult.SUCCESS)
                    jobFinished(params, false)
                }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification() {
        val notification = notificationUtils.showNotification(
            "Update Character", "uploading in progress"
        )
        startForeground(
            resizeRequest.characterEntity.id, notification
        )
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        job.cancel()
        return false
    }

    private fun setFinalResult(workerResult: WorkerResult, exception: Exception? = null) {
        val intent = Intent("RESIZE_IMAGE_WORKER_ACTION").apply {
            exception?.let {
                putExtra("RESIZE_IMAGE_EXCEPTION", exception.message)
            }
            putExtra("RESIZE_RESULT", workerResult.value)
            putExtra("COMIC_ID",resizeRequest.characterEntity.id)
        }
        sendBroadcast(intent)
    }

    fun <M> String.getModelFromJSON(tokenType: Type): M = Gson().fromJson(this, tokenType)

}