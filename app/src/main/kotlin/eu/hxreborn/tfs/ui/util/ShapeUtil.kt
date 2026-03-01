package eu.hxreborn.tfs.ui.util

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

fun shapeForPosition(
    count: Int,
    index: Int,
): RoundedCornerShape =
    when {
        count == 1 -> {
            RoundedCornerShape(24.dp)
        }

        index == 0 -> {
            RoundedCornerShape(
                topStart = 24.dp,
                topEnd = 24.dp,
                bottomEnd = 4.dp,
                bottomStart = 4.dp,
            )
        }

        index == count - 1 -> {
            RoundedCornerShape(
                topStart = 4.dp,
                topEnd = 4.dp,
                bottomEnd = 24.dp,
                bottomStart = 24.dp,
            )
        }

        else -> {
            RoundedCornerShape(4.dp)
        }
    }
