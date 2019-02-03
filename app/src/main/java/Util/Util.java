package Util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;



import org.json.JSONArray;
import org.json.JSONObject;

import model.ArtistDetailsModel;

/**
 * Created by ashish.kumar on 11-07-2018.
 */

public class Util {
    public static boolean isNetworkAvailable(Context act) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if ((activeNetworkInfo != null) && (activeNetworkInfo.isConnected())) {
            return true;
        } else {
            Toast.makeText(act, "Internet Unavailable", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public static void showToast(final Activity act, final String message) {
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(act, message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static JSONArray matchesArray(String response) {
        JSONArray searchResult = null;
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject result = jsonObject.getJSONObject("results");
            JSONObject artistMatch = result.getJSONObject("artistmatches");
            searchResult = artistMatch.isNull("artist") ? null : artistMatch.getJSONArray("artist");
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        return searchResult;
    }

    public static String getShareString(ArtistDetailsModel model) {
        String s = "";
        s = "Name : " + model.getName() + ",\n Image : " + model.getImage() + "\n Bio : " + model.getUrl() + "\n Details : " + model.getSummary();
        return s;
    }
}
