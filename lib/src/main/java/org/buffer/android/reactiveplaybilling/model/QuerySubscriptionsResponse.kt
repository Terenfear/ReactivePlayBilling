package org.buffer.android.reactiveplaybilling.model

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.Purchase

sealed class QuerySubscriptionsResponse(@BillingClient.BillingResponse val responseCode: Int) {

    class Success(@BillingClient.BillingResponse billingResponse: Int,
                  val purchases: List<Purchase>)
        : QuerySubscriptionsResponse(billingResponse)

    class Failure(@BillingClient.BillingResponse billingResponse: Int)
        : QuerySubscriptionsResponse(billingResponse)

}