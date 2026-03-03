package eu.hxreborn.tfs.action.flashlight

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Handler
import android.os.Looper
import eu.hxreborn.tfs.action.Action
import eu.hxreborn.tfs.util.log

class ToggleFlashlightAction(
    context: Context,
) : Action {
    private val cameraManager =
        context.getSystemService(Context.CAMERA_SERVICE) as CameraManager

    private val cameraId: String? = findFlashCameraId()

    @Volatile
    private var flashlightOn = false

    init {
        if (cameraId != null) {
            cameraManager.registerTorchCallback(
                object : CameraManager.TorchCallback() {
                    override fun onTorchModeChanged(
                        cameraId: String,
                        enabled: Boolean,
                    ) {
                        if (cameraId == this@ToggleFlashlightAction.cameraId) {
                            flashlightOn = enabled
                        }
                    }
                },
                Handler(Looper.getMainLooper()),
            )
        }
    }

    override fun execute() {
        val id =
            cameraId ?: run {
                log("No camera with flash found")
                return
            }
        runCatching {
            cameraManager.setTorchMode(id, !flashlightOn)
        }.onSuccess {
            log("Flashlight ${if (!flashlightOn) "on" else "off"}")
        }.onFailure {
            log("Flashlight toggle failed", it)
        }
    }

    private fun findFlashCameraId(): String? =
        runCatching {
            cameraManager.cameraIdList.firstOrNull { id ->
                cameraManager
                    .getCameraCharacteristics(id)
                    .get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
            }
        }.onFailure {
            log("Flash camera lookup failed", it)
        }.getOrNull()
}
