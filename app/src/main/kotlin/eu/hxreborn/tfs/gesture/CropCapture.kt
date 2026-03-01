package eu.hxreborn.tfs.gesture

import android.content.Context
import android.graphics.PixelFormat
import android.graphics.Rect
import android.hardware.display.DisplayManager
import android.os.Build
import android.os.Handler
import android.view.Display
import android.view.WindowManager
import androidx.annotation.RequiresApi
import eu.hxreborn.tfs.util.log
import eu.hxreborn.tfs.util.readField

internal object CropCapture {
    @RequiresApi(Build.VERSION_CODES.S)
    fun show(
        context: Context,
        displayPolicy: Any,
    ) {
        val handler =
            displayPolicy.readField("mHandler") as? Handler ?: run {
                log("CropCapture: mHandler unavailable in DisplayPolicy")
                return
            }
        val display =
            context
                .getSystemService(DisplayManager::class.java)
                ?.getDisplay(Display.DEFAULT_DISPLAY) ?: run {
                log("CropCapture: default display unavailable")
                return
            }
        val windowContext =
            context.createWindowContext(
                display,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                null,
            )
        val wm =
            windowContext.getSystemService(WindowManager::class.java) ?: run {
                log("CropCapture: WindowManager unavailable")
                return
            }
        handler.post { showOverlay(windowContext, wm, displayPolicy, handler) }
    }

    private fun showOverlay(
        context: Context,
        wm: WindowManager,
        displayPolicy: Any,
        policyHandler: Handler,
    ) {
        var selectorView: CropSelectorView? = null
        selectorView =
            CropSelectorView(context) { rect ->
                runCatching { wm.removeView(selectorView) }.onFailure {
                    log(
                        "CropCapture: failed to remove overlay",
                        it,
                    )
                }
                Thread {
                    runCatching {
                        captureAndDeliver(
                            displayPolicy,
                            policyHandler,
                            rect,
                        )
                    }.onFailure { log("CropCapture: capture+deliver failed", it) }
                }.start()
            }

        val params =
            WindowManager
                .LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    PixelFormat.TRANSLUCENT,
                ).apply { title = "TFS-CropSelector" }

        runCatching { wm.addView(selectorView, params) }.onFailure {
            log(
                "CropCapture: failed to add overlay (${it.javaClass.simpleName}: ${it.message})",
                it,
            )
        }
    }

    private fun captureAndDeliver(
        displayPolicy: Any,
        handler: Handler,
        rect: Rect,
    ) {
        val bitmap =
            DisplayCaptureGateway.captureRegion(
                rect,
                displayPolicy.javaClass.classLoader
                    ?: error("classLoader unavailable on DisplayPolicy"),
            ) ?: run {
                log("CropCapture: captureRegion returned null")
                return
            }
        ScreenshotDeliveryService.deliverToSystemUI(displayPolicy, handler, bitmap, rect)
    }
}
