package com.learn4.view.problem.basic;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.learn4.util.MySharedPreferences;
import com.learn4.R;
import com.learn4.view.recyclerview.RecyclerDecoration_Width;
import com.learn4.view.custom.view.AnswerItem;
import com.learn4.data.dto.Supplies;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Step1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Step1 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    AnswerItem supplies1,supplies2;
    RecyclerView supplies;
    TextView name,comment;

    String contents_name;
    SuppliesAdapter suppliesAdapter;
    private ArrayList<Supplies> supplies_list = new ArrayList<>();
    int previous_num = 0;

    public Step1() {
        // Required empty public constructor
    }

    public Step1(String contents_name) {
        this.contents_name = contents_name;
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
    public static Step1 newInstance(String param1, String param2) {
        Step1 fragment = new Step1();
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
        View view = inflater.inflate(R.layout.fragment_step1, container, false);

        Log.e("contents",contents_name);

        if (MySharedPreferences.getInt(getContext(),contents_name+" MAX") < 1) {
            MySharedPreferences.setInt(getContext(), contents_name+" MAX", 1);
        }

        MySharedPreferences.setInt(getContext(), contents_name, 1);

        String msg = "점퍼선은 납땜없이도 센서와 센서를 연결하거나 센서와 전류를 연결해주는 선입니다.";
//        supplies1 = view.findViewById(R.id.supplies1);
//        supplies2 = view.findViewById(R.id.supplies2);

        supplies = view.findViewById(R.id.supplies);
        name = view.findViewById(R.id.supplies_name);
        comment = view.findViewById(R.id.supplies_comment);

        supplies_list.add(new Supplies(R.drawable.supplies_img1,true));
        supplies_list.add(new Supplies(R.drawable.supplies_img2,false));
//        supplies_list.add(new Supplies(R.drawable.supplies_img2,false));
//        supplies_list.add(new Supplies(R.drawable.supplies_img2,false));

        suppliesAdapter = new SuppliesAdapter(getContext(),supplies_list);

        supplies.setAdapter(suppliesAdapter);

        RecyclerDecoration_Width decoration_height = new RecyclerDecoration_Width(70,supplies_list.size());
        supplies.addItemDecoration(decoration_height);


        supplies.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));

        suppliesAdapter.setOnItemClickListener((position, click) -> {
            Log.e("position",position+"");
            if (previous_num != position) {
                suppliesAdapter.notifyItemChanged(previous_num, "no");
                suppliesAdapter.notifyItemChanged(position, "click");
                previous_num = position;
                if (position ==0) {
                    name.setText("LED");
                    comment.setText("LED(Light Emitting Diode)는 우리 말로는 '발광 다이오드' 라고 하며,\n전류를 가하면 빛을 발하는 센서입니다.");
                }else{
                    name.setText("점퍼선");
                    comment.setText(msg);
                }
            }
        });
//        supplies1.setType("default");
//        supplies2.setType("default");
//        supplies1.setSelected(true);
//        supplies2.setSelected(false);

//        supplies1.setOnClickListener(v->{
//            supplies1.setSelected(true);
//            supplies2.setSelected(false);
//            name.setText("LED");
//            comment.setText("LED(Light Emitting Diode)는 우리 말로는 '발광 다이오드' 라고 하며,\n전류를 가하면 빛을 발하는 센서입니다.");
//        });
//        supplies2.setOnClickListener(v->{
//            supplies1.setSelected(false);
//            supplies2.setSelected(true);
//            name.setText("점퍼선");
//            comment.setText(msg);
//        });


        return view;

    }
}