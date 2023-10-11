package com.learn4.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
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
                    }

                }
            }
        })

        button.setOnClickListener {
            var flowProductDetailParams = BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productDetailsList[0])
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

    private fun querySkuDetails() {
        Log.e("in","querySkuDetails")
        val tempParam = QueryProductDetailsParams.newBuilder()
            .setProductList(
                ImmutableList.of(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId("test")
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build()
                )
            ).build()



        billingClient.queryProductDetailsAsync(tempParam) { billingResult, mutableList ->
            Log.e("queryProductDetailsAsync in",billingResult.toString())
            productDetailsList = mutableList
            mutableList.forEach {
                Log.e("queryProductDetailsAsync it", it.toString())
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