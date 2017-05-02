package br.com.myapp.ultracartola;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import br.com.myapp.ultracartola.business.Team;

/**
 * Created by Gustavo on 29/04/2017.
 */

public class TeamListAdapter extends ArrayAdapter<Team> {

    private FragmentActivity activity;
    private ArrayList<Team> listItems;

    public TeamListAdapter(FragmentActivity activity, ArrayList<Team> list) {
        super(activity, android.R.layout.simple_list_item_1, list);
        this.activity = activity;
        this.listItems = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.fragment_team_list_item,
                    parent, false);
        }

        Team team = (Team) getItem(position);

//        Glide.with(activity).load(team.getUrlEscudoPng()).into((ImageView) convertView.findViewById(R.id.teamBadge));
        ((NetworkImageView) convertView.findViewById(R.id.teamBadge))
                .setImageUrl(
                        team.getUrlEscudoPng(),
                        RequestQueueSingleton
                                .getInstance(convertView.getContext())
                                .getImageLoader()
                        );
        ((TextView) convertView.findViewById(R.id.teamName)).setText(team.getNome());
        ((TextView) convertView.findViewById(R.id.teamCartolaName)).setText(team.getNomeCartola());
        ((TextView) convertView.findViewById(R.id.teamPoints)).setText(Double.toString(team.getPontos()));

        return convertView;
    }
}
