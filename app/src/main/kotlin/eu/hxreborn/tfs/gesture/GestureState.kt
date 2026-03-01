package eu.hxreborn.tfs.gesture

import android.graphics.PointF

sealed interface GestureState {
    data object Idle : GestureState

    data class Tracking(
        val startPoints: Map<Int, PointF>,
        val startTimeMs: Long,
    ) : GestureState

    data object Triggered : GestureState
}
