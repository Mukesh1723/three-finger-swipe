package eu.hxreborn.tfs.xposed

import eu.hxreborn.tfs.BuildConfig
import eu.hxreborn.tfs.prefs.Prefs
import eu.hxreborn.tfs.util.Logger
import io.github.libxposed.api.XposedInterface
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface.ModuleLoadedParam
import io.github.libxposed.api.XposedModuleInterface.SystemServerLoadedParam

class ThreeFingerSwipeModule(
    base: XposedInterface,
    param: ModuleLoadedParam,
) : XposedModule(base, param) {
    init {
        Logger.attach(this)
        log(
            "Module v${BuildConfig.VERSION_NAME} on " +
                "${base.frameworkName} ${base.frameworkVersion}",
        )
    }

    override fun onSystemServerLoaded(param: SystemServerLoadedParam) {
        super.onSystemServerLoaded(param)

        val prefs =
            runCatching { getRemotePreferences(Prefs.GROUP) }
                .onFailure {
                    log(
                        "Remote prefs unavailable, using defaults",
                        it,
                    )
                }.getOrNull()

        prefs?.let { p ->
            Logger.debugEnabled = Prefs.DEBUG_LOGS.read(p)
            p.registerOnSharedPreferenceChangeListener { _, _ ->
                Logger.debugEnabled = Prefs.DEBUG_LOGS.read(p)
            }
        }

        runCatching {
            SystemServerHooks.hook(this, param, prefs)
        }.onSuccess { log("system_server hooks applied") }
            .onFailure { log("system_server hook registration failed", it) }
    }
}
