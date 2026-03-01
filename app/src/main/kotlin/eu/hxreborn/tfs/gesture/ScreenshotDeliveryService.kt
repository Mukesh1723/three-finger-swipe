package eu.hxreborn.tfs.gesture

import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Handler
import eu.hxreborn.tfs.util.log
import eu.hxreborn.tfs.util.readField

private const val TAKE_SCREENSHOT_PROVIDED_IMAGE = 3
private const val SCREENSHOT_VENDOR_GESTURE = 6

internal object ScreenshotDeliveryService {
    fun deliverToSystemUI(
        displayPolicy: Any,
        handler: Handler,
        bitmap: Bitmap,
        rect: Rect,
    ) {
        val screenshotHelper =
            displayPolicy.readField("mScreenshotHelper") ?: run {
                log("ScreenshotDeliveryService: mScreenshotHelper unavailable")
                return
            }

        val cl = displayPolicy.javaClass.classLoader
        val builderClass =
            Class.forName(
                "com.android.internal.util.ScreenshotRequest\$Builder",
                false,
                cl,
            )
        val builder =
            builderClass
                .getDeclaredConstructor(
                    Int::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType,
                ).newInstance(TAKE_SCREENSHOT_PROVIDED_IMAGE, SCREENSHOT_VENDOR_GESTURE)
        builderClass.getMethod("setBitmap", Bitmap::class.java).invoke(builder, bitmap)
        builderClass.getMethod("setBoundsOnScreen", Rect::class.java).invoke(builder, rect)
        val request = builderClass.getMethod("build").invoke(builder)

        val takeScreenshot =
            screenshotHelper.javaClass.methods.firstOrNull {
                it.name == "takeScreenshot" &&
                    it.parameterCount == 3
            }
                ?: run {
                    log("ScreenshotDeliveryService: takeScreenshot not found on ScreenshotHelper")
                    return
                }
        takeScreenshot.isAccessible = true
        takeScreenshot.invoke(screenshotHelper, request, handler, null)
        log(
            "ScreenshotDeliveryService: crop screenshot delivered to SystemUI (${rect.width()}x${rect.height()})",
        )
    }
}
