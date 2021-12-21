package com.corporation8793.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.corporation8793.R;

import org.w3c.dom.Text;

public class ProblemActivity extends AppCompatActivity {

    private View decorView;
    private int	uiOption;

    Button back_btn, next_btn;

    TextView title;

    String [] titles = {"준비물","전체 회로도","회로도 구성"};
    int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);

        title = findViewById(R.id.title);
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


        back_btn.setOnClickListener(v->{
            next_btn.setVisibility(View.VISIBLE);
            pos--;
            //Log.e("pos",pos+"");
            if (pos ==0){
                back_btn.setVisibility(View.INVISIBLE);
            }
            title.setText(titles[pos]);
        });

        next_btn.setOnClickListener(v->{
            back_btn.setVisibility(View.VISIBLE);
            pos++;
            //Log.e("pos",pos+"");
            if (pos ==2){
                next_btn.setVisibility(View.INVISIBLE);
            }
            title.setText(titles[pos]);
        });
    }
}