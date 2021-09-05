/**
 * Name: Nathan Almeida,
 * Date Created: 05/05/2021,
 * Purpose: Display a loading screen with an Icon Animation and Dog Sound Effect
 * Code References: Hawaiian Music- Homework, MediaPlayer, Animations, and Timer
 */

package com.example.geodoge;

import androidx.appcompat.app.AppCompatActivity;import android.content.Intent;import android.media.MediaPlayer;import android.os.Bundle;import android.view.animation.Animation;import android.view.animation.AnimationUtils;import android.view.animation.LinearInterpolator;import android.view.animation.RotateAnimation;import android.widget.ImageView;import java.util.Timer;import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
    //Declare needed objects
    ImageView spinIcon;
    MediaPlayer playDog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //Find Image and MediaPlayer ID's
        spinIcon = findViewById(R.id.spinIcon);
        playDog = new MediaPlayer();
        MediaPlayer playDog = MediaPlayer.create(this, R.raw.barking);
        //Call rotate method to play animation
        rotate();
        //Check if dog sound is playing, play/start it | Referenced from the Hawaiian Music- Homework
        if(!playDog.isPlaying()) {
            playDog.start();
        }

        //Create a timer for the window to be displayed for 5 seconds | Referenced from the Hawaiian Music- Homework
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                //Close the window once the timer runs out and stop/reset the audio, go to a new Page
                finish();
                //Stop dog sound if it's playing during the end of the timer
                if(playDog.isPlaying()) {
                    playDog.stop();
                    playDog.reset();
                }
                startActivity(new Intent(SplashActivity.this, SignUpActivity.class));
            }
        };
        Timer opening = new Timer();
        opening.schedule(task, 5000);
    }

    //Rotate or animate the GeoDoge Icon | Referenced from the Animation- Class Exercise
    public void rotate() {
        spinIcon.startAnimation(AnimationUtils.loadAnimation(this,R.anim.rotate));
        RotateAnimation rotate = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(5000);
        rotate.setInterpolator(new LinearInterpolator());
        spinIcon.startAnimation(rotate);
    }
}
