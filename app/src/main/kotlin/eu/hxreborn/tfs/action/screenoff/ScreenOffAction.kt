package eu.hxreborn.tfs.action.screenoff

import android.content.Context
import android.os.PowerManager
import android.os.SystemClock
import eu.hxreborn.tfs.action.Action
import eu.hxreborn.tfs.util.findMethodUpward
import eu.hxreborn.tfs.util.log

class ScreenOffAction(
    private val context: Context,
) : Action {
    override fun execute() {
        runCatching {
            val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            val method =
                pm.javaClass.findMethodUpward(
                    "goToSleep",
                    Long::class.javaPrimitiveType!!,
                ) ?: error("PowerManager.goToSleep(long) not found")
            method.invoke(pm, SystemClock.uptimeMillis())
        }.onSuccess {
            log("Screen turned off")
        }.onFailure {
            log("Screen off failed", it)
        }
    }
}
