package br.com.myapp.ultracartola;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Set;

import br.com.myapp.ultracartola.business.Team;
import br.com.myapp.ultracartola.common.Common;


public class TeamListFragment extends Fragment implements ListView.OnItemClickListener {

    //    public final static String ARG_IDS_LIST = "ids_list";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mListView;

    private TeamListAdapter mTeamListAdapter;
    private ArrayList<Team> mTeamList;
    private static Integer mResponseCounter = 0;
    private Set<Integer> mTeamIds;

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

//        Bundle args = getArguments();
//        if (args != null) {
//            mTeamIds = args.getIntegerArrayList(ARG_IDS_LIST);
//        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        mSwipeRefreshLayout.setRefreshing(true);
        initiateRefresh();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        if (savedInstanceState == null) {
//            mTeamIds = new ArrayList<Integer>();
//        }
        mTeamList = new ArrayList<Team>();
        mTeamListAdapter = new TeamListAdapter(getActivity(), mTeamList);
        mListView.setAdapter(mTeamListAdapter);

//        mSwipeRefreshLayout.setRefreshing(true);
//        initiateRefresh();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initiateRefresh();
            }
        });
    }

    /*
    * Clears the adapter and starts setTeamList
    * */
    private void initiateRefresh() {
        mTeamListAdapter.clear();
        setTeamList();
    }

    /*
    * Fetches Teams from webservice and adds them to mTeamList
    * */
    private void setTeamList() {
        mResponseCounter = 0;

        //Gets the list of teams by id from disk
        mTeamIds = Common.getTeamsIdsFromDisk((Context) getContext());
        if (mTeamIds.isEmpty()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        // And requests details from the webservice
        for (Integer i : mTeamIds) {
            requestTeamById(i);
        }
    }

    /*
    * Since there are several calls to the webservice, the right moment to setRefreshing to false
    * is when all the VolleyResponses arrived.
    * */
    private void onResponseCompleted() {
        mResponseCounter++;
        if (mResponseCounter == mTeamIds.size()) {
            mSwipeRefreshLayout.setRefreshing(false);
            Snackbar.make(getView(), mTeamList.size() + " times no total", Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * Calls webservice to get team details
     */
    private void requestTeamById(int id) {
        String url = "https://api.cartolafc.globo.com/time/id/" + id;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        mTeamList.add(Common.WebServices.parseTeam(response));
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

    /*
    * Callback to showing the details of a Team
    * */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(getContext(), AthletsListActivity.class);

        Team t = mTeamListAdapter.getItem(position);

        if (t.getAtletas().size() == 0) {
            Context context = this.getContext();
            CharSequence text = "O mercado ainda está aberto. \nNão é possível ver a escalação do time.";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }

        intent.putExtra("teamId", t.getTimeId());
        intent.putExtra("athlets", t.getAtletas());
        startActivity(intent);
    }
}
