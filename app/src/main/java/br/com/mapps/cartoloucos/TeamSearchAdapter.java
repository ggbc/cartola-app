package br.com.mapps.cartoloucos;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import br.com.mapps.cartoloucos.business.Team;

/**
 * Created by Gustavo on 04/05/2017.
 */

public class TeamSearchAdapter extends ArrayAdapter<Team> { //TODO: Mudar para ArrayAdapter<SelectableTeam>

    private Context mContext;
    private ArrayList<Team> mTeamList; //TODO: Mudar para ArrayList<SelectableTeam>

    private class ViewHolder {
        NetworkImageView teamBadge;
        TextView teamName;
        TextView teamCartolaName;
        CheckBox teamSelected;
    }

    public TeamSearchAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Team> objects) {
        super(context, resource, objects);
        this.mTeamList = objects;
        this.mContext = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.activity_search_team_item, parent, false);

            holder = new ViewHolder();
            holder.teamBadge = (NetworkImageView) convertView.findViewById(R.id.teamBadge);
            holder.teamName = (TextView) convertView.findViewById(R.id.teamName);
            holder.teamCartolaName = (TextView) convertView.findViewById(R.id.teamCartolaName);
            holder.teamSelected = (CheckBox) convertView.findViewById(R.id.teamSelected);
            convertView.setTag(holder);

            holder.teamSelected.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    CheckBox cb = (CheckBox) view;
                    Team team = (Team) cb.getTag();
                    team.setChecked(cb.isChecked());

                    // Update the list of selected teams
                    ((IOnTeamCheckedListener) mContext).onTeamChecked(team);
                }
            });
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Team team = mTeamList.get(position);
        holder.teamBadge.setImageUrl(
                team.getUrlEscudoPng(),
                RequestQueueSingleton
                        .getInstance(convertView.getContext())
                        .getImageLoader());
        holder.teamName.setText(team.getNome());
        holder.teamCartolaName.setText(team.getNomeCartola());
        holder.teamSelected.setChecked(team.isChecked());
        holder.teamSelected.setTag(team);

        return convertView;
    }

    /**
     * Every click on a checkbox sends a message to the activity
     * so that the list of selected ids can be updated
     */
    public interface IOnTeamCheckedListener {
        void onTeamChecked(Team team);
    }
}
