package com.learn4.view

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.*
import com.google.common.collect.ImmutableList
import com.learn4.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PaymentTestActivity : AppCompatActivity(), PurchasesUpdatedListener {

    lateinit var billingClient : BillingClient
    private var skuDetailsList: List<SkuDetails> = mutableListOf()
    private var productDetailsList: List<ProductDetails> = mutableListOf()
    private var productDetailsList2: List<Purchase> = mutableListOf()
    private lateinit var consumeListenser : ConsumeResponseListener
    var check = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_test)

        var button = findViewById<Button>(R.id.restart)

        billingClient = BillingClient.newBuilder(applicationContext)
            .setListener(PurchasesUpdatedListener { billingResult, purchases ->
                //모든 구매 관련 업데이트를 수신한다.
            })
            .enablePendingPurchases()
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                // 연결 실패 시 재시도 로직을 구현.
            }
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode ==  BillingClient.BillingResponseCode.OK) {
                    // 준비 완료가 되면 상품 쿼리를 처리 할 수 있다!
                    Log.e("준비완료","!!")
                    check = true
                    CoroutineScope(Dispatchers.Main).launch {
                        Log.e("in","main")

                        querySkuDetails()
//                        queryPurchases()
//                        test()
                    }

                }
            }
        })

        var result : BillingResult =  BillingResult.newBuilder().build()
        result.responseCode


        button.setOnClickListener {
            val offerToken = productDetailsList[0].subscriptionOfferDetails?.get(0)?.offerToken!!
            var flowProductDetailParams = BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productDetailsList[0])
                .setOfferToken(offerToken)
                .build()

            var flowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(listOf(flowProductDetailParams))
                .build()

            val responseCode = billingClient.launchBillingFlow(this, flowParams).responseCode

            Log.e("responseCode",responseCode.toString())

//            CoroutineScope(Dispatchers.Main).launch {
//                Log.e("in","main")
//
//                querySkuDetails()
//            }
        }
    }

    fun test(){
        val queryProductDetailsParams =
            QueryProductDetailsParams.newBuilder()
                .setProductList(
                    ImmutableList.of(
                        QueryProductDetailsParams.Product.newBuilder()
                            .setProductId("baeulrae_test1")
                            .setProductType(BillingClient.ProductType.SUBS)
                            .build()))
                .build()

        billingClient.queryProductDetailsAsync(queryProductDetailsParams) {
                billingResult,
                productDetailsList ->
            // check billingResult
            // process returned productDetailsList

            Log.e("payment queryProductDetailsAsync data", productDetailsList.size.toString())
        }

    }

    private fun querySkuDetails() {
        Log.e("in","querySkuDetails")

        val sku_contents_list: MutableList<QueryProductDetailsParams.Product> = ArrayList()
        sku_contents_list.add(QueryProductDetailsParams.Product.newBuilder()
            .setProductId(
                    "baeulrae_test1")
            .setProductType(BillingClient.ProductType.SUBS)
            .build())
        val tempParam = QueryProductDetailsParams.newBuilder()
            .setProductList(
                ImmutableList.of(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(
                            "baeulrae_test1")
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build()
            )).build()

        Log.e("payment queryProductDetailAsync product", "baeulrae_test1")



        billingClient.queryProductDetailsAsync(tempParam) { billingResult, mutableList ->
            Log.e("payment queryProductDetailsAsync in",billingResult.toString())
            Log.e("payment queryProductDetailsAsync size", mutableList.size.toString())
            productDetailsList = mutableList
            mutableList.forEach {
                Log.e("payment queryProductDetailsAsync it", it.toString())
            }
        }


    }

    fun queryPurchases() {
        if (!billingClient.isReady) {
            Log.e("check queryPurchases", "queryPurchases: BillingClient is not ready")
        }
        // Query for existing subscription products that have been purchased.
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.SUBS).build()
        ) { billingResult, purchaseList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                Log.e("payment check queryPurchases size", purchaseList.size.toString())
                if (!purchaseList.isNullOrEmpty()) {
                    productDetailsList2 = purchaseList
                } else {
                    productDetailsList2 = emptyList()
                }

            } else {
                Log.e("check queryPurchases error", billingResult.debugMessage)
            }
        }
    }

    override fun onPurchasesUpdated(p0: BillingResult, p1: MutableList<Purchase>?) {
        if (p0.responseCode == BillingClient.BillingResponseCode.OK && p1 != null) {
            for (purchase in p1) {
                Log.e("결과","구매 성공?")

                // 거래 성공 코드
                // ?
                val consumeParams = ConsumeParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()


                billingClient.consumeAsync(consumeParams, consumeListenser)

                

            }
        } else if (p0.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            Log.e("결과","실패")
            // 유저 취소 errorcode
        }
    }

}