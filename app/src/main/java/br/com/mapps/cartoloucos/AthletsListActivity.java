package br.com.mapps.cartoloucos;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.mapps.cartoloucos.business.Athlet;
import br.com.mapps.cartoloucos.business.Team;

public class AthletsListActivity extends AppCompatActivity {

    private int mTeamId = -1;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mListView;
    private AthletsListAdapter mListAdapter;
    private ArrayList<Athlet> mAthletsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_athlets_list);

        mSwipeRefreshLayout = (SwipeRefreshLayout) this.findViewById(R.id.swiperefresh_athlets);
        mListView = (ListView) this.findViewById(android.R.id.list);

        mAthletsList = new ArrayList<Athlet>();
        mListAdapter = new AthletsListAdapter(this, mAthletsList);
        mListView.setAdapter(mListAdapter);

        //TODO: passar o Team já pronto para esta atividade
        mTeamId = getIntent().getIntExtra("teamId", -1);
        ArrayList<Athlet> a = (ArrayList<Athlet>)getIntent().getSerializableExtra("athlets");

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
        mListAdapter.clear();
        requestTeamById(mTeamId);
    }

    /*
    * Action that is common to either success of failure on a request
    * */
    private void onResponseCompleted() {
        mSwipeRefreshLayout.setRefreshing(false);
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
                        mAthletsList.addAll(parseAtlhets(response));
                        mListAdapter.notifyDataSetChanged();
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

    /*
    * Parse athlets from the webservice response
    * */
    private ArrayList<Athlet> parseAtlhets(JSONObject response) {
        Team team = new Team();
        ArrayList<Athlet> athlets = new ArrayList<Athlet>();

        try {
//            //Get "time" from response
//            JSONObject jsonTeam = response.getJSONObject("time");
//            if (!response.isNull("pontos")) {
//                team.setPontos(response.getDouble("pontos"));
//            }

//            //Get details from "time"
//            team.setNome(jsonTeam.getString("nome"));
//            team.setNomeCartola(jsonTeam.getString("nome_cartola"));
//            team.setUrlEscudoPng(jsonTeam.getString("url_escudo_png"));

            // Get athlets
            if (!response.isNull("atletas")) {
                JSONArray jsonAthlets = response.getJSONArray("atletas");

                for (int i = 0; i < jsonAthlets.length(); i++) {
                    athlets.add(new Athlet(jsonAthlets.getJSONObject(i)));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return athlets;
    }
}
