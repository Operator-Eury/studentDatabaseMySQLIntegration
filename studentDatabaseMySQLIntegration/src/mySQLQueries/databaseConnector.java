/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mySQLQueries;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

/**
 *
 * @author John-Ronan Beira
 */
public class databaseConnector {
    
    private static Connection connection = null;
    
    private databaseConnector() {}
    
    public static Connection getConnection() {
        
        if (connection == null) {
            
            Properties properties = new Properties();
            
            try (InputStream input = databaseConnector.class.getClassLoader().getResourceAsStream("privateCredentials/credentials.properties")) {
                
                if (input != null) {
                    
                    properties.load(input);
                    String url = properties.getProperty("db.url");
                    String user = properties.getProperty("db.user");
                    String password = properties.getProperty("db.password");
                    
                    connection = DriverManager.getConnection(url, user, password);
                    
                } else {
                
                    System.err.println("Properties file not found.");
                    
                }
            } catch (IOException e) {
                
                System.err.println("IO Error: " + e.getMessage());
                
            } catch (SQLException e) {
                
                System.err.println("SQL Error: " + e.getMessage());
                
            }
        }
        
        return connection;
    }
}