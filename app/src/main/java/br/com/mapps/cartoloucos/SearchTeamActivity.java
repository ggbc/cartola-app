package br.com.mapps.cartoloucos;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
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

import br.com.mapps.cartoloucos.business.Team;
import br.com.mapps.cartoloucos.common.Common;

public class SearchTeamActivity extends AppCompatActivity
        implements TeamSearchAdapter.IOnTeamCheckedListener {


    private final static int TYPING_DELAY = 500;
    private Set<Integer> mTeamIds;
    private TeamSearchAdapter mSelectableTeamListAdapter;
    private ArrayList<Team> mTeamList; //TODO: Mudar para ArrayList<SelectableTeam>

    private String mQueryString;
    private Handler mHandler = new Handler();

    private ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_team);

//        mTeamIds = new LinkedHashSet<>(getIntent().getIntegerArrayListExtra(ARG_IDS_LIST));
        //Creates the list of Ids
        mTeamIds = new LinkedHashSet<>();

        // Creates and sets the adapter
        mTeamList = new ArrayList<Team>();
        mSelectableTeamListAdapter = new TeamSearchAdapter(this,
                R.layout.activity_search_team_item, mTeamList);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(mSelectableTeamListAdapter);

        mProgress = (ProgressBar) findViewById(R.id.progressBar);
        mProgress.setVisibility(View.INVISIBLE);

        SearchView searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setQueryHint(getResources().getString(R.string.search_hint));

        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();

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
    }


    @Override
    protected void onResume() {
        super.onResume();

        mTeamIds = Common.getTeamsIdsFromDisk(this);
    }

    /*
        *   Save the ids of the selected teams to disk
        * */
    private void saveTeamsIdsToDisk(Set<Integer> teamsIds) {
        //TODO: ver como se inclui um botão no Actionbar para salvar
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
        mProgress.setVisibility(View.VISIBLE);
        mSelectableTeamListAdapter.clear();
        requestTeamByNameOrOwner(query);
    }

    /*
    *
    * */
    private void onResponseCompleted() {
        mProgress.setVisibility(View.INVISIBLE);
        TextView textEmpty = (TextView) findViewById(R.id.textEmpty);
        if (mTeamList.isEmpty()) {
            textEmpty.setVisibility(View.VISIBLE);
        } else {
            textEmpty.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Calls webservice to get basic team and user info
     */
    private void requestTeamByNameOrOwner(String queryString) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(Common.WebServices.SCHEME)
                .authority(Common.WebServices.AUTHORITY)
                .appendPath(Common.WebServices.SEARCH_TEAMS_PATH)
                .appendQueryParameter(Common.WebServices.SEARCH_TEAMS_QUERY, queryString);
        String url = builder.build().toString();

        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        mTeamList.addAll(Common.WebServices.parseSearchResult(response, mTeamIds));
                        mSelectableTeamListAdapter.notifyDataSetChanged();
                        onResponseCompleted();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onResponseCompleted();
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
