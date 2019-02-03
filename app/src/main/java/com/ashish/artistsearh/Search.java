package com.ashish.artistsearh;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;

import Util.Util;
import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.BaseActivity;
import common.Common;
import database.DBManager;
import interfaces.DetailsButtonClickListner;
import interfaces.WebApiResponseCallback;
import model.ArtistDetailsModel;
import model.ArtistModel;

import static Util.Util.showToast;


/**
 * Created by ashish.kumar on 11-07-2018.
 */

public class Search extends BaseActivity  {
    @BindView(R.id.searchView)
    LinearLayout searchView_lyt;
    @BindView(R.id.mainView)
    LinearLayout mainView_lyt;
    @BindView(R.id.search1)
    EditText search1_edt;
    @BindView(R.id.search2)
    EditText search2_edt;
    @BindView(R.id.listView)
    ListView list;
    @BindView(R.id.button)
    Button search_btn;
    AppController controller;
    @BindView(R.id.progressView)
    ProgressBar progressView;
    @BindView(R.id.noResult)
    TextView noResult;
    @BindView(R.id.bookmark)
    View bookmark;
    @BindView(R.id.logout)
            Button logout;
    ArrayList<ArtistDetailsModel> searchlist = new ArrayList<>();
    adapter.ListAdapter adapter=null;

    static final String ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    BottomSheetDialog internetAlert;
    LinearLayout layout;
    common.Bold_TextView message;
    boolean isApplicationLaunching=true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        initializeAll();
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ACTION);
        registerReceiver( NetworkStateManager,filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(NetworkStateManager);
    }

    public void initializeAll() {
        ButterKnife.bind(this);
        controller = (AppController) getApplicationContext();
        search_btn.setTypeface(controller.getNormalFont());
        search1_edt.setTypeface(controller.getNormalFont());
        search2_edt.setTypeface(controller.getNormalFont());
        search_btn.setOnClickListener(this);
        bookmark.setOnClickListener(this);
        logout.setOnClickListener(this);
        search1_edt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (search1_edt.getText().length() > 0) {
                        hideKeyboardFrom(Search.this,v);
                        callApi(search1_edt);
                    } else {
                        Toast.makeText(Search.this, "Please enter artist name", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });
        search2_edt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (search2_edt.getText().length() > 0) {
                        callApi(search2_edt);
                        hideKeyboardFrom(Search.this,v);
                    } else {
                        Toast.makeText(Search.this, "Please enter artist name", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });


        isApplicationLaunching=false;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
            if (search1_edt.getText().toString().length() > 0) {
                callApi(search1_edt);
                hideKeyboardFrom(Search.this, view);
            }
            break;
            case R.id.bookmark:
                startActivityForResult(new Intent(Search.this,Bookmarked.class),2);
                break;
            case R.id.logout:
                showLogoutAlert();
                break;
        }
    }

    @Override
    public void onSucess(String value) {
        searchlist.clear();
        final JSONArray matchedartist = Util.matchesArray(value);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (matchedartist == null) {
                    noResult.setText("No Result Found for " + search1_edt.getText().toString());
                    list.setVisibility(View.GONE);
                    noResult.setVisibility(View.VISIBLE);
                } else {
                    DBManager manager = controller.getDbManager();
                    manager.open();

                    for (int i = 0; i < matchedartist.length(); i++) {
                        try {
                            ArtistDetailsModel model = new ArtistDetailsModel(new ArtistModel(matchedartist.getJSONObject(i)));
                            searchlist.add(model);
                            if (manager.isRecordExist(searchlist.get(i)) == false) {
                               model =searchlist.get(i);
                                manager.insert(model);
                            }


                        } catch (Exception ex) {
                            ex.fillInStackTrace();
                        }
                    }
                    manager.close();
                    if (searchlist.size() > 0) {
                        adapter=new adapter.ListAdapter(Search.this, searchlist);
                        list.setAdapter(adapter);
                    }
                    noResult.setVisibility(View.GONE);
                    list.setVisibility(View.VISIBLE);
                }
                search1_edt.setFocusable(true);
                search2_edt.setFocusable(true);
                search2_edt .setFocusableInTouchMode(true);
                searchView_lyt.setVisibility(View.GONE);
                mainView_lyt.setVisibility(View.VISIBLE);
                progressView.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onError(String value) {
        Util.showToast(this, value);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                search_btn.setVisibility(View.VISIBLE);
                progressView.setVisibility(View.GONE);
                search1_edt.setFocusable(true);
                search2_edt.setFocusable(true);
                search2_edt .setFocusableInTouchMode(true);
            }
        });

    }

    public void callApi(EditText edt) {
        if (Util.isNetworkAvailable(this)) {
            isApplicationLaunching=true;
            progressView.setVisibility(View.VISIBLE);
            search_btn.setVisibility(View.GONE);
            search1_edt.setFocusable(false);
            search2_edt.setFocusable(false);
            controller.getWebApiCall().getData(Common.getArtistSearchUrl(Common.artistSearchMethodKey, edt.getText().toString(), Common.json), this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(adapter!=null)
        {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDetailsClick(final ArtistDetailsModel model) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent in = new Intent(Search.this, ArtistDetails.class);
                in.putExtra("ArtistName", model.getName());
                startActivityForResult(in,1);
            }
        });
    }

    private final BroadcastReceiver NetworkStateManager = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {


            if (Util.isNetworkAvailable(context)) {
               if(isApplicationLaunching==false) {
                   internetPopUp(1);
               }
            } else {
                internetPopUp(0);
            }
        }
    };

    public void internetPopUp(int type) {
        internetAlert = new BottomSheetDialog(Search.this);
        View sheetView = getLayoutInflater().inflate(R.layout.interneterror, null);
        internetAlert.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        internetAlert.setContentView(sheetView);
        message = (common.Bold_TextView) sheetView.findViewById(R.id.message);
        layout = (LinearLayout) sheetView.findViewById(R.id.layout);
        final FrameLayout bottomSheet = (FrameLayout) internetAlert.getWindow().findViewById(android.support.design.R.id.design_bottom_sheet);
        if (type == 0) {
            layout.setBackgroundColor(getResources().getColor(R.color.red));
            message.setText("Offline");
            mainView_lyt.setVisibility(View.VISIBLE);
            search2_edt.setVisibility(View.GONE);
            searchView_lyt.setVisibility(View.GONE);
            getOfflineList();
            if(searchlist.size()>0) {
                adapter=new adapter.ListAdapter(Search.this, searchlist);
                list.setAdapter( adapter);
            }else{
                message.setText("No data Available for Offline");
            }
        } else {
            search2_edt.setVisibility(View.VISIBLE);
            layout.setBackgroundColor(getResources().getColor(R.color.greencolor));
            message.setText("Online");
            new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    if (internetAlert != null) {
                        internetAlert.cancel();
                    }
                }
            }, 1000);
        }
        internetAlert.show();
    }

    public ArrayList<ArtistDetailsModel> getOfflineList() {
        searchlist.clear();
        DBManager manager = controller.getDbManager();
        manager.open();
        searchlist = manager.getList();
        return searchlist;
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onShareClick(ArtistDetailsModel model) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, Util.getShareString(model));
        sendIntent.setType("text/plain");
        startActivity(sendIntent);

    }

    private  void showLogoutAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to logout  ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
controller.getManager().setUserLoggedIn(false);
startActivity(new Intent(Search.this,Login.class));
finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        finish();

                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
