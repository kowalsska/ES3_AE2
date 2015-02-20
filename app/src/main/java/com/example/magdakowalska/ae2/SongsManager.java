package com.example.magdakowalska.ae2;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;

/**
 * Created by magdakowalska on 19/02/15.
 */
public class SongsManager {

    final String MEDIA_PATH = "/storage/sdcard0/Music/Inne";
    File dir = new File(MEDIA_PATH);
    private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

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

    //Constructor
    public SongsManager(){}

    public ArrayList<HashMap<String,String>> getPlayList() {
        if(files.length != 0) for (File file : files) {
            HashMap<String, String> song = new HashMap<String, String>();
            song.put("songTitle", file.getName().substring(0, (file.getName().length() - 4)));
            song.put("songPath", file.getPath());
            songsList.add(song);
        }
        return songsList;
    }
}
