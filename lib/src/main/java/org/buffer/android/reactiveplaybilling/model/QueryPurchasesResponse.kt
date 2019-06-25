package org.buffer.android.reactiveplaybilling.model

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.Purchase

sealed class QueryPurchasesResponse(@BillingClient.BillingResponse val responseCode: Int) {

    class Success(@BillingClient.BillingResponse val billingResponse: Int,
                  val purchases: List<Purchase>)
        : QueryPurchasesResponse(billingResponse)

    class Failure(@BillingClient.BillingResponse billingResponse: Int)
        : QueryPurchasesResponse(billingResponse)

}