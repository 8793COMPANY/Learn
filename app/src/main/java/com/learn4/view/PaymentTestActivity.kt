package com.learn4.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.*
import com.android.billingclient.api.PurchasesUpdatedListener
import com.google.android.gms.ads.*
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.common.collect.ImmutableList
import com.learn4.R
import com.learn4.databinding.ActivityPaymentTestBinding
import com.learn4.view.onboarding.IntroActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date


class PaymentTestActivity : AppCompatActivity(), PurchasesUpdatedListener {

    private lateinit var binding: ActivityPaymentTestBinding

    lateinit var billingClient : BillingClient
    private var skuDetailsList: List<SkuDetails> = mutableListOf()
    private var productDetailsList: List<ProductDetails> = mutableListOf()
    private var productDetailsList2: List<Purchase> = mutableListOf()
    private lateinit var consumeListenser : ConsumeResponseListener
    var check = false
    var purchaseCheck = "on"

    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentTestBinding.inflate(layoutInflater)
        val view = binding.root
        //setContentView(R.layout.activity_payment_test)
        setContentView(view)

        MobileAds.initialize(this, OnInitializationCompleteListener {  })
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        setupInterstitialAd()

        binding.adFullBtn.setOnClickListener {
            if (mInterstitialAd != null) {
                mInterstitialAd?.show(this)
                mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdClicked() {
                        Log.d("DEBUG: ", "Ad was clicked.")
                    }

                    override fun onAdDismissedFullScreenContent() {
                        Log.d("DEBUG: ", "Ad dismissed fullscreen content.")
                        mInterstitialAd = null
                        setupInterstitialAd()
                    }

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                        Log.e("DEBUG: ", "Ad failed to show fullscreen content.")
                        mInterstitialAd = null
                    }

                    override fun onAdImpression() {
                        Log.d("DEBUG: ", "Ad recorded an impression.")
                    }

                    override fun onAdShowedFullScreenContent() {
                        Log.d("DEBUG: ", "Ad showed fullscreen content.")
                    }
                }
            } else {
                Log.e("testtest", "ok")
            }
        }

        binding.adDeleteBtn.setOnClickListener {
            if (binding.adDeleteBtn.text.equals("광고제거")) {
                binding.adDeleteBtn.text = "광고표시"
                binding.adView.visibility = GONE
            } else if (binding.adDeleteBtn.text.equals("광고표시")) {
                binding.adDeleteBtn.text = "광고제거"
                binding.adView.visibility = VISIBLE
            }
        }

        binding.adCheckBtn.setOnClickListener {
            if (purchaseCheck == "off") {
                binding.adView.visibility = GONE
                Log.e("testtest", "add state off")
            } else if (purchaseCheck == "on") {
                binding.adView.visibility = VISIBLE
                Log.e("testtest", "add state on")
            }
        }

        var button = findViewById<Button>(R.id.restart)

        billingClient = BillingClient.newBuilder(applicationContext)
            .setListener(PurchasesUpdatedListener { billingResult, purchases ->
                //모든 구매 관련 업데이트를 수신한다.

                Log.e("testtest", purchases?.get(0)?.isAutoRenewing.toString())

                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.e("testtest", "소모 성공")
                    purchaseCheck = "off"
                    binding.adView.visibility = GONE
                    binding.adDeleteBtn.text = "광고표시"
                } else if (billingResult.responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
                    Log.e("testtest", "이미 구매")
                } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                    Log.e("testtest", "구독 해지")
                } else {
                    Log.e("testtest", "소모 실패")
                    binding.adView.visibility = VISIBLE
                    purchaseCheck = "on"
                    binding.adDeleteBtn.text = "광고제거"
                }

                Log.e("testtest", billingResult.toString())
                Log.e("testtest", purchases.toString())
                if (purchases != null) {
                    Log.e("testtest", purchases[0].purchaseTime.toString())

                    val sdf = SimpleDateFormat("yyyy-MM-dd-HH:mm:ss")

                    val now = Date()
                    now.time = purchases[0].purchaseTime

                    Log.e("testtest", "purchase time : " + sdf.format(now))

                    Log.e("testtest", purchases[0].originalJson)
                    //purchases[0].
                }
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
                    Log.e("testtest", "ready")

                    binding.testText.text = "준비완료"

                    check = true
                    CoroutineScope(Dispatchers.Main).launch {
                        Log.e("in","main")

                        binding.testText.text = "main"

                        querySkuDetails()
//                        queryPurchases()
//                        test()
                    }

                }
            }
        })

        consumeListenser = ConsumeResponseListener{ billingResult, purchaseToken ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                Log.e("testtest", "소모 성공")
            } else {
                Log.e("testtest", "소모 실패")
            }
        }

        binding.adCancelBtn.setOnClickListener {
            val nextIntent = Intent(this, IntroActivity::class.java)
            startActivity(nextIntent)
        }

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

            binding.testText.text = responseCode.toString()
            // 0 나오면

            Log.e("testtest", "button click")

//            CoroutineScope(Dispatchers.Main).launch {
//                Log.e("in","main")
//
//                querySkuDetails()
//            }
        }
    }

    private fun setupInterstitialAd() {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this,
            //ca-app-pub-3940256099942544/6300978111 >> 배너
            //ca-app-pub-3940256099942544/8691691433 >> 동영상(전면)
            //
            // >> 이미지(전면)
            "ca-app-pub-3940256099942544/8691691433",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d("DEBUG: ", adError.message)
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d("DEBUG: ", "Ad was loaded.")
                    mInterstitialAd = interstitialAd
                }
            })
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

            //Toast.makeText(applicationContext, productDetailsList.size.toString(), Toast.LENGTH_SHORT).show()
            //binding.testText.text = billingResult.toString()
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

        Log.e("testtest", "query sku detail")

        binding.testText.text = "baeulrae_test1"



        billingClient.queryProductDetailsAsync(tempParam) { billingResult, mutableList ->
            Log.e("payment queryProductDetailsAsync in",billingResult.toString())
            Log.e("payment queryProductDetailsAsync size", mutableList.size.toString())
            productDetailsList = mutableList
            mutableList.forEach {
                Log.e("payment queryProductDetailsAsync it", it.toString())

                //binding.testText.text = binding.testText.text as String + it.toString()
            }

            Log.e("testtest", "billing check")

            queryPurchases()
        }


    }

    fun queryPurchases() {
        Log.e("testtest", "queryPurchases")
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
                    Log.e("testtest", "list1")
                    Log.e("testtest", productDetailsList2.toString())
                    Log.e("testtest", "purchaseList : $purchaseList")

                    if (purchaseList.isNotEmpty()) {
                        purchaseList.forEach {
                            if (it.products.contains("baeulrae_test1")) {
                                Log.e("testtest", "add off")
                                purchaseCheck = "off"
                                binding.adView.visibility = GONE
                            } else {
                                Log.e("testtest", "add on")
                                binding.adView.visibility = VISIBLE
                                purchaseCheck = "on"
                            }
                        }
                    } else {
                        Log.e("testtest", "list empty")
                    }
                } else {
                    productDetailsList2 = emptyList()
                    Log.e("testtest", "list2")
                }

            } else {
                Log.e("check queryPurchases error", billingResult.debugMessage)
            }

            onPurchasesUpdated(billingResult, purchaseList)
        }
    }

    override fun onPurchasesUpdated(p0: BillingResult, p1: MutableList<Purchase>?) {
        Log.e("testtest", "start")
        if (p0.responseCode == BillingClient.BillingResponseCode.OK && p1 != null) {
            for (purchase in p1) {
                Log.e("결과","구매 성공?")

                binding.testText.text = "성공"

                // 거래 성공 코드
                // ?
                val consumeParams = ConsumeParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()


                billingClient.consumeAsync(consumeParams, consumeListenser)

                Log.e("testtest", "good")

            }
        } else if (p0.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            Log.e("결과","실패")
            // 유저 취소 errorcode

            binding.testText.text = "실패"


            Log.e("testtest", "nope")
        }
    }

}