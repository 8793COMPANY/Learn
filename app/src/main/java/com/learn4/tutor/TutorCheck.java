package com.learn4.tutor;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.corporation8793.learn.xml.ParentXml;
import com.learn4.R;
import com.learn4.data.dto.Subclass;
import com.learn4.data.room.AppDatabase;
import com.learn4.data.room.AppDatabase2;
import com.learn4.data.room.dao.ContentsGoalDao;
import com.learn4.data.room.entity.ContentGoal;
import com.learn4.util.Application;
import com.learn4.view.MainActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class TutorCheck {

    Context context;
    boolean simulator_check, chapter_check;
    MediaPlayer mediaPlayer;
    String chapter_id = "";
    String changeChapterId = "";
    String solutionXmlAssetFilePath = "";
    String submittedXml = "";
    MainActivity mainActivity;

    AppDatabase2 db2 = null;
    ContentsGoalDao contentsGoalDao;
    List<ContentGoal> contentGoalList = new ArrayList<>();

    /*String project_contents_learning[][] = {
            {"LED(Pin 13)을 1초동안 켜지고 1초동안 꺼지도록 코딩하세요.","LED = 13번, 시간 = 1초"},
            {"LED(Pin 13)을 0.5초동안 켜지고 0.2초동안 꺼지도록 코딩하세요." ,"LED = 13번, 시간 = 0.5초"},
            {"LED(Pin 13)을 0.2초동안 켜지고 0.4초동안 꺼지도록 코딩하세요." ,"LED = 13번, 시간 = 1초"},

            {"스위치(Pin 3)을 눌렀을 때 시리얼 모니터(9600)에 ON이 출력되고 " +
                    "누르지 않았을 때 OFF가 출력되도록 코딩하세요.","스위치 = 3번"},
            {"스위치(Pin 2)을 눌렀을 때 시리얼 모니터(9600)에 ON이 출력되고 " +
                    "누르지 않았을 때 OFF가 출력되도록 코딩하세요." ,"스위치 = 2번"},
            {"스위치(Pin 3)을 눌렀을 때 시리얼 모니터(9600)에 LED(Pin 13)이 켜지고 " +
                    "누르지 않았을 때 LED(Pin 10)이 꺼지도록 코딩하세요." ,"스위치 = 3번, LED = 10번"},

            {"Tone 블록을 사용하여 피에조 부저(Pin 6)에서 1초동안 도( 523 )음이 " +
                    "울리도록 코딩하세요.","피에조 부저= 6, " +
                    "시간 = 1초"},
            {"Tone 블록을 사용하여 피에조 부저(Pin 6)에서 노래 음계에 맞춰 " +
                    "연주 되도록 코딩하세요." ,"피에조 부저= 6, " +
                    "시간 = 0.5~2초"},
            {"스위치(Pin 6)를 눌렀을 때 특정한 음계가 나오도록 " +
                    "코딩하세요." ,"피에조 부저= A1, " +
                    "스위치 = 6번"},

            {"네오픽셀(Pin 12)를 조작하여 웃는 표정을 나타나도록 코딩하세요.","네오픽셀 = 12번"},
            {"왼쪽 스위치(Pin 3)을 눌렀을 때 네오픽셀(Pin 12)에서 Yellow/thriling 표정이 " +
                    " 누르지 않을 때는 Green/happy 표정이 나오도록 코딩하세요." ,"스위치 = 3번 " +
                    "네오픽셀 = 12번"},
            {"네오픽셀(Pin 12)를 조작하여 다양한 색상에 다른 표정을 만들어 코딩하세요." ,"네오픽셀 = 12번"},
    };*/
    String goal = "";
    String condition = "";

    public TutorCheck(Context context, boolean simulator_check, String chapter_id, String submittedXml) {
        this.context = context;
        this.simulator_check = simulator_check;
        this.chapter_id = chapter_id;
        this.submittedXml = submittedXml;
        chapter_check = true;

        mediaPlayer = MediaPlayer.create(context, R.raw.bot_true_answer);

        // 봇 메시지 초기화
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        Log.e("check", chapter_id);

        db2 = AppDatabase2.getInstance(context);
        contentsGoalDao = db2.contentsGoalDao();
        contentGoalList = contentsGoalDao.findAll();

        for (int i = 0; i < contentGoalList.size(); i++) {
            if (contentGoalList.get(i).getTotal_category().equals(chapter_id)) {
                goal = contentGoalList.get(i).getCategory_goal();
                condition = contentGoalList.get(i).getCategory_condition();
            }
        }

        changeChapterId = chapter_id.replace("-", "_");
        // filepath 확인
        //ParentXml testparentXml = new ParentXml(context, "turtle/demo_workspaces/lv"+changeChapterId+".xml", submittedXml);

        if(chapter_id.equals("3-2")){
            solutionXmlAssetFilePath = "lv3_2.xml";

            solutionXmlAssetFilePath = "lv1_blink.xml";

            //goal = project_contents_learning[0][0];
            //condition = project_contents_learning[0][1];
        }else if (chapter_id.equals("3-3")){
            solutionXmlAssetFilePath = "lv3_3.xml";

            solutionXmlAssetFilePath = "lv2_blink.xml";
            //goal = project_contents_learning[1][0];
            //condition = project_contents_learning[1][1];
        }else if (chapter_id.equals("3-4")){
            solutionXmlAssetFilePath = "lv3_4.xml";
            solutionXmlAssetFilePath = "lv3_blink.xml";
            //goal = project_contents_learning[2][0];
            //condition = project_contents_learning[2][1];
        }else if (chapter_id.equals("5-2")) {
            solutionXmlAssetFilePath = "lv5_2.xml";
            //goal = project_contents_learning[3][0];
            //condition = project_contents_learning[3][1];
        }else if (chapter_id.equals("5-3")) {
            solutionXmlAssetFilePath = "lv5_3.xml";
            //goal = project_contents_learning[4][0];
            //condition = project_contents_learning[4][1];
        }else if (chapter_id.equals("5-4")) {
            solutionXmlAssetFilePath = "lv5_4.xml";
            //goal = project_contents_learning[5][0];
            //condition = project_contents_learning[5][1];
        }else if (chapter_id.equals("7-2")) {
            solutionXmlAssetFilePath = "lv7_2.xml";
        }else if (chapter_id.equals("7-3")) {
            solutionXmlAssetFilePath = "lv7_3.xml";
        }else if (chapter_id.equals("7-4")) {
            solutionXmlAssetFilePath = "lv7_4.xml";
        }else if (chapter_id.equals("9-2")) {
            solutionXmlAssetFilePath = "lv9_2.xml";
            //goal = project_contents_learning[6][0];
            //condition = project_contents_learning[6][1];
        }else if (chapter_id.equals("9-3")) {
            solutionXmlAssetFilePath = "lv9_3.xml";
            //goal = project_contents_learning[7][0];
            //condition = project_contents_learning[7][1];
        }else if (chapter_id.equals("9-4")) {
            solutionXmlAssetFilePath = "lv9_4.xml";
            //goal = project_contents_learning[8][0];
            //condition = project_contents_learning[8][1];
        }else if (chapter_id.equals("11-2")) {
            solutionXmlAssetFilePath = "lv11_2.xml";
        }else if (chapter_id.equals("11-3")) {
            solutionXmlAssetFilePath = "lv11_3.xml";
        }else if (chapter_id.equals("11-4")) {
            solutionXmlAssetFilePath = "lv11_4.xml";
        }else if (chapter_id.equals("13-2")) {
            solutionXmlAssetFilePath = "lv13_2.xml";
        }else if (chapter_id.equals("13-3")) {
            solutionXmlAssetFilePath = "lv13_3.xml";
        }else if (chapter_id.equals("13-4")) {
            solutionXmlAssetFilePath = "lv13_4.xml";
        }else if (chapter_id.equals("15-2")) {
            solutionXmlAssetFilePath = "lv15_2.xml";
        }else if (chapter_id.equals("15-3")) {
            solutionXmlAssetFilePath = "lv15_3.xml";
        }else if (chapter_id.equals("15-4")) {
            solutionXmlAssetFilePath = "lv15_4.xml";
        }else if (chapter_id.equals("17-2")) {
            solutionXmlAssetFilePath = "lv17_2.xml";
        }else if (chapter_id.equals("17-3")) {
            solutionXmlAssetFilePath = "lv17_3.xml";
        }else if (chapter_id.equals("17-4")) {
            solutionXmlAssetFilePath = "lv17_4.xml";
        }else if (chapter_id.equals("19-2")) {
            solutionXmlAssetFilePath = "lv19_2.xml";
        }else if (chapter_id.equals("19-3")) {
            solutionXmlAssetFilePath = "lv19_3.xml";
        }else if (chapter_id.equals("19-4")) {
            solutionXmlAssetFilePath = "lv19_4.xml";
        }else if (chapter_id.equals("21-2")) {
            solutionXmlAssetFilePath = "lv21_2.xml";
        }else if (chapter_id.equals("21-3")) {
            solutionXmlAssetFilePath = "lv21_3.xml";
        }else if (chapter_id.equals("21-4")) {
            solutionXmlAssetFilePath = "lv21_4.xml";
        }else if (chapter_id.equals("23-2")) {
            solutionXmlAssetFilePath = "lv23_2.xml";
        }else if (chapter_id.equals("23-3")) {
            solutionXmlAssetFilePath = "lv23_3.xml";
        }else if (chapter_id.equals("23-4")) {
            solutionXmlAssetFilePath = "lv23_4.xml";
        }else if (chapter_id.equals("43-2")) {
            solutionXmlAssetFilePath = "lv43_2.xml";
            //goal = project_contents_learning[9][0];
            //condition = project_contents_learning[9][1];
        }else if (chapter_id.equals("43-3")) {
            solutionXmlAssetFilePath = "lv43_3.xml";
            //goal = project_contents_learning[10][0];
            //condition = project_contents_learning[10][1];
        }else if (chapter_id.equals("43-4")) {
            solutionXmlAssetFilePath = "lv43_4.xml";
            //goal = project_contents_learning[11][0];
            //condition = project_contents_learning[11][1];
        }else{
            chapter_check = false;
            solutionXmlAssetFilePath = "lv3_2.xml";
            //solutionXmlAssetFilePath = "lv1_blink.xml";
        }

        ParentXml parentXml = new ParentXml(context, "turtle/demo_workspaces/"+solutionXmlAssetFilePath, submittedXml);

        String solution_str = parentXml.getSolutionString();
        String submitted_str = parentXml.getSubmittedString();

        Log.e("submittedXml",submittedXml);

        Log.e("solution_str",solution_str);
        Log.e("submitted_str",submitted_str);

        // 채점
        Log.d("Build Bot", "Is that the right answer? : " + solution_str.equals(submitted_str));
        Log.d("Build Bot", "===============================");

        // 정답지, 답안지 DOM 생성
        parentXml.initDocument();

        Document submitted_doc = parentXml.getSubmittedDocument();

        // 답안지 파싱 작업 시작
        NodeList submitted_statement_nl = submitted_doc.getElementsByTagName("statement");
        Node submitted_setup_node = null;
        Node submitted_loop_node = null;

        // 1. Setup 이랑 Loop 노드 분리
        for (int i = 0; i < submitted_statement_nl.getLength(); i++) {
            Node n = submitted_statement_nl.item(i);

            // turtle_setup_loop - statement node details (for debug log)
            Log.d("Build Bot", i + " - n0 name : " + n.getNodeName());
            Log.d("Build Bot", i + " - n0 attr name : " + n.getAttributes().getNamedItem("name").getNodeValue());

            // attr name : DO - Setup node
            // attr name : DO1 - Loop node
            switch (n.getAttributes().getNamedItem("name").getNodeValue()) {
                case "DO":
                    submitted_setup_node = n;
                case "DO1":
                    submitted_loop_node = n;
            }
        }

        // TODO : 2. Setup 노드 테스트 케이스 작성
        Element e = (Element) submitted_statement_nl.item(0);

        if (chapter_check) {
            if (e != null) {
                // 또는, " 답안지가 정답지와 일치 " 했을때 정답처리
                if (solution_str.equals(submitted_str)){
                    mediaPlayer = MediaPlayer.create(context, R.raw.bot_true_answer);
                    mediaPlayer.start();

                    showCustomDialog(1);
                }else{
                    showCustomDialog(2);
                }

                //Log.d("Build Bot pin number", e.getElementsByTagName("field").item(0).getTextContent());
                //Log.d("Build Bot pin IO", e.getElementsByTagName("field").item(1).getTextContent());
                //Log.d("Build Bot first line", parentXml.getPreprocessedString(submitted_setup_node.getTextContent()));
            } else {
                if (simulator_check) {
                    showCustomDialog(3);
                }
            }
        }
    }

    public void showCustomDialog(int num) {
        mainActivity = new MainActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_tutor, (ConstraintLayout) ((Activity)context).findViewById(R.id.tutor_dialog));

        builder.setView(view);

        String [] number = chapter_id.split("-");
        int contents_num = (Integer.parseInt(number[0]) -3) / 2;
        if (number[0].equals("43")){
            contents_num = 10;
        }
        Subclass subclass = Application.learningObjectives.get(contents_num).getSubclasses().get(Integer.parseInt(number[1])-2);

        /*if (Application.mode == 2){
            ((TextView)view.findViewById(R.id.main_text)).setText(goal);
            ((TextView)view.findViewById(R.id.condition_text)).setText(condition);
        }else{
            ((TextView)view.findViewById(R.id.main_text)).setText(subclass.getLearning_objective());
            ((TextView)view.findViewById(R.id.condition_text)).setText(subclass.getCondition());
        }*/

        ((TextView)view.findViewById(R.id.main_text)).setText(goal);
        ((TextView)view.findViewById(R.id.condition_text)).setText(condition);

        if (num == 1) {
            ((TextView)view.findViewById(R.id.tutor_text)).setText("정답입니다. 참 잘했어요~!");
            Application.setSimulatorEnabled(true);
        } else if (num == 2) {
            ((TextView)view.findViewById(R.id.tutor_text)).setText("틀렸습니다. 다시 한번 해보세요~!");
        } else {
            ((TextView)view.findViewById(R.id.tutor_text)).setText("빈 블록입니다. 블록 코딩을 해주세요~!");
        }

        AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.bottom_section).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        view.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();

        /*ViewGroup.LayoutParams params = alertDialog.getWindow().getAttributes();
        params.width = (int) (335*2.6);
        alertDialog.getWindow().setAttributes((WindowManager.LayoutParams) params);*/
    }
}
