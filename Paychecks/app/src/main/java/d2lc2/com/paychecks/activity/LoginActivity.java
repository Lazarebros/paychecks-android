package d2lc2.com.paychecks.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import d2lc2.com.paychecks.R;
import d2lc2.com.paychecks.app.AppController;
import d2lc2.com.paychecks.bean.User;
import d2lc2.com.paychecks.helper.SQLiteHandler;
import d2lc2.com.paychecks.helper.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private SessionManager sessionManager;
    private SQLiteHandler db;

    public LoginActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = AppController.getInstance().getSessionManager();
        db = AppController.getInstance().getSQLiteHandler();

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        if (sessionManager.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public void performLogin(View view) {
        EditText inputUserName = findViewById(R.id.user_name);
        EditText inputPassword = findViewById(R.id.password);
        String userName = inputUserName.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        // Check for empty data in the form
        if (!userName.isEmpty() && !password.isEmpty()) {
            // login user
            performLogin(userName, password);

            if(sessionManager.isLoggedIn()) {
                inputUserName.setText(null);
                inputPassword.setText(null);
            }

        } else {
            // Prompt user to enter credentials
            Toast.makeText(getApplicationContext(), "Please enter the credentials!", Toast.LENGTH_LONG).show();
        }
    }

    private void performLogin(final String userName, final String password) {
        pDialog.setMessage("Logging in ...");
        showDialog();

        try {

            Call<User> call = AppController.getInstance().getApiInterface().performUserLogin(userName, password);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Log.d(TAG, "Login Response: " + response.toString());
                    hideDialog();

                    if (response != null && response.body() != null) {
                        if (response.body().isSuccess()) {
                            sessionManager.setLogin(true);
                            User user = response.body();
                            user.setPassword(password);

                            db.addUser(user);

                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
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
            Log.d(TAG, e.getMessage());
            Toast.makeText(getApplicationContext(), "Something went wrong...", Toast.LENGTH_LONG).show();
            hideDialog();
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

    public void goToRegistrationActivity(View view) {
        Intent i = new Intent(getApplicationContext(), RegistrationActivity.class);
        startActivity(i);
    }
}
