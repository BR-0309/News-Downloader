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
        }
        System.out.println("Done!");
    }
    
    
    private static void makeLinksAbsolute(Document document) {
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
