package in.mobileappdev.news.models;

import java.util.List;

/**
 * Created by satyanarayana.avv on 29-12-2016.
 */

public class ErrorResponse {

    public List<String> messages;

    public ErrorResponse(List<String> messages) {
        this.messages = messages;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}
