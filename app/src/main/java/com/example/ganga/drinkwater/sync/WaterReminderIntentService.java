package com.example.ganga.drinkwater.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by ganga on 5/4/18.
 */

public class WaterReminderIntentService extends IntentService {

    public WaterReminderIntentService() {
        super("WaterReminderIntentService");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        String action = intent.getAction();

        ReminderTasks.executeTask(this, action);

    }
}
