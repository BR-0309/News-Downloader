package raison.benjamin.newsDownloader.db.objects;

public class Section {
    
    private int id;
    private String name;
    private String url;
    private Source source;
    
    public Section(int id, String name, String url, Source source) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.source = source;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
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
