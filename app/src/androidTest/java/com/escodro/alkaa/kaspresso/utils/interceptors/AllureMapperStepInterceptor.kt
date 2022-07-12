package com.escodro.alkaa.kaspresso.utils.interceptors

import com.kaspersky.kaspresso.interceptors.watcher.testcase.StepWatcherInterceptor
import com.kaspersky.kaspresso.testcases.models.info.StepInfo
import io.qameta.allure.kotlin.Allure
import io.qameta.allure.kotlin.model.Status
import io.qameta.allure.kotlin.model.StepResult
import io.qameta.allure.kotlin.util.ResultsUtils
import java.util.UUID

/**
 * Интерсептор для прокидывания step'ов Kaspresso в отчеты Allure
 */
class AllureMapperStepInterceptor : StepWatcherInterceptor {

    var uuid: String = String()
    val lifecycle = Allure.lifecycle

    override fun interceptBefore(stepInfo: StepInfo) {
        uuid = UUID.randomUUID().toString()
        lifecycle.startStep(uuid, StepResult().apply {
            this.name = stepInfo.description
            this.status = Status.PASSED
        })
    }

    override fun interceptAfterWithError(stepInfo: StepInfo, error: Throwable) {
        lifecycle.updateStep {
            with(it) {
                status = ResultsUtils.getStatus(error) ?: Status.BROKEN
                statusDetails = ResultsUtils.getStatusDetails(error)
            }
        }
    }

    override fun interceptAfterFinally(stepInfo: StepInfo) {
        lifecycle.stopStep()
    }
}
