package org.buffer.android.reactiveplaybilling.model

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.Purchase

sealed class PurchasesUpdatedResponse(@BillingClient.BillingResponse val responseCode: Int) {

    class Success(@BillingClient.BillingResponse billingResponse: Int,
                  val purchases: List<Purchase>?)
        : PurchasesUpdatedResponse(billingResponse)

    class Failure(@BillingClient.BillingResponse billingResponse: Int)
        : PurchasesUpdatedResponse(billingResponse)

}