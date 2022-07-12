package com.escodro.alkaa.kaspresso.screen

import androidx.fragment.app.Fragment
import com.agoda.kakao.text.KTextView
import com.escodro.alkaa.R
import com.escodro.alkaa.kaspresso.base.BaseScreen
import kotlin.reflect.KClass

object MainScreen : BaseScreen<MainScreen>() {
    override val layout: Int
        get() = TODO("Not yet implemented")
    override val fragment: KClass<out Fragment>
        get() = TODO("Not yet implemented")

     val myView = KTextView {withId(R.id.save_image_matrix)}
}