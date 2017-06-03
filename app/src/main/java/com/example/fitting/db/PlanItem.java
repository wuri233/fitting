package com.example.fitting.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/5/25.
 */

public class PlanItem extends DataSupport {

    private int planId;
    private int day;
    private String planName;
    private int intension;
    private boolean timingCheck;
    private int timeHour;
    private int timeMinute;
    private boolean finish;

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public int getIntension() {
        return intension;
    }

    public void setIntension(int intension) {
        this.intension = intension;
    }

    public boolean isTimingCheck() {
        return timingCheck;
    }

    public void setTimingCheck(boolean timingCheck) {
        this.timingCheck = timingCheck;
    }

    public int getTimeHour() {
        return timeHour;
    }

    public void setTimeHour(int timeHour) {
        this.timeHour = timeHour;
    }

    public int getTimeMinute() {
        return timeMinute;
    }

    public void setTimeMinute(int timeMinute) {
        this.timeMinute = timeMinute;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }
}
