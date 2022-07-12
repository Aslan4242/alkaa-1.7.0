package com.escodro.alkaa.kaspresso.base

import androidx.fragment.app.Fragment
import com.agoda.kakao.screen.Screen
import kotlin.reflect.KClass

abstract class BaseScreen<out T : BaseScreen<T>> : Screen<T>() {
    protected abstract val layout: Int
    protected abstract val fragment: KClass<out Fragment>
}