package iotawucon;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.wso2.balana.attr.AttributeValue;
import org.wso2.balana.attr.BagAttribute;
import org.wso2.balana.attr.StringAttribute;
import org.wso2.balana.cond.EvaluationResult;
import org.wso2.balana.ctx.EvaluationCtx;
import org.wso2.balana.finder.AttributeFinderModule;

public class DatabaseAttributeFinderModule extends AttributeFinderModule {
	
	private URI defaultSubjectURI;
	
    public DatabaseAttributeFinderModule() {

        try {
            defaultSubjectURI = new URI("urn:oasis:names:tc:xacml:1.0:subject:subject-id");
        } catch (URISyntaxException e) {
           //ignore
        }
    }
    @Override
    public Set<String> getSupportedCategories() {
        Set<String> categories = new HashSet<String>();
        categories.add("urn:oasis:names:tc:xacml:1.0:subject-category:access-subject");
        categories.add("http://iotawucon/category");
        return categories;
    }
    @Override
    public boolean isDesignatorSupported() {
        return true;
    }
    /**
     * determine the role associated to a user,
     * either data broker or data buyer.
     * @param usrAddr
     * @return the role
     */
    static String findRole(String usrAddr){
    	return PAPserviceNoSQL.getRoleFromAddr(usrAddr);
    }
    public Set getSupportedIds() {
        Set<String> ids = new HashSet<String>();
        ids.add("role");
        return ids;   
    }
    
      @Override
    public EvaluationResult findAttribute(URI attributeType, URI attributeId, String issuer,
                                                            URI category, EvaluationCtx context) {
        String roleName = null;
        List<AttributeValue> attributeValues = new ArrayList<AttributeValue>();

        EvaluationResult result = context.getAttribute(attributeType, defaultSubjectURI, issuer, category);
        if(result != null && result.getAttributeValue() != null && result.getAttributeValue().isBag()){
            BagAttribute bagAttribute = (BagAttribute) result.getAttributeValue();
            if(bagAttribute.size() > 0){
                String userName = ((AttributeValue) bagAttribute.iterator().next()).encode();
                roleName = findRole(userName);  // not needed everyone has the buyer role, needs to pay however
            }
        }

        if (roleName != null) {
            attributeValues.add(new StringAttribute(roleName));
        }
        return new EvaluationResult(new BagAttribute(attributeType, attributeValues));
    }
}
