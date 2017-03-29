package li.resonance.newsDownloader.db.objects;

import java.sql.Timestamp;

public class NewsStory {
    
    private int id;
    private String title;
    private Timestamp recorded;
    private String url;
    private Section source;
    
    public NewsStory(String title, String url, Section source) {
        this.title = title;
        this.url = url;
        this.source = source;
    }
    
    public NewsStory(int id, String title, Timestamp recorded, String url, Section source) {
        this.id = id;
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
    
    
    public Section getSection() {
        return source;
    }
    
    public void setSource(Section source) {
        this.source = source;
    }
}
