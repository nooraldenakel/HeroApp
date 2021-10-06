package com.example.heroapp.util

sealed class State<out T> {
    data class Success<T>(val data: T) : State<T>()
    data class Error<T>(val message: String) : State<Nothing>()
    object Loading : State<Nothing>()
}
