package com.example.completepokemondex.data.domain.model

class EncountersDomain : ArrayList<EncountersDomainItem>(){
    data class EncountersDomainItem(
        val location_area: LocationArea?,
        val version_details: List<VersionDetail?>?
    ) {
        data class LocationArea(
            val name: String?,
            val url: String?
        )
    
        data class VersionDetail(
            val encounter_details: List<EncounterDetail?>?,
            val max_chance: Int?,
            val version: Version?
        ) {
            data class EncounterDetail(
                val chance: Int?,
                val condition_values: List<ConditionValue?>?,
                val max_level: Int?,
                val method: Method?,
                val min_level: Int?
            ) {
                data class ConditionValue(
                    val name: String?,
                    val url: String?
                )
    
                data class Method(
                    val name: String?,
                    val url: String?
                )
            }
    
            data class Version(
                val name: String?,
                val url: String?
            )
        }
    }
}