package de.unihamburg.informatik.nlp4web.tutorial.tut5.db;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBUtils {
	
	private String url;
	
	public DBUtils(String url) {
		this.url = url;
	}
	
    /**
     * Connect to the test.db database
     * @return the Connection object
     */
    private Connection connect() {
        // SQLite connection string
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
	
    public void selectAllTitleAndReal() {
    	String sql = "SELECT title, real FROM news ";
    	
		try (Connection conn = this.connect();
			 Statement stmt  = conn.createStatement();
			 ResultSet rs    = stmt.executeQuery(sql)){

			try (PrintStream out = new PrintStream(new FileOutputStream("/home/inga/Documents/Uni/NLP4WEB/nlp/final_project/src/main/resources/db/titlereal.txt"))) {
				while (rs.next()) {
					out.print(rs.getString("title") + "\t" + rs.getBoolean("real") + "\n");
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}    	
    }
    
    public void selectAllTextAndReal() {
    	String sql = "SELECT text, real FROM news ";
    	
		try (Connection conn = this.connect();
			 Statement stmt  = conn.createStatement();
			 ResultSet rs    = stmt.executeQuery(sql)){

			try (PrintStream out = new PrintStream(new FileOutputStream("/home/inga/Documents/Uni/NLP4WEB/nlp/final_project/src/main/resources/db/textreal.txt"))) {
				while (rs.next()) {
					out.print(rs.getString("text").replaceAll("(\\r\\n|\\n)", " ") + "\t" + rs.getBoolean("real") + "\n");
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}    	
    }
    
    public static void main(String[] args) {
    	new DBUtils("jdbc:sqlite:/home/inga/Documents/Uni/NLP4WEB/nlp/final_project/src/main/resources/db/fakenewsnet.db").selectAllTextAndReal();
    }
    
    /**
     * select all rows in the warehouses table
     */
    public List<NewsModel> selectAllNews(){
        String sql = "SELECT id, authors, keywords, publishDate, real, source, text, title, url FROM news ORDER BY id ASC";

        return mapQueryResults(sql, rs -> new NewsModel(
        		rs.getLong("id"), rs.getString("authors"), rs.getString("keywords"),
				rs.getLong("publishDate"), rs.getBoolean("real"), rs.getString("source"),
				rs.getString("text"), rs.getString("title"), rs.getString("url")));
    }

    public List<UserModel> selectAllUsers() {
    	String sql = "SELECT id, name FROM user";

    	return mapQueryResults(sql, rs -> new UserModel(
    			rs.getLong("id"), rs.getString("name")
		));
	}

    public List<ShareRelation> selectAllShareRelations() {
    	String sql = "SELECT news_id, user_id, count FROM news_user";

    	return mapQueryResults(sql, rs -> new ShareRelation(
    			rs.getLong("news_id"), rs.getLong("user_id"), rs.getLong("count")
		));
	}

    public List<FollowRelation> selectAllFollowRelations() {
    	String sql = "SELECT user_id_1, user_id_2 FROM user_user";

    	return mapQueryResults(sql, rs -> new FollowRelation(
    			rs.getLong("user_id_1"), rs.getLong("user_id_2")
		));
	}



	private <T> List<T> mapQueryResults(String sql, ResultMapFn<T> mapFn) {
		List<T> result = new ArrayList<>();
		try (Connection conn = this.connect();
			 Statement stmt  = conn.createStatement();
			 ResultSet rs    = stmt.executeQuery(sql)){

			// loop through the result set
			while (rs.next()) {
				result.add(mapFn.map(rs));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return result;
	}

	@FunctionalInterface
   	private interface ResultMapFn<T> {
    	T map(ResultSet rs) throws SQLException;
	}
}
