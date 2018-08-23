package d2lc2.com.paychecks.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import d2lc2.com.paychecks.R;

public class SessionManager {

    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context context;

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(context.getString(R.string.pref_file), context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(context.getString(R.string.pref_login_status), isLoggedIn);
        editor.commit();
        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(context.getString(R.string.pref_login_status), false);
    }
}
