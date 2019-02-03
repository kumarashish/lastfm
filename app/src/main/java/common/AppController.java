package common;

import android.app.Application;
import android.graphics.Typeface;

import database.DBManager;
import networkcall.WebApiCall;

/**
 * Created by ashish.kumar on 11-07-2018.
 */

public class AppController extends Application {
    Typeface font,normalFont;
WebApiCall webApiCall;
   DBManager dbManager;
   PrefManager manager;
    @Override
    public void onCreate() {
        super.onCreate();
        font= Typeface.createFromAsset(getApplicationContext().getAssets(), "regular.otf");
        normalFont= Typeface.createFromAsset(getApplicationContext().getAssets(), "font.ttf");
        webApiCall=new WebApiCall(this);
        dbManager = new DBManager(this);
        manager=new PrefManager(getApplicationContext());
    }

    public Typeface getFont() {
        return font;
    }

    public WebApiCall getWebApiCall() {
        return webApiCall;
    }

    public DBManager getDbManager() {
        return dbManager;
    }

    public Typeface getNormalFont() {
        return normalFont;
    }

    public PrefManager getManager() {
        return manager;
    }
}
