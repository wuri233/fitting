package com.example.fitting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fitting.db.PlanItem;
import com.example.fitting.service.AutoRemindService;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Plan> planList=new ArrayList<>();
    private List<PlanItem> planItemList;
    private PlanAdapter adapter;
    public DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPlan();


        adapter=new PlanAdapter(MainActivity.this,R.layout.plan_item,planList);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout) ;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final  String nowDate = sDateFormat.format(new java.util.Date());

        ListView listView=(ListView)findViewById(R.id.plan_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox =(CheckBox)view.findViewById(R.id.checkBox);
                if (!checkBox.isChecked()) {
                    Plan plan = planList.get(position);
                    int addDay;
                    List<PlanItem> planItems= DataSupport.where("planName=?",plan.getName()).find(PlanItem.class);
                    for (PlanItem planItem2:planItems){
                        addDay=planItem2.getDay();
                        PlanItem planItem = new PlanItem();
                        planItem.setFinish(true);
                        planItem.setDay(addDay+1);
                        planItem.updateAll("planName=?", plan.getName());
                        Toast.makeText(MainActivity.this, "继续坚持!完成" + planItem.getDay()+"天"+nowDate, Toast.LENGTH_SHORT).show();
                    }
                    checkBox.setChecked(true);
                    SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                    editor.putString("date",nowDate);
                    editor.apply();
                }else {
                    Plan plan = planList.get(position);
                    Intent intent=new Intent(MainActivity.this,NewPlanActivity.class);
                    intent.putExtra("plan_name",plan.getName());
                    startActivity(intent);
                    finish();
                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Plan plan = planList.get(position);
                Snackbar.make(view,plan.getName()+"  Delete?",Snackbar.LENGTH_LONG).setAction("Yes", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DataSupport.deleteAll(PlanItem.class,"planName=?",plan.getName());
                        initPlan();
                        stopRemind();
                        openRemind();
                    }
                }).show();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void initPlan(){
        clearFinish();
        openRemind();
        try {
            planList.clear();
            planItemList=DataSupport.findAll(PlanItem.class);
            for (PlanItem planItem:planItemList){
                Plan i=new Plan(planItem.getPlanName(),planItem.isFinish());
                planList.add(i);
            }
            adapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
  public void clearFinish(){
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final  String nowDate = sDateFormat.format(new java.util.Date());
        SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
        String date=prefs.getString("date","");
        if (date.equals(nowDate)){
            Toast.makeText(this, "今天加油", Toast.LENGTH_SHORT).show();
        }else {
            PlanItem planItem = new PlanItem();
            planItem.setToDefault("finish");
            planItem.updateAll();
            Toast.makeText(this, "新的一天", Toast.LENGTH_SHORT).show();

        }

    }


    public void openRemind(){
     Intent intent=new Intent(this, AutoRemindService.class);
     startService(intent);
 }
 public void stopRemind(){
     Intent intent=new Intent(this, AutoRemindService.class);
     stopService(intent);
 }

}
