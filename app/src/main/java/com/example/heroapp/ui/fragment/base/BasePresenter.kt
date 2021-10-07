package com.example.heroapp.ui.fragment.base

import com.example.heroapp.util.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

abstract class BasePresenter {
    val customScope = CoroutineScope(Dispatchers.Default)

    fun <T> handleRequest(
        response: Flow<State<T>>,
        onError: (String) -> Unit,
        onLoading: () -> Unit,
        onSuccess: (T?) -> Unit
    ) {
       customScope.launch{
           response.flowOn(Dispatchers.IO)
               .catch { throwable ->
                   onError( throwable.message.toString())
               }.collect { state ->
                   state.handle(onError,onLoading,onSuccess)
               }
       }
    }

    private fun <T> State<T>.handle(
        onError: (String) -> Unit,
        onLoading: () -> Unit,
        onSuccess: (T?) -> Unit,
    ){
        when(this){
            is State.Error -> onError(message)
            State.Loading -> onLoading()
            is State.Success -> onSuccess(data)
        }
    }
}