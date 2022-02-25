package iotawucon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.wso2.balana.cond.Condition;
import org.wso2.balana.ctx.Attribute;



/**
 * This class is designed to handle any xacml utils,
 * particularly automated generation for testing purposes.
 * Based on JABX (Java Architecture for XML Binding), turns
 * structured basic xml files into other xml files.
 */
public class XACMLutils {
		private static String URLRESOURCE = "/PATH_TO_FILE/IOTAwUCON/src/resource/";
			/**
			 * example using JABX to unmarshall a XACML file.
			 * Objects can be recovered directly in the shell
			 * with the fitting xacml file.
			 * @return PolicyType
			 */
		public static PolicyType unmarshallJABX() throws JAXBException, IOException {
			JAXBContext context = JAXBContext.newInstance(PolicyType.class);
			return (PolicyType) context.createUnmarshaller().unmarshal(new FileReader(URLRESOURCE + "policy_example_xacml3.xml"));
			}
			/**
			 * generate a random attribute fitting the xacml(3.0?)
			 * requirements for testing purposes.
			 * @return attribute generated
			 */
			public static Attribute generateRandomResource() {
				return null;
			}
			/**
			 * function that takes a xml file and replace a given
			 * string by another one.
			 * @param file where the change will occur
			 * @param string the string to be replaced in the
			 * original file.
			 * @param repString the replacement string.
			 */
			static void changeStringXml(final String file, final String string, final String repString) {

		        File inputXML = new File(file);
		        BufferedReader br = null;
		        String newString = "";
		        StringBuilder strTotale = new StringBuilder();
		        try {

		        FileReader reader = new FileReader(inputXML);
		        String search = string;


		        br = new BufferedReader(reader);
		        while ((newString = br.readLine()) != null) {
		            newString = newString.replaceAll(search, repString);
		            strTotale.append(newString);
		        }

		        } catch (IOException  e) {
		            e.printStackTrace();
		        }
		        finally
		        {
		            try {
		                br.close();
		            } catch (IOException e) {
		                e.printStackTrace();
		            }
		        }


		        System.out.println(strTotale.toString());


		    }
			/**
			 * return a random condition.
			 * @return condition randomly generated
			 */
			public static Condition generateRandomCondition() {
				return null;

			}
		  /**
		   * function whose purpose is to generate a first xml file,
		   * to serve as a basis for the xacml file.
		   * @return nothing
		   */
		  public static void generateXml() {
			  return;
		  }
		  
		    public static String[] findAttributeValue() throws Exception {
		    	String attrChanging[] = new String[3];
		        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		        DocumentBuilder db = dbf.newDocumentBuilder();
		        Document document = db.parse(new File(URLRESOURCE + "policies/changing.xml"));
		        NodeList nodeList = document.getElementsByTagName("AttributeValue");
		        attrChanging[0] = nodeList.item(0).getFirstChild().getNodeValue();//the buyer address
		        attrChanging[1] = nodeList.item(1).getFirstChild().getNodeValue();// the iota value
		        attrChanging[2] = nodeList.item(4).getFirstChild().getNodeValue();// the dest address
		        //for(int x=0,size= nodeList.getLength(); x<size; x++) {
		        //    System.out.println(nodeList.item(x).getFirstChild().getNodeValue());
		        //}
		        System.out.println(attrChanging[0]);
		        System.out.println(attrChanging[1]);
		        System.out.println(attrChanging[2]);
		        return attrChanging;
		}
}


