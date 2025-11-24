package com.example.clinicaveterinariaapp.ui.theme

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.clinicaveterinariaapp.R

@Composable
fun AppBackground(
    modifier: Modifier = Modifier,
    @DrawableRes resId: Int = R.drawable.fondo1,
    alpha: Float = 0.85f,
    blurDp: Dp = 12.dp,
    content: @Composable () -> Unit = {}
) {
    Box(modifier = modifier.fillMaxSize()) {
        // Dibujamos la imagen también durante la inspección/preview
        Image(
            painter = painterResource(id = resId),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .blur(blurDp),
            contentScale = ContentScale.Crop,
            alpha = alpha
        )

        // Contenido de la app encima del fondo
        Box(modifier = Modifier.fillMaxSize()) {
            content()
        }
    }
}