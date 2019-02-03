package common;

/**
 * Created by ashish.kumar on 11-07-2018.
 */

public class Common {
    public static String baseUrl = "http://ws.audioscrobbler.com/2.0/?";
    public static String ApiKeyValue = "fa2e62987b8c372e16daa60331164d12";
    public static String methodKey = "method=";
    public static String artistKey = "artist=";
    public static String apikey = "api_key=";
    public static String format = "format=";
    public static String json = "json";
    public static String artistSearchMethodKey = "artist.search";
    public static String artistInfoMethodKey = "artist.getinfo";


    public static String getArtistSearchUrl(String method, String artist, String type) {
        return baseUrl + methodKey + method + "&" + artistKey + artist + "&" + apikey + ApiKeyValue + "&" + format + type;

    }

}
