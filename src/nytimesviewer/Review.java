/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nytimesviewer;

/**
 *
 * @author Brian L
 */
public class Review implements java.io.Serializable {
    
    private String timeStamp;
    private String title;
    private String url;
    private String rating;
    private String desc;
    
    public Review(String timeStamp, String title, String url, String rating, String desc){
        this.timeStamp = timeStamp;
        this.title = title;
        this.url = url;
        this.rating = rating;
        this.desc = desc;
    }
    
    public String getTimeStamp() {
        return timeStamp;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public void setRating(String rating) {
        this.rating = rating;
    }
  
    public String getTitle() {
        return title;
    }
    
    public String getUrl() {
        return url;
    }
    
    public String getRating() {
        return rating;
    }
    
    public String getDesc(){
        return desc;
    }
  
    
}
