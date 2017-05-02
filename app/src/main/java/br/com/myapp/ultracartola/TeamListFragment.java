package br.com.myapp.ultracartola;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import br.com.myapp.ultracartola.business.Team;
import br.com.myapp.ultracartola.common.Common;


public class TeamListFragment extends Fragment implements ListView.OnItemClickListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mListView;

    private TeamListAdapter mTeamListAdapter;
    private ArrayList<Team> mTeamList;
    private static Integer mResponseCounter = 0;
    private ArrayList<Integer> mTeamIds;

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
        mListView.setOnItemClickListener(this);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTeamIds = new ArrayList<Integer>();
        mTeamList = new ArrayList<Team>();
        mTeamListAdapter = new TeamListAdapter(getActivity(), mTeamList);
        mListView.setAdapter(mTeamListAdapter);

        mSwipeRefreshLayout.setRefreshing(true);
        initiateRefresh();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initiateRefresh();
            }
        });
    }

    private void initiateRefresh() {
        mTeamListAdapter.clear();
        setTeamList();
    }

    /*
    *  The teams selected by the user must be retrieved from disk
    * */
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
        mResponseCounter = 0;

        //Gets the list of teams by id from disk
        mTeamIds = getTeamsIdsFromDisk();

        // And requests details from the webservice
        for (Integer i : mTeamIds) {
            requestTeamById(i);
        }
    }

    /*
    * Since there are several calls to the webservice, the right moment to setRefreshing to false
    * is when all the HttpResponses arrived.
    * */
    private void onResponseCompleted() {
        mResponseCounter++;
        if (mResponseCounter == mTeamIds.size()) {
            mSwipeRefreshLayout.setRefreshing(false);
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
                        mTeamList.add(Common.parseTeam(response));
                        mTeamListAdapter.notifyDataSetChanged();
                        onResponseCompleted();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onResponseCompleted();
                    }
                });
        RequestQueueSingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest);
    }


//    private Team parseTeam(JSONObject response) {
//        Team team = new Team();
//
//        try {
//
//            //Get "time" from response
//            JSONObject jsonTeam = response.getJSONObject("time");
//            if (!response.isNull("pontos")) {
//                team.setPontos(response.getDouble("pontos"));
//            }
//
//            //Get details from "time"
//            team.setTimeId(jsonTeam.getInt("time_id"));
//            team.setNome(jsonTeam.getString("nome"));
//            team.setNomeCartola(jsonTeam.getString("nome_cartola"));
//            team.setUrlEscudoPng(jsonTeam.getString("url_escudo_png"));
//
//
//            // Get athlets
//            if (!response.isNull("atletas")) {
//                JSONArray jsonAthlets = response.getJSONArray("atletas");
//
//                ArrayList<Athlet> athlets = new ArrayList<Athlet>();
//                for (int i = 0; i < jsonAthlets.length(); i++) {
//                    athlets.add(new Athlet(jsonAthlets.getJSONObject(i)));
//                }
//                team.setAtletas(athlets);
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        return team;
//    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(getContext(), AthletsListActivity.class);

        Team t = mTeamListAdapter.getItem(position);

//        if (t.getAtletas() == null) {
//            Context context = this.getContext();
//            CharSequence text = "Nenhum atleta em campo. O mercado já fechou?";
//            int duration = Toast.LENGTH_SHORT;
//
//            Toast toast = Toast.makeText(context, text, duration);
//            toast.show();
//            return;
//        }

        intent.putExtra("teamId", t.getTimeId());
        intent.putExtra("athlets", t.getAtletas());
        startActivity(intent);
    }
}
