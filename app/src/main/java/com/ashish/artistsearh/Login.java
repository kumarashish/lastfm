package com.ashish.artistsearh;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Validation;
import database.DBManager;

public class Login extends Activity implements View.OnClickListener {
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.signUp)
    TextView signUp;
    @BindView(R.id.email_id)
    EditText emailId;
    @BindView(R.id.password)
    EditText password;
    AppController controller;
    @BindView(R.id.view)
    LinearLayout view;
    @BindView(R.id.progress_bar)
    ProgressBar progressbar;
    Validation validation;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);
        controller = (AppController) getApplicationContext();
        validation=new Validation(Login.this);
        signUp.setOnClickListener(this);
        submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.submit:
                if((validation.isEmailIdValid(emailId))&&(validation.isNotNull(password))) {
                    progressbar.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.GONE);
                    new Handler().postDelayed(new Runnable() {

                        /*
                         * Showing splash screen with a timer. This will be useful when you
                         * want to show case your app logo / company
                         */

                        @Override
                        public void run() {
                            DBManager manager = controller.getDbManager();
                            manager.open();
                            boolean isUserAuthenticated=manager.checkloginDetails(emailId.getText().toString(),password.getText().toString());
                            // This method will be executed once the timer is over
                            if(isUserAuthenticated) {
                                controller.getManager().setUserLoggedIn(true);
                                Toast.makeText(Login.this, "LoggedIn Sucessfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Login.this, Search.class));
                                manager.close();
                                finish();
                            }else{
                                progressbar.setVisibility(View.GONE);
                                submit.setVisibility(View.VISIBLE);
                                if(manager.IsEmailExixts(emailId.getText().toString()))
                                {
                                    Toast.makeText(Login.this, "Please enter valid password", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(Login.this, "User not registered,please register first", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }, 4000);
                }
                break;

            case R.id.signUp:
                startActivity(new Intent(Login.this,Register.class));
                break;
        }
    }
}
