package interfaces;

import model.ArtistDetailsModel;
import model.ArtistModel;

/**
 * Created by ashish.kumar on 11-07-2018.
 */

public interface DetailsButtonClickListner {
    public void onDetailsClick( ArtistDetailsModel model);
    public void onBookMarkClicked(String name);
    public void onShareClick(ArtistDetailsModel model);


}
