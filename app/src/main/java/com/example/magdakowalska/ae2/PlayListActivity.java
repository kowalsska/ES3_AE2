package com.example.magdakowalska.ae2;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class PlayListActivity extends ListActivity {

    public ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);

        ArrayList<HashMap<String, String>> songsListData = new ArrayList<HashMap<String, String>>();

        SongsManager playlistManager = new SongsManager();
        this.songsList = playlistManager.getPlayList();

        // Looping through playlist
        for (int i = 0; i < songsList.size(); i++) {
            HashMap<String, String> song = songsList.get(i);
            songsListData.add(song);
        }

        // Adding menuItems to ListView
        ListAdapter adapter = new SimpleAdapter(this, songsListData,
                R.layout.single_list_item, new String[] { "songTitle" }, new int[] {R.id.songTitle });

        setListAdapter(adapter);

        // Selecting single ListView item
        ListView lv = getListView();
        // Listening to single item click
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Getting item index
                int songIndex = position;
                // Starting new intent
                Intent in = new Intent(getApplicationContext(), MainActivity.class);
                // Sending songIndex to PlayerActivity
                in.putExtra("songIndex", songIndex);
                setResult(100, in);
                // Closing PlayListView
                finish();
            }
        });
    }
}