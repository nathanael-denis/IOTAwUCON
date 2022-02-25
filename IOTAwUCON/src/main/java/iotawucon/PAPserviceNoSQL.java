package iotawucon;



import java.io.File;
import java.net.InetSocketAddress;
import java.util.List;

import org.wso2.balana.Policy;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.github.javafaker.Faker;



/**
 * The PAP service is used by the Policy Administration
 * Point to query the policy database. As opposed to
 * the SQL PAP service, the NoSQL PAP service enable
 * to query Cassandra distributed database.
 */
public class PAPserviceNoSQL {
	/**
	 * The address of the running cassandra node.
	 */
	private static final String CONTACTPOINT = "127.0.0.1";
	/**
	 * Port to connect to (standard).
	 */
	private static final int PORT = 9042;
	/**
	 * The keyspace that defines data replication on nodes.
	 * To see the local keyspace, use DESCRIBE KEYSPACES
	 * on cqlsh.
	 */
	private static final String KEYSPACE = "simplekeyspace";
	/**
	 * the local datacenter. To list datacenters,
	 * type <cqlsh> use system; then
	 * <cqlsh> select data_center from local;
	 */
	private static String dataCenter = "datacenter1";
	/**
	 * return all the entries for a given column.
	 * Meant to be used for policies.
	 * @param table to be queried
	 * @param column whose rows will be printed
	 */
	/**
	 * Path to resources.
	 */
	private static String URLRESOURCE = "/PATH_TO_FILE/IOTAwUCON/src/resource/";
	public static void getAllEntriesFromRow(final String table, final String column) {
		try (CqlSession session = CqlSession.builder().addContactPoint(new InetSocketAddress(CONTACTPOINT, PORT))
			.withLocalDatacenter(dataCenter).withKeyspace(KEYSPACE).build()) {
		ResultSet rs = session.execute("select * from " + table); //change table according to use case
		for (Row row : rs.all()) {
			System.out.println(row.getString(column)); // change column according to use case
		}

	} catch (Exception e) {
		System.out.println(e.getMessage());
	}
	}
	/**
	 * insert a user into a Cassandra database. The user is
	 * randomely generated using a faker, also using a csv file
	 * for the car names and java.random for number and characters.
	 * @param table where the entries must be inserted.
	 * @param faker used as a basis to generate users.
	 */
	public static void insertUserIntoDatabase(final String table, final Faker faker) {
		try (CqlSession session = CqlSession.builder().addContactPoint(new InetSocketAddress(CONTACTPOINT, PORT))
				.withLocalDatacenter(dataCenter).withKeyspace(KEYSPACE).build()) {
			PreparedStatement prepared = session.prepare("insert into users (user_id,user_car, user_name) values (?,?,?)");
			int randomInt =  DataUtils.standardRandomInt(0, 1000000);
			String userName = faker.name().fullName();
			List<String> setOfCarManufacturer =
					DataUtils.csvToList(new File(URLRESOURCE + "car_manufacturers.csv"));
			String carManufacturer = DataUtils.pickRandomElementStringList(setOfCarManufacturer);
			String color = faker.color().name().toString();
			String carModel = DataUtils.generateString(DataUtils.standardRandomInt(0, 2)).toUpperCase();
			BoundStatement bound = prepared.bind(randomInt, carManufacturer + " " + carModel + " " + color, userName);
			session.execute(bound);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	/**
	 * the simplest way to achieve deleting all the table entries
	 * is to execute the commande TRUNCATE keyspace.table.
	 * This is used to clean the tables after tests.
	 * @param table to be clean.
	 */
	public static void deleteAllTableEntries(final String table) {
		try (CqlSession session = CqlSession.builder().addContactPoint(new InetSocketAddress(CONTACTPOINT, PORT))
				.withLocalDatacenter(dataCenter).withKeyspace(KEYSPACE).build()) {
			ResultSet rs = session.execute("TRUNCATE " + table);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	/**
	 * add the policy into the Cassandra database,
	 * under it's encoded form. Using the actual
	 * formatting is hard to handled with databases
	 * due to recursively defined types.
	 * @param policy the balana XACML policy.
	 * @return true if the insertion was successful.
	 */
	public static boolean addPolicyIntoDstrDatabase(final Policy policy) {
		String policyEncodedXML = policy.encode();
			try (CqlSession session = CqlSession.builder().addContactPoint(new InetSocketAddress(CONTACTPOINT, PORT))
					.withLocalDatacenter(dataCenter).withKeyspace(KEYSPACE).build()) {
				PreparedStatement prepared = session.prepare("insert into policies (policy_id,policyxml) values (?,?)");
				int randomInt =  DataUtils.standardRandomInt(0, 1000000);
				BoundStatement bound = prepared.bind(randomInt, policyEncodedXML);
				session.execute(bound);

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		return true;
		/*
		 * FUNCTIONS RELATED TO THE SECOND USE CASE, TEMPERATURE AND HUMIDITY
		 */
	}
	/**
	 * @param table where the entries must be inserted.
	 * @param temp the temperature instance added.
	 */
	public static void insertTempIntoDatabase(final String table, final Temperature temp) {
		try (CqlSession session = CqlSession.builder().addContactPoint(new InetSocketAddress(CONTACTPOINT, PORT))
				.withLocalDatacenter(dataCenter).withKeyspace(KEYSPACE).build()) {
			PreparedStatement prepared = session.prepare("insert into temperatures (key, temperatureVal, latitude, longitude, genTime, iotaPrice)"
					+ " values (?,?,?,?,?,?)");
			
			BoundStatement bound = prepared.bind(DataUtils.standardRandomInt(0, 100000), temp.getTemperatureVal(), temp.getLatitude(),
					temp.getLongitude(), temp.getGenTime().toString(), temp.getIotaPrice());
			session.execute(bound);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	/**
	 * @param table where the entries must be inserted.
	 * @param humlev the humidity level added to the database.
	 */
	public static void insertHumLevIntoDatabase(final String table, final HumidityLevel humlev) {
		try (CqlSession session = CqlSession.builder().addContactPoint(new InetSocketAddress(CONTACTPOINT, PORT))
				.withLocalDatacenter(dataCenter).withKeyspace(KEYSPACE).build()) {
			PreparedStatement prepared = session.prepare("insert into humiditylevels (key, humidityLevel, latitude, longitude, genTime, iotaPrice)"
					+ " values (?,?,?,?,?,?)");
			BoundStatement bound = prepared.bind(DataUtils.standardRandomInt(0, 100000),humlev.getHumidityLevel(), humlev.getLatitude(),
					humlev.getLongitude(), humlev.getGenTime().toString(),humlev.getIotaPrice());
			session.execute(bound);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	/*
	 * INSERTION OF BUYERS AND DATA BROKERS INTO THE DATABASE
	 */
	
	/**
	 * insert a data buyer into the Cassandra user database.
	 * There is no information about the user apart from its 
	 */
	public static void insertBuyerIntoDatabase() {
		try (CqlSession session = CqlSession.builder().addContactPoint(new InetSocketAddress(CONTACTPOINT, PORT))
				.withLocalDatacenter(dataCenter).withKeyspace(KEYSPACE).build()) {
			PreparedStatement prepared = session.prepare("insert into users (key, iota_address, role) values (?,?,?)");
			int randomInt =  DataUtils.standardRandomInt(0, 1000000);
			String[] addresses = IOTAUtils.generateUsableAddress();
			String usrAddr = addresses[1];
			String carModel = DataUtils.generateString(DataUtils.standardRandomInt(0, 2)).toUpperCase();
			BoundStatement bound = prepared.bind(randomInt, usrAddr, "buyer");
			session.execute(bound);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	/**
	 * insert a data buyer into the Cassandra user database.
	 * There is no information about the user apart from its 
	 */
	public static void insertBuyersIntoDatabase(int n) {
		for (int i = 0; i < n; i++) {
		try (CqlSession session = CqlSession.builder().addContactPoint(new InetSocketAddress(CONTACTPOINT, PORT))
				.withLocalDatacenter(dataCenter).withKeyspace(KEYSPACE).build()) {
			PreparedStatement prepared = session.prepare("insert into users (key, iota_address, role) values (?,?,?)");
			int randomInt =  DataUtils.standardRandomInt(0, 1000000);
			String[] addresses = IOTAUtils.generateUsableAddress();
			String usrAddr = addresses[1];
			BoundStatement bound = prepared.bind(randomInt, usrAddr, "buyer");
			session.execute(bound);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	}
	/**
	 * insert a data broker into the Cassandra user database.
	 */
	public static void insertBrokerIntoDatabase() {
		try (CqlSession session = CqlSession.builder().addContactPoint(new InetSocketAddress(CONTACTPOINT, PORT))
				.withLocalDatacenter(dataCenter).withKeyspace(KEYSPACE).build()) {
			PreparedStatement prepared = session.prepare("insert into users (key, iota_address, role) values (?,?,?)");
			int randomInt =  DataUtils.standardRandomInt(0, 1000000);
			String[] addresses = IOTAUtils.generateUsableAddress();
			String usrAddr = addresses[1];
			BoundStatement bound = prepared.bind(randomInt, usrAddr, "broker");
			session.execute(bound);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * insert several data brokers into the Cassandra user database.
	 * @param n the number of brokers
	 */
	public static void insertBrokersIntoDatabase(int n) {
		for (int i = 0; i < n; i++) {
		try (CqlSession session = CqlSession.builder().addContactPoint(new InetSocketAddress(CONTACTPOINT, PORT))
				.withLocalDatacenter(dataCenter).withKeyspace(KEYSPACE).build()) {
			PreparedStatement prepared = session.prepare("insert into users (key, iota_address, role) values (?,?,?)");
			int randomInt =  DataUtils.standardRandomInt(0, 1000000);
			String[] addresses = IOTAUtils.generateUsableAddress();
			String usrAddr = addresses[1];
			BoundStatement bound = prepared.bind(randomInt, usrAddr, "broker");
			session.execute(bound);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	}
	
	/**
	 * return all the entries for a given column.
	 * Meant to be used for policies.
	 * @param table to be queried
	 * @param column whose rows will be printed
	 * @return 
	 */
	public static String getRoleFromAddr(final String usrAddr) {
		try (CqlSession session = CqlSession.builder().addContactPoint(new InetSocketAddress(CONTACTPOINT, PORT))
			.withLocalDatacenter(dataCenter).withKeyspace(KEYSPACE).build()) {
		Row row = session.execute("SELECT role FROM users WHERE iota_address='"+usrAddr+"' ALLOW FILTERING").one();
		String role = row.getString("role");
		return role;
	} catch (Exception e) {
		System.out.println(e.getMessage());
	}
		return "Error: role not found";
	}
	
	/**
	 * insert a data broker into the Cassandra user database.
	 */
	public static void insertTransactionIntoDatabase() {
		try (CqlSession session = CqlSession.builder().addContactPoint(new InetSocketAddress(CONTACTPOINT, PORT))
				.withLocalDatacenter(dataCenter).withKeyspace(KEYSPACE).build()) {
			PreparedStatement prepared = session.prepare("insert into transactions (key, iota_address, role) values (?,?,?)");
			int randomInt =  DataUtils.standardRandomInt(0, 1000000);
			String[] addresses = IOTAUtils.generateUsableAddress();
			String usrAddr = addresses[1];
			BoundStatement bound = prepared.bind(randomInt, usrAddr, "broker");
			session.execute(bound);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	}



