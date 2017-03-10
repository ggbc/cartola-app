package br.com.myapp.ultracartola;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.myapp.ultracartola.business.Athlet;
import br.com.myapp.ultracartola.business.UserTeam;

import static android.media.CamcorderProfile.get;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(br.com.myapp.ultracartola.R.layout.activity_main);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

    public void updateScores(View view) {
        String url = "https://api.cartolafc.globo.com/time/nikiti-red-bull";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        parseTeam(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                    }
                });
        RequestQueueSingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }

    private void parseTeam(JSONObject response) {
        try {
            parseAthlets(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseAthlets(JSONObject response) throws JSONException {
        UserTeam userTeam = new UserTeam();
        JSONArray atletas = response.getJSONArray("atletas");

        for(int i = 0; i < atletas.length(); i++){
            userTeam.addAtleta(new Athlet(atletas.getJSONObject(i)));
        }
    }
}
