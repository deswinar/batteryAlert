package com.ar.wins.firebasetraining;

import android.widget.Switch;

/**
 * Created by USER on 12/08/2018.
 */

public class AlertData {

    String name, desc;
    Boolean toggle;

    public AlertData(String name, String desc, Boolean toggle){
        this.name = name;
        this.desc = desc;
        this.toggle = toggle;
    }

    public String getName(){
        return name;
    }

    public String getDesc(){
        return desc;
    }

    public Boolean getToggle(){
        return toggle;
    }
}
