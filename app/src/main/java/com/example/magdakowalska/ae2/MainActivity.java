package com.example.magdakowalska.ae2;

import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import java.io.*;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView myTextView = (TextView) findViewById(R.id.myText1);
        ImageButton playButtonView = (ImageButton) findViewById(R.id.playButton);
        ImageButton pauseButtonView = (ImageButton) findViewById(R.id.pauseButton);
        ImageButton previousButtonView = (ImageButton) findViewById(R.id.previousButton);
        ImageButton nextButtonView = (ImageButton) findViewById(R.id.nextButton);
        MediaPlayer myMediaPlayer = new MediaPlayer();
        FilenameFilter myFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if(filename.lastIndexOf('.')>0)
                {
                    // get last index for '.' char
                    int lastIndex = filename.lastIndexOf('.');
                    // get extension
                    String str = filename.substring(lastIndex);
                    // match path name extension
                    if(str.equals(".mp3"))
                    {
                        return true;
                    }
                }
                return false;
            }

            public File[] listAvailableMP3s(){
                File f = new File("/sdcard/media");
                File[] listFiles = f.listFiles();
                File[] files = {};
                int i = 0;
                for(File file: listFiles){
                    if(file.isFile()) {
                        files[i] = file;
                        i++;
                    }
                }
                return files;
            }

            public String[] listFileNames(File[] f){
                f = listAvailableMP3s();
                String[] songs = {};
                int i = 0;
                for(File file:f){
                    if(accept(file, file.toString())) {
                        songs[i] = file.toString();
                        i++;
                    }
                }
                return songs;
            }
        };




        pauseButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myTextView.setText("Music paused");
            }
        });

        playButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myTextView.setText("Music playing");
            }
        });

        previousButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myTextView.setText("Previous song");
            }
        });

        nextButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myTextView.setText("Next song");
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
