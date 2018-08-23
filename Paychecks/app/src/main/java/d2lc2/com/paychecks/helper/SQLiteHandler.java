package d2lc2.com.paychecks.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import d2lc2.com.paychecks.bean.User;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "paychecks_android_api";

    private static final String TABLE_NAME_USER = "user";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_UID = "uid";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_DATE_CREATED = "date_created";
    private static final String KEY_DATE_UPDATED = "date_updated";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_NAME_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_UID + " TEXT,"
                + KEY_USER_NAME + " TEXT UNIQUE,"
                + KEY_PASSWORD + " TEXT,"
                + KEY_FIRST_NAME + " TEXT,"
                + KEY_LAST_NAME + " TEXT,"
                + KEY_DATE_CREATED + " TEXT,"
                + KEY_DATE_UPDATED + " TEXT" + ")";

        db.execSQL(CREATE_LOGIN_TABLE);
        Log.d(TAG, "Database tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USER);
        onCreate(db);
    }

    public void addUser(User user) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_UID, user.getUid());
            values.put(KEY_USER_NAME, user.getUserName());
            values.put(KEY_PASSWORD, user.getPassword());
            values.put(KEY_FIRST_NAME, user.getFirstName());
            values.put(KEY_LAST_NAME, user.getLastName());
            values.put(KEY_DATE_CREATED, user.getDateCreated());
            values.put(KEY_DATE_UPDATED, user.getDateUpdated());

            // Inserting Row
            long id = db.insert(TABLE_NAME_USER, null, values);
            Log.d(TAG, "New user inserted into sqlite: Id=" + id + " : " + user.toString() );
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public User getUserDetails() {
        User user = new User();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME_USER;

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);
            // Move to first row
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                user.setUid(cursor.getString(1));
                user.setUserName(cursor.getString(2));
                user.setPassword(cursor.getString(3));
                user.setFirstName(cursor.getString(4));
                user.setLastName(cursor.getString(5));
                user.setDateCreated(cursor.getString(6));
                user.setDateUpdated(cursor.getString(7));
            }
            Log.d(TAG, "Fetching user from Sqlite: " + user.toString());
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return user;
    }

    public void deleteUsers() {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            db.delete(TABLE_NAME_USER, null, null);
            Log.d(TAG, "Deleted all user info from sqlite");
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

}
