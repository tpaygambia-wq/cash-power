package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.MeterInfo
import com.example.data.model.PurchaseReceipt

@Entity(tableName = "saved_meters")
data class SavedMeterEntity(
    @PrimaryKey
    val meterNumber: String,
    val ownerName: String,
    val nickname: String,
    val tariffCategory: String,
    val area: String,
    val balanceKwh: String,
    val lastUsedTimestamp: Long = System.currentTimeMillis()
) {
    fun toMeterInfo() = MeterInfo(
        meterNumber = meterNumber,
        ownerName = ownerName,
        balanceKwh = balanceKwh,
        tariffCategory = tariffCategory,
        area = area,
        nickname = nickname
    )

    companion object {
        fun fromMeterInfo(info: MeterInfo, customNickname: String? = null) = SavedMeterEntity(
            meterNumber = info.meterNumber,
            ownerName = info.ownerName,
            nickname = customNickname ?: info.nickname ?: "Saved Meter",
            tariffCategory = info.tariffCategory,
            area = info.area,
            balanceKwh = info.balanceKwh,
            lastUsedTimestamp = System.currentTimeMillis()
        )
    }
}

@Entity(tableName = "token_receipts")
data class TokenReceiptEntity(
    @PrimaryKey
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
    val timestamp: Long,
    val isLoaded: Boolean
) {
    fun toReceipt() = PurchaseReceipt(
        id = id,
        meterNumber = meterNumber,
        ownerName = ownerName,
        formattedToken = formattedToken,
        rawToken = rawToken,
        amountPaid = amountPaid,
        fee = fee,
        totalCharged = totalCharged,
        unitsKwh = unitsKwh,
        paymentMethod = paymentMethod,
        payerPhone = payerPhone,
        referenceId = referenceId,
        timestamp = timestamp,
        isLoaded = isLoaded
    )

    companion object {
        fun fromReceipt(receipt: PurchaseReceipt) = TokenReceiptEntity(
            id = receipt.id,
            meterNumber = receipt.meterNumber,
            ownerName = receipt.ownerName,
            formattedToken = receipt.formattedToken,
            rawToken = receipt.rawToken,
            amountPaid = receipt.amountPaid,
            fee = receipt.fee,
            totalCharged = receipt.totalCharged,
            unitsKwh = receipt.unitsKwh,
            paymentMethod = receipt.paymentMethod,
            payerPhone = receipt.payerPhone,
            referenceId = receipt.referenceId,
            timestamp = receipt.timestamp,
            isLoaded = receipt.isLoaded
        )
    }
}
