package com.escodro.alkaa.kaspresso.utils.interceptors

import com.escodro.alkaa.kaspresso.utils.EspressoScreenshot
import com.kaspersky.kaspresso.interceptors.watcher.testcase.TestRunWatcherInterceptor
import com.kaspersky.kaspresso.testcases.models.info.TestInfo
import io.qameta.allure.android.AllureAndroidLifecycle

/**
 * Интерсептор добавляет скриншот к упавшему тесту
 */
class ScreenshotTestRunInterceptor(
    private val screenShooter: EspressoScreenshot
) : TestRunWatcherInterceptor {

    override fun onAfterSectionFinishedFailed(testInfo: TestInfo, throwable: Throwable) {
        val tag = "AfterTestSection_failure_${throwable.javaClass.simpleName}"
        screenShooter.takeScreenshot(tag).let {
            AllureAndroidLifecycle.addAttachment(
                name = tag,
                body = it.readBytes(),
                type = "image/png",
                fileExtension = "png"
            )
        }
    }

    override fun onBeforeSectionFinishedFailed(testInfo: TestInfo, throwable: Throwable) {
        val tag = "BeforeTestSection_failure_${throwable.javaClass.simpleName}"
        screenShooter.takeScreenshot(tag).let {
            AllureAndroidLifecycle.addAttachment(
                name = tag,
                body = it.readBytes(),
                type = "image/png",
                fileExtension = "png"
            )
        }
    }
}
