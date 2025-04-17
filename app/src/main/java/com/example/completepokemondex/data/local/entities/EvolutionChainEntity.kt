package com.example.completepokemondex.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

/**
 * Entidad que representa una cadena de evolución.
 * Los campos complejos se almacenan como JSON serializado.
 */
@Entity(tableName = "evolution_chain_table")
data class EvolutionChainEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "baby_trigger_item")
    val babyTriggerItem: String?, // JSON serializado

    @ColumnInfo(name = "chain")
    val chain: String? // JSON serializado
)
