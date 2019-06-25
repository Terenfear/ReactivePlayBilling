package org.buffer.android.reactiveplaybilling.model

import com.android.billingclient.api.BillingClient

sealed class SubscriptionResponse(@BillingClient.BillingResponse val responseCode: Int) {

    class Success(@BillingClient.BillingResponse billingResponse: Int)
        : SubscriptionResponse(billingResponse)

    class Failure(@BillingClient.BillingResponse billingResponse: Int)
        : SubscriptionResponse(billingResponse)

}