package org.buffer.android.reactiveplaybilling.model

import com.android.billingclient.api.BillingClient

sealed class ConsumptionResponse(@BillingClient.BillingResponse val responseCode: Int,
                                 val outToken: String?) {

    data class ConsumptionSuccess(@BillingClient.BillingResponse private val billingResponse: Int,
                                  private val outputToken: String)
        : ConsumptionResponse(billingResponse, outToken = outputToken)

    data class ConsumptionFailure(@BillingClient.BillingResponse
                                  private val billingResponse: Int)
        : ConsumptionResponse(billingResponse, outToken = null)

}