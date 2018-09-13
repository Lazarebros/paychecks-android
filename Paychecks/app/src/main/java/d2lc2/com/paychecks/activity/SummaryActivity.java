package d2lc2.com.paychecks.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.d2l2c.paycheck.util.bean.PaycheckSummary;

import java.util.List;

import d2lc2.com.paychecks.R;
import d2lc2.com.paychecks.adapter.PaycheckPagerAdapter;
import d2lc2.com.paychecks.app.AppController;
import d2lc2.com.paychecks.bean.User;
import d2lc2.com.paychecks.helper.SQLiteHandler;
import d2lc2.com.paychecks.helper.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SummaryActivity extends AppCompatActivity {

    private static final String TAG = SummaryActivity.class.getSimpleName();

    private SQLiteHandler db;
    private SessionManager sessionManager;

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        sessionManager = AppController.getInstance().getSessionManager();
        db = AppController.getInstance().getSQLiteHandler();

        viewPager = findViewById(R.id.view_pager);


        User user = db.getUserDetails();

        String authKey = getAuthorizationHeader(user.getUserName(), user.getPassword());

        Call<List<PaycheckSummary>> call = AppController.getInstance().getApiInterface().getPaychecks(authKey);

        call.enqueue(new Callback<List<PaycheckSummary>>() {
            @Override
            public void onResponse(Call<List<PaycheckSummary>> call, Response<List<PaycheckSummary>> response) {
                if (response.isSuccessful()) {
                    List<PaycheckSummary> paycheckSummaryList = response.body();

                    viewPager.setAdapter(new PaycheckPagerAdapter(getApplicationContext(), paycheckSummaryList));
                }
            }

            @Override
            public void onFailure(Call<List<PaycheckSummary>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });

    }

    /**
     * this method is API implemetation specific
     * might not work with other APIs
     **/
    public static String getAuthorizationHeader(String userName, String password) {
        String credential = userName + ":" + password;
        return "Basic " + Base64.encodeToString(credential.getBytes(), Base64.NO_WRAP);
    }
}