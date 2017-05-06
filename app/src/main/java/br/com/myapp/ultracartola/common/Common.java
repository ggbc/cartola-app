package br.com.myapp.ultracartola.common;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import br.com.myapp.ultracartola.business.Athlet;
import br.com.myapp.ultracartola.business.Team;

/**
 * Created by Gustavo on 01/05/2017.
 */

public final class Common {
    public final static String TEAMS_FILNEMANE = "teams";

    public static HashMap<Integer, String> POSITIONS = new HashMap<Integer, String>();
    public static HashMap<Integer, String> CLUBS = new HashMap<Integer, String>();

    public static final void setAthletPositions() {
        POSITIONS.put(1, "Goleiro");
        POSITIONS.put(2, "Lateral");
        POSITIONS.put(3, "Zagueiro");
        POSITIONS.put(4, "Meia");
        POSITIONS.put(5, "Atacante");
        POSITIONS.put(6, "Técnico");
    }

    public static final void setClubPhotoUrl() {
        CLUBS.put(303, "https://s.glbimg.com/es/sde/f/equipes/2014/04/14/ponte_preta_60x60.png");
        CLUBS.put(264, "https://s.glbimg.com/es/sde/f/equipes/2014/04/14/corinthians_60x60.png");
        CLUBS.put(265, "https://s.glbimg.com/es/sde/f/equipes/2014/04/14/bahia_60x60.png");
        CLUBS.put(277, "https://s.glbimg.com/es/sde/f/equipes/2014/04/14/santos_60x60.png");
        CLUBS.put(283, "https://s.glbimg.com/es/sde/f/equipes/2015/04/29/cruzeiro_65.png");
        CLUBS.put(293, "https://s.glbimg.com/es/sde/f/equipes/2015/06/24/atletico-pr_2015_65.png");
        CLUBS.put(284, "https://s.glbimg.com/es/sde/f/equipes/2014/04/14/gremio_60x60.png");
        CLUBS.put(275, "https://s.glbimg.com/es/sde/f/equipes/2014/04/14/palmeiras_60x60.png");
        CLUBS.put(287, "https://s.glbimg.com/es/sde/f/equipes/2014/04/14/vitoria_60x60.png");
        CLUBS.put(373, "https://s.glbimg.com/es/sde/f/organizacoes/2017/04/10/Atletico-GO-65.png");
        CLUBS.put(282, "https://s.glbimg.com/es/sde/f/equipes/2014/04/14/atletico_mg_60x60.png");
        CLUBS.put(314, "https://s.glbimg.com/es/sde/f/equipes/2014/04/14/avai_60x60_.png");
        CLUBS.put(263, "https://s.glbimg.com/es/sde/f/equipes/2014/04/14/botafogo_60x60.png");
        CLUBS.put(315, "https://s.glbimg.com/es/sde/f/equipes/2015/08/03/Escudo-Chape-165.png");
        CLUBS.put(294, "https://s.glbimg.com/es/sde/f/equipes/2017/03/27/coritiba65.png");
        CLUBS.put(262, "https://s.glbimg.com/es/sde/f/equipes/2014/04/14/flamengo_60x60.png");
        CLUBS.put(266, "https://s.glbimg.com/es/sde/f/equipes/2015/07/21/fluminense_60x60.png");
        CLUBS.put(292, "https://s.glbimg.com/es/sde/f/equipes/2015/07/21/sport65.png");
        CLUBS.put(276, "https://s.glbimg.com/es/sde/f/equipes/2014/04/14/sao_paulo_60x60.png");
        CLUBS.put(267, "https://s.glbimg.com/es/sde/f/equipes/2016/07/29/Vasco-65.png");
    }

    /*
    *  Save the teams selected by the user to disk
    * */
    public static void saveTeamsIdsToDisk(Context context, Set<Integer> teamsIds)
            throws IOException {
        FileOutputStream fos = context.openFileOutput(Common.TEAMS_FILNEMANE, Context.MODE_PRIVATE);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        for (Integer id : teamsIds) {
            bw.write(Integer.toString(id));
            bw.newLine();
        }

        bw.close();
        fos.close();
    }

    /*
    *  Get the teams selected by the user from disk
    * */
    public static Set<Integer> getTeamsIdsFromDisk(Context context) {
        Set<Integer> ids = new LinkedHashSet<>();
        try {
            FileInputStream fis = null;
            fis = context.openFileInput(Common.TEAMS_FILNEMANE);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String teamId;
            while ((teamId = br.readLine()) != null) {
                int id = Integer.valueOf(teamId);
                ids.add(id);
            }
            br.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new LinkedHashSet<>(ids);
    }

    public static class WebServices {
        /*
        * Parse Athlets from the webservice response
        * */
        public static ArrayList<Athlet> parseAtlhets(JSONObject response) {
            ArrayList<Athlet> athlets = null;

            try {
                // Get athlets
                if (!response.isNull("atletas")) {
                    athlets = new ArrayList<Athlet>();

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

        /*
        * Parse a Team from the webservice response
        * WebService: https://api.cartolafc.globo.com/time/id/[?]
        * */
        public static Team parseTeam(JSONObject response) {
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

                team.setAtletas(WebServices.parseAtlhets(response));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return team;
        }

        /*
        * Parse a Team's basic info from the webservice response
        * WebService: https://api.cartolafc.globo.com/times?q=[?]
        * */
        public static ArrayList<Team> parseSearchResult(JSONArray response,
                                                        Set<Integer> selectedTeamsIds) {

            //TODO: Mudar de Team para SelectableTeam
            ArrayList<Team> result = new ArrayList<Team>();
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject jsonTeam = (JSONObject) response.get(i);
                    Team t = new Team();

                    t.setTimeId(jsonTeam.getInt("time_id"));
                    t.setNome(jsonTeam.getString("nome"));
                    t.setNomeCartola(jsonTeam.getString("nome_cartola"));
                    t.setUrlEscudoPng(jsonTeam.getString("url_escudo_png"));
                    t.setChecked(selectedTeamsIds.contains(t.getTimeId()));

                    result.add(t);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return result;
        }
    }

}
