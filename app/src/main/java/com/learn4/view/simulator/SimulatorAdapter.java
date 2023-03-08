package com.learn4.view.simulator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.learn4.R;
import com.learn4.data.dto.SimulatorComponent;

import java.util.ArrayList;

public class SimulatorAdapter extends RecyclerView.Adapter<SimulatorAdapter.CustomViewHolder> {

    ArrayList<SimulatorComponent> simulatorComponents;
    ArrayAdapter<CharSequence> simulatorSpinnerAdapter;

    public SimulatorAdapter(ArrayList<SimulatorComponent> simulatorComponents) {
        this.simulatorComponents = simulatorComponents;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.component_itemview, parent, false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.component_item.setBackgroundResource(simulatorComponents.get(position).getComponent_image());
        holder.component_spinner.setSelection(0);

        holder.component_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 다른 항목 선택시
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageView component_item;
        Spinner component_spinner;

        public CustomViewHolder(View view) {
            super(view);

            component_item = view.findViewById(R.id.component_item);
            component_spinner = view.findViewById(R.id.component_spinner);

            simulatorSpinnerAdapter = ArrayAdapter.createFromResource(view.getContext(), R.array.autosize_text_sizes, R.layout.spinner_component_item);
            component_spinner.setAdapter(simulatorSpinnerAdapter);

        }
    }
}
