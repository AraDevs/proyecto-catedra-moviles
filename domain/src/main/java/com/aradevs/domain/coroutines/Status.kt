package com.aradevs.domain.coroutines

/**
 * [Status] sealed class
 * Represents one of 3 different states to be observed in live data or flows
 * [Status.Initial] may represent the initial status before any request is made
 * [Status.Loading] may represent a loading state (fetching information)
 * [Status.Success] may represent a successful retrieval of data
 * [Status.Error] may represent an emergent error inside the coroutine scope
 */
sealed class Status<out T> {
    class Initial<T> : Status<T>()
    class Loading<T> : Status<T>()
    class Success<T>(val data: T, vararg val extras: Any?) : Status<T>()
    class Error<T>(val exception: Exception) : Status<T>()
}