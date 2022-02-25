package iotawucon;

/**
 * Session Manager
 * A database which stores all active sessions, and the necessary informations
 * to perform the monitoring of the status of all the sessions.
 */


/*
 * For this module, the main problematic is to decide
 * either we manage the session of a mobile application (Android most likely)
 * or in a more theoretical context working only in the perimeter of the Java
 * project.
 * 
 * Another issue is to decide the form of the database
 * Either a collection on Java, or a sql database with external interactions.
 * The java collection is easier to implement, but a sql database would likely
 * be more efficient for tests. In the first case, a Session.java class must be
 * implemented as well.
 */
public class SessionManager {
	
}
