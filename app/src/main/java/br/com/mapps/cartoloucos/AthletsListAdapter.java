package br.com.mapps.cartoloucos;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import br.com.mapps.cartoloucos.business.Athlet;
import br.com.mapps.cartoloucos.common.Common;

/**
 * Created by Gustavo on 01/05/2017.
 */

public class AthletsListAdapter extends ArrayAdapter<Athlet> {

    private Context context;

    public AthletsListAdapter(@NonNull Context context, @NonNull List<Athlet> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_athlets_list_item, parent, false);
        }

        Athlet athlet = (Athlet) getItem(position);

        Common.setAthletPositions();
        Common.setClubPhotoUrl();

        ((NetworkImageView) convertView.findViewById(R.id.athletPhoto))
                .setImageUrl(
                        athlet.getFotoUrl(),
                        RequestQueueSingleton
                                .getInstance(convertView.getContext())
                                .getImageLoader()
                );
        ((NetworkImageView) convertView.findViewById(R.id.teamBadge))
                .setImageUrl(
                        athlet.getFotoClubeUrl(),
                        RequestQueueSingleton
                                .getInstance(convertView.getContext())
                                .getImageLoader()
                );
        ((TextView) convertView.findViewById(R.id.athletAlias)).setText(athlet.getApelido());
        ((TextView) convertView.findViewById(R.id.athletPosition))
                .setText(Common.POSITIONS.get(athlet.getPosicaoId()));
        ((TextView) convertView.findViewById(R.id.athletPoints)).setText(Double.toString(athlet.getPontos()));
        ((TextView) convertView.findViewById(R.id.athletCost)).setText("C$ "+ athlet.getPreco());

        return convertView;
    }
}
