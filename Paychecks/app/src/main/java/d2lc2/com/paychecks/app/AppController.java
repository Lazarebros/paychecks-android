package d2lc2.com.paychecks.app;

import android.app.Application;

import d2lc2.com.paychecks.helper.SQLiteHandler;
import d2lc2.com.paychecks.service.ApiClient;
import d2lc2.com.paychecks.service.ApiInterface;

public class AppController extends Application {

    private static AppController mInstance;

    private ApiInterface apiInterface;
    private SQLiteHandler sqLiteHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public ApiInterface getApiInterface() {
        if (apiInterface == null) {
            apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        }
        return apiInterface;
    }

    public SQLiteHandler getSQLiteHandler() {
        if (sqLiteHandler == null) {
            sqLiteHandler = new SQLiteHandler(getApplicationContext());
        }
        return sqLiteHandler;
    }

}
