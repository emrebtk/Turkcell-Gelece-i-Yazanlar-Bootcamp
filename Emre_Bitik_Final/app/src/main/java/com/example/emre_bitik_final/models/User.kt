package com.example.emre_bitik_final.models

import java.io.Serializable

data class User (
    val id: Long,
    val firstName: String,
    val lastName: String,
    val maidenName: String,
    val age: Int,
    val gender: String,
    val email: String,
    val phone: String,
    val username: String,
    val password: String,
    val birthDate: String,
    val image: String,
    val bloodGroup: String,
    val height: Double,
    val weight: Double,
    val eyeColor: String,
    val ip: String,
    val macAddress: String,
    val university: String,
    val ein: String,
    val ssn: String,
    val userAgent: String,


):Serializable
