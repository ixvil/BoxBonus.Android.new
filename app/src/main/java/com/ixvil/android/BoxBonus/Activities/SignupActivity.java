package com.ixvil.android.BoxBonus.Activities;

import android.annotation.TargetApi;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.test.espresso.core.deps.guava.hash.Hashing;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ixvil.android.BoxBonus.Models.User;
import com.ixvil.android.BoxBonus.R;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.BindView;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @BindView(R.id.input_mobile)
    EditText _mobileText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.input_reEnterPassword)
    EditText _reEnterPasswordText;
    @BindView(R.id.btn_signup)
    Button _signupButton;
    @BindView(R.id.link_login)
    TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        _mobileText.addTextChangedListener(
                new TextWatcher() {
                    boolean ignoreChange = false;
                    EditText phoneTextView = (EditText) findViewById(R.id.input_mobile);

                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            /**
                             * TODO: implement custom phone validation
                             * */
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
        _mobileText.setSelection(_mobileText.getText().length());
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed("");
            return;
        }

        // _signupButton.setEnabled(false);

//        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
//                R.style.AppTheme_Base);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Creating Account...");
//        progressDialog.show();

        String mobile = User.deformatPhone(_mobileText.getText().toString());
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (!reEnterPassword.equals(password)) {
            onSignupFailed("Passwords are not equal");
            return;
        }

        try {
            final String mPassword = Hashing.sha256()
                    .hashString(password, StandardCharsets.UTF_8)
                    .toString();
            Ion.with(getApplicationContext())
                    .load(getResources().getString(R.string.hostname) + "json/register")
                    .setMultipartParameter("phone", mobile)
                    .setMultipartParameter("password", Hashing.sha256()
                            .hashString(password, StandardCharsets.UTF_8)
                            .toString())
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            if (e == null) {
//                                progressDialog.dismiss();
                                JsonObject userJson = result.getAsJsonObject("data");
                                int userId = userJson.get("id").getAsInt();
                                if (0 != userId) {
                                    onSignupSuccess(userJson);
                                } else {
                                    onSignupFailed(userJson.get("message").getAsString());
                                }
                            } else {
                                onSignupFailed(e.getMessage());
                            }
                        }

                    });
        } catch (Exception e) {
            onSignupFailed(e.getMessage());
        }
    }


    public void onSignupSuccess(JsonObject userJson) {
        User user = User.createUserFromJson(userJson);
        User.proceedAuth(user);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void onSignupFailed(String message) {
        if (message != "") {
            Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
        }
        _signupButton.setEnabled(true);
        User.proceedDeAuth();
    }

    public boolean validate() {
        boolean valid = true;

        String phone = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();

        if (phone.isEmpty() || phone.length() != 16) {
            Toast.makeText(
                    getBaseContext(),
                    "Введите номер телефона в формате +7 000 000-00-00",
                    Toast.LENGTH_LONG
            ).show();
            valid = false;
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            Toast.makeText(
                    getBaseContext(),
                    "Пароль дожен быть не меньше 4 и не больше 10 символов",
                    Toast.LENGTH_LONG
            ).show();
            valid = false;
        }

        return valid;
    }
}