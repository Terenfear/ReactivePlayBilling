package org.buffer.android.reactiveplaybilling

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import org.buffer.android.reactiveplaybilling.model.*

open class ReactivePlayBilling constructor(context: Context) : PurchasesUpdatedListener {

    private val publishSubject = PublishSubject.create<PurchasesUpdatedResponse>()
    private var billingClient: BillingClient =
            BillingClient.newBuilder(context).setListener(this).build()

    override fun onPurchasesUpdated(responseCode: Int, purchases: MutableList<Purchase>?) {
        if (responseCode == BillingClient.BillingResponse.OK) {
            publishSubject.onNext(PurchasesUpdatedResponse.Success(responseCode,
                    purchases))
        } else {
            publishSubject.onNext(PurchasesUpdatedResponse.Failure(responseCode))
        }
    }

    open fun connect(): Observable<ConnectionResult> {
        return Observable.create {
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(@BillingClient.BillingResponse
                                                    responseCode: Int) {
                    if (responseCode == BillingClient.BillingResponse.OK) {
                        it.onNext(ConnectionResult.Success)
                    } else {
                        it.onNext(ConnectionResult.Failure(responseCode))
                    }
                }

                override fun onBillingServiceDisconnected() {
                    it.onNext(ConnectionResult.Disconnected)
                }
            })
        }
    }

    open fun disconnect(): Completable {
        return Completable.defer {
            billingClient.endConnection()
            Completable.complete()
        }
    }

    open fun observePurchaseUpdates(): Observable<PurchasesUpdatedResponse> {
        return publishSubject
    }

    open fun queryItemsForPurchase(skuList: List<String>): Single<ItemsForPurchaseResponse> {
        return Single.create {
            val flowParams = SkuDetailsParams.newBuilder()
                    .setSkusList(skuList)
                    .setType(BillingClient.SkuType.INAPP)
                    .build()
            billingClient.querySkuDetailsAsync(flowParams) { responseCode, detailsList ->
                if (responseCode == BillingClient.BillingResponse.OK) {
                    it.onSuccess(ItemsForPurchaseResponse.Success(responseCode, detailsList))
                } else {
                    it.onSuccess(ItemsForPurchaseResponse.Failure(responseCode))
                }
            }
        }
    }

    fun querySubscriptionsForPurchase(skuList: List<String>):
            Single<ItemsForSubscriptionResponse> {
        return Single.create {
            val flowParams = SkuDetailsParams.newBuilder()
                    .setSkusList(skuList)
                    .setType(BillingClient.SkuType.SUBS)
                    .build()
            billingClient.querySkuDetailsAsync(flowParams) { responseCode, detailsList ->
                if (responseCode == BillingClient.BillingResponse.OK) {
                    it.onSuccess(ItemsForSubscriptionResponse.Success(responseCode,
                            detailsList))
                } else {
                    it.onSuccess(ItemsForSubscriptionResponse.Failure(
                            responseCode))
                }
            }
        }
    }

    open fun purchaseItem(skuDetails: SkuDetails, activity: Activity): Single<PurchaseResponse> {
        return Single.create {
            val flowParams = BillingFlowParams.newBuilder()
                    .setSkuDetails(skuDetails)
                    .build()
            val responseCode = billingClient.launchBillingFlow(activity, flowParams)
            if (responseCode == BillingClient.BillingResponse.OK) {
                it.onSuccess(PurchaseResponse.Success(responseCode))
            } else {
                it.onSuccess(PurchaseResponse.Failure(responseCode))
            }
        }
    }

    open fun queryPurchases(): Single<List<Purchase>> {
        return Single.create {
            val queryResult = billingClient.queryPurchases(BillingClient.SkuType.INAPP)

            if (queryResult.responseCode == BillingClient.BillingResponse.OK) {
                it.onSuccess(queryResult.purchasesList)
            } else {
                it.onError(Throwable("Failed to query purchases. Response code: ${queryResult.responseCode}"))
            }
        }
    }

    open fun queryPurchaseHistory(): Single<QueryPurchasesResponse> {
        return Single.create {
            billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.INAPP) { responseCode, detailsList ->
                if (responseCode == BillingClient.BillingResponse.OK && detailsList != null) {
                    it.onSuccess(QueryPurchasesResponse.Success(responseCode, detailsList))
                } else {
                    it.onSuccess(QueryPurchasesResponse.Failure(responseCode))
                }
            }
        }
    }

    open fun querySubscriptions(): Single<List<Purchase>> {
        return Single.create {
            val queryResult = billingClient.queryPurchases(BillingClient.SkuType.SUBS)

            if (queryResult.responseCode == BillingClient.BillingResponse.OK) {
                it.onSuccess(queryResult.purchasesList)
            } else {
                it.onError(Throwable("Failed to query subscriptions. Response code: ${queryResult.responseCode}"))
            }
        }
    }

    open fun querySubscriptionHistory(): Single<QuerySubscriptionsResponse> {
        return Single.create {
            billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.SUBS)
            { responseCode, detailsList ->
                if (responseCode == BillingClient.BillingResponse.OK && detailsList != null) {
                    it.onSuccess(QuerySubscriptionsResponse.Success(responseCode,
                            detailsList))
                } else {
                    it.onSuccess(QuerySubscriptionsResponse.Failure(responseCode))
                }

            }
        }
    }

    open fun consumeItem(purchaseToken: String): Single<ConsumptionResponse> {
        return Single.create {
            billingClient.consumeAsync(purchaseToken) { responseCode, outToken ->
                if (responseCode == BillingClient.BillingResponse.OK) {
                    it.onSuccess(ConsumptionResponse.Success(responseCode, outToken))
                } else {
                    it.onSuccess(ConsumptionResponse.Failure(responseCode))
                }
            }
        }
    }

    open fun purchaseSubscription(skuDetails: SkuDetails, activity: Activity):
            Single<SubscriptionResponse> {
        return Single.create {
            val flowParams = BillingFlowParams.newBuilder()
                    .setSkuDetails(skuDetails)
                    .build()
            val responseCode = billingClient.launchBillingFlow(activity, flowParams)
            if (responseCode == BillingClient.BillingResponse.OK) {
                it.onSuccess(SubscriptionResponse.Success(responseCode))
            } else {
                it.onSuccess(SubscriptionResponse.Failure(responseCode))
            }
        }
    }

    open fun upgradeSubscription(oldSkuId: String, newSkuDetails: SkuDetails, @BillingFlowParams.ProrationMode proration: Int, activity: Activity):
            Single<SubscriptionResponse> {
        return Single.create {
            val flowParams = BillingFlowParams.newBuilder()
                    .setSkuDetails(newSkuDetails)
                    .setOldSku(oldSkuId)
                    .setReplaceSkusProrationMode(proration)
                    .build()
            val responseCode = billingClient.launchBillingFlow(activity, flowParams)
            if (responseCode == BillingClient.BillingResponse.OK) {
                it.onSuccess(SubscriptionResponse.Success(responseCode))
            } else {
                it.onSuccess(SubscriptionResponse.Failure(responseCode))
            }
        }
    }

}