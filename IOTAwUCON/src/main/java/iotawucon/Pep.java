package iotawucon;

import java.util.Set;

import org.w3c.dom.Node;
import org.wso2.balana.ctx.xacml3.RequestCtx;
import org.wso2.balana.xacml3.Attributes;


/**
 * Policy Enforcement Point
 * Send access request to the Context Handler for evaluation. After the
 * response from CH, enforces policy evaluation results
 * i.e. grants or denies access to the data.
 */




public class Pep {
	   /**
	   * create a request from XACML, that can be sent to the UCS,
	   * using Sun XACML request constructor.
	   * @param attributesSet the attributes to be given to the request.
	   * @param node the root node of the DOM tree for this requet.
	   * @return requestContext built by the PEP.
	   */
		//alternatively, take a XACML as parameter and process it?
       public static RequestCtx buildRequest(Set<Attributes> attributesSet, Node node) {
    	   RequestCtx request = new RequestCtx(attributesSet, node);
		 return request;
	}
	   /**
	   * create a request from XACML, that can be sent to the UCS
       * This is not supposed to be done by the PEP and could be part of utils,
       * but it is done here for convenience.
	   *
	   * @param requestCtx the request transformed with buildRequest
	   * @return the result of the try access
	   */
       public boolean tryAccess(final RequestCtx requestCtx) {
    	   return true;
       }
       /**
        * follows a successful try access.
        * @param sessionId the session id granted to the user who has access
        * @return boolean true if the access if successfully granted
        */
       public boolean startAccess(final int sessionId) {
    	   return true;
       }
       /**
        * ends the session when asked by the user.
        * @param sessionId the id of the session to be terminated
        * @return if the access was successful or not
        */
       public boolean endAccess(final int sessionId) {
    	   return true;
       }
       
}
