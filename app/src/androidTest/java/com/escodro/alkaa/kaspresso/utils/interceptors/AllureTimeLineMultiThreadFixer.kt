package com.escodro.alkaa.kaspresso.utils.interceptors

import android.app.Instrumentation
import androidx.test.uiautomator.UiDevice
import com.kaspersky.kaspresso.interceptors.watcher.testcase.TestRunWatcherInterceptor
import com.kaspersky.kaspresso.testcases.models.info.TestInfo
import io.qameta.allure.android.AllureAndroidLifecycle

/**
 * Интерсептор исправляет некорректное отображение параллельного прогона в Allure отчете
 */
class AllureTimeLineMultiThreadFixer : TestRunWatcherInterceptor {

    override fun onTestFinished(testInfo: TestInfo, success: Boolean) {
        AllureAndroidLifecycle.updateTestCase { testResult ->
            val threadName = UiDevice.getInstance(Instrumentation()).executeShellCommand("getprop emu.name").trim()
            val hostName = UiDevice.getInstance(Instrumentation()).executeShellCommand("getprop host.name").trim()

            testResult.labels.find { label ->
                label.name == "thread"
            }?.value = threadName

            testResult.labels.find { label ->
                label.name == "host"
            }?.value = hostName
        }
    }
}
