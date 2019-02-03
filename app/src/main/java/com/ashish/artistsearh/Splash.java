package com.ashish.artistsearh;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.WindowManager;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;

/**
 * Created by ashish.kumar on 11-07-2018.
 */

public class Splash extends Activity {
    private static int SPLASH_TIME_OUT = 3000;
AppController controller;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);
        controller=(AppController)getApplicationContext();
        switchToSearchScreen();
    }

    public void switchToSearchScreen() {
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                Intent in=null;
        if(controller.getManager().isUserLoggedIn())
        {
            in = new Intent(Splash.this, Search.class);
        }else {
             in =new Intent(Splash.this, Login.class);
        }


                // This method will be executed once the timer is over
                // Start your app main activity
                startActivity(in);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}
