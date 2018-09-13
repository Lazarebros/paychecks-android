package d2lc2.com.paychecks.service;

import com.d2l2c.paycheck.util.bean.PaycheckSummary;
import com.d2l2c.paycheck.util.bean.PaycheckUnit;

import java.io.OutputStream;
import java.util.List;

import d2lc2.com.paychecks.bean.User;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("users/register")
    Call<User> performRegistration(
            @Query("first_name") String firstName,
            @Query("last_name") String lastName,
            @Query("user_name") String userName,
            @Query("user_password") String password,
            @Query("registration_code") String registrationCode
    );

    @GET("users/login")
    Call<User> performUserLogin(
            @Query("user_name") String userName,
            @Query("user_password") String userPassword
    );

    @GET("paychecks/{year}")
    Call<List<PaycheckSummary>> getPaychecks(
            @Header("Authorization") String authKey,
            @Path("year") int year
    );

    @GET("paychecks/summary")
    Call<List<PaycheckSummary>> getPaychecks(
            @Header("Authorization") String authKey
    );

    @POST("paychecks/upload")
    Call<PaycheckUnit> uploadPaycheck(
            @Header("Authorization") String authKey,
            @Body RequestBody body
    );

}
