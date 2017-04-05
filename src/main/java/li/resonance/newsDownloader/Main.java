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
package li.resonance.newsDownloader;

import li.resonance.newsDownloader.db.ConnectionFactory;
import li.resonance.newsDownloader.db.Database;
import li.resonance.newsDownloader.db.objects.NewsStory;
import li.resonance.newsDownloader.db.objects.ParseRule;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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

    static void parseAll() throws IOException {
        List<String> excludeUrls = Database.getExcludedURLs();
        for (ParseRule rule : Database.getParseRules()) {
            System.out.println("Fetching " + rule.getSection().getUrl());
            Document document = Jsoup.connect(rule.getSection().getUrl()).get();
            System.out.println("Parsing...");
            List<NewsStory> stories = parseDocument(document, rule, excludeUrls);
            System.out.println("Saving...");
            for (NewsStory story : stories) {
                Database.registerNewsStory(story);
            }
        }
    }
    
    static List<NewsStory> parseDocument(Document document, ParseRule rule, List<String> ignoreUrls) {
        List<NewsStory> list = new ArrayList<>();
        makeLinksAbsolute(document);
        Elements classes = document.select(rule.getCssSelector());
        for (Element e : classes) {
            String title = getTextBySelector(e, rule.getTextSelector());
            String url = getAttrBySelector(e, "href", rule.getUrlSelector());
    
            if (url.isEmpty()) {
                System.err.println("No url for title '" + title + "', section '" + rule.getSection().getUrl() + "'!");
                continue;
            }
            
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
            if (valid) {
                list.add(new NewsStory(title, url, rule.getSection()));
            }
        }
        return list;
    }
    
    static String getAttrBySelector(Element element, String attr, String selector) {
        String attribute;
        switch (selector) {
            case "self":
                attribute = element.attr(attr);
                break;
            case "parent":
                attribute = getParentAttribute(element, attr);
                break;
            case "self_parent":
                if (!element.attr(attr).isEmpty()) {
                    attribute = element.attr(attr);
                } else {
                    attribute = getParentAttribute(element, attr);
                }
                break;
            default:
                attribute = element.select(selector).get(0).attr(attr);
                break;
        }
        return attribute;
    }
    
    private static String getParentAttribute(Element element, String attr) {
        String result = element.parent().attr(attr);
        if (result != null && !result.isEmpty()) {
            return result;
        }
        for (Element parent : element.parents()) {
            result = parent.attr(attr);
            if (result != null && !result.isEmpty()) {
                return result;
            }
        }
        return "";
    }
    
    static String getTextBySelector(Element element, String selector) {
        String text;
        switch (selector) {
            case "self":
                text = element.text().trim();
                break;
            case "parent":
                text = element.parent().text().trim();
                break;
            case "self_parent":
                if (!element.text().trim().isEmpty()) {
                    text = element.text().trim();
                } else {
                    text = element.parent().text().trim();
                }
                break;
            default:
                if (element.select(selector).size() == 0) {
                    text = element.attr(selector);
                } else {
                    text = element.select(selector).get(0).text().trim();
                }
                break;
        }
        return text;
    }
    
    static void makeLinksAbsolute(Document document) {
        Elements elements = document.getAllElements();
        for (Element e : elements) {
            if (e.is("a") || e.is("link")) {
                if (e.attr("href").startsWith("android-app") || e.attr("href").startsWith("ios-app")) continue;
                e.attr("href", e.absUrl("href"));
            } else if (e.is("img")) {
                e.attr("src", e.absUrl("src"));
            }
        }
    }
}
