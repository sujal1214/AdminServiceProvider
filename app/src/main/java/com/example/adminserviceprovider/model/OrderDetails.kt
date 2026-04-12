package com.example.adminserviceprovider.model
import java.io.Serializable

data class OrderDetails(
    var userUid: String? = null,
    var userName: String? = null,
    var address: String? = null,
    var totalPrice: String? = null,
    var phoneNumber: String? = null,
    var orderAccepted: Boolean = false,
    var paymentReceived: Boolean = false,
    var itemPushKey: String? = null,
    var currentTime: Long = 0,
    var serviceNames: MutableList<String>? = null,
    var serviceImages: MutableList<String>? = null,
    var servicePrices: MutableList<String>? = null,
    var serviceQuantities: MutableList<Int>? = null
) : Serializable
