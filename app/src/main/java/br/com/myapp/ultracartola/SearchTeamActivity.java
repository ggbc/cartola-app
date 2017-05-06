package br.com.myapp.ultracartola;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import br.com.myapp.ultracartola.business.Team;
import br.com.myapp.ultracartola.common.Common;

public class SearchTeamActivity extends AppCompatActivity
        implements TeamSearchAdapter.IOnTeamCheckedListener {

    public final static String ARG_IDS_LIST = "ids_list";
    private final static int TYPING_DELAY = 500;
    private Set<Integer> mTeamIds;
    private TeamSearchAdapter mSelectableTeamListAdapter;
    private ArrayList<Team> mTeamList; //TODO: Mudar para ArrayList<SelectableTeam>

    private String mQueryString;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_team);

        mHandler = new Handler();
        SearchView searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                doSearch(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mQueryString = s;
                mHandler.removeCallbacksAndMessages(null);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doSearch(mQueryString);
                    }
                }, TYPING_DELAY);
                return true;
            }
        });

        // Get the Ids from the list sent by MainActivity
        mTeamIds = new LinkedHashSet<>(getIntent().getIntegerArrayListExtra(ARG_IDS_LIST));

        // Creates and sets the adapter
        mTeamList = new ArrayList<Team>();
        mSelectableTeamListAdapter = new TeamSearchAdapter(this,
                R.layout.activity_search_team_item, mTeamList);
        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(mSelectableTeamListAdapter);

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                // When clicked, show a toast with the TextView text
//                Team team = (Team) parent.getItemAtPosition(position);
//                Toast.makeText(getApplicationContext(),
//                        "Clicked on Row: " + team.getNome(),
//                        Toast.LENGTH_LONG).show();
//            }
//        });

//        Button buttonSave = (Button) findViewById(R.id.buttonSave);
//        buttonSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                saveTeamsIdsToDisk(mTeamIds);
//            }
//        });
    }

    /*
    *   Save the ids of the selected teams to disk
    * */
    private void saveTeamsIdsToDisk(Set<Integer> teamsIds) {
        try {
            Common.saveTeamsIdsToDisk(this, teamsIds);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    * Calls the webservice and fills the list with results
    * */
    private void doSearch(String query) {
        mSelectableTeamListAdapter.clear();
        requestTeamByNameOrOwner(query);
    }

    /**
     * Calls webservice to get basic team and user info
     */
    private void requestTeamByNameOrOwner(String queryString) {
        String url = "https://api.cartolafc.globo.com/times?q=" + queryString;
        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        mTeamList.addAll(Common.WebServices.parseSearchResult(response, mTeamIds));
                        mSelectableTeamListAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //TODO algo quando der erro?
                    }
                });
        RequestQueueSingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }

    /**
     * Updates the selected teams list
     */
    @Override
    public void onTeamChecked(Team team) {
        if (team.isChecked()) {
            mTeamIds.add(team.getTimeId());
            Toast.makeText(this, R.string.team_added, Toast.LENGTH_SHORT).show();
        } else {
            mTeamIds.remove(team.getTimeId());
            Toast.makeText(this,R.string.team_removed, Toast.LENGTH_SHORT).show();
        }
        saveTeamsIdsToDisk(mTeamIds);
    }
}
