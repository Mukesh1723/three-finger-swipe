package eu.hxreborn.tfs.gesture

data class GestureConfig(
    val requiredFingers: Int = 3,
    val fingerLandingWindowMs: Long = 800L,
    val longPressHoldMs: Long = 350L,
    val startingProximityPx: Float = 500f,
    val swipeThresholdFraction: Float = 0.14f,
    val edgeExclusionDp: Float = 50f,
    val cooldownMs: Long = 500L,
)
