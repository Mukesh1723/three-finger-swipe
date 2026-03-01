package eu.hxreborn.tfs.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import eu.hxreborn.tfs.R

private val THEMED_LAYERS =
    listOf(
        "Shape Layer 15",
        "Shape Layer 12",
        "Shape Layer 11",
        "Shape Layer 10",
        "Shape Layer 6",
        "Shape Layer 5",
        "Shape Layer 4",
    )

@Composable
fun GestureIllustration(modifier: Modifier = Modifier) {
    val primary = MaterialTheme.colorScheme.primary.toArgb()
    val dynamicProperties =
        rememberLottieDynamicProperties(
            *THEMED_LAYERS
                .map { layer ->
                    rememberLottieDynamicProperty(
                        property = LottieProperty.COLOR,
                        value = primary,
                        layer,
                        "**",
                        "Fill 1",
                    )
                }.toTypedArray(),
        )
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.swipe_down_phone),
    )

    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        dynamicProperties = dynamicProperties,
        modifier = modifier.fillMaxWidth().height(200.dp),
    )
}
