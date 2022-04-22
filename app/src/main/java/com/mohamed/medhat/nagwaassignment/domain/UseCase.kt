package com.mohamed.medhat.nagwaassignment.domain

/**
 * Represents an action that can occur inside the app.
 */
abstract class UseCase<Q : UseCase.RequestValues, R : UseCase.ResponseValues, P : UseCase.ProgressValues> {

    var requestValues: Q? = null
    var onSuccess: (R) -> Unit = {}
    var onError: (String) -> Unit = {}
    var onProgress: (P) -> Unit = {}

    /**
     * Enables the [UseCaseHandler] to execute the use case.
     */
    internal suspend fun run() {
        executeUseCase(requestValues)
    }

    /**
     * Executes this use case using the passed [RequestValues].
     */
    protected abstract suspend fun executeUseCase(requestValues: Q?)

    /**
     * Data passed to a use case.
     */
    interface RequestValues

    /**
     * Updates coming from the use case.
     */
    interface ProgressValues

    /**
     * Data received from a use case.
     */
    interface ResponseValues

    /**
     * Used by use cases that don't require request values.
     */
    class NoRequestValues : RequestValues

    /**
     * Used by use cases that don't have response values.
     */
    class NoResponseValues : ResponseValues

    /**
     * Used by use cases that don't have progress values.
     */
    class NoProgressValues : ProgressValues
}