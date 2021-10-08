package com.example.music;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.media.MediaParser;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    //goi bien
    TextView playerPosition,playDuration;
    SeekBar seekbar;
    ImageView btRew,btPlay,btPause,btFF;

    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //gan gia tri
        playDuration = findViewById(R.id.player_duration);
        playerPosition = findViewById(R.id.player_position);
        seekbar = findViewById(R.id.SeekBar);
        btRew = findViewById(R.id.bt_rew);
        btPlay = findViewById(R.id.bt_play);
        btPause = findViewById(R.id.bt_pause);
        btFF = findViewById(R.id.bt_ff);

        //khoi tao nhac
        mediaPlayer = MediaPlayer.create(this,R.raw.music);

        //khoi tao chay nhac
        runnable = new Runnable() {
            @Override
            public void run() {
                //cai thanh chay thoi gian
                seekbar.setProgress(mediaPlayer.getCurrentPosition());
                // Hanler chay tre 0.5 giay
                handler.postDelayed(this,500);
            }
        };
        // xac nhan thoi gian tu media
        int duration = mediaPlayer.getDuration();
        //Convert millisecond  to minute and second
        String sDuration = convertFomat(duration);
        //Set duration on tex view
        playDuration.setText(sDuration);

        btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Hide button play
                btPlay.setVisibility(View.GONE);
                //Show Pause button
                btPause.setVisibility(View.VISIBLE);
                // Star media player
                mediaPlayer.start();
                // Set max on seek bar
                seekbar.setMax(mediaPlayer.getDuration());
                //Star Handler
                handler.postDelayed(runnable,0);
            }
        });

        btPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Hide Pause button
                btPause.setVisibility(View.GONE);
                // Show star button
                btPlay.setVisibility(View.VISIBLE);
                //Pause media
                mediaPlayer.pause();
                //Stop Handler
                handler.removeCallbacks(runnable);
            }
        });

        btFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get current positiont of media player
                int current = mediaPlayer.getCurrentPosition();
                // Get duration of media player
                int duration = mediaPlayer.getDuration();
                //check condition
                if(mediaPlayer.isPlaying() && duration != current){
                    //when media is playing and duration is not equal to current position
                    //fast  forward for 5 second
                    current = current + 5000;
                    //set current position on text view
                    playerPosition.setText(convertFomat(current));
                    //Set progess on seek bar
                    mediaPlayer.seekTo(current);
                }
            }
        });

        btRew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get current position media player
                int currentPosition = mediaPlayer.getCurrentPosition();
                //check condition
                if(mediaPlayer.isPlaying() && currentPosition > 5000){
                    //when media is playing and current position in greater than 5s
                    //Rewind for 5s
                    currentPosition = currentPosition - 5000;
                    //Get current position
                    playerPosition.setText(convertFomat(currentPosition));
                    //Set progress on seek bar
                    mediaPlayer.seekTo(currentPosition);
                }
            }
        });
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //check condition
                if (b){
                    //when drag the seek bar
                    //Set progress on seek bar
                    mediaPlayer.seekTo(i);
                }
                //Set current position on text view
                playerPosition.setText(convertFomat(mediaPlayer.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                //Hide pause button
                btPause.setVisibility(View.GONE);
                //Show play button
                btPlay.setVisibility(View.VISIBLE);
                //Set media player to initial position
                mediaPlayer.seekTo(0);
            }
        });
    }
    @SuppressLint("DefaultLocate")
    private String convertFomat(int duration) {
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -  TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));

    }
}