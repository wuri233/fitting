package com.example.fitting;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017/5/25.
 */

public class PlanAdapter extends ArrayAdapter<Plan>{
    private int resourceId;
    public PlanAdapter(Context context, int textViewResourceId, List<Plan> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Plan plan=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView planItem=(TextView)view.findViewById(R.id.plan_name);
        CheckBox checkBox=(CheckBox)view.findViewById(R.id.checkBox);
        planItem.setText(plan.getName());
        checkBox.setClickable(false);
        checkBox.setChecked(plan.getPlanCheck());
        return view;
    }

}
