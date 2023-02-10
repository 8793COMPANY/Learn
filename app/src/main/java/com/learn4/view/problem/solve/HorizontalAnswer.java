package com.learn4.view.problem.solve;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.learn4.util.MySharedPreferences;
import com.learn4.R;
import com.learn4.view.custom.view.AnswerItem;
import com.learn4.view.custom.view.ImgAnswerItem;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HorizontalAnswer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HorizontalAnswer extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    AnswerItem supplies1,supplies2;
    TextView name,comment;

    String contents_name;
    int number;

    public HorizontalAnswer() {
        // Required empty public constructor
    }

    public HorizontalAnswer(int number, String contents_name) {
        this.contents_name = contents_name;
        this.number = number;
        // Required empty public constructor
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Step1.
     */
    // TODO: Rename and change types and number of parameters
    public static HorizontalAnswer newInstance(String param1, String param2) {
        HorizontalAnswer fragment = new HorizontalAnswer();
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
        View view = inflater.inflate(R.layout.fragment_horizontal_answer, container, false);

        String chapter_id = ((SolvingProblem)getActivity()).chapter_id;

        Log.e("chapter_id",chapter_id);

        TextView question_number_text = view.findViewById(R.id.question_number_text);
        TextView question_text = view.findViewById(R.id.question_text);
        ImgAnswerItem supplies1 = view.findViewById(R.id.supplies1);
        ImgAnswerItem supplies2 = view.findViewById(R.id.supplies2);
        ImgAnswerItem supplies3 = view.findViewById(R.id.supplies3);
        ImgAnswerItem supplies4 = view.findViewById(R.id.supplies4);

        question_number_text.setText("Q"+number);
        question_text.setText(contents_name);
        Log.e("number",((SolvingProblem)getActivity()).answers[number-1]+"");

        if ( ((SolvingProblem)getActivity()).answers[number-1] == 0) {
            Log.e("in??","hi");
            ((SolvingProblem) getActivity()).initBtn();
        }else{
            Log.e("chapter_id",chapter_id);
            MySharedPreferences.setInt(getContext(), "문제풀이"+chapter_id, number);
            switch ( ((SolvingProblem)getActivity()).answers[number-1]){
                case 1:
                    supplies1.setSelected(true);
                    break;
                case 2:
                    supplies2.setSelected(true);
                    break;
                case 3:
                    supplies3.setSelected(true);
                    break;
                case 4:
                    supplies4.setSelected(true);
                    break;
            }
        }




        supplies1.setOnClickListener(v->{
            ((SolvingProblem)getActivity()).addValue(number-1,1);
            ((SolvingProblem)getActivity()).printAnswer();
            supplies1.setSelected(true);
            supplies2.setSelected(false);
            supplies3.setSelected(false);
            supplies4.setSelected(false);
        });
        supplies2.setOnClickListener(v->{
            ((SolvingProblem)getActivity()).addValue(number-1,2);
            ((SolvingProblem)getActivity()).printAnswer();
            supplies1.setSelected(false);
            supplies2.setSelected(true);
            supplies3.setSelected(false);
            supplies4.setSelected(false);
        });
        supplies3.setOnClickListener(v->{
            ((SolvingProblem)getActivity()).addValue(number-1,3);
            ((SolvingProblem)getActivity()).printAnswer();
            supplies1.setSelected(false);
            supplies2.setSelected(false);
            supplies3.setSelected(true);
            supplies4.setSelected(false);
        });
        supplies4.setOnClickListener(v->{
            ((SolvingProblem)getActivity()).addValue(number-1,4);
            ((SolvingProblem)getActivity()).printAnswer();
            supplies1.setSelected(false);
            supplies2.setSelected(false);
            supplies3.setSelected(false);
            supplies4.setSelected(true);
        });


        return view;

    }
}