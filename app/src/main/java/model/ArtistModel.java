package model;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by ashish.kumar on 11-07-2018.
 */

public class ArtistModel {
    String  name;
    String url;
    String image="";

    public ArtistModel(JSONObject jsonObject) {
        try {
            this.name = jsonObject.getString("name");
            this.url = jsonObject.getString("url");
            JSONArray jsonArray = jsonObject.isNull("image") ? null : jsonObject.getJSONArray("image");
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject imageObject = jsonArray.getJSONObject(i);
                    if (imageObject.getString("size").equalsIgnoreCase("extralarge")) {
                        image = imageObject.getString("#text");
                    }
                }
            }
        } catch (Exception ex) {

        }
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getUrl() {
        return url;
    }

}
