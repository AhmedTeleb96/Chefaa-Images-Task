package com.teleb.chefaaimagestask.presentation.details

import android.annotation.SuppressLint
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.teleb.chefaaimagestask.data.models.ResizeRequest
import com.teleb.chefaaimagestask.data.utils.ImageFileUtils
import com.teleb.chefaaimagestask.domain.entities.CharacterEntity
import com.teleb.chefaaimagestask.domain.usecases.GetCharacterByIdUseCase
import com.teleb.chefaaimagestask.presentation.details.resizeService.ResizeImageService
import com.teleb.chefaaimagestask.presentation.details.resizeService.WorkerResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getCharacterByIdUseCase: GetCharacterByIdUseCase,
    private val context: Context
) :
    ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error

    private val _character = MutableLiveData<CharacterEntity?>()
    val character: LiveData<CharacterEntity?>
        get() = _character

    fun getCharacter(id: Int) {
        viewModelScope.launch {
            getCharacterByIdUseCase.invoke(id)
                .onStart {
                    _loading.postValue(true)
                }
                .onCompletion {
                    _loading.postValue(false)
                }
                .catch { e ->
                    _error.postValue(e.message)
                }
                .collect { character ->
                    _character.postValue(character)
                }
        }
    }

    fun resizeImageWithValidation(
        characterEntity: CharacterEntity,
        width: String, height: String,
        imageUriSelected: Uri?,
        isInternetAvailable: Boolean
    ) {
         startResizeImageWorker(
                characterEntity.id,
                buildResizeRequest(characterEntity, imageUriSelected, width.toFloat(), height.toFloat()),
                isInternetAvailable
            )
    }
    private fun startResizeImageWorker(
        id: Int,
        resizeRequest: ResizeRequest,
        isInternetAvailable: Boolean
    ) {

        if (!isInternetAvailable)
            _error.postValue("no internet connection")

        val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.cancel(id)
        val jobInfo = JobInfo.Builder(
            id, ComponentName(context, ResizeImageService::class.java)
        )
        val job =
            jobInfo.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setExtras(PersistableBundle().apply {
                    putString("RESIZE_REQUEST", Gson().toJson(resizeRequest))
                }
                )
                .build()
        jobScheduler.schedule(job)
    }

    private fun buildResizeRequest(
       characterEntity: CharacterEntity,
        imageUriSelected: Uri?, width: Float, height: Float
    ): ResizeRequest {
        /**
         * we are check if we have a local image from gallery also we are will upload image file to server and remove the old path of image url in server
         * if we not have a local image so we will completed with a path url on server
         */
        val selectedFile = if (imageUriSelected != null) {
            characterEntity.thumbnail.path = ""
            ImageFileUtils(context).createFileFromUri(context, imageUriSelected)
        } else ImageFileUtils(context).saveBitmapToFile(characterEntity.thumbnail.imageBitmap!!)

        return ResizeRequest(selectedFile.path,height, width, characterEntity)
    }



    private val resizeImageWorkerBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "RESIZE_IMAGE_WORKER_ACTION") {
                val workerResult =
                    WorkerResult.find(
                        intent.getIntExtra("RESIZE_RESULT", 0)
                    )
                when (workerResult) {
                    WorkerResult.SUCCESS -> getCharacter(
                        intent.getIntExtra("COMIC_ID",
                            -1
                        )
                    )

                    WorkerResult.FAILED -> {
                        val exception = intent.getStringExtra("RESIZE_IMAGE_EXCEPTION")!!
                        _error.postValue(exception)
                    }

                    WorkerResult.SHOW_LOADING -> _loading.postValue(true)

                    WorkerResult.DISMISS_LOADING -> _loading.postValue(false)
                }
            }
        }
    }

    private fun registerBroadCast() {
        context.registerReceiverNotExported(
            resizeImageWorkerBroadcastReceiver,
            IntentFilter("RESIZE_IMAGE_WORKER_ACTION")
        )
    }
    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    fun Context.registerReceiverNotExported(
        receiver: BroadcastReceiver, filter: IntentFilter
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            registerReceiver(receiver, filter, AppCompatActivity.RECEIVER_NOT_EXPORTED)
        else
            registerReceiver(receiver, filter)
    }
    fun clear() {
        context.unregisterReceiver(resizeImageWorkerBroadcastReceiver)
    }

    init {
        registerBroadCast()
    }

}