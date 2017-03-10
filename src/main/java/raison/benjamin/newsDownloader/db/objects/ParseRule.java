package raison.benjamin.newsDownloader.db.objects;

public class ParseRule {
    
    private int id;
    private Section section;
    private String cssSelector;
    private String textTag;
    private String[] excludeUrls;
    
    public ParseRule(int id, Section section, String cssSelector, String textTag, String excludeUrls) {
        this.id = id;
        this.section = section;
        this.cssSelector = cssSelector;
        this.textTag = textTag;
        if (excludeUrls != null) {
            this.excludeUrls = excludeUrls.split(";");
        } else {
            this.excludeUrls = new String[]{};
        }
        
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
    
    public String getTextTag() {
        return textTag;
    }
    
    public void setTextTag(String textTag) {
        this.textTag = textTag;
    }
    
    public String[] getExcludeUrls() {
        return excludeUrls;
    }
    
    public void setExcludeUrls(String[] excludeUrls) {
        this.excludeUrls = excludeUrls;
    }
}
