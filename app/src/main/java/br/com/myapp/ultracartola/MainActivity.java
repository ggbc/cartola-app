package br.com.myapp.ultracartola;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            TeamListFragment teamsFragment = new TeamListFragment();
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_placeholder, teamsFragment);
            transaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(br.com.myapp.ultracartola.R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_refresh) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



//    public void updateScores(View view) {
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
//        RequestQueueSingleton.getInstance(get).addToRequestQueue(jsObjRequest);
////    }
//
//    private void parseTeam(JSONObject response) {
//        try {
//            Team team = new Team();
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
