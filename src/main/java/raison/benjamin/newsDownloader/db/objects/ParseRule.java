package raison.benjamin.newsDownloader.db.objects;

public class ParseRule {
    
    private int id;
    private Section section;
    private String cssClass;
    private String textTag;
    private String[] excludeUrls;
    
    public ParseRule(int id, Section section, String cssClass, String textTag, String excludeUrls) {
        this.id = id;
        this.section = section;
        this.cssClass = cssClass;
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
    
    public String getCssClass() {
        return cssClass;
    }
    
    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
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
