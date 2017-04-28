package br.com.myapp.ultracartola;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import br.com.myapp.ultracartola.business.UserTeam;

public class MainActivity extends AppCompatActivity {

//    public static final String FILENAME = "teams";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        restoreTeamsFromDisk();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(br.com.myapp.ultracartola.R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == br.com.myapp.ultracartola.R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private ArrayList<UserTeam> restoreTeamsFromDisk() {
        //TODO: Escolher melhor tipo de lista: um que NÃO permita duplicidade
        ArrayList<UserTeam> chosenTeams = new ArrayList<>();

        // Gets the file from the /res/raw directory
        InputStream is = getApplicationContext().getResources().openRawResource(R.raw.teams);

        // Then read it
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String team;
            while ((team = br.readLine()) != null) {
                int id = Integer.valueOf(team);
                chosenTeams.add(new UserTeam(id));
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return chosenTeams;
    }


    public void addTeam(View view) {

    }


    public void updateScores(View view) {
//        String url = "https://api.cartolafc.globo.com/atletas/mercado";
////        String url = "https://api.cartolafc.globo.com/time/slug/stacfc-bb";
//
//        JsonObjectRequest jsObjRequest = new JsonObjectRequest
//                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        // TODO completar código com loop sobre todos os times selecionados
//                        parseTeam(response);
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // TODO Auto-generated method stub
//                    }
//                });
//        RequestQueueSingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }
//
//    private void parseTeam(JSONObject response) {
//        try {
//            UserTeam team = new UserTeam();
//            JSONArray athlets = response.getJSONArray("atletas");
//            parseAthlets(athlets);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static ArrayList<Athlet> parseAthlets(JSONArray json) throws JSONException {
//        ArrayList<Athlet> athlets = new ArrayList<Athlet>();
//        for (int i = 0; i < json.length(); i++) {
//            athlets.add(new Athlet(json.getJSONObject(i)));
//        }
//        return athlets;
//    }
}
