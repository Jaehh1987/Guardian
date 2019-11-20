package com.portfolio.guardian.Util;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import java.util.HashMap;
import java.util.Map;

public class DangerousTime {

    Context context;
    ArrayList<Crime> crimes;

    public DangerousTime(Context context, ArrayList<Crime> crimes) {
        this.context = context;
        this.crimes = crimes;
    }

    public void findTimeline() {
        String result = "";
        HashMap<String, Integer> timeCount = new HashMap<>();
        timeCount.put("late night", 0);
        timeCount.put("morning", 0);
        timeCount.put("day time", 0);
        timeCount.put("evening", 0);
        timeCount.put("night", 0);
        Calendar calendar = Calendar.getInstance();

        for (Crime c : crimes) {
            calendar.setTime(c.getDate());
            int hour = calendar.get(Calendar.HOUR);
            if ( 0 <= hour && hour < 6 ) {
                timeCount.put("late night", timeCount.get("late night") + 1);
            } else if ( 6 <= hour && hour < 10 ) {
                timeCount.put("morning", timeCount.get("morning") + 1);
            } else if ( 10 <= hour && hour < 16 ) {
                timeCount.put("day time", timeCount.get("day time") + 1);
            } else if ( 16 <= hour && hour < 20 ) {
                timeCount.put("evening", timeCount.get("evening") + 1);
            } else {
                timeCount.put("night", timeCount.get("night") + 1);
            }
        }

        int max = 0;

        for (Map.Entry<String, Integer> entry : timeCount.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                result = entry.getKey();
            }
        }

        String output = "Most crimes in your route have happened in " + result;
        output += ". Please be aware";

        Toast.makeText(context, output, Toast.LENGTH_LONG).show();
    }
}
