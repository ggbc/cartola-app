package br.com.myapp.ultracartola;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import br.com.myapp.ultracartola.business.Team;


public class TeamListFragment extends Fragment {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mListView;

    private TeamListAdapter mTeamListAdapter;
    private ArrayList<Team> mTeamList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Notify the system to allow an options menu for this fragment.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_list, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);

        mListView = (ListView) view.findViewById(android.R.id.list);

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTeamList = new ArrayList<Team>();
        setTeamList();
        mTeamListAdapter = new TeamListAdapter(getActivity(), mTeamList);
        mListView.setAdapter(mTeamListAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initiateRefresh();
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:

                // We make sure that the SwipeRefreshLayout is displaying it's refreshing indicator
                if (!mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(true);
                }

                // Start our refresh background task
                initiateRefresh();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * By abstracting the refresh process to a single method, the app allows both the
     * SwipeGestureLayout onRefresh() method and the Refresh action item to refresh the content.
     */
    private void initiateRefresh() {

//        setTeamList();

        new DummyBackgroundTask().execute();
        //TODO: implementar aqui os requests no lugar da AsyncTask
    }

    /*
    *   TODO: Obrigatório manter isso. Vai representar o fim da atualização da lista toda.
    * */
    private void onRefreshComplete(Void result) {
        mTeamListAdapter.clear();
        mSwipeRefreshLayout.setRefreshing(false);
    }


    private ArrayList<Integer> getTeamsIdsFromDisk() {
        //A LinkedHashSet does not allow duplicates and gets the data in the order it was saved
        Set<Integer> ids = new LinkedHashSet<>();

        // Gets the file from the /res/raw directory
        InputStream is = getActivity()
                .getApplicationContext()
                .getResources()
                .openRawResource(R.raw.teams);

        // Then read it
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String teamId;
            while ((teamId = br.readLine()) != null) {
                int id = Integer.valueOf(teamId);
                ids.add(id);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<Integer>(ids);
    }

    /*
    * Fetches Teams from webservice and adds them to mTeamList
    * */
    private void setTeamList() {
        //Gets the list of teams by id from disk
        ArrayList<Integer> ids = getTeamsIdsFromDisk();

        // And requests details from the webservice
        for (Integer i : ids) {
            requestTeamById(i);
        }
    }

    private class DummyBackgroundTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            setTeamList();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // Tell the Fragment that the refresh has completed
            onRefreshComplete(result);
        }
    }

    /**
     * Calls webservice https://api.cartolafc.globo.com/time/id/[?] to get team details
     */
    private void requestTeamById(int id) {
        String url = "https://api.cartolafc.globo.com/time/id/" + id;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        mTeamList.add(parseTeam(response));
                        mTeamListAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                    }
                });
        RequestQueueSingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest);
    }

    //TODO: criar classe Common.Parser
    private Team parseTeam(JSONObject response){
        Team team = new Team();

        try {

            //Get "time" from response
            JSONObject jsonTeam = response.getJSONObject("time");
            if (!response.isNull("pontos")) {
                team.setPontos(response.getDouble("pontos"));
            }

            //Get details from "time"
            team.setTimeId(jsonTeam.getInt("time_id"));
            team.setNome(jsonTeam.getString("nome"));
            team.setNomeCartola(jsonTeam.getString("nome_cartola"));
            team.setUrlEscudoPng(jsonTeam.getString("url_escudo_png"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return team;
    }
}