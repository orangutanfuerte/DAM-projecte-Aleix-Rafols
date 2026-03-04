package com.example.travellikeasigma.model

import androidx.compose.ui.graphics.Color
import com.example.travellikeasigma.R

data class Photo(val id: Int, val drawableRes: Int? = null)

val samplePhotos = listOf(
    Photo(1, R.drawable.photo1),
    Photo(2, R.drawable.photo2),
    Photo(3, R.drawable.photo3),
    Photo(4,  R.drawable.photo4),
    Photo(5,  R.drawable.photo5),
    Photo(6,  R.drawable.photo6),
)
