package raison.benjamin.newsDownloader.db;

import raison.benjamin.newsDownloader.db.objects.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Database {
    
    private static Connection connection = ConnectionFactory.getInstance().getConnection();
    
    public static void insertNewsStory(NewsStory story) {
        if (containsNewsStoryUrl(story.getUrl())) {
            return;
        }
        try {
            String sql = "insert into news_story (title, url, source_id) values (?,?,?);";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, story.getTitle());
            statement.setString(2, story.getUrl());
            statement.setInt(3, story.getSource().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void insertNewsStory(String title, String url, Source source) {
        insertNewsStory(new NewsStory(title, url, source));
    }
    
    public static boolean containsNewsStoryUrl(String url) {
        try {
            String sql = "select id from news_story where url=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, url);
            ResultSet result = statement.executeQuery();
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static List<ParseRule> getParseRules() {
        List<ParseRule> list = new ArrayList<>();
        try {
            String sql = "select parse_rule.id, parse_rule.class, parse_rule.text_tag, parse_rule.exclude_urls, " +
                         "section.id, section.name, section.name, section.url, " +
                         "source.id, source.name, source.homepage, " + "language.id, language.name, " +
                         "country.id, country.name " + "from parse_rule join section on section_id=section.id " +
                         "join source on source_id=source.id " + "join language on language_id=language.id " +
                         "join country on country_id=country.id;";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Language language = new Language(result.getInt("language.id"), result.getString("language.name"));
                Country country = new Country(result.getInt("country.id"), result.getString("country.name"));
                Source source = new Source(result.getInt("source.id"), result.getString("source.name"),
                                           result.getString("source.homepage"), language, country);
                Section section = new Section(result.getInt("section.id"), result.getString("section.name"),
                                              result.getString("section.url"), source);
                ParseRule rule = new ParseRule(result.getInt("parse_rule.id"), section, result.getString("class"),
                                               result.getString("text_tag"), result.getString("exclude_urls"));
                list.add(rule);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
}
