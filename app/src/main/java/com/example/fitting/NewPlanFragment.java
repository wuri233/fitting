package com.example.fitting;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.fitting.db.PlanItem;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Administrator on 2017/5/25.
 */

public class NewPlanFragment extends Fragment{

    private EditText editText;
    private TextView textView;
    private SeekBar seekBar;
    private TextView textView2;
    private CheckBox checkBox;
    private TimePicker timePicker;
    private Button button;
    private Button button2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.new_plan,container,false);
        editText =(EditText)view.findViewById(R.id.in_plan_name);
        textView=(TextView)view.findViewById(R.id.day);
        seekBar=(SeekBar)view.findViewById(R.id.intension_seekBar);
        textView2=(TextView)view.findViewById(R.id.intension_text);
        checkBox=(CheckBox)view.findViewById(R.id.timing_check);
        timePicker=(TimePicker)view.findViewById(R.id.timePicker);
        button=(Button)view.findViewById(R.id.button);
        button2=(Button)view.findViewById(R.id.button2);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        timePicker.setIs24HourView(true);
        if (getActivity() instanceof MainActivity) {
            button2.setText("创建");
            button.setVisibility(View.GONE);
            timePicker.setVisibility(View.GONE);
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PlanItem planItem = new PlanItem();
                    planItem.setPlanName(editText.getText().toString());
                    planItem.setIntension(seekBar.getProgress());
                    planItem.setTimingCheck(checkBox.isChecked());
                    planItem.setTimeHour(timePicker.getCurrentHour());
                    planItem.setTimeMinute(timePicker.getCurrentMinute());
                    planItem.setFinish(false);
                    planItem.save();
                    MainActivity activity = (MainActivity) getActivity();
                    activity.drawerLayout.closeDrawers();
                    activity.initPlan();
                    activity.stopRemind();
                    activity.openRemind();
                }
            });
        } else if(getActivity() instanceof NewPlanActivity){
            final String planName;
            planName=getActivity().getIntent().getStringExtra("plan_name");
            List<PlanItem> planItems= DataSupport.where("planName=?",planName).find(PlanItem.class);
            for (PlanItem planItem:planItems){
                textView.setText("完成"+String.valueOf(planItem.getDay())+"天");
                seekBar.setProgress(planItem.getIntension());
                checkBox.setChecked(planItem.isTimingCheck());
                timePicker.setCurrentHour(planItem.getTimeHour());
                timePicker.setCurrentMinute(planItem.getTimeMinute());
                if (checkBox.isChecked()) {
                    timePicker.setVisibility(View.VISIBLE);
                } else {
                    timePicker.setVisibility(View.GONE);
                }
            }

            editText.setText(planName);
            button.setText("删除");
            button2.setText("更改");
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()&&seekBar.getProgress()!=0) {
                        PlanItem planItem = new PlanItem();
                        planItem.setPlanName(editText.getText().toString());
                        planItem.setIntension(seekBar.getProgress());
                        planItem.setTimingCheck(checkBox.isChecked());
                        planItem.setTimeHour(timePicker.getCurrentHour());
                        planItem.setTimeMinute(timePicker.getCurrentMinute());
                        planItem.updateAll("planName=?", planName);
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        restart();
                    }else if (!(checkBox.isChecked())&&seekBar.getProgress()!=0){
                        PlanItem planItem = new PlanItem();
                        planItem.setPlanName(editText.getText().toString());
                        planItem.setIntension(seekBar.getProgress());
                        planItem.setToDefault("timingCheck");
                        planItem.setTimeHour(timePicker.getCurrentHour());
                        planItem.setTimeMinute(timePicker.getCurrentMinute());
                        planItem.updateAll("planName=?", planName);
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        restart();
                    }else if (!(checkBox.isChecked())&&seekBar.getProgress()==0) {
                        PlanItem planItem = new PlanItem();
                        planItem.setPlanName(editText.getText().toString());
                        planItem.setToDefault("intension");
                        planItem.setToDefault("timingCheck");
                        planItem.setTimeHour(timePicker.getCurrentHour());
                        planItem.setTimeMinute(timePicker.getCurrentMinute());
                        planItem.updateAll("planName=?", planName);
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        restart();
                    }else if (checkBox.isChecked()&&seekBar.getProgress()==0) {
                        PlanItem planItem = new PlanItem();
                        planItem.setPlanName(editText.getText().toString());
                        planItem.setToDefault("intension");
                        planItem.setTimingCheck(checkBox.isChecked());
                        planItem.setTimeHour(timePicker.getCurrentHour());
                        planItem.setTimeMinute(timePicker.getCurrentMinute());
                        planItem.updateAll("planName=?", planName);
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        restart();
                    }
                }
            });
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataSupport.deleteAll(PlanItem.class,"planName=?",planName);
                    Intent intent=new Intent(getActivity(),MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                    restart();

                }
            });
        }
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    textView2.setText(String.valueOf(seekBar.getProgress() * 5));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        timePicker.setVisibility(View.VISIBLE);
                    } else {
                        timePicker.setVisibility(View.GONE);
                    }
                }
            });

    }
    public void restart(){
        NewPlanActivity activity = (NewPlanActivity) getActivity();
        activity.stopRemind();
        activity.openRemind();
    }

}
