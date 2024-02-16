package com.teleb.chefaaimagestask.presentation.details.resizeService

enum class WorkerResult(val value: Int) {
    SUCCESS(1), FAILED(0), SHOW_LOADING(2), DISMISS_LOADING(3);

    companion object {
        fun find(value: Int) = entries.find { it.value == value } ?: FAILED
    }
}
