package iotawucon;

import java.util.logging.Logger;

import org.wso2.balana.PDP;



/**
 * Context Handler
 * The main part of the Usage Control System, responsible of
 * exchanging information and routing processing among the various components.
 * It receives access requests from the PEP, and enhances the requests the with
 * the attribute values that the PIPs acquire from the Attribute Manager (AM)s.
 * Then sends the enhanced request to the PDP for evaluation, and forwards the
 * result to the PEP and takes the necessary internal actions on the UCS
 * according to the result.
 */

public class ContextHandler {
	private static final Logger LOGGER = Logger.getLogger(PDP.class.getName());
	/**
	 * pdp attached to the CH
	 */
	private Pdp pdp = null;
	/**
	 * pap attached to the CH
	 */
	private Pap pap = null;
	/**
	 * pep attached to the CH, one for each controlled system, collection?.
	 */
	private Pep pep = null;
	/**
	 * pip attached to the CH, one for each attr environment, collection?.
	 */
	private Pip pip = null;
   /**
   * if during the continuous re-evaluation, an attribute changes its
   * value and violates the policy, the UCS detects it and informs the session
   * manager to keep the session recorded but in an inactive step.
   *
   * @param sessionId the id of the session to be revoked.
   */
   public void revokeAccessSessionManager(int sessionId) {
   }
   /**
   * If during the continuous re-evaluation, an attribute changes its
   * value and violates the policy,
   * the UCS detects it and informs the PEP to stop
   * the access.
   *
   * @param sessionId the id of the session to be revoked.
   */
 public void revokeAccessPep(int sessionId) {
	 
   }
}
