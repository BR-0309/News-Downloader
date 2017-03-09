package raison.benjamin.newsDownloader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import raison.benjamin.newsDownloader.db.ConnectionFactory;
import raison.benjamin.newsDownloader.db.Database;
import raison.benjamin.newsDownloader.db.objects.ParseRule;

import java.io.IOException;

public class Main {
    
    public static void main(String[] args) {
        try {
            parseAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ConnectionFactory.getInstance().closeConnection();
    }
    
    private static void parseAll() throws IOException {
        System.out.println("Starting download...");
        for (ParseRule rule : Database.getParseRules()) {
            System.out.println("Retrieving " + rule.getSection().getUrl());
            Document document = Jsoup.connect(rule.getSection().getUrl()).get();
            makeLinksAbsolute(document);
            System.out.println("Retrieved!");
            Elements classes = document.getElementsByClass(rule.getCssClass());
            for (Element e : classes) {
                String title = e.text();
                String url;
                boolean insert = true;
                if (rule.getTextTag() == null) {
                    url = e.attr("href");
                } else if (rule.getTextTag().equals("parent")) {
                    url = e.parents().get(0).attr("href");
                } else {
                    url = e.attr("href");
                }
                for (String pattern : rule.getExcludeUrls()) {
                    if (pattern.endsWith("*")) {
                        if (url.startsWith(pattern.substring(0, pattern.length() - 1))) {
                            insert = false;
                            break;
                        }
                    } else if (url.equals(pattern)) {
                        insert = false;
                        break;
                    }
                }
                if (insert) {
                    Database.insertNewsStory(title, url, rule.getSection().getSource());
                }
            }
            /*
            if(rule.getTextTag() == null) {
                for (Element e : classes) {
                    String title = e.text();
                    Database.insertNewsStory(title, e.parents().get(0).attr("href"), rule.getSection().getSource());
                }
            }else {
                for (Element e : classes) {
                    String title = e.getElementsByTag(rule.getTextTag()).get(0).text();
                    Database.insertNewsStory(title, e.attr("href"), rule.getSection().getSource());
                }
            }*/
        }
        System.out.println("Done!");
    }
    
    /*
    * Main page
    * gs-o-faux-block-link -> h3
    *
    * Any other
    * title-link__title -> text()
    * */
    private static void downloadTest2() {
        // There was this title__something span or div or something. Whatever it was, use that.
        try {
            Document document = Jsoup.connect("http://www.faz.net").get();
            makeLinksAbsolute(document);
            //Elements titles = document.getElementsByClass("gs-o-faux-block-link");
            Elements titles = document.getElementsByClass("TeaserHeadLink");
            for (Element e : titles) {
                System.out.println(e.text() + "\t" + e.attr("href"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /*private static void downloadTest() {
        try {
            Source source = new SourceJDBCDao().findSourceByID(1);
            for (Subsection subsection : new SubsectionJDBCDao().getSubsections(source)) {
                String match = subsection.getUrl().substring(0, subsection.getUrl().length() - 1);
                System.out.println(match);
                Document document = document = Jsoup.connect(subsection.getUrl()).get();
                makeLinksAbsolute(document);
                Element main = document.getElementsByClass("container--primary-and-secondary-columns column-clearfix")
                                       .get(0);
                NewsStoryJDBDao dao = new NewsStoryJDBDao();
                for (Element e : main.select("a[href]")) {
                    if (!e.parent().is("span") && !e.parent().is("li") && e.html().startsWith("<") &&
                        e.attr("href").matches(".*\\d$") && e.attr("href").startsWith(match)) {
                        String url = e.attr("href");
                        System.out.println("Get: " + url);
                        String title = e.getElementsByTag("h3").get(0).text();
                        String content = getHtmlFromURL(url);
                        NewsStory story = new NewsStory(0, title, new Timestamp(System.currentTimeMillis()), url, subsection, null);
                        dao.insertNewsStory(story);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private static String getHtmlFromURL(String url) {
        try {
            Document document = Jsoup.connect(url).get();
            makeLinksAbsolute(document);
            return document.outerHtml();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/
    
    public static void makeLinksAbsolute(Document document) {
        Elements elements = document.getAllElements();
        for (Element e : elements) {
            //if (!e.parent().is("span") && !e.parent().is("li")) {
            if (e.is("a") || e.is("link")) {
                e.attr("href", e.absUrl("href"));
            } else if (e.is("img")) {
                e.attr("src", e.absUrl("src"));
            }
        }
    }
    
}
