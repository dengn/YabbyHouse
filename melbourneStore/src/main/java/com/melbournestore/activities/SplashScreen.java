package com.melbournestore.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.melbournestore.db.SharedPreferenceUtils;

public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FrameLayout mainFrame = ((FrameLayout) findViewById(R.id.frame_splash));
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this,
                R.anim.hyperspace_jump);
        mainFrame.startAnimation(hyperspaceJumpAnimation);

        new Handler().postDelayed(new Runnable() {
 
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                if(SharedPreferenceUtils.getFirstTimeLaunch(SplashScreen.this)){
                    Intent i = new Intent(SplashScreen.this, StarterActivity.class);
                    startActivity(i);
                }
                else{
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);
                }


                // close this activity
                finish();
                overridePendingTransition(0, 0);
                //Details.this.overridePendingTransition(R.anim.nothing,R.anim.nothing);
            }
        }, SPLASH_TIME_OUT);
    }

}
