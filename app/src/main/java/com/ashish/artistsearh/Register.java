package com.ashish.artistsearh;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Validation;
import database.DBManager;
import model.RegisterModel;

public class Register extends Activity implements View.OnClickListener {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.firstName)
    EditText fname;
    @BindView(R.id.lastName)
    EditText lname;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.confirmPassword)
    EditText confirmPassword;
    @BindView(R.id.submit)
    Button submit ;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    Validation validation;
    AppController controller;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_step1);
        ButterKnife.bind(this);
        controller = (AppController) getApplicationContext();
        back.setOnClickListener(this);
        submit.setOnClickListener(this);
        validation=new Validation(Register.this);

    }

    @Override
    public void onClick(final View view) {
        switch (view.getId())
        {
            case R.id.back:
                finish();
                break;
            case R.id.submit:
                if((validation.isNotNull(fname))&&(validation.isNotNull(lname))&&(validation.isEmailIdValid(email)&&(validation.isNotNull(password)&&(validation.isPassword_ConfirmPasswordSame(password,confirmPassword))))) {
                    progressBar.setVisibility(View.VISIBLE);
                submit.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {

                    /*
                     * Showing splash screen with a timer. This will be useful when you
                     * want to show case your app logo / company
                     */

                    @Override
                    public void run() {
                        // This method will be executed once the timer is over

                            DBManager manager = controller.getDbManager();
                            manager.open();
                            if(manager.IsEmailExixts(email.getText().toString())==false) {
                                RegisterModel model = new RegisterModel(email.getText().toString(), fname.getText().toString() + " " + lname.getText().toString(), "123", password.getText().toString());
                                manager.register(model);
                                Toast.makeText(Register.this, "Registered Sucessfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                progressBar.setVisibility(View.GONE);
                                submit.setVisibility(View.VISIBLE);
                                Toast.makeText(Register.this, "User already registered ! Please try with different emailId", Toast.LENGTH_SHORT).show();
                            }

                    }
                }, 4000);
        }
                break;
        }

    }
}
