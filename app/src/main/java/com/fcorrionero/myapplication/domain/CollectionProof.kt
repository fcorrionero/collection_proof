package com.fcorrionero.myapplication.domain

data class ClientData(
    val name: String?,
    val phone: String?,
    val address: String?,
    val dni: String?
) {

}

class CollectionProof(
    val clientData: ClientData?
){
}