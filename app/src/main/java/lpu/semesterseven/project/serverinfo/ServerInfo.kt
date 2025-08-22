package lpu.semesterseven.project.serverinfo

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class EmergencyInfo(
    var description : List<String>
)

fun parseEmergencyInfo(jsonString: String)  : EmergencyInfo = Json.decodeFromString(jsonString)
