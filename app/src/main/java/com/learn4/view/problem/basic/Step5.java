package com.learn4.view.problem.basic;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.learn4.R;
import com.learn4.data.dto.Subclass;
import com.learn4.util.Application;
import com.learn4.util.DisplaySize;
import com.learn4.util.MySharedPreferences;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Step5#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Step5 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String chapter_id, contents_name;

    String [] difficulty = {"기본문제","심화문제 1","심화문제 2"};

    String project_contents_learning[][] = {
            {"LED(Pin 13)을 1초동안 켜지고 1초동안 꺼지도록 코딩하세요.","LED = 13번, 시간 = 1초"},
            {"LED(Pin 13)을 0.5초동안 켜지고 0.2초동안 꺼지도록 코딩하세요." ,"LED = 13번, 시간 = 0.5초"},
            {"LED(Pin 13)을 0.2초동안 켜지고 0.4초동안 꺼지도록 코딩하세요." ,"LED = 13번, 시간 = 1초"},

            {"스위치(Pin 3)을 눌렀을 때 시리얼 모니터(9600)에 ON이 출력되고\n" +
                    "누르지 않았을 때 OFF가 출력되도록 코딩하세요.","스위치 = 3번"},
            {"스위치(Pin 2)을 눌렀을 때 시리얼 모니터(9600)에 ON이 출력되고\n" +
                    "누르지 않았을 때 OFF가 출력되도록 코딩하세요." ,"스위치 = 2번"},
            {"스위치(Pin 3)을 눌렀을 때 시리얼 모니터(9600)에 LED(Pin 13)이 켜지고\n" +
                    "누르지 않았을 때 LED(Pin 10)이 꺼지도록 코딩하세요." ,"스위치 = 3번, LED = 10번"},

            {"Tone 블록을 사용하여 피에조 부저(Pin 6)에서 1초동안 도( 523 )음이\n" +
                    "울리도록 코딩하세요.","피에조 부저= 6,\n" +
                    "시간 = 1초"},
            {"Tone 블록을 사용하여 피에조 부저(Pin 6)에서 노래 음계에 맞춰\n" +
                    "연주 되도록 코딩하세요." ,"피에조 부저= 6,\n" +
                    "시간 = 0.5~2초"},
            {"스위치(Pin 6)를 눌렀을 때 특정한 음계가 나오도록\n" +
                    "코딩하세요." ,"피에조 부저= A1,\n" +
                    "스위치 = 6번"},

            {"네오픽셀(Pin 12)를 조작하여 웃는 표정을 나타나도록 코딩하세요.","네오픽셀 = 12번"},
            {"왼쪽 스위치(Pin 3)을 눌렀을 때 네오픽셀(Pin 12)에서 Yellow/thriling 표정이\n" +
                    " 누르지 않을 때는 Green/happy 표정이 나오도록 코딩하세요." ,"스위치 = 3번\n" +
                    "네오픽셀 = 12번"},
            {"네오픽셀(Pin 12)를 조작하여 다양한 색상에 다른 표정을 만들어 코딩하세요." ,"네오픽셀 = 12번"},
    };



    public Step5() {
        // Required empty public constructor
    }

    public Step5(String chapter_id, String contents_name) {
        this.chapter_id = chapter_id;
        this.contents_name = contents_name;
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Step5.
     */
    // TODO: Rename and change types and number of parameters
    public static Step5 newInstance(String param1, String param2) {
        Step5 fragment = new Step5();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_step5, container, false);
        Map<String,String[]> learning_object = new HashMap<>();
        learning_object.put("3-2",project_contents_learning[0]);
        learning_object.put("3-3",project_contents_learning[1]);
        learning_object.put("3-4",project_contents_learning[2]);

        learning_object.put("5-2",project_contents_learning[3]);
        learning_object.put("5-3",project_contents_learning[4]);
        learning_object.put("5-4",project_contents_learning[5]);


        learning_object.put("9-2",project_contents_learning[6]);
        learning_object.put("9-3",project_contents_learning[7]);
        learning_object.put("9-4",project_contents_learning[8]);


        learning_object.put("43-2",project_contents_learning[9]);
        learning_object.put("43-3",project_contents_learning[10]);
        learning_object.put("43-4",project_contents_learning[11]);




        TextView title = root.findViewById(R.id.title);
        TextView item_difficulty = root.findViewById(R.id.item_difficulty);
        TextView learning_objective = root.findViewById(R.id.learning_objective);
        TextView contents_condition = root.findViewById(R.id.contents_condition);

        title.setTextSize(DisplaySize.font_size_y_38);
        item_difficulty.setTextSize(DisplaySize.font_size_y_31);
        learning_objective.setTextSize(DisplaySize.font_size_y_32);
        contents_condition.setTextSize(DisplaySize.font_size_y_32);

        String [] number = chapter_id.split("-");

        if (MySharedPreferences.getInt(getContext(),contents_name+" MAX") < 1) {
            MySharedPreferences.setInt(getContext(), contents_name+" MAX", 1);
        }

        Log.e("number check",chapter_id);
        if (number[0].equals("3")) {
            if (chapter_id.equals("3-2")) {
//                Application.mediaPlayer = MediaPlayer.create(getContext(), R.raw.led_2);
            } else if (chapter_id.equals("3-3")) {
//                Application.mediaPlayer = MediaPlayer.create(getContext(), R.raw.led_7);
            } else if (chapter_id.equals("3-4")) {
//                Application.mediaPlayer = MediaPlayer.create(getContext(), R.raw.led_8);
            }
//            Application.mediaPlayer.start();
        }

        MySharedPreferences.setInt(getContext(), contents_name, 1);
//        if (number[0].equals("3") || number[0].equals("5")){
            Log.e("contents","in");
            int contents_num = (Integer.parseInt(number[0]) -3) / 2;
            Log.e("contents_num",contents_num+"");
            if (number[0].equals("43")){
                contents_num = 10;
            }
            Subclass subclass = Application.learningObjectives.get(contents_num).getSubclasses().get(Integer.parseInt(number[1])-2);
            Log.e("subclass id",subclass.getLearning_objective());

            title.setText(contents_name);
            item_difficulty.setText(difficulty[Integer.parseInt(number[1])-2]);

            if (Application.mode == 2){
                learning_objective.setText(learning_object.get(chapter_id)[0]);
                contents_condition.setText(learning_object.get(chapter_id)[1]);
            }else{
                learning_objective.setText(subclass.getLearning_objective());
                contents_condition.setText(subclass.getCondition());
            }




//        }
        Log.e("contents_name",contents_name);
        Log.e("contents_name",chapter_id);


        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Application.mediaPlayer != null) {
            Application.mediaPlayer.release();
            Application.mediaPlayer = null;
        }
    }
}