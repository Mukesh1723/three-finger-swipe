package eu.hxreborn.tfs.prefs

import eu.hxreborn.tfs.ModuleConstants

object Prefs {
    val GROUP: String = ModuleConstants.prefsGroup

    val SWIPE_ENABLED = BoolPref("swipe_enabled", true)
    val LONG_PRESS_ENABLED = BoolPref("long_press_enabled", false)
    val DEBUG_LOGS = BoolPref("debug_logs", false)

    val all: List<PrefSpec<*>> =
        listOf(
            SWIPE_ENABLED,
            LONG_PRESS_ENABLED,
            DEBUG_LOGS,
        )
}
