package org.buffer.android.reactiveplaybilling.model

import com.android.billingclient.api.BillingClient

sealed class PurchaseResponse(@BillingClient.BillingResponse val responseCode: Int) {

    class Success(@BillingClient.BillingResponse billingResponse: Int)
        : PurchaseResponse(billingResponse)

    class Failure(@BillingClient.BillingResponse billingResponse: Int)
        : PurchaseResponse(billingResponse)

}