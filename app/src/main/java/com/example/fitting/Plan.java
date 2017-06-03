package com.example.fitting;

/**
 * Created by Administrator on 2017/5/25.
 */

public class Plan {
    private String name;
    private boolean planCheck;

    public Plan(String name, boolean planCheck){
        this.name=name;
        this.planCheck=planCheck;
    }

    public String getName(){
        return name;
    }

    public boolean getPlanCheck(){
        return planCheck;
    }
}
