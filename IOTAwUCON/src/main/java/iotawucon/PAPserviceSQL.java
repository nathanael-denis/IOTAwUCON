package iotawucon;

import java.io.IOException;
import java.sql.Connection;
import java.util.logging.Logger;

import org.wso2.balana.Policy;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * the PAP Service is used by the Policy Administration Point to
 * interact with the database (table policies), to retrieve policies,
 * update or delete them. The database is queried using low-level
 * JBDC. JBDC needs drivers for the database used (sqlite3 in our
 * case) and may trigger warnings if not installed.
 */


public class PAPserviceSQL {
	/**
	 * use to log messages on the PAPservice.
	 */
	public static final Logger LOGGER = Logger.getLogger(PAPserviceSQL.class.getName());
	/**
	 * connection to the sql database.
	 */
	private static Connection connection;
	/**
	 * fetch all the policies from the database.
	 */
	public static void getAllPolicies() {
	    try {
	    	/*
	    	 * change path to your local database.
	    	 */
	        String url = "jdbc:sqlite:PATH_TO_POLICY/policy.db";
	        connection = DriverManager.getConnection(url);
	        Statement stmt = null;
	        String query = "select * from policies";
	        try {
	            stmt = connection.createStatement();
	            ResultSet rs = stmt.executeQuery(query);
	            while (rs.next()) {
	                String name = rs.getString("title");
	                System.out.println(name);
	            }
	        } catch (SQLException e) {
	            throw new Error("Problem connecting to database", e);
	        } finally {
	            if (stmt != null) {
	            	stmt.close();
	            }
	        }

	      } catch (SQLException e) {
	          throw new Error("Problem connecting to the database", e);
	      } finally {
	        try {
	          if (connection != null) {
	              connection.close();
	          }
	        } catch (SQLException ex) {
	            System.out.println(ex.getMessage());
	        }
	      }
	    }
	/**
	 * add a policy in the store using sun most simple constructor.
	 * The policy is added in the policies table of the
	 * @param policy added to the store
	 * @throws SQLexeption
	 * @return long the generated id of the policy.
	 * @throws IOException when reading url.
	 */
	public static long addPolicy(final Policy policy) throws IOException {
		String sql = "INSERT INTO policies(uri,combining_alg,description,target)"
                + "VALUES(?,?,?,?)";
		long id = 0;
		String url = ConfigUtils.readUrl();
        try (Connection conn =
        		DriverManager.getConnection("jdbc:sqlite:"+ url);
                PreparedStatement pstmt = conn.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
        	//le choix des types dans la base de donnée est très discutable
            pstmt.setString(1, policy.getId().toString());
            pstmt.setString(2, policy.getCombiningAlg().toString());
            pstmt.setString(3, policy.getDescription().toString());
            pstmt.setString(4, policy.getTarget().toString());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                // get the ID back
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        id = rs.getLong(1);
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return id;
	}
	/**
	 * remove policy from the store.
	 * @param policy , or any policy identifiers
	 * @throws IOException when reading the cassandra URL.
	 */
	public static void removePolicy(final Policy policy) throws IOException {
		String sql = "DELETE FROM policies WHERE uri = ?;";
		String url = ConfigUtils.readUrl();
		try (Connection conn =
        		DriverManager.getConnection("jdbc:sqlite:" + url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
					pstmt.setString(1, policy.getId().toString());
					pstmt.executeUpdate(sql);
					ResultSet rs = pstmt.executeQuery(sql);
			        while (rs.next()) {
			            //Display URI of removed policy
			            System.out.print("ID: "
			               + rs.getString(policy.getId().toString()));

			         }
			         rs.close();
		} catch (SQLException e) {
	         e.printStackTrace();
		}
	}
}


