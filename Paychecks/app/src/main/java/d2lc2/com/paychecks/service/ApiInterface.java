package d2lc2.com.paychecks.service;

import com.d2l2c.paycheck.util.bean.PaycheckSummary;

import java.util.List;

import d2lc2.com.paychecks.bean.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("users/register")
    Call<User> performRegistration(@Query("first_name") String firstName, @Query("last_name") String lastName, @Query("user_name") String userName, @Query("user_password") String password, @Query("registration_code") String registrationCode);

    @GET("users/login")
    Call<User> performUserLogin(@Query("user_name") String userName, @Query("user_password") String userPassword);

    @GET("paychecks/{year}")
    Call<List<PaycheckSummary>> getPaychecks(@Header("Authorization") String authKey, @Path("year") int year);

}
