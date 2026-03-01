package eu.hxreborn.tfs.xposed

import android.content.SharedPreferences
import eu.hxreborn.tfs.util.log
import eu.hxreborn.tfs.util.methodAccessible
import eu.hxreborn.tfs.xposed.hook.PhoneWindowManagerHooker
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface.SystemServerLoadedParam

object SystemServerHooks {
    fun hook(
        module: XposedModule,
        param: SystemServerLoadedParam,
        prefs: SharedPreferences?,
    ) {
        PhoneWindowManagerHooker.init(prefs)
        val phoneWindowManager =
            param.classLoader.loadClass(
                "com.android.server.policy.PhoneWindowManager",
            )
        module.hook(
            phoneWindowManager.methodAccessible("systemReady"),
            PhoneWindowManagerHooker::class.java,
        )
        log("Registered PhoneWindowManager.systemReady hook")
    }
}
