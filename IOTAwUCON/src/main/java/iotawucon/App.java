package iotawucon;
/**
 * Main module
 *
 */
public class App {
	/**
	 * @param args any arguments to be given
	 * @throws Exception 
	 */
    public static void main(String[] args) throws Exception {
    	/**
    	 * Minimum value for Hornet node to
    	 * approve a transaction.
    	 */
    	int MIOTA = 1000000;
    	//IOTA Java library
		System.loadLibrary("iota_client");
		// BEGINNING OF PERFORMANCE TESTS
		
		/* Distribute the IOTA Tokens to addresses used for testing.
		 * Might need a first distribution using the official faucet
		 * (Pelase, refer to readme.md file).
		 */
		// Performance tests for the peripheral model 
		IOTAUtils.homeMadeFaucet(500);
		SampleDataEnergy.eventChainAccessControlSeveralUsers(1000000, 500, "REMOTE");
		//Performance tests for the integrated model
		IOTAUtils.homeMadeFaucet(500);
		SampleDataEnergy.eventChainAccessControlSeveralUsers(1000000, 500, "LOCALHOST");
		//Compute the overhead introduced buy transaction querying
		SampleDataEnergy.remoteTransactionsOverhead(100, MIOTA);
    	}
}