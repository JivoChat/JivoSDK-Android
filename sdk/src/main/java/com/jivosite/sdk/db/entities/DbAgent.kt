package com.jivosite.sdk.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created on 3/21/21.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
@Entity(tableName = "agent")
data class DbAgent(
    @PrimaryKey
    val id: Long,
    val name: String,
    val title: String,
    val photo: String
)