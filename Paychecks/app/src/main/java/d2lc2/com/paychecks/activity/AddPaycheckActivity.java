package d2lc2.com.paychecks.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.d2l2c.paycheck.util.bean.PaycheckUnit;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import d2lc2.com.paychecks.R;
import d2lc2.com.paychecks.app.AppController;
import d2lc2.com.paychecks.bean.User;
import d2lc2.com.paychecks.helper.SQLiteHandler;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPaycheckActivity extends AppCompatActivity {

    private static final String TAG = AddPaycheckActivity.class.getSimpleName();
    private static final int REQUEST_CODE = 43;

    private SQLiteHandler db;
    private String authKey;
    private byte[] fileByte;
    private Uri fileUri;
    private PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_paycheck);

        db = AppController.getInstance().getSQLiteHandler();
        User user = db.getUserDetails();
        authKey = getAuthorizationHeader(user.getUserName(), user.getPassword());

        pdfView = findViewById(R.id.pdfPreview);

        fileByte = getIntent().getByteArrayExtra(HomeActivity.EXTRA_FILE_BYTE);
        fileUri = (Uri) getIntent().getExtras().get(HomeActivity.EXTRA_FILE_URI);

        pdfView.fromBytes(fileByte)
                .defaultPage(0)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableAnnotationRendering(true)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();

    }

    public static String getAuthorizationHeader(String userName, String password) {
        String credential = userName + ":" + password;
        return "Basic " + Base64.encodeToString(credential.getBytes(), Base64.NO_WRAP);
    }

    public void uploadFile(View view) {
        RequestBody filePart = RequestBody.create(
                MediaType.parse(getContentResolver().getType(fileUri)),
                fileByte
        );

        Call<PaycheckUnit> call = AppController.getInstance().getApiInterface().uploadPaycheck(authKey, filePart);

        call.enqueue(new Callback<PaycheckUnit>() {
            @Override
            public void onResponse(Call<PaycheckUnit> call, Response<PaycheckUnit> response) {
                Log.d(TAG, response.toString());

                Intent intent = new Intent(AddPaycheckActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<PaycheckUnit> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something went wrong...", Toast.LENGTH_LONG).show();
                Log.e(TAG, "ERROR " + t.getMessage());
            }
        });
    }

}
