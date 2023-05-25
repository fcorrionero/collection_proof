package com.fcorrionero.myapplication.domain

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class ClientData(
    val name: String?,
    val phone: String?,
    val address: String?,
    val dni: String?
)

data class DeviceData(
    val deviceAccessories: String?,
    val deviceBrandModel: String?,
    val deviceSerialImei: String?
)

data class IssueData(
    val issueType: String?,
    val issueSolution: String?,
    val issueObservations: String?
)

data class BudgetData(
    val budgetQuantity: String?,
    val budgetAcceptation: Boolean,
    val signature: Bitmap
)

class CollectionProof: ViewModel(){
    private var clientData = MutableLiveData<ClientData?>(null)
    private var deviceData = MutableLiveData<DeviceData?>(null)
    private var issueData = MutableLiveData<IssueData?>(null)
    private var budgetData = MutableLiveData<BudgetData?>(null)

    fun setClientData(clientData: ClientData) {
        this.clientData.value = clientData
    }

    fun setDeviceData(deviceData: DeviceData) {
        this.deviceData.value = deviceData
    }

    fun setIssueData(issueData: IssueData) {
        this.issueData.value = issueData
    }

    fun setBudgetData(budgetData: BudgetData) {
        this.budgetData.value = budgetData
    }

    fun getClientData(): ClientData? {
        return this.clientData.value
    }

    fun getDeviceData(): DeviceData? {
        return this.deviceData.value
    }

    fun getIssueData(): IssueData? {
        return this.issueData.value
    }

    fun getBudgetData(): BudgetData? {
        return this.budgetData.value
    }

}