package d2lc2.com.paychecks.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

import d2lc2.com.paychecks.R;
import d2lc2.com.paychecks.app.AppController;
import d2lc2.com.paychecks.helper.SQLiteHandler;
import d2lc2.com.paychecks.helper.SessionManager;

public class HomeActivity extends AppCompatActivity {

    public static final String EXTRA_FILE_BYTE = "d2lc2.com.paychecks.EXTRA_FILE_BYTE";
    public static final String EXTRA_FILE_URI = "d2lc2.com.paychecks.EXTRA_FILE_URI";

    private static final String TAG = HomeActivity.class.getSimpleName();
    private static final int REQUEST_CODE = 43;

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

        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void gotoSummaryActivity(View view) {
        Intent intent = new Intent(HomeActivity.this, SummaryActivity.class);
        startActivity(intent);
    }

    public void selectFile(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE);
    }

    public void goToPreviewPDFActivity(Uri fileUri) {
        Intent intent = new Intent(HomeActivity.this, AddPaycheckActivity.class);
        InputStream inputStream = null;
        try {
            inputStream = getContentResolver().openInputStream(fileUri);
            byte[] body = IOUtils.toByteArray(inputStream);
            intent.putExtra(EXTRA_FILE_BYTE, body);
            intent.putExtra(EXTRA_FILE_URI, fileUri);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Something went wrong...", Toast.LENGTH_LONG).show();
            Log.e(TAG, "ERROR: " + e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e(TAG, "ERROR: " + e.getMessage());
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri fileUri = data.getData();
                try {
                    goToPreviewPDFActivity(fileUri);
                } catch (Exception e) {
                    Log.e(TAG, "ERROR: " + e.getMessage());
                }
            }
        }
    }

}
