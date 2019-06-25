package org.buffer.android.reactiveplaybilling.model

import com.android.billingclient.api.BillingClient

sealed class ConnectionResult {

    object Success : ConnectionResult()

    data class Failure(@BillingClient.BillingResponse val responseCode: Int)
        : ConnectionResult()

    object Disconnected : ConnectionResult()

}