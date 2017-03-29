package li.resonance.newsDownloader.db;

import li.resonance.newsDownloader.db.objects.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    
    private static final Date NOW = new Date(System.currentTimeMillis());
    private static Connection connection = ConnectionFactory.getInstance().getConnection();
    
    public static void insertNewsStory(NewsStory story) {
        try {
            String sql = "insert into news_story (title, url, section_id) values (?,?,?);";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, story.getTitle());
            statement.setString(2, story.getUrl());
            statement.setInt(3, story.getSection().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Either inserts or updates a story
     *
     * @param story
     */
    public static void registerNewsStory(NewsStory story) {
        NewsStory fromDB = getIncompleteNewsStoryByUrl(story.getUrl());
        if (fromDB == null) {
            insertNewsStory(story);
        } else {
            if (!fromDB.getRecorded().after(NOW)) {
                updateNewsStory(story, fromDB.getId());
            }
        }
    }
    
    private static void updateNewsStory(NewsStory story, int id) {
        try {
            String sql = "UPDATE news_story SET title=?, recorded=now(), section_id=? WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, story.getTitle());
            statement.setInt(2, story.getSection().getId());
            statement.setInt(3, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static NewsStory getIncompleteNewsStoryByUrl(String url) {
        try {
            String sql =
                    "SELECT news_story.id, news_story.title, news_story.url, news_story.recorded, news_story.section_id " +
                    "FROM news_story WHERE url=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, url);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return new NewsStory(result.getInt("id"), result.getString("title"), result.getTimestamp("recorded"),
                                     result.getString("url"), null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static List<ParseRule> getParseRules() {
        List<ParseRule> list = new ArrayList<>();
        try {
            String sql =
                    "select parse_rule.id, parse_rule.css_selector, parse_rule.text_selector, parse_rule.url_selector, " +
                    "section.id, section.name, section.url, section.url, source.id, source.name, source.homepage, " +
                    "language.id, language.name, " +
                    "country.id, country.name from parse_rule join section on section_id=section.id " +
                    "join source on source_id=source.id join language on language_id=language.id " +
                    "join country on country_id=country.id order by parse_rule.id;";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Language language = new Language(result.getInt("language.id"), result.getString("language.name"));
                Country country = new Country(result.getInt("country.id"), result.getString("country.name"));
                Source source = new Source(result.getInt("source.id"), result.getString("source.name"),
                                           result.getString("source.homepage"), language, country);
                Section section = new Section(result.getInt("section.id"), result.getString("section.name"),
                                              result.getString("section.url"), source);
                ParseRule rule = new ParseRule(result.getInt("parse_rule.id"), section,
                                               result.getString("css_selector"), result.getString("text_selector"),
                                               result.getString("url_selector"));
                list.add(rule);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public static List<String> getExcludedURLs() {
        List<String> list = new ArrayList<>();
        try {
            String sql = "select url from exclude_urls;";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                list.add(result.getString("url"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
}
