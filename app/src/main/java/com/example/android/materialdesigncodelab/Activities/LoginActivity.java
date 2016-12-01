package com.example.android.materialdesigncodelab.Activities;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.test.espresso.core.deps.guava.hash.Hashing;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import android.content.Intent;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.materialdesigncodelab.R;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.Bind;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;


    @Bind(R.id.input_phone)
    EditText _phoneText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.btn_login)
    Button _loginButton;
    @Bind(R.id.link_signup)
    TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
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
    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Base);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String mobile = _phoneText.getText().toString();
        String password = _passwordText.getText().toString();

        try {
            final String mPassword = Hashing.sha256()
                    .hashString(password, StandardCharsets.UTF_8)
                    .toString();
            Ion.with(getApplicationContext())
                    .load(getResources().getString(R.string.hostname) + "json/login")
                    .setMultipartParameter("mobile", mobile)
                    .setMultipartParameter("password", mPassword)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            if (e == null) {
                                progressDialog.dismiss();
                                JsonObject userJson = result.getAsJsonObject("data");
                                int userId = userJson.get("id").getAsInt();
                                if (0 != userId) {
                                    onLoginSuccess();
//                                    User user = User.createUserFromJson(userJson, getApplicationContext());
//                                    user.saveToAccountManager(email, mPassword);
//                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                                    startActivity(intent);
                                } else {
//                                    User.userId = 0;
//                                    mPasswordView.setError(getString(R.string.auth_error));
//                                    mPasswordView.requestFocus();
//                                    showProgress(false);
                                }
                            } else {
                                _passwordText.setError(getString(R.string.auth_error));
                                _passwordText.requestFocus();
                            }
                        }
                    });
        } catch (Exception e) {
            _passwordText.setError(e.getMessage().toString());
            _passwordText.requestFocus();
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

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String phone = _phoneText.getText().toString();
        String password = _passwordText.getText().toString();

        if (phone.isEmpty() || phone.length() != 16) {
            _phoneText.setError("Введите номер телефона в формате +7 000 000-00-00");
            valid = false;
        } else {
            _phoneText.setError(null);
            _phoneText.requestFocus();
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("Пароль дожен быть не меньше 4 и не больше 10 символов");
            valid = false;
        } else {
            _passwordText.setError(null);
            _passwordText.requestFocus();
        }

        return valid;
    }
}
