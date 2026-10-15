package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.CashPowerDatabase
import com.example.data.model.MeterInfo
import com.example.data.model.PurchaseReceipt
import com.example.data.repository.CashPowerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AppTab {
    BUY,
    SAVED_METERS,
    HISTORY,
    GUIDE
}

data class CashPowerUiState(
    val currentTab: AppTab = AppTab.BUY,
    val meterInput: String = "04219985523",
    val verifiedMeter: MeterInfo? = null,
    val isVerifying: Boolean = false,
    val amountInput: String = "1000",
    val selectedProvider: String = "Wave",
    val phoneNumber: String = "7123456",
    val calculatedFee: Int = 20,
    val totalCost: Int = 1020,
    val estimatedUnits: Double = 98.6,
    val isPurchasing: Boolean = false,
    val purchaseProgressText: String = "",
    val activeReceipt: PurchaseReceipt? = null,
    val savedMeters: List<MeterInfo> = emptyList(),
    val purchaseHistory: List<PurchaseReceipt> = emptyList(),
    val snackbarMessage: String? = null,
    val showSaveMeterDialog: Boolean = false,
    val quickDemoMeters: List<String> = listOf("04219985523", "07114429018", "02883491204", "05501839211")
)

class CashPowerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CashPowerRepository(CashPowerDatabase.getDatabase(application))

    private val _uiState = MutableStateFlow(CashPowerUiState())
    val uiState: StateFlow<CashPowerUiState> = _uiState.asStateFlow()

    init {
        // Collect saved meters
        viewModelScope.launch {
            repository.getAllSavedMeters().collectLatest { meters ->
                _uiState.update { it.copy(savedMeters = meters) }
            }
        }

        // Collect receipts history
        viewModelScope.launch {
            repository.getAllReceipts().collectLatest { receipts ->
                _uiState.update { it.copy(purchaseHistory = receipts) }
            }
        }

        // Pre-verify initial meter for effortless first-launch experience
        verifyCurrentMeter()
        updateCalculations("1000")
    }

    fun setTab(tab: AppTab) {
        _uiState.update { it.copy(currentTab = tab) }
    }

    fun onMeterInputChanged(newInput: String) {
        val filtered = newInput.filter { it.isDigit() }.take(11)
        _uiState.update { it.copy(meterInput = filtered) }
    }

    fun onAmountInputChanged(newAmount: String) {
        val filtered = newAmount.filter { it.isDigit() }.take(6)
        _uiState.update { it.copy(amountInput = filtered) }
        updateCalculations(filtered)
    }

    fun selectPresetAmount(amount: Int) {
        val str = amount.toString()
        _uiState.update { it.copy(amountInput = str) }
        updateCalculations(str)
    }

    private fun updateCalculations(amountText: String) {
        val amount = amountText.toIntOrNull() ?: 0
        val fee = repository.calculateFee(amount)
        val units = repository.calculateEstimatedUnits(amount)
        _uiState.update {
            it.copy(
                calculatedFee = fee,
                totalCost = if (amount > 0) amount + fee else 0,
                estimatedUnits = units
            )
        }
    }

    fun selectProvider(provider: String) {
        _uiState.update { it.copy(selectedProvider = provider) }
    }

    fun onPhoneInputChanged(newPhone: String) {
        val filtered = newPhone.filter { it.isDigit() }.take(8)
        _uiState.update { it.copy(phoneNumber = filtered) }
    }

    fun verifyCurrentMeter() {
        val meter = _uiState.value.meterInput.trim()
        if (meter.length != 11 || !meter.all { it.isDigit() }) {
            showSnackbar("Please enter a valid 11-digit NAWEC meter number.")
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isVerifying = true) }
            val result = repository.verifyMeter(meter)
            _uiState.update { it.copy(isVerifying = false) }

            result.onSuccess { info ->
                _uiState.update { it.copy(verifiedMeter = info) }
            }.onFailure { err ->
                showSnackbar(err.message ?: "Failed to verify meter.")
            }
        }
    }

    fun selectSavedMeter(meter: MeterInfo) {
        _uiState.update {
            it.copy(
                meterInput = meter.meterNumber,
                verifiedMeter = meter,
                currentTab = AppTab.BUY
            )
        }
        showSnackbar("Selected meter: ${meter.meterNumber} (${meter.ownerName})")
    }

    fun clearActiveMeter() {
        _uiState.update {
            it.copy(
                meterInput = "",
                verifiedMeter = null
            )
        }
    }

    fun processPurchase() {
        val state = _uiState.value
        val verified = state.verifiedMeter

        if (verified == null) {
            showSnackbar("Please verify a NAWEC meter first.")
            return
        }

        val amount = state.amountInput.toIntOrNull()
        if (amount == null || amount < 500) {
            showSnackbar("Minimum purchase amount is 500 GMD.")
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isPurchasing = true,
                    purchaseProgressText = "Connecting to ${state.selectedProvider}..."
                )
            }

            kotlinx.coroutines.delay(600)
            _uiState.update {
                it.copy(purchaseProgressText = "Awaiting mobile payment authorization on +220 ${state.phoneNumber}...")
            }

            kotlinx.coroutines.delay(800)
            _uiState.update {
                it.copy(purchaseProgressText = "Contacting NAWEC STS Key System...")
            }

            val result = repository.processPurchase(
                meterNumber = verified.meterNumber,
                ownerName = verified.ownerName,
                amount = amount,
                paymentMethod = state.selectedProvider,
                phone = "+220 ${state.phoneNumber}"
            )

            _uiState.update { it.copy(isPurchasing = false, purchaseProgressText = "") }

            result.onSuccess { receipt ->
                _uiState.update { it.copy(activeReceipt = receipt) }
            }.onFailure { err ->
                showSnackbar(err.message ?: "Transaction failed.")
            }
        }
    }

    fun dismissReceiptDialog() {
        _uiState.update { it.copy(activeReceipt = null) }
    }

    fun openSaveMeterDialog() {
        _uiState.update { it.copy(showSaveMeterDialog = true) }
    }

    fun dismissSaveMeterDialog() {
        _uiState.update { it.copy(showSaveMeterDialog = false) }
    }

    fun saveCurrentMeter(customNickname: String) {
        val meter = _uiState.value.verifiedMeter ?: return
        viewModelScope.launch {
            repository.saveMeter(meter, customNickname.ifBlank { "Home Meter" })
            _uiState.update { it.copy(showSaveMeterDialog = false) }
            showSnackbar("Meter saved to favorites!")
        }
    }

    fun deleteSavedMeter(meterNumber: String) {
        viewModelScope.launch {
            repository.deleteMeter(meterNumber)
            showSnackbar("Meter removed from saved list.")
        }
    }

    fun toggleLoadedStatus(receiptId: String, currentStatus: Boolean) {
        viewModelScope.launch {
            repository.toggleLoadedStatus(receiptId, !currentStatus)
        }
    }

    fun deleteReceipt(receiptId: String) {
        viewModelScope.launch {
            repository.deleteReceipt(receiptId)
            showSnackbar("Receipt deleted from history.")
        }
    }

    fun showSnackbar(message: String) {
        _uiState.update { it.copy(snackbarMessage = message) }
    }

    fun clearSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }
}
