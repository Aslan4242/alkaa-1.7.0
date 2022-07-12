package com.escodro.alkaa.kaspresso.utils

import androidx.test.espresso.ViewInteraction
import com.kaspersky.kaspresso.autoscroll.AutoScrollProvider
import com.kaspersky.kaspresso.interceptors.behavior.ViewBehaviorInterceptor
import com.kaspersky.kaspresso.logger.UiTestLogger
import com.kaspersky.kaspresso.params.AutoScrollParams

/**
 * The implementation of [ViewBehaviorInterceptor] and [AutoScrollProvider] interfaces.
 * Provides autoscroll on failure functionality for [ViewInteraction.perform] and [ViewInteraction.check] calls.
 */
class AutoNestedScrollViewBehaviorInterceptor(
        params: AutoScrollParams,
        logger: UiTestLogger
) : ViewBehaviorInterceptor,
    AutoScrollProvider<ViewInteraction> by AutoNestedScrollProviderImpl(params, logger) {

    /**
     * Wraps the given [action] invocation with the autoscrolling on failure.
     *
     * @param interaction the intercepted [ViewInteraction].
     * @param action the action to invoke.
     */
    override fun <T> intercept(interaction: ViewInteraction, action: () -> T): T = withAutoScroll(interaction, action)
}