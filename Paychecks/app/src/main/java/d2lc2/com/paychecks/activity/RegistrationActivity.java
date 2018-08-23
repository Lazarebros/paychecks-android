package d2lc2.com.paychecks.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import d2lc2.com.paychecks.R;
import d2lc2.com.paychecks.app.AppController;
import d2lc2.com.paychecks.bean.User;
import d2lc2.com.paychecks.helper.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = RegistrationActivity.class.getSimpleName();

    private Button btnLinkToLogin;
    private ProgressDialog pDialog;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        btnLinkToLogin = findViewById(R.id.btnLinkToLoginScreen);
        sessionManager = new SessionManager(getApplicationContext());

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        if (sessionManager.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void performRegistration(View view) {
        EditText inputFirstName = findViewById(R.id.first_name);
        EditText inputLastName = findViewById(R.id.last_name);
        EditText inputUserName = findViewById(R.id.user_name);
        EditText inputPassword = findViewById(R.id.password);
        EditText inputConfirmPassword = findViewById(R.id.confirm_password);
        EditText inputRegistrationCode = findViewById(R.id.registration_code);

        String firstName = inputFirstName.getText().toString().trim();
        String lastName = inputLastName.getText().toString().trim();
        String userName = inputUserName.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        String confirmPassword = inputConfirmPassword.getText().toString().trim();
        String registrationCode = inputRegistrationCode.getText().toString().trim();

        // Check for empty data in the form
        if (!firstName.isEmpty() && !lastName.isEmpty() && !userName.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty() && !registrationCode.isEmpty()) {
            if (password.equals(confirmPassword)) {
                // login user
                performRegistration(firstName, lastName, userName, password, registrationCode);

            } else {
                Toast.makeText(getApplicationContext(), "Passwords don't matched!", Toast.LENGTH_LONG).show();
            }
        } else {
            // Prompt user to enter details
            Toast.makeText(getApplicationContext(), "Please enter your details!", Toast.LENGTH_LONG).show();
        }
    }

    private void performRegistration(final String firstName, final String lastName, final String userName, final String password, String registrationCode) {
        pDialog.setMessage("Registering ...");
        showDialog();
        try {
            Call<User> call = AppController.getInstance().getApiInterface().performRegistration(firstName, lastName, userName, password, registrationCode);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Log.d(TAG, "Registration Response: " + response.toString());
                    hideDialog();

                    if (response != null && response.body() != null) {
                        if (response.body().isSuccess()) {
                            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), response.body().getErrorMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Something went wrong...", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e(TAG, "Login Error: " + t.getMessage());
                    Toast.makeText(getApplicationContext(), "Something went wrong...", Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            });

        } catch (Exception e) {

        }
    }

    private void showDialog() {
        if (!pDialog.isShowing()) {
            pDialog.show();
        }
    }

    private void hideDialog() {
        if (pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    public void goToLoginActivity(View view) {
        Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(i);
    }
}
