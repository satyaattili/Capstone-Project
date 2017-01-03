package in.mobileappdev.news.models;

/**
 * Created by satyanarayana.avv on 29-10-2016.
 */

public class Notification {

  private String notification;
  private String notificationImgUrl;
  private String createdAt;

  public Notification(String notification, String notificationImgUrl, String createdAt) {
    this.notification = notification;
    this.notificationImgUrl = notificationImgUrl;
    this.createdAt = createdAt;
  }

  public String getNotification() {
    return notification;
  }

  public void setNotification(String notification) {
    this.notification = notification;
  }

  public String getNotificationImgUrl() {
    return notificationImgUrl;
  }

  public void setNotificationImgUrl(String notificationImgUrl) {
    this.notificationImgUrl = notificationImgUrl;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }
}
