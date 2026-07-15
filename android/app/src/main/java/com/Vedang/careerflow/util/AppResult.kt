package com.Vedang.careerflow.util

/** A success value or a user-presentable failure from an app operation. */
sealed interface AppResult<out T> {
    data class Success<T>(val data: T) : AppResult<T>

    data class Error(val message: String, val cause: Throwable? = null) : AppResult<Nothing>
}
