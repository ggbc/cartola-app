package br.com.myapp.ultracartola;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import br.com.myapp.ultracartola.common.Common;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Integer> mTeamIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Gets the list of ids of the teams from disk
        mTeamIds = new ArrayList<>(Common.getTeamsIdsFromDisk(this));

        if (savedInstanceState == null) {
            TeamListFragment teamsFragment = new TeamListFragment();

            Bundle args = new Bundle();
            args.putIntegerArrayList(TeamListFragment.ARG_IDS_LIST, mTeamIds);
            teamsFragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_placeholder, teamsFragment);
            transaction.commit();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchTeamActivity.class);
                intent.putExtra(SearchTeamActivity.ARG_IDS_LIST, mTeamIds);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(br.com.myapp.ultracartola.R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_refresh) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
