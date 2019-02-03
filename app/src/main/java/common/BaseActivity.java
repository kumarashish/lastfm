package common;

import android.app.Activity;
import android.view.View;

import interfaces.DetailsButtonClickListner;
import interfaces.WebApiResponseCallback;
import model.ArtistDetailsModel;

/**
 * Created by ashish.kumar on 11-07-2018.
 */

public class BaseActivity extends Activity implements WebApiResponseCallback , View.OnClickListener, DetailsButtonClickListner {

    @Override
    public void onSucess(String value) {

    }

    @Override
    public void onError(String value) {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onDetailsClick(ArtistDetailsModel model) {

    }

    @Override
    public void onBookMarkClicked(String name) {

    }

    @Override
    public void onShareClick(ArtistDetailsModel model) {

    }


}
