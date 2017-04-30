package br.com.myapp.ultracartola;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import br.com.myapp.ultracartola.business.Team;

/**
 * Created by Gustavo on 29/04/2017.
 */

public class TeamListAdapter extends ArrayAdapter<Team> {

    private FragmentActivity activity;
    private ArrayList<Team> listItems;

    public static class ViewHolder {
        public ImageView badgeImageView;
        public TextView idTextView;
        public TextView nameTextView;
    }

    public TeamListAdapter(FragmentActivity activity, ArrayList<Team> list) {
        super(activity, android.R.layout.simple_list_item_1, list);
        this.activity = activity;
        this.listItems = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.team_list_item,
                    parent, false);
        }

        Team team = (Team) getItem(position);
        //TODO: Tirar Glide daqui. Não é lugar para fazer request para a internet
        Glide.with(activity).load(team.getUrlEscudoPng()).into((ImageView) convertView.findViewById(R.id.teamBadge));
//        ((ImageView) convertView.findViewById(R.id.teamBadge)).setImageURI(Uri.parse());
        ((TextView) convertView.findViewById(R.id.teamName)).setText(team.getNome());
        ((TextView) convertView.findViewById(R.id.teamCartolaName)).setText(team.getNomeCartola());
        ((TextView) convertView.findViewById(R.id.teamPoints)).setText(Double.toString(team.getPontos()));

        return convertView;
    }
}
