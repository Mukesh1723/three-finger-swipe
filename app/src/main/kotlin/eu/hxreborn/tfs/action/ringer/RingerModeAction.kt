package eu.hxreborn.tfs.action.ringer

import android.content.Context
import android.media.AudioManager
import eu.hxreborn.tfs.action.Action
import eu.hxreborn.tfs.util.log

class RingerModeAction(
    private val context: Context,
) : Action {
    override fun execute() {
        runCatching {
            val am = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val next =
                when (am.ringerMode) {
                    AudioManager.RINGER_MODE_NORMAL -> AudioManager.RINGER_MODE_VIBRATE
                    AudioManager.RINGER_MODE_VIBRATE -> AudioManager.RINGER_MODE_SILENT
                    else -> AudioManager.RINGER_MODE_NORMAL
                }
            am.ringerMode = next
        }.onSuccess {
            log("Ringer mode changed")
        }.onFailure {
            log("Ringer mode change failed", it)
        }
    }
}
