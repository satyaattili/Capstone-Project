
package in.mobileappdev.news.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class Article implements Parcelable
{

  private String author;
  private String title;
  private String description;
  private String url;
  private String urlToImage;
  private String publishedAt;
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();
  public final static Parcelable.Creator<Article> CREATOR = new Creator<Article>() {


    @SuppressWarnings({
            "unchecked"
    })
    public Article createFromParcel(Parcel in) {
      Article instance = new Article();
      instance.author = ((String) in.readValue((String.class.getClassLoader())));
      instance.title = ((String) in.readValue((String.class.getClassLoader())));
      instance.description = ((String) in.readValue((String.class.getClassLoader())));
      instance.url = ((String) in.readValue((String.class.getClassLoader())));
      instance.urlToImage = ((String) in.readValue((String.class.getClassLoader())));
      instance.publishedAt = ((String) in.readValue((String.class.getClassLoader())));
      instance.additionalProperties = ((Map<String, Object> ) in.readValue((Map.class.getClassLoader())));
      return instance;
    }

    public Article[] newArray(int size) {
      return (new Article[size]);
    }

  }
          ;

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUrlToImage() {
    return urlToImage;
  }

  public void setUrlToImage(String urlToImage) {
    this.urlToImage = urlToImage;
  }

  public String getPublishedAt() {
    return publishedAt;
  }

  public void setPublishedAt(String publishedAt) {
    this.publishedAt = publishedAt;
  }

  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }



  public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(author);
    dest.writeValue(title);
    dest.writeValue(description);
    dest.writeValue(url);
    dest.writeValue(urlToImage);
    dest.writeValue(publishedAt);
    dest.writeValue(additionalProperties);
  }

  public int describeContents() {
    return 0;
  }

  @Override
  public String toString() {
    return "Title : "+title + "\n URL : " + url + "\n IMAGE URL : " + urlToImage;
  }

}
