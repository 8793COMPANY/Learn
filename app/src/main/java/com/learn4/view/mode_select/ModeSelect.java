package com.learn4.view.mode_select;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.android.billingclient.api.AcknowledgePurchaseParams;
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
import com.google.android.gms.common.GoogleSourceStampsChecker;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.ImmutableList;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.learn4.R;
import com.google.android.material.navigation.NavigationView;
import com.learn4.util.Application;
import com.learn4.util.DataSetting;
import com.learn4.util.MySharedPreferences;
import com.learn4.view.custom.dialog.CodeInputDialog;
import com.learn4.view.custom.dialog.ProgressDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.checkerframework.checker.units.qual.A;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.http.Url;

public class ModeSelect extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, PurchasesUpdatedListener {

    private AppBarConfiguration mAppBarConfiguration;

    private View decorView;
    private int	uiOption;

    NavigationView nav_view;


    private BillingClient billingClient;
    private List<ProductDetails> productDetailsList;

    Purchase testPurchase;

    String code_list;
    ArrayList<String> arrayList;

    CodeInputDialog codeInputDialog;
    Application application;

    PurchaseHistoryResponseListener purchaseHistoryResponseListener;
    AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener;

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                //Toast.makeText(this, "nav_home", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_payment:
                //Toast.makeText(this, "nav_payment", Toast.LENGTH_SHORT).show();
                SubPurchase();
                break;
            case R.id.nav_payment_cancel:
                //Toast.makeText(this, "nav_payment_cancel", Toast.LENGTH_SHORT).show();
                SubPurchaseCancel();
//                if (testPurchase != null) {
//                    Log.e("testtest", testPurchase.getPurchaseState()+"");
//                } else {
//                    Log.e("testtest", "testPurchase is null 2");
//                }
                break;
            case R.id.nav_input_code:
//                Toast.makeText(this, "nav_input_code", Toast.LENGTH_SHORT).show();
                InputCode();
                break;
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_select);

        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility( uiOption );

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        Toolbar toolbar = findViewById(R.id.toolbar);
        DrawerLayout  drawer = findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);            //홈 아이콘을 숨김처리합니다.
        DataSetting setting = DataSetting.getInstance(getApplicationContext());
        setting.dataCheck();

        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
//        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
//        View actionbar = inflater.inflate(R.layout.custom_actionbar, null);
//
//        actionBar.setCustomView(actionbar);
//
//        getSupportActionBar().getCustomView().findViewById(R.id.btnMenu).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                Log.e("hi","in!");
//                drawer.openDrawer(GravityCompat.START);
//            }
//        });

        findViewById(R.id.btnMenu).setOnClickListener(v->{
            drawer.openDrawer(GravityCompat.START);
        });

//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
//                .setDrawerLayout(drawer)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);

        nav_view = findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);

        billingClient = BillingClient.newBuilder(this)
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

        arrayList = new ArrayList<>();

        application = (Application) this.getApplication();

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

    public void verifySubPurchase(Purchase purchase) {
        if (!purchase.isAcknowledged()) {
            billingClient.acknowledgePurchase(AcknowledgePurchaseParams
                    .newBuilder()
                    .setPurchaseToken(purchase.getPurchaseToken())
                    .build(), billingResult -> {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    for (String pur : purchase.getProducts()) {
                        if (pur.equalsIgnoreCase("baeulrae_test1")) {
                            Log.e("testtest", "Purchase is successful" + pur);
                            Toast.makeText(this, "Yay! Purchased", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Log.e("testtest", billingResult.getResponseCode()+"");
                }
            });

        }
    }

    public void handlePurchase(Purchase purchase) {
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams =
                        AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.getPurchaseToken())
                                .build();
                billingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);
            }
        }
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
                                        .setProductId("baeulrae_test1")
                                        .setProductType(BillingClient.ProductType.SUBS)
                                        .build()
                        )
                ).build();

        Log.e("payment queryProductDetailAsync product", "baeulrae_test1");

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

    public void SubPurchase() {
        String offerToken = productDetailsList.get(0).getSubscriptionOfferDetails().get(0).getOfferToken();

        BillingFlowParams.ProductDetailsParams flowProductDetailParams =
                BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(productDetailsList.get(0))
                        .setOfferToken(offerToken)
                        .build();

        BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(Collections.singletonList(flowProductDetailParams))
                .build();

        int responseCode = billingClient.launchBillingFlow(this, flowParams).getResponseCode();

        Log.e("testtest2", responseCode+"");
    }


    public void SubPurchaseCancel() {
        if (testPurchase != null) {
            Log.e("testtest", testPurchase.getPurchaseState()+"");

        } else {
            Log.e("testtest2", "testPurchase is null");
        }

        String url = "https://play.google.com/store/account/subscriptions";

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);

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
                    }
                }
        );
    }

    private View.OnClickListener code_input_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (codeInputDialog.code_input_box.getText().toString().trim().equals("")) {
                Toast.makeText(ModeSelect.this, "코드를 입력해주세요!", Toast.LENGTH_SHORT).show();
            } else {
                if (arrayList.contains(codeInputDialog.code_input_box.getText().toString())) {
                    // 여기서 광고 유무 체크
                    Log.e("testtest", "code ok");
                    Application.ad_check = true;

                    MySharedPreferences.setString(getApplicationContext(), "school_code", codeInputDialog.code_input_box.getText().toString());

                    Log.e("testtest", codeInputDialog.code_input_box.getText().toString());
                    Log.e("testtest", MySharedPreferences.getString(getApplicationContext(), "school_code"));

                    codeInputDialog.dismiss();

                    Toast.makeText(ModeSelect.this, "등록 완료!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ModeSelect.this, "해당 코드는 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    public void InputCode() {
        application.showLoadingScreen(this);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        arrayList.clear();

        db.collection("AdTest").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Log.e("testtest", documentSnapshot.getId() + " => " + documentSnapshot.getData());
                                Log.e("testtest", documentSnapshot.getId()+"");
                                Log.e("testtest", documentSnapshot.getData().keySet()+"");
                                Log.e("testtest", documentSnapshot.getData().get("elementary")+"");

                                if (documentSnapshot.getData().keySet().toString().equals("[elementary]")) {
                                    code_list = documentSnapshot.getData().get("elementary").toString();
                                    Log.e("testtest", code_list+"");
                                } else {
                                    Log.e("testtest", "no");
                                }

                                if (code_list != null) {
                                    code_list = code_list.replace("[", "");
                                    code_list = code_list.replace("]", "");
                                    code_list = code_list.replace(" ", "");

                                    Log.e("testtest", code_list+"");

                                    String[] splitCodeList = code_list.split(",");

                                    for (int i = 0; i < splitCodeList.length; i++) {
                                        Log.e("testtest", splitCodeList[i]);
                                        arrayList.add(splitCodeList[i]);
                                    }

                                    if (arrayList != null) {
                                        for (int i = 0; i < arrayList.size(); i++) {
                                            Log.e("testtest", "arrayList : " + arrayList.get(i));
                                        }
                                    } else {
                                        Log.e("testtest", "arrayList null");
                                    }
                                } else {
                                    Log.e("testtest", "code list null");
                                }

                                application.hideLoadingScreen();
                                codeInputDialog = new CodeInputDialog(ModeSelect.this, code_input_listener, Application.ad_check);
                                codeInputDialog.show();
                            }
                        } else {
                            Log.e("testtest", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.mode_select, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)
            hideSystemUI();
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                    handlePurchase(purchase);

                    Log.e("testtest", "for문 : " + purchase);
                    testPurchase = purchase;
                }
                Log.e("testtest", "소모 성공");
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

//    @Override
//    protected void onStop() {
//        super.onStop();
//        unregisterNetworkCallback();
//    }
}