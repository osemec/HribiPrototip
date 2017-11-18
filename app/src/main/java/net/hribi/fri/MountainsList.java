package net.hribi.fri;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import net.hribi.fri.Adapter.MountainsListAdapter;

public class MountainsList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mountains);

        int pos = getIntent().getIntExtra("pos", -1);
        String title = getIntent().getStringExtra("title");

        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        tb.setTitle(title);
        tb.setTitleTextColor(0xffffffff);

        RecyclerView rv = (RecyclerView) findViewById(R.id.mountains);
        MountainsListAdapter mla = new MountainsListAdapter(getBaseContext(),pos);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv.setAdapter(mla);
    }
}
