
package com.learn4;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.learn4.util.Application;
import com.learn4.util.DisplaySize;
import com.learn4.view.custom.dialog.WordChangeDialog;

public class BluetoothActivity extends AppCompatActivity {

    TextView read_text, write_text, read_text_display, time_text_display;
    Button read_reset_btn, start_btn, stop_btn, reset_btn, send_btn;
    EditText write_edit_text;
    Button[] key_btn = new Button[9];
    Integer[] key_btn_id = { R.id.Aged, R.id.Trades, R.id.qsdd, R.id.Xfchaer, R.id.Retort,
            R.id.Higher, R.id.Tdsdsd, R.id.Sjhfds, R.id.Sdfhdfh};

    WordChangeDialog wordChangeDialog;

    int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        // 변수 선언
        init_variable();

        // 변수 선언 - 키버튼 따로 설정
        init_variable_key_btn();

        // 변수 글씨 크기 및 패딩값 설정
        init_size();

        // 하단 소프트키 숨기기
        hide_soft_key();
    }

    private void init_variable() {
        read_text = findViewById(R.id.read_text);
        write_text = findViewById(R.id.write_text);
        read_text_display = findViewById(R.id.read_text_display);
        time_text_display = findViewById(R.id.time_text_display);

        read_reset_btn = findViewById(R.id.read_reset_btn);
        start_btn = findViewById(R.id.start_btn);
        stop_btn = findViewById(R.id.stop_btn);
        reset_btn = findViewById(R.id.reset_btn);

        write_edit_text = findViewById(R.id.write_edit_text);
        send_btn = findViewById(R.id.send_btn);
    }

    private void init_variable_key_btn() {
        for (int i = 0; i < key_btn_id.length; i++) {
            key_btn[i] = (Button) findViewById(key_btn_id[i]);
            key_btn[i].setTextSize(DisplaySize.font_size_y_30);

            int finalI = i;

            // 키 버튼 클릭 이벤트
            key_btn[i].setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View v) {
                    Log.e("action 확인~~!", key_btn[finalI].getText()+"");

                    // edit text 입력되는 부분
                    write_edit_text.setText(write_edit_text.getText().toString() + key_btn[finalI].getText());
                    write_edit_text.setSelection(write_edit_text.getText().length());
                }
            });

            // 키 버튼 롱 클릭 이벤트
            key_btn[i].setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.e("action 확인~~!", key_btn[finalI].getText()+" : Long~!");

                    num = finalI;

                    // 다이얼로그 실행
                    wordChangeDialog = new WordChangeDialog(BluetoothActivity.this, word_change_ok_listener);
                    wordChangeDialog.show();

                    // 다이얼로그 화면 크기 조절
                    Window window = wordChangeDialog.getWindow();
                    int x = (int) (Application.displaySize_X * 0.43f);
                    int y = (int) (Application.displaySize_Y * 0.278f);
                    window.setLayout(x, y);

                    return true;
                }
            });
        }
    }

    private final View.OnClickListener word_change_ok_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.e("action 확인~~!", "word_change_ok_listener");
            Log.e("action 확인~~!", wordChangeDialog.word_input_box.getText() + "");

            if (wordChangeDialog.word_input_box.getText().toString().trim().equals("")) {
                Toast.makeText(getApplicationContext(), "단어를 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else {
                key_btn[num].setText(wordChangeDialog.word_input_box.getText().toString().trim());
            }

            wordChangeDialog.dismiss();
        }
    };

    private void init_size() {
        read_text.setTextSize(DisplaySize.font_size_y_36);
        write_text.setTextSize(DisplaySize.font_size_y_36);
        read_text_display.setTextSize(DisplaySize.font_size_y_50);
        time_text_display.setTextSize(DisplaySize.font_size_y_50);

        read_reset_btn.setTextSize(DisplaySize.font_size_y_28);
        start_btn.setTextSize(DisplaySize.font_size_y_28);
        stop_btn.setTextSize(DisplaySize.font_size_y_28);
        reset_btn.setTextSize(DisplaySize.font_size_y_28);

        write_edit_text.setTextSize(DisplaySize.font_size_y_29);
        write_edit_text.setPadding((int) DisplaySize.font_size_x_20,0,(int) DisplaySize.font_size_x_20,0);
        send_btn.setTextSize(DisplaySize.font_size_y_28);
    }

    private void hide_soft_key() {
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        boolean isImmersiveModeEnabled = ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        if (isImmersiveModeEnabled) {
            Log.i("Is on?", "Turning immersive mode mode off. ");
        } else {
            Log.i("Is on?", "Turning immersive mode mode on.");
        }
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }
}