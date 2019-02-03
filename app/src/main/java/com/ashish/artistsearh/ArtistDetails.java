package com.ashish.artistsearh;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import Util.Util;
import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.BaseActivity;
import common.Common;
import database.DBManager;
import model.ArtistDetailsModel;
import model.ArtistModel;

/**
 * Created by ashish.kumar on 11-07-2018.
 */

public class ArtistDetails extends BaseActivity {
    @BindView(R.id.back)
    ImageView back;
   String artistName="";
   AppController controller;
   @BindView(R.id.loading_view)
   LinearLayout loading_view;
   @BindView(R.id.mainView)
   LinearLayout mainView;
    @BindView(R.id.similarArtist)
    LinearLayout similarArtist;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.link)
    TextView link;
    @BindView(R.id.summary)
    TextView summary;
     ArtistDetailsModel model=null;
     @BindView(R.id.progress)
     ProgressBar progress;
     @BindView(R.id.artistImage)
    ImageView artistImage;
     @BindView(R.id.name)
     TextView name;
     @BindView(R.id.bookmark)
     ImageView bookmark;
     @BindView(R.id.similarArtistLLy)
     LinearLayout similarArtlistLayout;
     boolean isbookmarked=false;
     DBManager manager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artist_details);
        initializeAll();
    }

    public void initializeAll() {
        artistName = getIntent().getStringExtra("ArtistName");
        ButterKnife.bind(this);
        controller = (AppController) getApplicationContext();
        manager=controller.getDbManager();
        link.setOnClickListener(this);
        back.setOnClickListener(this);
        bookmark.setOnClickListener(this);
        callApi();
    }

    public void callApi() {
        if (Util.isNetworkAvailable(this)) {
            loading_view.setVisibility(View.VISIBLE);
            controller.getWebApiCall().getData(Common.getArtistSearchUrl(Common.artistInfoMethodKey, artistName, Common.json), this);
        }else{
            manager.open();
            Cursor c=manager.getOfflineDetails( artistName);
            if(c!=null)
            {
                model=new ArtistDetailsModel(c);

                Picasso.with(ArtistDetails.this).load(model.getImage()).resize(120, 120).into(artistImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        progress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {

                    }
                });
                if (manager.isBookmarked(model.getName(),model.getImage())) {
                    isbookmarked = true;
                    bookmark.setImageResource(R.drawable.bookmark);
                }else{
                    bookmark.setImageResource(R.drawable.unmark);
                }
                loading_view.setVisibility(View.GONE);
                name.setText(model.getName());
                link.setText("Link : "+model.getLink());
                content.setText("Published On : "+model.getPublished());
                summary.setText("Details : "+model.getContent());
                mainView.setVisibility(View.VISIBLE);
                similarArtlistLayout.setVisibility(View.GONE);
            }
        }
    }
    @Override
    public void onSucess(String value) {
        model=new ArtistDetailsModel(value);
        manager.open();
        if(manager.isRecordExist(model))
        {
            manager.updateDetails( model);
        }else{
            manager.insert( model);
        }

        manager.close();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Picasso.with(ArtistDetails.this).load(model.getImage()).resize(120, 120).into(artistImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        progress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {

                    }
                });
                manager.open();
                if (manager.isBookmarked(model.getName(),model.getImage())) {
                    isbookmarked = true;
                    bookmark.setImageResource(R.drawable.bookmark);
                }else{
                    bookmark.setImageResource(R.drawable.unmark);
                }
                loading_view.setVisibility(View.GONE);
                name.setText(model.getName());
                link.setText("Link : "+model.getLink());
                content.setText("Published On : "+model.getPublished());
                summary.setText("Details : "+model.getContent());
                mainView.setVisibility(View.VISIBLE);
                addView();


            }
        });

    }

    @Override
    public void onError(String value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loading_view.setVisibility(View.GONE);
            }
        });


    }

    public void addView() {
        similarArtist.removeAllViews();
        for (int i = 0; i < model.getSimilarModel().size(); i++) {
           final ArtistModel similarModel=model.getSimilarModel().get(i);
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.similar_artist_row, null);
            ImageView image = (ImageView) view.findViewById(R.id.artistImage);
            common.Bold_TextView name = (common.Bold_TextView) view.findViewById(R.id.name);
            final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress);
            Picasso.with(this).load(similarModel.getImage()).resize(80, 80).into(image, new Callback() {
                @Override
                public void onSuccess() {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError() {

                }
            });
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    artistName=similarModel.getName();
                    mainView.setVisibility(View.GONE);
                    callApi();
                }
            });
            name.setText(similarModel.getName());
            similarArtist.addView(view);
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.bookmark:
                if (isbookmarked) {
                    isbookmarked = false;
                    bookmark.setImageResource(R.drawable.unmark);
                    manager.open();
                    manager.updateRecord(model, false);
                    manager.close();
                    Toast.makeText(ArtistDetails.this,"Unmarked Sucessfully",Toast.LENGTH_SHORT).show();
                } else {
                    manager.open();
                    manager.updateRecord(model, true);
                    manager.close();
                    isbookmarked = true;
                    bookmark.setImageResource(R.drawable.bookmark);
                    Toast.makeText(ArtistDetails.this,"Bookmarked Sucessfully",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.link:
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(model.getLink()));
                startActivity(i);
                break;
        }

    }
}
