@file:Suppress("unused")

package dev.jianastrero.journey.example.screens

private const val CENTS_PER_DOLLAR = 100
private const val ROUNDING_OFFSET = 0.5
private const val SINGLE_DIGIT_MAX = 10

internal fun Double.toPrice(): String {
    val cents = (this * CENTS_PER_DOLLAR + ROUNDING_OFFSET).toLong()
    val dollars = cents / CENTS_PER_DOLLAR
    val centsPart = cents % CENTS_PER_DOLLAR
    val decStr = if (centsPart < SINGLE_DIGIT_MAX) "0$centsPart" else "$centsPart"
    return "$dollars.$decStr"
}
