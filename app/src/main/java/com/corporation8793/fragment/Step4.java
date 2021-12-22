package com.corporation8793.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.corporation8793.R;
import com.corporation8793.custom.AnswerItem;
import com.corporation8793.dto.Answer;
import com.corporation8793.recyclerview.AnswerAdapter;
import com.corporation8793.recyclerview.RecyclerDecoration;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Step4#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Step4 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    ArrayList<Answer> answers = new ArrayList<>();

    AnswerItem problem1,problem2;

    public Step4() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Step4.
     */
    // TODO: Rename and change types and number of parameters
    public static Step4 newInstance(String param1, String param2) {
        Step4 fragment = new Step4();
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
        View view = inflater.inflate(R.layout.fragment_step4, container, false);
        problem1 = view.findViewById(R.id.problem1);
        problem2 = view.findViewById(R.id.problem2);

        problem1.setType("search");
        problem2.setType("search");
        problem1.setSelected(true);
        problem2.setSelected(false);
        RecyclerView answer_list = view.findViewById(R.id.answer_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        answer_list.setLayoutManager(layoutManager);
        answer_list.addItemDecoration(new RecyclerDecoration(30));
        setDataList();
        AnswerAdapter adapter = new AnswerAdapter(getContext(),answers);
        answer_list.setAdapter(adapter);
        return view;
    }


    public void setDataList(){
        Answer answer = new Answer();
        answer.setImg(R.drawable.yellow_led_img);
        answer.setAnswer("LED");
        answers.add(answer);

        Answer answer1 = new Answer();
        answer1.setImg(R.drawable.red_led_img);
        answer1.setAnswer("LED");
        answers.add(answer1);

        Answer answer2 = new Answer();
        answer2.setImg(R.drawable.green_led_img);
        answer2.setAnswer("LED");
        answers.add(answer2);

        Answer answer3 = new Answer();
        answer3.setImg(R.drawable.led3_img);
        answer3.setAnswer("LED3");
        answers.add(answer3);

        Answer answer4 = new Answer();
        answer4.setImg(R.drawable.on_off_img);
        answer4.setAnswer("etc");
        answers.add(answer4);

        Answer answer5 = new Answer();
        answer5.setImg(R.drawable.etc_led_img);
        answer5.setAnswer("etc");
        answers.add(answer5);

        Answer answer6 = new Answer();
        answer6.setImg(R.drawable.etc_img);
        answer6.setAnswer("etc");
        answers.add(answer6);
    }
}