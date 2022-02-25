package iotawucon;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.logging.Logger;

import org.wso2.balana.ConfigurationStore;
import org.wso2.balana.PDP;
import org.wso2.balana.PDPConfig;
import org.wso2.balana.ctx.ResponseCtx;
import org.wso2.balana.ctx.xacml3.RequestCtx;



/**
 * Policy Decision Point
 * Responsible for evaluating the policies. It takes as an input
 * the access request enhanced by attribute values along with the policy,
 * and outputs the result of the evaluation: Deny, Accept or Undetermined.
 */

public class Pdp {
	/**
	 * use to log messages on a specific component here the PDP.
	 */
	private static final Logger LOGGER = Logger.getLogger(PDP.class.getName());
	/**
	 * Take the sun standard as a base for the PDP.
	 */
	private PDP pdp = null;
	/**
	 * associate a pap with the pdp to retrieve the policies.
	 */
	private Pap pap = null;
	/**
	 * constructor for the PDP.
	 * @param configFile the configuration information to create the PDP
	 */
	public Pdp(final File configFile) {
		try {
			System.setProperty("com.sun.xacml.PDPConfigFile",
					configFile.getAbsolutePath());
			ConfigurationStore store = new ConfigurationStore();
			store.useDefaultFactories();
			PDPConfig config = store.getDefaultPDPConfig();
			pdp = new PDP(config);
		}
		catch (org.wso2.balana.ParsingException e) {
			LOGGER.warning(e.getMessage());
		}
		catch (org.wso2.balana.UnknownIdentifierException e) {
			LOGGER.warning(e.getMessage());
		}
	}
	

	/**
	* Evaluate the request after enhancements with attributes.
	*
	* @param request the context of the request created by the Pep.
	* @return a response paired to the request.
	*/
	public ResponseCtx evaluateRequest(final RequestCtx request) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		request.encode(output); //encode into .xml representation
		byte[] buffer = output.toByteArray();
		LOGGER.info(new String(buffer));
		return pdp.evaluate(request);
	   }
	}



