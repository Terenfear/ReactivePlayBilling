package org.buffer.android.reactiveplaybilling.model

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.SkuDetails

sealed class ItemsForPurchaseResponse(@BillingClient.BillingResponse val responseCode: Int) {

    class Success(@BillingClient.BillingResponse billingResponse: Int,
                  val purchaseDetails: List<SkuDetails>)
        : ItemsForPurchaseResponse(billingResponse)

    class Failure(@BillingClient.BillingResponse billingResponse: Int)
        : ItemsForPurchaseResponse(billingResponse)

}