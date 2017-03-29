package li.resonance.newsDownloader.db.objects;

public class ParseRule {
    
    private int id;
    private Section section;
    private String cssSelector;
    private String textSelector;
    private String urlSelector;
    
    public ParseRule(int id, Section section, String cssSelector, String textSelector, String urlSelector) {
        this.id = id;
        this.section = section;
        this.cssSelector = cssSelector;
        this.textSelector = textSelector;
        this.urlSelector = urlSelector;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public Section getSection() {
        return section;
    }
    
    public void setSection(Section section) {
        this.section = section;
    }
    
    public String getCssSelector() {
        return cssSelector;
    }
    
    public void setCssSelector(String cssSelector) {
        this.cssSelector = cssSelector;
    }
    
    public String getTextSelector() {
        return textSelector;
    }
    
    public void setTextSelector(String textSelector) {
        this.textSelector = textSelector;
    }
    
    public String getUrlSelector() {
        return urlSelector;
    }
    
    public void setUrlSelector(String urlSelector) {
        this.urlSelector = urlSelector;
    }
}
