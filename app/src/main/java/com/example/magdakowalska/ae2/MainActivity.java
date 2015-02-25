package com.example.magdakowalska.ae2;

import android.media.MediaPlayer;
import android.os.Bundle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements OnCompletionListener, SeekBar.OnSeekBarChangeListener{

    private TextView songTitleLabel;
    private TextView songCurrentDurationLabel;
    private TextView songTotalDurationLabel;

    private ImageButton playButton;
    private ImageButton previousButton;
    private ImageButton nextButton;
    private ImageButton backButton;
    private ImageButton forwardButton;
    private SeekBar songProgressBar;
    private ImageButton playlistButton;
    private ImageButton shuffleButton;
    private ImageButton repeatButton;
    private Handler mHandler = new Handler();
    private MediaPlayer player;
    private SongsManager manager;
    private CalculationsActivity calculations;
    private int seekForwardTime = 5000; // 5000 milliseconds
    private int seekBackwardTime = 5000; // 5000 milliseconds
    private int currentSongIndex = 0;
    private boolean isShuffle = false;
    private boolean isRepeat = false;
    private boolean isPlaying = false;
    public Intent serviceIntent;

    private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serviceIntent = new Intent(this, MusicService.class);

        startService(serviceIntent);

        //Buttons and other fields

        songTitleLabel = (TextView) findViewById(R.id.songTitleView);
        songCurrentDurationLabel = (TextView) findViewById(R.id.currentDuration);
        songTotalDurationLabel = (TextView) findViewById(R.id.totalDuration);

        playButton = (ImageButton) findViewById(R.id.playButton);

        previousButton = (ImageButton) findViewById(R.id.previousButton);
        nextButton = (ImageButton) findViewById(R.id.nextButton);
        backButton = (ImageButton) findViewById(R.id.backButton);
        forwardButton = (ImageButton) findViewById(R.id.forwardButton);
        songProgressBar = (SeekBar) findViewById(R.id.seekBar);
        playlistButton = (ImageButton) findViewById(R.id.playlistButton);
        shuffleButton = (ImageButton) findViewById(R.id.shuffleButton);
        repeatButton = (ImageButton) findViewById(R.id.repeatButton);

        player = new MediaPlayer();
        manager = new SongsManager();
        calculations = new CalculationsActivity();

        // Listeners

        songProgressBar.setOnSeekBarChangeListener(this);
        player.setOnCompletionListener(this);

        // Getting songs list
        songsList = manager.getPlayList();

        //By default play first song
        playSong(currentSongIndex);


        /**
         * Buttons on-click listeners
         */

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check for already playing
                if(isPlaying){
                    if(player!=null){
                        player.pause();
                        // Changing button image to play button
                        playButton.setBackgroundResource(R.drawable.play);
                        isPlaying = false;

                    }
                }else{
                    // Resume song
                    if(player!=null){
                        player.start();
                        // Changing button image to pause button
                        playButton.setBackgroundResource(R.drawable.pause);
                        isPlaying = true;
                    }
                }

            }

        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentSongIndex > 0){
                    playSong(currentSongIndex - 1);
                    currentSongIndex = currentSongIndex - 1;
                }else{
                    // play last song
                    playSong(songsList.size() - 1);
                    currentSongIndex = songsList.size() - 1;
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentSongIndex < (songsList.size() - 1)){
                    if(isShuffle){
                        Random rand = new Random();
                        currentSongIndex = rand.nextInt((songsList.size() - 1) - 0 + 1) + 0;
                        playSong(currentSongIndex);
                    }else {
                        playSong(currentSongIndex + 1);
                        currentSongIndex += 1;
                    }
                }else{
                    //Play first song from the playlist
                    playSong(0);
                    currentSongIndex = 0;
                }

            }
        });

        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get current song position
                int currentPosition = player.getCurrentPosition();
                // check if seekForward time is lesser than song duration
                if(currentPosition + seekForwardTime <= player.getDuration()){
                    // forward song
                    player.seekTo(currentPosition + seekForwardTime);
                }else{
                    // forward to end position
                    player.seekTo(player.getDuration());
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get current song position
                int currentPosition = player.getCurrentPosition();
                // check if seekBackward time is greater than 0 sec
                if(currentPosition - seekBackwardTime >= 0){
                    // forward song
                    player.seekTo(currentPosition - seekBackwardTime);
                }else{
                    // backward to starting position
                    player.seekTo(0);
                }

            }
        });

        playlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent i = new Intent(getApplicationContext(), PlayListActivity.class);
                startActivityForResult(i, 100);
            }
        });

        shuffleButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(isShuffle){
                    isShuffle = false;
                    shuffleButton.setBackgroundResource(R.drawable.shuffle);
                    Toast.makeText(getApplicationContext(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();
                }else{
                    isShuffle = true;
                    shuffleButton.setBackgroundResource(R.drawable.shuffleon);
                    Toast.makeText(getApplicationContext(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
                }
            }
        });

        repeatButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(isRepeat){
                    isRepeat = false;
                    repeatButton.setBackgroundResource(R.drawable.repeat);
                    Toast.makeText(getApplicationContext(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
                }else{
                    isRepeat= true;
                    repeatButton.setBackgroundResource(R.drawable.repeaton);
                    Toast.makeText(getApplicationContext(), "Repeat is ON", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        if(player != null && !isPlaying)
            player.start();
            playButton.setBackgroundResource(R.drawable.pause);
            startService(serviceIntent);
        super.onResume();
    }

    //What happens when songs gets to its end
    @Override
    public void onCompletion(MediaPlayer player) {
        // check for repeat is ON or OFF
        if(isRepeat){
            // repeat is on play same song again
            playSong(currentSongIndex);
        } else if(isShuffle){
            // shuffle is on - play a random song
            Random rand = new Random();
            currentSongIndex = rand.nextInt((songsList.size() - 1) - 0 + 1) + 0;
            playSong(currentSongIndex);
        } else{
            // no repeat or shuffle ON - play next song
            if(currentSongIndex < (songsList.size() - 1)){
                playSong(currentSongIndex + 1);
                currentSongIndex = currentSongIndex + 1;
            }else{
                // play first song
                playSong(0);
                currentSongIndex = 0;
            }
        }

    }

    private void playSong(int songIndex) {
        try {
            player.reset();
            player.setDataSource(songsList.get(songIndex).get("songPath"));
            player.prepare();
            player.start();
            isPlaying = true;

            // Displaying Song title
            String songTitle = songsList.get(songIndex).get("songTitle");
            songTitleLabel.setText(songTitle);

            // Changing Button Image to pause image
            playButton.setBackgroundResource(R.drawable.pause);

            // set Progress bar values
            songProgressBar.setProgress(0);
            songProgressBar.setMax(100);

            // Updating progress bar
            updateProgressBar();

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = player.getDuration();
            long currentDuration = player.getCurrentPosition();

            // Displaying Total Duration time
            songTotalDurationLabel.setText("" + calculations.milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            songCurrentDurationLabel.setText("" + calculations.milliSecondsToTimer(currentDuration));

            // Updating progress bar
            int progress = (int)(calculations.getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            songProgressBar.setProgress(progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 100){
            currentSongIndex = data.getExtras().getInt("songIndex");
            // play selected song
            playSong(currentSongIndex);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {}

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = player.getDuration();
        int currentPosition = calculations.progressToTimer(seekBar.getProgress(), totalDuration);

        // Forward or backward to certain seconds
        player.seekTo(currentPosition);

        // Update timer progress again
        updateProgressBar();
    }

    @Override
    public void onStop(){
        super.onStop();
        if(!isPlaying){
            super.onDestroy();
            stopService(serviceIntent);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        stopService(serviceIntent);
    }

}