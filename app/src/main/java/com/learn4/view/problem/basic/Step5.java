package com.learn4.view.problem.basic;

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
import com.learn4.util.MySharedPreferences;

import org.w3c.dom.Text;

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

        TextView title = root.findViewById(R.id.title);
        TextView item_difficulty = root.findViewById(R.id.item_difficulty);
        TextView learning_objective = root.findViewById(R.id.learning_objective);
        TextView contents_condition = root.findViewById(R.id.contents_condition);

        String [] number = chapter_id.split("-");


        if (MySharedPreferences.getInt(getContext(),contents_name+" MAX") < 1) {
            MySharedPreferences.setInt(getContext(), contents_name+" MAX", 1);
        }

        MySharedPreferences.setInt(getContext(), contents_name, 1);
        if (number[0].equals("3") || number[0].equals("5")){
            Log.e("contents","in");
            int contents_num = (Integer.parseInt(number[0]) -3) / 2;
            Subclass subclass = Application.learningObjectives.get(contents_num).getSubclasses().get(Integer.parseInt(number[1])-2);
            Log.e("id",subclass.getLearning_objective());

            title.setText(contents_name);
            item_difficulty.setText(difficulty[Integer.parseInt(number[1])-2]);
            learning_objective.setText(subclass.getLearning_objective());
            contents_condition.setText(subclass.getCondition());


        }
        Log.e("contents_name",contents_name);
        Log.e("contents_name",chapter_id);


        return root;
    }
}