package d2lc2.com.paychecks.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import d2lc2.com.paychecks.R;
import d2lc2.com.paychecks.app.AppController;
import d2lc2.com.paychecks.helper.SQLiteHandler;
import d2lc2.com.paychecks.helper.SessionManager;

public class HomeActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sessionManager = AppController.getInstance().getSessionManager();
        db = AppController.getInstance().getSQLiteHandler();
    }

    public void performLogout(View view) {
        sessionManager.setLogin(false);
        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void gotoSummaryActivity(View view) {
        // Launching the login activity
        Intent intent = new Intent(HomeActivity.this, SummaryActivity.class);
        startActivity(intent);
    }
}
