package com.escodro.alkaa.kaspresso.base

import android.Manifest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.escodro.alkaa.presentation.MainActivity
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.kaspersky.kaspresso.testcases.core.sections.InitSection
import com.kaspersky.kaspresso.testcases.core.testcontext.BaseTestContext
import org.junit.Rule
import com.escodro.alkaa.kaspresso.utils.AutoNestedScrollViewBehaviorInterceptor
import com.escodro.alkaa.kaspresso.utils.EspressoScreenshot
import com.escodro.alkaa.kaspresso.utils.interceptors.AllureMapperStepInterceptor
import com.escodro.alkaa.kaspresso.utils.interceptors.AllureTimeLineMultiThreadFixer
import com.escodro.alkaa.kaspresso.utils.interceptors.ScreenshotStepInterceptor
import com.escodro.alkaa.kaspresso.utils.interceptors.ScreenshotTestRunInterceptor

open class BaseTestCase(builder: Kaspresso.Builder = defaultBuilder) : TestCase(builder) {

    /**
     * Указываем необходимые разрешения для приложения перед тестом
     */
    @get:Rule
    val runtimePermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
           // Manifest.permission.WRITE_EXTERNAL_STORAGE,
           // Manifest.permission.READ_EXTERNAL_STORAGE
    )

    private var currentData: Map<String, Any>? = null

    /**
     * Получает все данные текущего пользователя [UserDataType] в виде Map<String, String>
     *
     * @return Данные текущего пользователя
     */
    fun getAllUserData() = currentData as Map<String, String>

    /**
     * Получает данные текущего пользователя
     *
     * @param [data] Название данных пользователя из [UserDataType]
     * @return Значение поля
     */
    private fun getUserData(data: String) = currentData?.getValue(data)
            ?: throw IllegalStateException("Неверные данные в тесте")

    /**
     * Получает данные текущего пользователя в виде строки
     *
     * @param [data] Название данных пользователя из [UserDataType]
     * @return Значение поля
     */
    fun getUserDataAsString(data: String)  = getUserData(data) as String

    /**
     * Получает данные текущего пользователя в виде Map<String, String>
     *
     * @param [data] Название данных пользователя из [UserDataType]
     * @return Значение поля
     */
    fun getUserDataAsMap(data: String)  = getUserData(data) as Map<String, String>

    /**
     * Получает данные текущего пользователя в виде List<String>
     *
     * @param [data] Название данных пользователя из [UserDataType]
     * @return Значение поля
     */
    fun getUserDataAsList(data: String)  = getUserData(data) as List<String>

    /**
     * Действия, выполняемые до запуска теста
     *
     * @param [userDataType] Пользователь из списка [UserDataType], от лица которого будет выполняться сценарий
     * @param [before] Действия, которые следует выполнить до теста
     * @param [after] Действия, которые следует выполнить после теста
     */
    protected fun launch(
            //userDataType: UserDataType? = null,
            before: BaseTestContext.() -> Unit = {},
            after: BaseTestContext.() -> Unit = {}
    ): InitSection<Unit, Unit> {
        ActivityTestRule(MainActivity::class.java, false, false)
                .launchActivity(null)

        //currentData = userDataType?.let { environment.configuration?.get(it) }

        return before(actions = before).after(after)
    }

    companion object {
        val defaultBuilder
            get() = Kaspresso.Builder.simple().apply {
                flakySafetyParams.timeoutMs = 10_000L
                viewBehaviorInterceptors[0] = AutoNestedScrollViewBehaviorInterceptor(autoScrollParams, libLogger)
                stepWatcherInterceptors.add(ScreenshotStepInterceptor(EspressoScreenshot()))
                stepWatcherInterceptors.add(AllureMapperStepInterceptor())
                testRunWatcherInterceptors.addAll(
                    arrayOf(
                        ScreenshotTestRunInterceptor(EspressoScreenshot()),
                        AllureTimeLineMultiThreadFixer()
                    )
                )
                continuouslyParams.timeoutMs = 1000L
            }
    }
}
