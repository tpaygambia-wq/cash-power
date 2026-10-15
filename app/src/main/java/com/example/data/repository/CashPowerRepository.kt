package com.example.data.repository

import com.example.data.local.CashPowerDatabase
import com.example.data.local.SavedMeterEntity
import com.example.data.local.TokenReceiptEntity
import com.example.data.model.MeterInfo
import com.example.data.model.PurchaseReceipt
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlin.random.Random

class CashPowerRepository(private val database: CashPowerDatabase) {

    private val savedMeterDao = database.savedMeterDao()
    private val tokenReceiptDao = database.tokenReceiptDao()

    // Pre-registered authentic Gambian NAWEC customer meters for instant demo & lookup
    private val knownMeters = mapOf(
        "04219985523" to MeterInfo(
            meterNumber = "04219985523",
            ownerName = "Fatoumatta Ceesay",
            balanceKwh = "14.2 kWh (Low Balance)",
            tariffCategory = "Domestic D1 (Residential)",
            area = "Brusubi Phase 2, Kombo North",
            connectionType = "STS Single-Phase Prepaid",
            nickname = "Home"
        ),
        "07114429018" to MeterInfo(
            meterNumber = "07114429018",
            ownerName = "Alhaji Modou Gaye",
            balanceKwh = "88.5 kWh (Active)",
            tariffCategory = "Commercial C1",
            area = "Kairaba Avenue, Pipeline",
            connectionType = "STS Three-Phase Commercial",
            nickname = "Bakery & Shop"
        ),
        "02883491204" to MeterInfo(
            meterNumber = "02883491204",
            ownerName = "Pa Ousman Jallow",
            balanceKwh = "3.4 kWh (Critical Low)",
            tariffCategory = "Domestic D1 (Residential)",
            area = "Nyambai, Brikama",
            connectionType = "STS Single-Phase Prepaid",
            nickname = "Family Compound"
        ),
        "05501839211" to MeterInfo(
            meterNumber = "05501839211",
            ownerName = "Awa Sarr",
            balanceKwh = "45.0 kWh (Active)",
            tariffCategory = "Domestic D1 (Residential)",
            area = "Cape Point, Bakau",
            connectionType = "STS Single-Phase Prepaid",
            nickname = "Beach Apartment"
        ),
        "03192084712" to MeterInfo(
            meterNumber = "03192084712",
            ownerName = "Ebrima Camara",
            balanceKwh = "120.4 kWh (Active)",
            tariffCategory = "Domestic D1 (Residential)",
            area = "Senegambia Highway, Kololi",
            connectionType = "STS Single-Phase Prepaid",
            nickname = "Townhouse"
        )
    )

    private val gambianNames = listOf(
        "Lamin Sanneh", "Mariama Jammeh", "Bakary Touray", "Isatou Badjie",
        "Alieu Bah", "Sulayman Drammeh", "Kaddy Fatty", "Momodou Njie",
        "Haddy Nyan", "Yusupha Bojang", "Fatou Jobe", "Saikou Marong"
    )

    private val gambianAreas = listOf(
        "Brusubi Tranquil, West Coast", "Westfield, Serrekunda", "Fajara Waterfront",
        "Sukuta Nema, Kombo North", "Bakoteh Tipper Garage", "Banjul City Central",
        "Bundung Six Junction", "Kotu Power Station Area", "Lamin Village"
    )

    suspend fun verifyMeter(meterNumber: String): Result<MeterInfo> {
        val cleanNumber = meterNumber.replace("\\s|-".toRegex(), "")
        if (cleanNumber.length != 11 || !cleanNumber.all { it.isDigit() }) {
            return Result.failure(IllegalArgumentException("Please enter a valid 11-digit NAWEC meter number."))
        }

        // Simulate network round-trip to NAWEC STS database
        delay(700)

        // Check if cached in saved meters first
        val saved = savedMeterDao.getMeter(cleanNumber)
        if (saved != null) {
            return Result.success(saved.toMeterInfo())
        }

        // Check known demo meters
        val known = knownMeters[cleanNumber]
        if (known != null) {
            return Result.success(known)
        }

        // Deterministically generate a realistic Gambian NAWEC record for any other valid meter
        val hash = cleanNumber.hashCode().let { if (it < 0) -it else it }
        val name = gambianNames[hash % gambianNames.size]
        val area = gambianAreas[(hash / 3) % gambianAreas.size]
        val randomKwh = 5.0 + (hash % 850) / 10.0
        val balanceStr = String.format(Locale.US, "%.1f kWh", randomKwh)

        val generated = MeterInfo(
            meterNumber = cleanNumber,
            ownerName = name,
            balanceKwh = balanceStr,
            tariffCategory = if (hash % 5 == 0) "Commercial C1" else "Domestic D1 (Residential)",
            area = area,
            connectionType = "STS Single-Phase Prepaid",
            nickname = null
        )
        return Result.success(generated)
    }

    fun calculateFee(amount: Int): Int {
        if (amount <= 0) return 0
        return if (amount < 1000) 15 else 20
    }

    fun calculateEstimatedUnits(amount: Int): Double {
        if (amount <= 0) return 0.0
        // NAWEC Standard Domestic D1 block average tariff is approximately 10.14 GMD per kWh
        val tariffPerKwh = 10.14
        val units = amount / tariffPerKwh
        return (Math.round(units * 10.0) / 10.0)
    }

    suspend fun processPurchase(
        meterNumber: String,
        ownerName: String,
        amount: Int,
        paymentMethod: String,
        phone: String
    ): Result<PurchaseReceipt> {
        val cleanNumber = meterNumber.replace("\\s|-".toRegex(), "")
        if (cleanNumber.isEmpty()) {
            return Result.failure(IllegalStateException("No verified meter selected."))
        }
        if (amount < 500) {
            return Result.failure(IllegalArgumentException("Minimum purchase amount is 500 GMD."))
        }

        // Simulating the 3-step STS token issuing transaction
        delay(1200)

        val fee = calculateFee(amount)
        val total = amount + fee
        val units = calculateEstimatedUnits(amount)

        // Generate 20-digit STS token: 5 blocks of 4 digits
        val rawDigits = StringBuilder()
        for (i in 0 until 20) {
            rawDigits.append(Random.nextInt(0, 10))
        }
        val rawToken = rawDigits.toString()
        val formattedToken = rawToken.chunked(4).joinToString(" - ")

        val datePrefix = SimpleDateFormat("yyyyMMdd", Locale.US).format(Date())
        val randomRef = Random.nextInt(10000, 99999)
        val referenceId = "NAWEC-GM-$datePrefix-$randomRef"

        val receipt = PurchaseReceipt(
            id = UUID.randomUUID().toString(),
            meterNumber = cleanNumber,
            ownerName = ownerName,
            formattedToken = formattedToken,
            rawToken = rawToken,
            amountPaid = amount,
            fee = fee,
            totalCharged = total,
            unitsKwh = units,
            paymentMethod = paymentMethod,
            payerPhone = phone.ifBlank { "+220 7000000" },
            referenceId = referenceId,
            timestamp = System.currentTimeMillis(),
            isLoaded = false
        )

        // Save receipt to database
        tokenReceiptDao.insertReceipt(TokenReceiptEntity.fromReceipt(receipt))

        // Update meter last used
        val existingSaved = savedMeterDao.getMeter(cleanNumber)
        if (existingSaved != null) {
            savedMeterDao.insertOrUpdateMeter(existingSaved.copy(lastUsedTimestamp = System.currentTimeMillis()))
        }

        return Result.success(receipt)
    }

    fun getAllSavedMeters(): Flow<List<MeterInfo>> {
        return savedMeterDao.getAllSavedMeters().map { list ->
            list.map { it.toMeterInfo() }
        }
    }

    suspend fun saveMeter(meter: MeterInfo, customNickname: String) {
        val entity = SavedMeterEntity.fromMeterInfo(meter, customNickname)
        savedMeterDao.insertOrUpdateMeter(entity)
    }

    suspend fun deleteMeter(meterNumber: String) {
        savedMeterDao.deleteByMeterNumber(meterNumber)
    }

    fun getAllReceipts(): Flow<List<PurchaseReceipt>> {
        return tokenReceiptDao.getAllReceipts().map { list ->
            list.map { it.toReceipt() }
        }
    }

    suspend fun toggleLoadedStatus(receiptId: String, isLoaded: Boolean) {
        tokenReceiptDao.updateLoadedStatus(receiptId, isLoaded)
    }

    suspend fun deleteReceipt(receiptId: String) {
        tokenReceiptDao.deleteReceipt(receiptId)
    }
}
