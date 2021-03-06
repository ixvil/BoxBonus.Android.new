package com.ixvil.android.BoxBonus.Activities;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.test.espresso.core.deps.guava.hash.Hashing;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import android.content.Intent;
import com.ixvil.android.BoxBonus.Models.User;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ixvil.android.BoxBonus.R;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.BindView;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;


    @BindView(R.id.input_phone)
    EditText _phoneText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.link_signup)
    TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String defaultValue = " ";
        String[] creds = sharedPref.getString("creds", defaultValue).split(":");
        if (creds.length == 2) {
            login(creds[0], creds[1]);
        }

        ButterKnife.bind(this);
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login(null, null);
            }
        });
        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        TextView phoneTextView = (TextView) findViewById(R.id.input_phone);
        phoneTextView.addTextChangedListener(
                new TextWatcher() {
                    boolean ignoreChange = false;
                    EditText phoneTextView = (EditText) findViewById(R.id.input_phone);

                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            if (!ignoreChange) {
                                ignoreChange = true;
                                String number = charSequence.toString().replace("+", "");
                                number = "+" + number;
                                String formattedNumber = PhoneNumberUtils.formatNumber(
                                        number,
                                        Locale.getDefault().getISO3Country()
                                );
                                if (formattedNumber != null) {
                                    phoneTextView.setText(formattedNumber);
                                    phoneTextView.setSelection(phoneTextView.getText().length());
                                }
                                ignoreChange = false;
                            }
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                    }
                }
        );

        _phoneText.setSelection(phoneTextView.getText().length());
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void login(String inputMobile, String inputPassword) {

        final String mobile;
        final String password;
        final String mPassword;
        if (inputMobile == null || inputPassword == null) {
            mobile = User.deformatPhone(_phoneText.getText().toString());
            password = _passwordText.getText().toString();

            if (!validate(mobile, password)) {
                onLoginFailed("Fields not valid");
                return;
            }

            mPassword = Hashing.sha256()
                    .hashString(password, StandardCharsets.UTF_8)
                    .toString();

        } else {
            mobile = inputMobile;
            mPassword = inputPassword;
        }


        Log.d(TAG, "Login");



//        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Base);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        try {
            Ion.with(getApplicationContext())
                    .load(getResources().getString(R.string.hostname) + "json/login")
                    .setMultipartParameter("phone", mobile)
                    .setMultipartParameter("password", mPassword)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressDialog.dismiss();
                            if (e == null) {

                                JsonObject userJson = result.getAsJsonObject("data");
                                int userId = userJson.get("id").getAsInt();
                                if (0 != userId) {
                                    onLoginSuccess(userJson, mobile, mPassword);
                                } else {
                                    onLoginFailed("Auth error, Please check phone and password");
                                }
                            } else {
                                onLoginFailed(e.getMessage().toString());
                            }
                        }

                    });
        } catch (Exception e) {
            onLoginFailed(e.getMessage().toString());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess(JsonObject userJson, String mobile, String password) {
        User user = User.createUserFromJson(userJson);
        User.proceedAuth(user);

        String creds = mobile + ":" + password;
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("creds", creds);
        editor.apply();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void onLoginFailed(String message) {
        if (message != "") {
            Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
        }
//        _loginButton.setEnabled(true);
        User.proceedDeAuth();
    }

    public boolean validate(String inputMobile, String inputPassword) {
        boolean valid = true;

        if (inputMobile.isEmpty() || inputMobile.length() != 11) {
            Toast.makeText(getBaseContext(), "Введите номер телефона в формате +7 000 000-00-00", Toast.LENGTH_LONG).show();
            valid = false;
            // TODO: valid phone in both cases: from prefs and direct

        }

        if (inputPassword.isEmpty() || inputPassword.length() < 4 || inputPassword.length() > 10) {
            Toast.makeText(getBaseContext(), "Пароль дожен быть не меньше 4 и не больше 10 символов", Toast.LENGTH_LONG).show();
            valid = false;
        }

        return valid;
    }
}
