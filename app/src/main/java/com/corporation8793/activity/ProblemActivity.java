package com.corporation8793.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceControl;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.corporation8793.R;
import com.corporation8793.fragment.Step1;
import com.corporation8793.fragment.Step2;
import com.corporation8793.fragment.Step3;
import com.corporation8793.fragment.Step4;

import org.w3c.dom.Text;

public class ProblemActivity extends AppCompatActivity {

    private View decorView;
    private int	uiOption;

    Button back_btn, next_btn;

    TextView title, title2;

    String [] titles = {"준비물","전체 회로도","회로도 구성","필요한 부품을 찾아보세요"};
    int pos = 0;

    ConstraintLayout background;
    LinearLayout title_background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);

        background = findViewById(R.id.problem_background);
        title_background = findViewById(R.id.problem_title_background);

        title = findViewById(R.id.title);
        title2 = findViewById(R.id.title2);

        back_btn = findViewById(R.id.back_btn);
        next_btn = findViewById(R.id.next_btn);

        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility( uiOption );

        replaceFragment(0);


        back_btn.setOnClickListener(v->{
            next_btn.setVisibility(View.VISIBLE);
            pos--;
            replaceFragment(pos);
            Log.e("pos",pos+"");
            if (pos ==0){
                back_btn.setVisibility(View.INVISIBLE);
            }
            title_background.setVisibility(View.VISIBLE);
            title.setText(titles[pos]);
            title2.setVisibility(View.INVISIBLE);
        });

        next_btn.setOnClickListener(v->{
            if (pos < 4) {
                back_btn.setVisibility(View.VISIBLE);
                pos++;
                replaceFragment(pos);
            }
            Log.e("pos",pos+"");
            if (pos ==3){
                background.setBackgroundColor(Color.WHITE);
                back_btn.setVisibility(View.INVISIBLE);
                title_background.setVisibility(View.INVISIBLE);
                title2.setVisibility(View.VISIBLE);
            }else if(pos ==4){
                back_btn.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(ProblemActivity.this, MainActivity.class);
                startActivity(intent);
            }
            else{
                title.setText(titles[pos]);
            }

        });
    }


    public void replaceFragment(int pos){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (pos == 0){
            transaction.replace(R.id.fragment, new Step1());
        }else if (pos ==1){
            transaction.replace(R.id.fragment, new Step2());
        }else if (pos ==2){
            transaction.replace(R.id.fragment, new Step3());
        }else{
            transaction.replace(R.id.fragment, new Step4());
        }

        transaction.commit();

    }
}