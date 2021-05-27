package kz.eztech.stylyts.presentation.utils

object ColorUtils {

    fun getColorTitleFromHex(hex: String): String {
        return when (hex) {
            ColorEnum.BLACK.hex -> ColorEnum.BLACK.title
            ColorEnum.WHITE.hex -> ColorEnum.WHITE.title
            else -> EMPTY_STRING
        }
    }
}

enum class ColorEnum(val hex: String, val title: String) {
    BLACK(hex = "#000000", title = "Black"),
    WHITE(hex = "#ffffff", title = "white")
}