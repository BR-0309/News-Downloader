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
            //downloadTest2();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ConnectionFactory.getInstance().closeConnection();
    }
    
    private static void parseAll() throws IOException {
        System.out.println("Starting download...");
        for (ParseRule rule : Database.getParseRules()) {
            System.out.println("============\nRetrieving " + rule.getSection().getUrl());
            Document document = Jsoup.connect(rule.getSection().getUrl()).get();
            makeLinksAbsolute(document);
            System.out.println("Retrieved " + rule.getSection().getUrl());
            Elements classes = document.select(rule.getCssSelector());
            for (Element e : classes) {
                String title = e.text().trim();
                String url;
                boolean insert = true;
                if (rule.getTextTag() == null) {
                    url = e.attr("href");
                } else if (rule.getTextTag().equals("self_parent")) {
                    if (e.attr("href").isEmpty()) {
                        url = e.parents().get(0).attr("href");
                    } else {
                        url = e.attr("href");
                    }
                } else {
                    url = e.attr("href");
                }
                if (url.isEmpty()) {
                    continue;
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
            System.out.println("Parsed " + rule.getSection().getUrl());
        }
        System.out.println("Done!");
    }
    
    private static void downloadTest2() {
        System.out.println(":" + "\t".trim());
        try {
            Document document = Jsoup.connect("http://www.francetvinfo.fr/societe/").get();
            makeLinksAbsolute(document);
            Elements titles = document.select(".col-2 a");
            for (Element e : titles) {
                String url;
                if (e.attr("href").equals("")) {
                    url = e.parents().get(0).attr("href");
                } else {
                    url = e.attr("href");
                }
                if (!e.text().trim().isEmpty()) {
                    System.out.println(e.text().trim() + "\t" + url);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
