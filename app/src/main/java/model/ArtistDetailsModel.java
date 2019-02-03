package model;

import android.database.Cursor;

import com.ashish.artistsearh.ArtistDetails;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import common.Bold_TextView;

/**
 * Created by ashish.kumar on 11-07-2018.
 */

public class ArtistDetailsModel {
    String name;
    String image=null;
    String url;
    String link;
    String published;
    String summary;
    String content;
    ArrayList<ArtistModel> similarModel=new ArrayList<>();
    boolean isbookmarked=false;

    public ArtistDetailsModel(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject artist = jsonObject.getJSONObject("artist");
            JSONObject bio = artist.getJSONObject("bio");
            JSONObject links = bio.getJSONObject("links");
            JSONObject linkdescription = links.getJSONObject("link");
            JSONObject similar = artist.getJSONObject("similar");
            JSONArray similarArtist = similar.getJSONArray("artist");


            name = artist.getString("name");
            url = artist.getString("url");

            JSONArray jsonArray = artist.isNull("image") ? null : artist.getJSONArray("image");
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject imageObject = jsonArray.getJSONObject(i);
                    if (imageObject.getString("size").equalsIgnoreCase("extralarge")) {
                        image = imageObject.getString("#text");
                    }
                }
            }
            published = bio.getString("published");
            summary = bio.getString("summary");
            content = bio.getString("content");
            link = linkdescription.getString("href");
            similarModel.clear();
            for (int i = 0; i < similarArtist.length(); i++) {
                try {
                    similarModel.add(new ArtistModel(similarArtist.getJSONObject(i)));
                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
    }

    public ArtistDetailsModel(ArtistModel model) {
        name = model.getName();
        image = model.getImage();
        url = model.getUrl();
        link = "";
        published = "";
        summary = "";
        content = "";
        isbookmarked = false;
    }

    public ArtistDetailsModel(Cursor c) {
        name = c.getString(1);
        image = c.getString(2);
        url = c.getString(3);
        link = c.getString(4);
        published = c.getString(5);
        summary = c.getString(6);
        content = c.getString(7);
        isbookmarked = Boolean.parseBoolean(c.getString(8));
    }

    public void setIsbookmarked(boolean isbookmarked) {
        this.isbookmarked = isbookmarked;
    }

    public boolean isbookmarked() {
        return isbookmarked;
    }

    public String getUrl() {
        return url;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public ArrayList<ArtistModel> getSimilarModel() {
        return similarModel;
    }

    public String getContent() {
        return content;
    }

    public String getLink() {
        return link;
    }

    public String getPublished() {
        return published;
    }

    public String getSummary() {
        return summary;
    }

}
