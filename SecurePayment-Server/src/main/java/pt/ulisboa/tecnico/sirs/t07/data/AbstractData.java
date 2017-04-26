package pt.ulisboa.tecnico.sirs.t07.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by trosado on 09/11/16.
 */
public abstract class AbstractData {
    private final Logger logger = LoggerFactory.getLogger(AbstractData.class);
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/bank";

    //  Database credentials
    static final String USER = "bank";
    static final String PASS = "bank";
    Connection conn;

    public AbstractData(){
        try{
            Class.forName(JDBC_DRIVER);
            logger.debug("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
        }catch(Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
