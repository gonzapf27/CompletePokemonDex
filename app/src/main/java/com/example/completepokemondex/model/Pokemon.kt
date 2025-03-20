package com.example.completepokemondex.model

import java.net.URI

data class Pokemon(
    val name: String,
    val height: Int,
    val weight: Int,
    val sprites: Sprites
)
