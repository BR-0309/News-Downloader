package raison.benjamin.newsDownloader.db.objects;

import java.sql.Timestamp;

public class NewsStory {
    
    private int id;
    private String title;
    private Timestamp recorded;
    private String url;
    private Source source;
    
    public NewsStory(String title, String url, Source source) {
        this.title = title;
        this.url = url;
        this.source = source;
    }
    
    public NewsStory(String title, Timestamp recorded, String url, Source source) {
        this.title = title;
        this.recorded = recorded;
        this.url = url;
        this.source = source;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public Timestamp getRecorded() {
        return recorded;
    }
    
    public void setRecorded(Timestamp recorded) {
        this.recorded = recorded;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public Source getSource() {
        return source;
    }
    
    public void setSource(Source source) {
        this.source = source;
    }
}
