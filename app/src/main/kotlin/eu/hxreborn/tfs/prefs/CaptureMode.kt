package eu.hxreborn.tfs.prefs

enum class CaptureMode(
    val key: String,
) {
    SYSTEM_API("system_api"),
    SYSRQ("sysrq"),
    ;

    companion object {
        fun fromKey(key: String): CaptureMode = entries.find { it.key == key } ?: SYSTEM_API
    }
}
