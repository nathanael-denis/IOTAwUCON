package iotawucon;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;


import org.wso2.balana.ParsingException;
import org.wso2.balana.Policy;
import org.wso2.balana.finder.PolicyFinder;




/**
 * Policy Administration Point
 * Connect the policy database and retrieve the security policies,
 * before returning them to the Policy Decision Point.
 * Works as a policy storage
 */

public class Pap {
	/**
	 * used to log messages on a specific component, here the PAP.
	 */
	public static final Logger LOGGER = Logger.getLogger(Pap.class.getName());
	/**
	 * The directory where the policies are stored locally.
	 */
	private File policiesDirectory;
	/**
	 * to find all files with a given name pattern.
	 */
	private FilenameFilter xacmlFilter;
	/**
	 * PAP constructor
	 * @param policyDir where the policies are stored
	 */
	public Pap(final File policyDir) {
			this.policiesDirectory = policyDir;
			/**
			 * if the directory specified does not exist,
			 * the constructor creates one
			 * using the specified path.
			 */
			if (!policiesDirectory.exists()) {
				LOGGER.info("Creating dir for policies [" + policiesDirectory.getAbsolutePath() + "]");
				policiesDirectory.mkdirs();
			}
	}
	// OBSOLETE since PAPservice.java
	/**
	 * write all the policies into an array.
	 * @return the list of the policies as a array
	 */
	public ArrayList<String> getPolicies() {
		return getPolicies(policiesDirectory);
	}

	//OBSOLETE since PAPservice.java
	/**
	 * writes the policies from a file .txt
	 * into an given file (for external processing).
	 * @param dir the directory where the policies are written
	 * @return the list of the policies as an array
	 */
	private ArrayList<String> getPolicies(final File dir) {
		ArrayList<String> policies = new ArrayList<String>();
		if (dir.exists()) {
			LOGGER.info("Writing policies into " + dir.getAbsolutePath());
			String[] filenames = dir.list(xacmlFilter);
			for (String filename: filenames) {
				File file = new File(dir, filename);
				if (file.isFile()) {
					LOGGER.info("Loading XACML policy " + file.getAbsolutePath());
					policies.add(file.getAbsolutePath());
				}
				else {
					policies.addAll(getPolicies(file));
				}
			}
		}
		LOGGER.info(policies.size() + " policies loaded");
		return policies;
		}
	/**
	 * return all the policies of the database.
	 * @return boolean is the function worked properly.
	 */
	public boolean getAllPoliciesDatabase() {
		PAPserviceSQL.getAllPolicies();
		return true;
	}

	/**
	 * add a policy to the local store, e.g. if a user asks for it.
	 * @param policy the policy to be added to the dabatase,
	 * using wso2 balana standard
	 * @return boolean true if the policy is successfully added
	 * @throws IOException
	 */
	public boolean addPolicy(final Policy policy) throws IOException {
		PAPserviceSQL.addPolicy(policy);
		return true;
	}
	/**
	 * checks if a policy is in the store.
	 * @param policy to look for in the store.
	 * @return boolean true if the policy is in the policy store
	 */
	public boolean hasPolicy(final Policy policy) {
		return true;
	}
	/**
	 * remove the policy from the store at the demand of a user.
	 * @param policy the policy to be removed
	 * @return boolean, true if the policy is indeed destroyed
	 * @throws IOException
	 */
	public boolean removePolicy(final Policy policy) throws IOException {
		PAPserviceSQL.removePolicy(policy);
		return true;
	}
	/**
	 * nosql version of policy insertion. Note that the
	 * policy type must be a User Defined Type directly
	 * in the keyspace, otherwise the insertion will
	 * not work. The policy is written encoded in its
	 * xml form to avoid intricate type definition.
	 * @param policy to be added.
	 * @return true if the policy was successfully added.
	 */
	public boolean addPolicyCassandra(final Policy policy) {
		return PAPserviceNoSQL.addPolicyIntoDstrDatabase(policy);
	}
	
	public void removeAllPoliciesCassandra() {
		PAPserviceNoSQL.deleteAllTableEntries("Policies");
	}
}
