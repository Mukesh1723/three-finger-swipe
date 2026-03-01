package eu.hxreborn.tfs.gesture

import android.graphics.Bitmap
import android.graphics.Rect
import eu.hxreborn.tfs.util.log

internal object DisplayCaptureGateway {
    fun captureRegion(
        rect: Rect,
        frameworkClassLoader: ClassLoader,
    ): Bitmap? {
        val screenCaptureClass =
            Class.forName("android.window.ScreenCapture", false, frameworkClassLoader)

        val displayControlClass =
            Class.forName(
                "com.android.server.display.DisplayControl",
                false,
                frameworkClassLoader,
            )
        val ids =
            displayControlClass.getMethod("getPhysicalDisplayIds").invoke(null) as? LongArray
                ?: run {
                    log("DisplayCaptureGateway: getPhysicalDisplayIds unavailable")
                    return null
                }
        if (ids.isEmpty()) {
            log("DisplayCaptureGateway: no physical display IDs")
            return null
        }
        val displayToken =
            displayControlClass
                .getMethod(
                    "getPhysicalDisplayToken",
                    Long::class.javaPrimitiveType,
                ).invoke(null, ids[0]) ?: run {
                log("DisplayCaptureGateway: display token unavailable")
                return null
            }

        val displayArgsBuilderClass =
            Class.forName(
                "android.window.ScreenCapture\$DisplayCaptureArgs\$Builder",
                false,
                frameworkClassLoader,
            )
        val argsBuilder =
            displayArgsBuilderClass
                .getDeclaredConstructor(android.os.IBinder::class.java)
                .newInstance(displayToken)

        val setSourceCrop = argsBuilder.javaClass.getMethod("setSourceCrop", Rect::class.java)
        setSourceCrop.invoke(argsBuilder, rect)

        val captureArgs = argsBuilder.javaClass.getMethod("build").invoke(argsBuilder)

        val syncListener = screenCaptureClass.getMethod("createSyncCaptureListener").invoke(null)

        val captureDisplay =
            screenCaptureClass.methods.firstOrNull {
                it.name == "captureDisplay" && it.parameterCount == 2
            } ?: run {
                log("DisplayCaptureGateway: captureDisplay(2-arg) not found")
                return null
            }
        captureDisplay.isAccessible = true
        captureDisplay.invoke(null, captureArgs, syncListener)

        val buffer =
            syncListener?.javaClass?.getMethod("getBuffer")?.invoke(syncListener) ?: run {
                log("DisplayCaptureGateway: getBuffer returned null")
                return null
            }

        return buffer.javaClass.getMethod("asBitmap").invoke(buffer) as? Bitmap
    }
}
