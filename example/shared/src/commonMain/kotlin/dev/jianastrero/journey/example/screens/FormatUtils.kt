package dev.jianastrero.journey.example.screens

internal fun Double.toPrice(): String {
    val cents = (this * 100 + 0.5).toLong()
    val dollars = cents / 100
    val centsPart = cents % 100
    val decStr = if (centsPart < 10) "0$centsPart" else "$centsPart"
    return "$dollars.$decStr"
}
