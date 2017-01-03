package in.mobileappdev.news.models;

/**
 * Udacity
 * Created by satyanarayana.avv on 03-01-2017.
 */

public class TokenResponse {

    private Boolean error;
    private String error_msg;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return error_msg;
    }

    public void setMessage(String message) {
        this.error_msg = message;
    }

}
