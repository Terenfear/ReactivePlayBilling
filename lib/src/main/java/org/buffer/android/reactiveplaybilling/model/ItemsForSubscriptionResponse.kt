package org.buffer.android.reactiveplaybilling.model

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.SkuDetails

sealed class ItemsForSubscriptionResponse(@BillingClient.BillingResponse val responseCode: Int) {

    class Success(@BillingClient.BillingResponse billingResponse: Int,
                  val subDetails: List<SkuDetails>)
        : ItemsForSubscriptionResponse(billingResponse)

    class Failure(@BillingClient.BillingResponse billingResponse: Int)
        : ItemsForSubscriptionResponse(billingResponse)

}