package com.ashish.artistsearh;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import Util.Util;
import butterknife.BindAnim;
import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.BaseActivity;
import database.DBManager;
import model.ArtistDetailsModel;

/**
 * Created by ashish.kumar on 12-07-2018.
 */

public class Bookmarked extends BaseActivity  {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.bookmarklist)
    ListView list;
    @BindView(R.id.noResult)
    TextView noResult;
  AppController controller;
  ArrayList<ArtistDetailsModel> bookmarkList=new ArrayList<>();
  DBManager manager;
    adapter.ListAdapter adapter=null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookmarked);
        initializeAll();
    }
    public void initializeAll()
    {
        ButterKnife.bind(this);
        controller = (AppController) getApplicationContext();
        manager=controller.getDbManager();
        manager.open();
        bookmarkList.clear();
        bookmarkList.addAll(manager.getBookMarkedList());
        manager.close();
        if(bookmarkList.size()>0) {
            adapter=new adapter.ListAdapter(Bookmarked.this, bookmarkList);
            list.setAdapter(adapter);
            list.setVisibility(View.VISIBLE);
            noResult.setVisibility(View.GONE);
        }else{
            list.setVisibility(View.GONE);
            noResult.setVisibility(View.VISIBLE);
        }
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        onBackPressed();
    }

    @Override
    public void onBookMarkClicked(String name) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                handleBookMark();
            }
        });
    }
    @Override
    public void onShareClick(ArtistDetailsModel model) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Name :"+model.getName());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
    @Override
    public void onDetailsClick(final ArtistDetailsModel model) {
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              Intent in = new Intent(Bookmarked.this, ArtistDetails.class);
                              in.putExtra("ArtistName", model.getName());
                              startActivityForResult(in,1);
                          }
                      }
        );


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        handleBookMark();
    }

    public void handleBookMark()
    {
        bookmarkList.clear();
        manager.open();
        bookmarkList.addAll(manager.getBookMarkedList());
        manager.close();
        if (bookmarkList.size() > 0) {
            adapter=new adapter.ListAdapter(Bookmarked.this, bookmarkList);
            list.setAdapter(adapter);
            list.setVisibility(View.VISIBLE);
            noResult.setVisibility(View.GONE);
        } else {
            list.setVisibility(View.GONE);
            noResult.setVisibility(View.VISIBLE);
        }
    }
}
