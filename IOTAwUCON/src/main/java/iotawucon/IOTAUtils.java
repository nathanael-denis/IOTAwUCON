package iotawucon;


import java.util.Arrays;

import org.iota.client.BalanceAddressResponse;
import org.iota.client.Client;
import org.iota.client.GetAddressesBuilder;
import org.iota.client.Message;
import org.iota.client.MessageId;
import org.iota.client.MessageMetadata;
import org.iota.client.NodeInfoWrapper;

import org.iota.client.SecretKey;
import org.iota.client.local.ClientException;
public class IOTAUtils {
	private static final int HTTPSPORT = 441;
	private static final int APIPORT = 14265;
	private static final String SEED="SUPERSECURESEEDREALLY";
	private static final int MIOTA = 1000000;
	/**
	 * generate a secure seed used in
	 * turn to generate iota addresses.
	 * @return a random seed
	 */
//	public static String generateIOTASeed() {
//	    char[] chars = Constants.TRYTE_ALPHABET.toCharArray();
//	    StringBuilder builder = new StringBuilder();
//	    SecureRandom random = new SecureRandom();
//	    for (int i = 0; i < Constants.SEED_LENGTH_MAX; i++) {
//	        char c = chars[random.nextInt(chars.length)];
//	        builder.append(c);
//	    }
//	    return builder.toString();
//	}
	
	/*
	 * Part I: Connecting to different kind of nodes without knowing the
	 * address. Three nodes are considered: a devnet node and a mainnet
	 * node backed by the IOTA foundation, and the localhost (can connect
	 * to both networks)
	 */
	
	 /**
	  * basic client to connect to a remote node.
	  * The url is corresponding to the Chrysalis
	  * node maintained by the IOTA network.
	  * @return
	  */
	 private static Client RemoteNode() {
		 String URL = "https://chrysalis-nodes.iota.cafe:443";
	        Client iota = Client.Builder().withNode(URL) // Insert your node URL here
	                // .withNodeAuth("https://somechrysalisiotanode.com", "jwt_or_null",
	                // "name_or_null", "password_or_null") //
	                // Optional authentication
	                .finish();
	        return iota;
	    }
	  /**
	  * remote node information,
	  * maintained by the IOTA foundation.
	  */
	    public static void remoteNodeInfo() {
	        try {
	            Client iota = RemoteNode();

	            System.out.println("Node healthy: " + iota.getHealth());

	            NodeInfoWrapper info = iota.getInfo();
	            System.out.println("Node url: " + info.url());
	            System.out.println("Node Info: " + info.nodeInfo());
	        } catch (ClientException e) {
	            System.out.println("Error: " + e.getMessage());
	        }
	    }
	    /**
	     * get the information of the node
	     * locally deploy the UCS.
	     * @return the Client information.
	     */
	    public static Client localNode() {
	    	String URL = "http://localhost:14265";
	        Client iota = Client.Builder().withNode(URL) // Insert your node URL here
	                // .withNodeAuth("https://somechrysalisiotanode.com", "jwt_or_null",
	                // "name_or_null", "password_or_null") //
	                // Optional authentication
	                .finish();
	        return iota;
	    }
	    
		  /**
		  * local node information, exposed
		  * chrysalis node for load balancing.
		  */
		    public static void localNodeInfo() {
		        try {
		            Client iota = localNode();

		            System.out.println("Node healthy: " + iota.getHealth());

		            NodeInfoWrapper info = iota.getInfo();
		            System.out.println("Node url: " + info.url());
		            System.out.println("Node Info: " + info.nodeInfo());
		        } catch (ClientException e) {
		            System.out.println("Error: " + e.getMessage());
		        }
		    }
		    
		    /**
		     * basic client to connect to the
		     * development network.
		     * @return the Client information.
		     */
		    public static Client remoteDevnetNode() {
		    	String URL = "https://api.lb-0.h.chrysalis-devnet.iota.cafe/ ";
		        Client iota = Client.Builder().withNode(URL) // Insert your node URL here
		                // .withNodeAuth("https://somechrysalisiotanode.com", "jwt_or_null",
		                // "name_or_null", "password_or_null") //
		                // Optional authentication
		                .finish();
		        return iota;
		    }
			  /**
			  * devnet node information, ran by
			  * the IOTA foundation.
			  */
		    public static void remoteDevnetNodeInfo() {
		    	try {
		    		Client iota = remoteDevnetNode();
		            System.out.println("Node healthy: " + iota.getHealth());
		            NodeInfoWrapper info = iota.getInfo();
			        System.out.println("Node url: " + info.url());
			        System.out.println("Node Info: " + info.nodeInfo());
			        } catch (ClientException e) {
			            System.out.println("Error: " + e.getMessage());
			        }
			    }
		    
		    /*
		     * Part II: Sending transaction using a remote node and a local
		     * node. For the local node, the connection to the development
		     * network must be configured in the config.json file.
		     */
		    /**
		     * Generate several addresses associated to one seed.
		     * @return 10 generated addresses
		     */
		    public static String[] generateUsableAddress() {
		    	//used to generate the seed
		    	SecretKey secret_key = SecretKey.generate();
		    	//connect to a node to build several addresses from one seed.
		    	Client iota = remoteDevnetNode();
		    	String[] addresses = new GetAddressesBuilder(secret_key.toString()).withClient(iota).withRange(0, 10).finish();
		    	System.out.println(Arrays.toString(addresses));
				return addresses;
		    }

		    /**
		     * Will return the last
		     * four tips of the devnet node.
		     * @return the four last tips as a string[]
		     */
		    public static String[] getTipsFromDevNode() {
		    	Client iota = remoteDevnetNode();
		    	String[] tips = iota.getTips();
		    	return tips;
		    }
		   /**
		    *  This method will return the amount of
		    *  iota associated with a given address,
		    *  using the devnet load balancer.
		    * @param address to look up
		    * @return the response holding the balance
		    */
		   public static BalanceAddressResponse getBalanceDevnet(String address) {
			   Client iota = remoteDevnetNode();
			   return iota.getAddressBalance(address);
		   }
		   /**
		    * send a message on the devnet using the load balancer.
		    */
		   public static void sendEmptyMessageDevnet() {
			   Client iota = remoteDevnetNode();
			// Make and send an empty message
			Message messageToSend = iota.message().finish();
			// getMessage.metadata() returns message metadata from the MessageId we supplied
			MessageMetadata metadata = iota.getMessage().metadata(messageToSend.id());
			System.out.println("Message metadata: " + metadata);

			// Now we send a message by index "Hello". The message itself will contain "Tangle" as data here, but this could be anything.
			Message message = iota.message().withIndexString("Hello").withDataString("Tangle").finish();
			System.out.println("Message sent to devnet" + message.id());

			// Lets find all messages with the "Hello" index.
			// This will include the message we just send
			MessageId[] fetched_message_ids = iota.getMessage().indexString("Hello");

			// With these ids, we could look up the content on a per-id bases
			System.out.println("Messages with Hello index: " + Arrays.toString(fetched_message_ids));
			   
		   }
		   /**
		    * sends an empty message using the local node.
		    */
		   public static void sendEmptyMessageLocalNode() {
			   Client iota = localNode();
			// Make and send an empty message
			Message messageToSend = iota.message().finish();
			// getMessage.metadata() returns message metadata from the MessageId we supplied
			MessageMetadata metadata = iota.getMessage().metadata(messageToSend.id());
			System.out.println("Message metadata: " + metadata);

			// Now we send a message by index "Hello". The message itself will contain "Tangle" as data here, but this could be anything.
			Message message = iota.message().withIndexString("Hello").withDataString("Tangle").finish();
			System.out.println("Message sent to devnet" + message.id());

			// Lets find all messages with the "Hello" index.
			// This will include the message we just send
			MessageId[] fetched_message_ids = iota.getMessage().indexString("Hello");

			// With these ids, we could look up the content on a per-id bases
			System.out.println("Messages with Hello index: " + Arrays.toString(fetched_message_ids));
			   
		   }
		    /**
		     * transaction on the devnet. Takes the first address associated to
		     * the seed as the buyer and the given brokerAddr as destination.
		     * Note that the node will refuse the transaction if the balance has
		     * insufficient funds, which means we can keep the information locally.
		     * @param seed, enable to generate the buyer address from seed
		     * and have the private keys necessary for the transaction.
		     * @param brokerAddr
		     * @param value
		     */
		    public static String[] transaction(String seed, String brokerAddr, int value, String node) {
		    	Client iota = null;
				if (node.equals("LOCALHOST")) {
		    		iota  = localNode();
		    	}
				if (node.equals("REMOTE")) {
		    		iota  = remoteDevnetNode();
		    	}
		        GetAddressesBuilder addresses = iota.getAddresses(seed).withRange(0, 1);
		        String[] addresses_str = addresses.finish();
		        System.out.println("Buyer address generated :" + addresses_str[0]);
		        Message message = iota
		            .message()
		            .withSeed(seed)
		            .withOutput(
		                // We generate an address from our seed so that we send the funds to ourselves
		                        //iota.getAddresses(seed_1).withRange(0, 1).finish()[0], 1000000
		            		brokerAddr, value
		            ).finish();
		        //System.out.println("Transaction Id on network" + iota.getMessage().data(message.id()));
		        //System.out.println("Transaction sent: https://explorer.iota.org/devnet/message/" +  message.id());
		        // The transaction was validated and sent to the network, we store the transaction locally to
		        // reuse it. For that, we catch the args and send them to the PAP service.
		        String[] args = new String[4];
		        args[0] = addresses_str[0]; //buyer address
		        args[1] = brokerAddr;
		        args[2] = String.valueOf(value);
		        args[3] = message.id().toString();
		        return args;
		    }
		    /**
		     * used in remote setting to get message info query time,
		     * not having it locally. Not relevant in local
		     * setting since the UCS keeps the arguments (when used,
		     * query time is under 1ms in local setting).
		     * @param messageId the resulting id of the transaction.
		     * @return
		     */
		    public static void fetchMessage() {
		    	Client iota = remoteDevnetNode();
		    	Message message = iota
			            .message()
			            .withSeed(SEED).finish();
		    	long start_query= System.currentTimeMillis();
		    	Message data = iota.getMessage().data(message.id());
		    	long finish_query = System.currentTimeMillis();
		    	System.out.println(data);
		    	
		    	// Query time is about twice the time to ping 
		    	// the node. Takes 1ms for local node
		    	long query_time = finish_query-start_query;
		    	System.out.println(query_time);
		    }
		    /**
		     * generate a transaction then fetch it and print the time necessary for
		     * the operation. It does take into account the connection to the node since
		     * @param value
		     * @param node
		     */
		    public static void fetchTransactionTime(String seed, String brokerAddr, int value, String node) {
		    		Client iota = null;
					if (node.equals("LOCALHOST")) {
			    		iota  = localNode();
			    	}
					if (node.equals("REMOTE")) {
			    		iota  = remoteDevnetNode();
			    	}
			        GetAddressesBuilder addresses = iota.getAddresses(seed).withRange(0, 1);
			        Message message = iota
			            .message()
			            .withSeed(seed)
			            .withOutput(
			            		brokerAddr, value
			            ).finish();
			    	long start_query= System.currentTimeMillis();
			    	Message fetch = iota.getMessage().data(message.id());
			    	long finish_query = System.currentTimeMillis();
			    	
			    	// Query time is about twice the time to ping 
			    	// the node. Takes 1ms for local node
			    	long query_time = finish_query-start_query;
		    }
		    public static void processTransactionTime(String brokerAddress, int value) {
		    	/* Node used to connect and fetch the transaction,
		    	 * does not impact the UCS processing accessed in
		    	 * this function.
		    	 */
		    	Client iota = remoteDevnetNode(); 

		        GetAddressesBuilder addresses = iota.getAddresses(SEED).withRange(0, 1);
		        // Create and send the message to the node, not taken 
		        // into account for time measure as well.
		        Message message = iota
		            .message()
		            .withIndexString(brokerAddress)
		            .withSeed(SEED)
		            .withOutput(
		            		brokerAddress, value
		            ).finish();
		        // fetch the message
		        MessageId messageId = message.id();
		        System.out.println(messageId);
		    	Message toBeProcessed = iota.getMessage().data(message.id());
		    	System.out.println(toBeProcessed);
		    	long start_processing= System.currentTimeMillis();
		        String fetch_serialised = toBeProcessed.payload().get().serialize(); //almost 0
		        Boolean amount = fetch_serialised.contains("100000"); //almost 0
		        MessageId[] fetched_message_ids = iota.getMessage().indexString(brokerAddress);
		        int length = fetched_message_ids.length;
		        Boolean rightAddr= false;
		        if (messageId.equals(fetched_message_ids[0])) {
		        	rightAddr=true;
		        }
		        Boolean transactionIsValid = amount & rightAddr;
		    	long finish_processing = System.currentTimeMillis();
		    	long processing_time = finish_processing - start_processing;
		    	System.out.println(fetched_message_ids);
		    	System.out.println(fetch_serialised);
		    	System.out.println(processing_time);
		        System.out.println(amount);
		        System.out.println(transactionIsValid);
		    }
		    /**
		     * since we can't interact with the faucet algorithmically,
		     * we turn the first address of the reference SEED into a faucet 
		     * and then distribute to the other addresses.
		     * @param n
		     * @throws InterruptedException 
		     */
		    public static void homeMadeFaucet(int n) throws InterruptedException {
	    		Client iota = remoteDevnetNode();
	    		// First address is the faucet address. 1 to n are the buyer addresses.
	    		GetAddressesBuilder addresses = iota.getAddresses(SEED).withRange(0, n);
	    		String[] addresses_str = addresses.finish();
	    		for( int i = 1; i < n; i++) {
	    			System.out.println(addresses_str[i]);
	    			Thread.sleep(2); // avoid conflicts on Tangle
	    			transaction(SEED, addresses_str[i], 1000000, "REMOTE");
	    		}
	    	}
		    
		    /**
		     * Generate a given number of addresses, serve as a
		     * basis for transactions.
		     * @param n the number of addresses
		     * @return the n generated addresses
		     * @throws InterruptedException
		     */
		    public static String[] generateUsersAddresses(int n) throws InterruptedException {
	    		Client iota = remoteDevnetNode();
	    		GetAddressesBuilder addresses = iota.getAddresses(SEED).withRange(0, n);
	    		String[] addresses_str = addresses.finish();
	    		return addresses_str;
	    		
	    	}
	}
