package common;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jaya.krishna on 14-12-2016.
 */

public class Validation {
    private static Pattern pattern;
    private static Matcher matcher;
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,3})$";
    private static final String PASSWORD_REGEX = "[_A-Za-z0-9]{8,16}";
    Activity context;

    public Validation(Activity context) {
        this.context = context;
    }


    /***
     * validate password with given password regular expression
     */
    public boolean isPasswordValid(EditText password) {
        boolean matches = false;
        if (isNotNull(password)) {
            String pwd = password.getText().toString().trim();
            if (pwd.length() < 4) {
                Toast.makeText(context, "Password should be at least 4 characters ", Toast.LENGTH_SHORT).show();
            } else {
                return true;
            }
        }else{
            Toast.makeText(context, "Please enter password ", Toast.LENGTH_SHORT).show();
        }

        return matches;
    }

    public boolean isPassword_ConfirmPasswordSame(EditText password, EditText confirmPassword) {
        if (password.getText().toString().equals(confirmPassword.getText().toString())) {
            return true;
        }else{
            if(confirmPassword.getText().toString().length()==0)
            {
                Toast.makeText(context, "Confirm password should not be blank. ", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(context, "Password and confirm password should be same. ", Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void requestFocus(View view) {
        if (view.requestFocus()) {
            context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    /***
     * validate email string with given email regular expression
     */
    public boolean isEmailIdValid(EditText email) {
        if (isNotNull(email)) {
            if(isValidEmail(email.getText().toString()))
            {
                return true;
            }else{
                Toast.makeText(context, "Please enter valid email Id", Toast.LENGTH_SHORT).show();
            }



        } else {
            Toast.makeText(context, "Please enter email Id", Toast.LENGTH_SHORT).show();

        }
        return false;
    }



    /**
     * check whether string contains value or not
     */
    public boolean isNameValidated(EditText txt) {
        try {

            if ((txt != null) && (txt.getText().toString().trim().length() > 0)) {
                return true;
            } else {
                Toast.makeText(context, "Please enter name", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
            return false;

        }
    }


    /**
     * check whether string contains value or not
     */
    public boolean isNotNull(EditText txt) {
        try {

            if ((txt != null) && (txt.getText().toString().trim().length() > 0)) {
                return true;
            }else{
                Toast.makeText(context,"Please enter "+txt.getHint().toString() ,Toast.LENGTH_SHORT).show();
            }
            return false;
        } catch (Exception ex) {
            ex.fillInStackTrace();
            return false;

        }
    }


    /***
     * validate phone string with given phone regular expression
     */
    public boolean isPhoneNumberValid(EditText phone) {
        boolean matches = false;
        if (isNotNull(phone)) {
            String phoneNumber = phone.getText().toString().trim();
            if(phoneNumber.length() == 10){
                matches = true;
            }else{
                Toast.makeText(context, "Phone number should be of 10 characters ", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Phone number should not be empty", Toast.LENGTH_SHORT).show();
        }
        return matches;
    }

}
