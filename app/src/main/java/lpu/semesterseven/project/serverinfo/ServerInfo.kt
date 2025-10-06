package lpu.semesterseven.project.serverinfo

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import lpu.semesterseven.project.R

@Serializable
data class Info(
    var description : String,
    var level       : Int,
    var icon        : String? = null
)

fun parseInfo(jsonString: String)   : List<Info>{
    var json = Json{ ignoreUnknownKeys = true }
    return json.decodeFromString(jsonString)
}

const val SERVER_INFO_STATE_CRITICAL      = 1
const val SERVER_INFO_STATE_IMPORTANT     = 2
const val SERVER_INFO_STATE_WARNING       = 3
const val SERVER_INFO_STATE_NORMAL        = 4
const val SERVER_INFO_STATE_GOOD          = 5
const val SERVER_INFO_STATE_NEUTRAL       = 6

fun getInfoStateName(state: Int): String{
    return when(state){
        SERVER_INFO_STATE_CRITICAL  -> "Critical"
        SERVER_INFO_STATE_IMPORTANT -> "Important"
        SERVER_INFO_STATE_WARNING   -> "Warning"
        SERVER_INFO_STATE_NORMAL    -> "Normal"
        SERVER_INFO_STATE_GOOD      -> "Good"
        else -> "Neutral"
    }
}

val infoIconDefault: Int = R.drawable.baseline_info_outline_24
var infoIcon: Map<String, Int> = mapOf(
    "plant_grass"       to R.drawable.baseline_grass_24,
    "medical_bag"       to R.drawable.baseline_medical_services_24,
    "medical_mask"      to R.drawable.baseline_masks_24,
    "weather_tornado"   to R.drawable.baseline_tornado_24,
    "weather_sunny"     to R.drawable.baseline_sunny_24
)