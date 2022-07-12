package com.escodro.alkaa.kaspresso.utils

import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import com.kaspersky.kaspresso.autoscroll.AutoScrollProvider
import com.kaspersky.kaspresso.logger.UiTestLogger
import com.kaspersky.kaspresso.params.AutoScrollParams
import io.reactivex.exceptions.ExtCompositeException

/**
 * The implementation of the [AutoScrollProvider] interface for [ViewInteraction]
 */
class AutoNestedScrollProviderImpl(
        private val params: AutoScrollParams,
        private val logger: UiTestLogger
) : AutoScrollProvider<ViewInteraction> {

    /**
     * Invokes the given [action] and calls [scroll] if it fails. Helps in cases when test fails because of the
     * need to scroll to interacted view.
     *
     * @param interaction the instance of [ViewInteraction] interface to perform actions and assertions.
     * @param action the actual action on the interacted view.
     *
     * @throws Throwable if the exception caught while invoking [action] is not allowed via [params].
     * @return the result of [action] invocation.
     */
    @Throws(Throwable::class)
    override fun <T> withAutoScroll(interaction: ViewInteraction, action: () -> T): T {
        return try {
            action()
        } catch (error: Throwable) {
            if (error.isAllowed(params.allowedExceptions)) {
                return scroll(interaction, action, error)
            }
            throw error
        }
    }

    /**
     * Performs the autoscrolling functionality. Performs scroll and re-invokes the given [action].
     *
     * @param interaction the instance of [ViewInteraction] interface to perform actions and assertions.
     * @param action the actual action on the interacted view.
     * @param cachedError the error to be thrown if autoscroll would not help.
     *
     * @throws cachedError if autoscroll action did not help.
     * @return the result of [action] invocation.
     */
    @Throws(Throwable::class)
    override fun <T> scroll(interaction: ViewInteraction, action: () -> T, cachedError: Throwable): T {
        return try {
            interaction.perform(ViewActions.actionWithAssertions(NestedScrollToAction()))
            logger.i("View autoScroll successfully performed.")
            action.invoke()
        } catch (error: Throwable) {
            throw cachedError
        }
    }

    /**
     * @return true if the given throwable is contained by [allowed] set, false otherwise.
     */
    private fun <T : Throwable> T.isAllowed(allowed: Set<Class<out Throwable>>): Boolean {
        return when (this) {
            is ExtCompositeException -> {
                exceptions.find { e: Throwable ->
                    allowed.find { it.isAssignableFrom(e.javaClass) } != null
                } != null
            }

            else -> {
                allowed.find { it.isAssignableFrom(javaClass) } != null
            }
        }
    }
}