package br.com.mapps.cartoloucos;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import br.com.mapps.cartoloucos.business.Team;

/**
 * Created by Gustavo on 29/04/2017.
 */

public class TeamListAdapter extends ArrayAdapter<Team> {

    private FragmentActivity activity;
    private ArrayList<Team> listItems;

    private class ViewHolder {
        NetworkImageView teamBadge;
        TextView teamName;
        TextView teamCartolaName;
        TextView teamPoints;
    }

    public TeamListAdapter(FragmentActivity activity, ArrayList<Team> list) {
        super(activity, android.R.layout.simple_list_item_1, list);
        this.listItems = list;
        this.activity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TeamListAdapter.ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.fragment_team_list_item, parent, false);

            holder = new TeamListAdapter.ViewHolder();
            holder.teamBadge = (NetworkImageView) convertView.findViewById(R.id.teamBadge);
            holder.teamName = (TextView) convertView.findViewById(R.id.teamName);
            holder.teamCartolaName = (TextView) convertView.findViewById(R.id.teamCartolaName);
            holder.teamPoints = (TextView) convertView.findViewById(R.id.teamPoints);
            convertView.setTag(holder);

        } else {
            holder = (TeamListAdapter.ViewHolder) convertView.getTag();
        }

        Team team = (Team) getItem(position);
        holder.teamBadge.setImageUrl(
                        team.getUrlEscudoPng(),
                        RequestQueueSingleton
                                .getInstance(convertView.getContext())
                                .getImageLoader()
                );
        holder.teamName.setText(team.getNome());
        holder.teamCartolaName.setText(team.getNomeCartola());
        holder.teamPoints.setText(Double.toString(team.getPontos()));

        return convertView;
    }
}
