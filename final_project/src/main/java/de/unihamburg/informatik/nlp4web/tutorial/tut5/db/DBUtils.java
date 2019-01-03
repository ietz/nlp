package de.unihamburg.informatik.nlp4web.tutorial.tut5.db;

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
	
    /**
     * select all rows in the warehouses table
     */
    public List<NewsModel> selectAllNews(){
        String sql = "SELECT id, authors, keywords, publishDate, real, source, text, title, url FROM news ORDER BY id ASC";
        
    	List<NewsModel> result = new ArrayList<>();
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
                    	
            // loop through the result set
            while (rs.next()) {
            	NewsModel model = new NewsModel(rs.getLong("id"), rs.getString("authors"), rs.getString("keywords"), 
            									rs.getLong("publishDate"), rs.getBoolean("real"), rs.getString("source"), 
            									rs.getString("text"), rs.getString("title"), rs.getString("url"));
            	result.add(model);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }
    
   /**
    * @param args the command line arguments
    */
   public static void main(String[] args) {
       DBUtils utils = new DBUtils("jdbc:sqlite:C:/Development/git/nlp/final_project/fakenewsnet.db");
       utils.selectAllNews();
   }
}
