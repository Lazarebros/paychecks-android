package d2lc2.com.paychecks.bean;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("SUCCESS")
    private Boolean success;

    @SerializedName("ERROR_MSG")
    private String errorMessage;

    @SerializedName("USER_UID")
    private String uid;

    @SerializedName("USER_NAME")
    private String userName;

    @SerializedName("PASSWORD")
    private String password;

    @SerializedName("FIRST_NAME")
    private String firstName;

    @SerializedName("LAST_NAME")
    private String lastName;

    @SerializedName("DATE_CREATED")
    private String dateCreated;

    @SerializedName("DATE_UPDATED")
    private String dateUpdated;

    public Boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(String dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", dateUpdated='" + dateUpdated + '\'' +
                '}';
    }
}
