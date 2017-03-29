package li.resonance.newsDownloader.db.objects;

public class Source {
    
    private int id;
    private String name;
    private String homepage;
    private Language language;
    private Country country;
    
    public Source(int id, String name, String homepage, Language language, Country country) {
        this.id = id;
        this.name = name;
        this.homepage = homepage;
        this.language = language;
        this.country = country;
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
    
    public String getHomepage() {
        return homepage;
    }
    
    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }
    
    public Language getLanguage() {
        return language;
    }
    
    public void setLanguage(Language language) {
        this.language = language;
    }
    
    public Country getCountry() {
        return country;
    }
    
    public void setCountry(Country country) {
        this.country = country;
    }
}
