/*
 * Copyright 2017 Benjamin Raison
 *
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,software distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package raison.benjamin.newsDownloader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import raison.benjamin.newsDownloader.db.ConnectionFactory;
import raison.benjamin.newsDownloader.db.Database;
import raison.benjamin.newsDownloader.db.objects.NewsStory;
import raison.benjamin.newsDownloader.db.objects.ParseRule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    
    public static void main(String[] args) {
        try {
            parseAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ConnectionFactory.getInstance().closeConnection();
        System.out.println("Goodbye! o/");
    }
    
    private static void parseAll() throws IOException {
        List<String> excludeUrls = Database.getExcludedURLs();
        for (ParseRule rule : Database.getParseRules()) {
            System.out.println("Fetching " + rule.getSection().getUrl());
            Document document = Jsoup.connect(rule.getSection().getUrl()).get();
            System.out.println("Parsing...");
            for (NewsStory story : parseDocument(document, rule, excludeUrls)) {
                Database.insertNewsStory(story);
            }
        }
    }
    
    private static List<NewsStory> parseDocument(Document document, ParseRule rule, List<String> ignoreUrls) {
        List<NewsStory> list = new ArrayList<>();
        makeLinksAbsolute(document);
        Elements classes = document.select(rule.getCssSelector());
        for (Element e : classes) {
            String title = getTextBySelector(e, rule.getTextSelector());
            String url = getAttrBySelector(e, "href", rule.getUrlSelector());
            boolean valid = true;
            for (String s : ignoreUrls) {
                if (s.endsWith("*")) {
                    if (url.startsWith(s.substring(0, s.length() - 1))) {
                        valid = false;
                        break;
                    }
                } else if (s.equals(url)) {
                    valid = false;
                    break;
                }
            }
            if (url.isEmpty()) {
                System.err.println("No url for title '" + title + "', section '" + rule.getSection().getUrl() + "'!");
                valid = false;
            }
            if (valid) {
                list.add(new NewsStory(title, url, rule.getSection()));
            }
        }
        return list;
    }
    
    private static String getAttrBySelector(Element element, String attr, String selector) {
        String url;
        switch (selector) {
            case "self":
                url = element.attr(attr);
                break;
            case "parent":
                url = element.parent().attr(attr);
                break;
            case "self_parent":
                if (!element.attr(attr).isEmpty()) {
                    url = element.attr(attr);
                } else {
                    url = element.parent().attr(attr);
                }
                break;
            default:
                url = element.select(selector).get(0).attr(attr);
                break;
        }
        return url;
    }
    
    private static String getTextBySelector(Element element, String selector) {
        String url;
        switch (selector) {
            case "self":
                url = element.text().trim();
                break;
            case "parent":
                url = element.parent().text().trim();
                break;
            case "self_parent":
                if (!element.text().trim().isEmpty()) {
                    url = element.text().trim();
                } else {
                    url = element.parent().text().trim();
                }
                break;
            default:
                url = element.select(selector).get(0).text().trim();
                break;
        }
        return url;
    }
    
    private static void makeLinksAbsolute(Document document) {
        Elements elements = document.getAllElements();
        for (Element e : elements) {
            if (e.is("a") || e.is("link")) {
                e.attr("href", e.absUrl("href"));
            } else if (e.is("img")) {
                e.attr("src", e.absUrl("src"));
            }
        }
    }
}
