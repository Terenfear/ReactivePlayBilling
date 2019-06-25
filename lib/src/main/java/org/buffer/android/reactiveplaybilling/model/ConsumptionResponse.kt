package org.buffer.android.reactiveplaybilling.model

import com.android.billingclient.api.BillingClient

sealed class ConsumptionResponse(@BillingClient.BillingResponse val responseCode: Int) {

    class Success(@BillingClient.BillingResponse billingResponse: Int,
                  val outputToken: String)
        : ConsumptionResponse(billingResponse)

    class Failure(@BillingClient.BillingResponse billingResponse: Int)
        : ConsumptionResponse(billingResponse)

}