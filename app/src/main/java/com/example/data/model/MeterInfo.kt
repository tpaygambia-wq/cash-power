package com.example.data.model

data class MeterInfo(
    val meterNumber: String,
    val ownerName: String,
    val balanceKwh: String,
    val tariffCategory: String = "Domestic D1 (Residential)",
    val area: String = "Greater Banjul Area",
    val connectionType: String = "STS Single-Phase Prepaid",
    val nickname: String? = null
)
