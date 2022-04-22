package com.mohamed.medhat.nagwaassignment.domain

import com.mohamed.medhat.nagwaassignment.domain.UseCase.RequestValues
import com.mohamed.medhat.nagwaassignment.domain.UseCase.ResponseValues
import javax.inject.Inject

/**
 * Responsible for executing the use cases.
 */
class UseCaseHandler @Inject constructor() {

    /**
     * Executes a specific use case.
     * @param useCase The use case to execute.
     * @param values The input values that are required by the use case so that it can run.
     * @param onSuccess What to do after a successful execution of the use case.
     * @param onError What to do after a failed execution of the use case.
     */
    suspend fun <T : RequestValues, R : ResponseValues> execute(
        useCase: UseCase<T, R>,
        values: T,
        onSuccess: (R) -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        useCase.apply {
            requestValues = values
            this.onSuccess = onSuccess
            this.onError = onError
            run()
        }
    }
}