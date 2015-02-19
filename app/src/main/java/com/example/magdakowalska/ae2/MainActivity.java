package com.example.magdakowalska.ae2;

import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import java.io.*;


public class MainActivity extends ActionBarActivity {

    public void playSong(int songIndex){


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView myTextView = (TextView) findViewById(R.id.myText1);
        ImageButton playButtonView = (ImageButton) findViewById(R.id.playButton);
        ImageButton pauseButtonView = (ImageButton) findViewById(R.id.pauseButton);
        ImageButton previousButtonView = (ImageButton) findViewById(R.id.previousButton);
        ImageButton nextButtonView = (ImageButton) findViewById(R.id.nextButton);
        final MediaPlayer player = new MediaPlayer();
        String path = "/storage/sdcard/raw";
        File dir = new File(path);

        File[] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File fileDirectory, String filename) {
                    if(filename.endsWith(".mp3"))
                    {
                        return true;
                    }
                return false;
            }
        });

        String[] songTitles = new String[files.length];

        for(int i = 0; i < files.length; i++){
            if(files[i].isFile()){
                songTitles[i] = files[i].getName();
                System.out.println(songTitles[i]);
            }
        }




        pauseButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pauseButtonView) {
                myTextView.setText("Music paused");
                player.pause();
                //isPlaying = !isPlaying;
            }
        });

        playButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myTextView.setText("Music playing");
                player.start();
            }
        });

        previousButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myTextView.setText("Previous song");
                //currentTrackNumber -= 1;
            }
        });

        nextButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myTextView.setText("Next song");
                //currentTrackNumber += 1;
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
