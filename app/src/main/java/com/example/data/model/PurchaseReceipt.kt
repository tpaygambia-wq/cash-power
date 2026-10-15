package com.example.data.model

data class PurchaseReceipt(
    val id: String,
    val meterNumber: String,
    val ownerName: String,
    val formattedToken: String,
    val rawToken: String,
    val amountPaid: Int,
    val fee: Int,
    val totalCharged: Int,
    val unitsKwh: Double,
    val paymentMethod: String,
    val payerPhone: String,
    val referenceId: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isLoaded: Boolean = false
)
