package com.learn4.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryRecord;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.QueryPurchaseHistoryParams;
import com.android.billingclient.api.QueryPurchasesParams;
import com.google.common.collect.ImmutableList;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class PaymentCheck extends AppCompatActivity implements PurchasesUpdatedListener {

    private static PaymentCheck instance = null;
    Context context;

    public BillingClient billingClient;
    private List<ProductDetails> productDetailsList;

    PurchaseHistoryResponseListener purchaseHistoryResponseListener;
    AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener;

    public PaymentCheck(Context context) {
        this.context = context;
    }

    public static PaymentCheck getInstance(Context context) {
        if (instance == null) {
            instance = new PaymentCheck(context);
        }
        return instance;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initSetting();
    }

    public void initSetting() {
        billingClient = BillingClient.newBuilder(context)
                .enablePendingPurchases()
                .setListener(
                        (billingResult, list) -> {
                            // 구매 관련 업데이트 수신
                            onPurchasesUpdated(billingResult, list);
//                            if (list != null) {
//                                Log.e("testtest", list.get(0).isAutoRenewing()+"");
//
//                                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
//                                    for (Purchase purchase : list) {
////                                        verifySubPurchase(purchase);
//                                        Log.e("testtest", "for문 : " + purchase);
//                                    }
//                                    Log.e("testtest", "소모 성공");
//                                } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
//                                    Log.e("testtest", "이미 구매");
//                                } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
//                                    Log.e("testtest", "소모 실패");
//                                } else {
//                                    Log.e("testtest", "소모 실패 : " + billingResult.getResponseCode());
//                                }
//
//                                Log.e("testtest", billingResult.toString());
//                                Log.e("testtest", list.toString());
//
//                                Log.e("testtest", list.get(0).getPurchaseTime()+"");
//
//                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
//                                Date now = new Date();
//                                now.setTime(list.get(0).getPurchaseTime());
//
//                                Log.e("testtest", "purchase time : " + sdf.format(now));
//                                Log.e("testtest", list.get(0).getOriginalJson());
//                            }
                        }
                ).build();

        //start the connection after initializing the billing client
        establishConnection();

        purchaseHistoryResponseListener = new PurchaseHistoryResponseListener() {
            @Override
            public void onPurchaseHistoryResponse(@NonNull BillingResult billingResult, @Nullable List<PurchaseHistoryRecord> list) {
                Log.e("testtest", "onPurchaseHistoryResponse");
                Log.e("testtest", billingResult+"");
                Log.e("testtest", list+"");
            }
        };

        acknowledgePurchaseResponseListener = new AcknowledgePurchaseResponseListener() {
            @Override
            public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult) {
                Log.e("testtest", "onAcknowledgePurchaseResponse");
                Log.e("testtest", billingResult+"");
                Log.e("testtest", "구매 처리");
            }
        };
    }

    public void establishConnection() {
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {
                // 연결 실패 시 재시도 로직 구현
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                Log.e("testtest",  "Connection Not Established");
                //establishConnection();
            }

            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // 준비 완료 시 상품 쿼리 처리 가능
                    // The BillingClient is ready. You can query purchases here.
                    // Use any of function below to get details upon successful connection
                    Log.e("testtest",  "Connection Established");

                    GetListsSubDetail();
                }
            }
        });
    }

    public void GetListsSubDetail() {
        Log.e("testtest","GetListsSubDetail");

//        ArrayList<QueryProductDetailsParams.Product> productList = new ArrayList<>();
//        productList.add(QueryProductDetailsParams.Product.newBuilder()
//                .setProductId("baeulrae_test1")
//                .setProductType(BillingClient.ProductType.SUBS)
//                .build());

        QueryProductDetailsParams params = QueryProductDetailsParams.newBuilder()
                .setProductList(
                        ImmutableList.of(
                                QueryProductDetailsParams.Product.newBuilder()
                                        .setProductId("8793inc")
                                        .setProductType(BillingClient.ProductType.SUBS)
                                        .build()
                        )
                ).build();

        Log.e("payment queryProductDetailAsync product", "8793inc");

        billingClient.queryProductDetailsAsync(params, new ProductDetailsResponseListener() {
            @Override
            public void onProductDetailsResponse(@NonNull BillingResult billingResult, @NonNull List<ProductDetails> list) {
                Log.e("testtest", "payment queryProductDetailsAsync in " + billingResult);
                Log.e("testttes", "payment queryProductDetailsAsync size " + list.size());

                productDetailsList = list;

                Log.e("testtest", productDetailsList+"");
            }
        });
    }

    public void SubPurchase(Activity activity) {
        String offerToken = productDetailsList.get(0).getSubscriptionOfferDetails().get(0).getOfferToken();

        BillingFlowParams.ProductDetailsParams flowProductDetailParams =
                BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(productDetailsList.get(0))
                        .setOfferToken(offerToken)
                        .build();

        BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(Collections.singletonList(flowProductDetailParams))
                .build();

        int responseCode = billingClient.launchBillingFlow(activity, flowParams).getResponseCode();

        Log.e("testtest2", responseCode+"");
    }

    public void SubPurchaseCancel() {
        String url = "https://play.google.com/store/account/subscriptions";

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);

        //billingClient.queryPurchaseHistoryAsync(BillingClient.ProductType.SUBS, purchaseHistoryResponseListener);

        billingClient.queryPurchaseHistoryAsync(
                QueryPurchaseHistoryParams.newBuilder()
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build(), purchaseHistoryResponseListener
        );

//        billingClient.queryPurchaseHistoryAsync(
//                QueryPurchaseHistoryParams.newBuilder()
//                        .setProductType(BillingClient.ProductType.SUBS)
//                        .build(),
//                new PurchaseHistoryResponseListener() {
//                    @Override
//                    public void onPurchaseHistoryResponse(@NonNull BillingResult billingResult, @Nullable List<PurchaseHistoryRecord> list) {
//                        Log.e("testtest", "onPurchaseHistoryResponse");
//                        Log.e("testtest", billingResult+"");
//                        Log.e("testtest", list+"");
//                    }
//                }
//        );
//
//        billingClient.queryPurchasesAsync(
//                QueryPurchasesParams.newBuilder()
//                        .setProductType(BillingClient.ProductType.SUBS)
//                        .build(),
//                new PurchasesResponseListener() {
//                    @Override
//                    public void onQueryPurchasesResponse(@NonNull BillingResult billingResult, @NonNull List<Purchase> list) {
//                        Log.e("testtest", "onQueryPurchasesResponse");
//                        Log.e("testtest", billingResult+"");
//                        Log.e("testtest", list+"");
//                    }
//                }
//        );

//        billingClient.queryPurchasesAsync(
//                QueryPurchasesParams.newBuilder()
//                        .setProductType(BillingClient.ProductType.SUBS)
//                        .build(),
//                new PurchasesResponseListener() {
//                    @Override
//                    public void onQueryPurchasesResponse(@NonNull BillingResult billingResult, @NonNull List<Purchase> list) {
//                        Log.e("testtest", "onQueryPurchasesResponse");
//                        Log.e("testtest", billingResult+"");
//                        Log.e("testtest", list+"");
//                    }
//                }
//        );
        checkSub();
    }

    public void checkSub() {
        billingClient.queryPurchasesAsync(
                QueryPurchasesParams.newBuilder()
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build(),
                new PurchasesResponseListener() {
                    @Override
                    public void onQueryPurchasesResponse(@NonNull BillingResult billingResult, @NonNull List<Purchase> list) {
                        Log.e("testtest", "onQueryPurchasesResponse");
                        Log.e("testtest", billingResult+"");
                        Log.e("testtest", list+"");

                        if (list.isEmpty()) {
                            Log.e("testtest", "payment list is null");
                            Application.payment_check = false;
                        } else {
                            Log.e("testtest!", list.get(0).getProducts()+"");

                            if (list.get(0).getProducts().toString().equals("[8793inc]")) {
                                Application.payment_check = true;
                                Log.e("testtest", "payment true");
                            } else {
                                Application.payment_check = false;
                                Log.e("testtest", "payment false");
                            }
                        }
                    }
                }
        );
    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
        Log.e("testtest2", "onPurchasesUpdated");

        if (list != null) {
            Log.e("testtest", list.get(0).isAutoRenewing()+"");
            Log.e("testtest", list.get(0).getPurchaseState()+"");

            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                for (Purchase purchase : list) {
//                    verifySubPurchase(purchase);
//                    handlePurchase(purchase);

                    Log.e("testtest", "for문 : " + purchase);
                }
                Log.e("testtest", "소모 성공");
                Application.payment_check = true;
            } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
                Log.e("testtest", "이미 구매");
            } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
                Log.e("testtest", "소모 실패");
            } else {
                Log.e("testtest", "소모 실패 : " + billingResult.getResponseCode());
            }

            Log.e("testtest", billingResult.toString());
            Log.e("testtest", list.toString());

            Log.e("testtest", list.get(0).getPurchaseTime()+"");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
            Date now = new Date();
            now.setTime(list.get(0).getPurchaseTime());

            Log.e("testtest", "purchase time : " + sdf.format(now));
            Log.e("testtest", list.get(0).getOriginalJson());
        } else {
            Log.e("testtest2", "Purchase List is null");
        }
    }
}
