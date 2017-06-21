package mrerror.bakingtime.volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by ahmed on 20/06/17.
 */

public class NetworkConnection {

    public final static String url = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private OnCompleteFetchingData OnCompleteFetchingData;

    public NetworkConnection(OnCompleteFetchingData onComplete) {
        OnCompleteFetchingData = onComplete;
    }

    public void getData(Context context) {
        JsonArrayRequest jsArrRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            OnCompleteFetchingData.onCompleted(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });

// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(context).addToRequestQueue(jsArrRequest);
    }

    public interface OnCompleteFetchingData {
        void onCompleted(JSONArray result) throws JSONException;
    }
}
