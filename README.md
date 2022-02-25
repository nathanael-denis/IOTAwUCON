IOTA with UCON 

This is the Java implementation of a **IOTA-based usage control system(UCS)**, whose purpose is to show the relevance 
of integrating the UCS with the IOTA network for both performances and security purposes. The readme.md is structured
as follows: 1) the general instructions to reproduce the tests, including the possibe issues and how to correct them; 2) general and unstructured notes written during the conception of the solution, which might be of interest for understanding.

----------------------------------- FIRST PART: TESTING INSTRUCTIONS --------------------------------

The test were conducting on the IOTA development network also known as the devnet or formerly the testnet. The implementation includes a faucet to get the iotas tokens necessary (IOTAUtils.homeMadeFaucet(int value)), but it might be necessary to get tokens from an external tokens in case the error message is returned indicating a lack of token. The **faucet** is available at https://faucet.chrysalis-devnet.iota.cafe/

To test the performances, the access is controled using **Balana WSO2** XACML implementation and XACML policies, that were designed either using UMU XACML (http://umu-xacmleditor.sourceforge.net/) or the Eclipse IDE. In order to generate enough policies, we first designed a policy using the editor, then algorithmically modified them to match each user requirements ( using XACMLUtils.changeStringXML()).

In order to execute the Java IOTA library, one needs to use the Rust bindings proposed by the IOTA foundation. This step will most likely be the most troublesome, in particular to load the libray in the App.java class using System.loadLibrary("iota_client"). In case the library does not load properly, please refer to https://github.com/iotaledger/iota.rs/tree/dev/bindings/java for manual installation. 

You also need a **Hornet Node** to test the integrated model, since it is deployed along the UCS, that's to say on the machine you are doing the testing with. Refer to https://wiki.iota.org/hornet/getting_started for installation.
Tests are conducting in the two different settings as follows: 

1) the peripheral model using a remote node, corresponding to eventChainAccessControlSeveralUsers(REMOTE) in the App.java class. Change the global parameter URLFILE ET URLRESULTS in SampleEnergyData to fit your file system. The call remoteTransactionsOverhead is used to test the time taken to query the remote node; this time is added to time to push a transaction;

2) the integrated model is tested with eventChainAccessControlSeveralUsers(LOCALHOST).

Note that part of the classes such as the Context Handler are left empty, they were supposed to be used to 
create a UCS from scratch, but were not useful thanks to Balana Wso2 implementation.


------------------------------------- SECOND PART ---------------------------------------------------
----- GENERAL NOTES -----

This is the implementation of an usage control system which alternatively uses a local IOTA node
to fetch transaction information, or rely on a distant node (which is either a 


Maven, Spotbugs and Checkstyle were used to respectively manage dependencies and for code verification.

To manage decentralised storage, we relied on Cassandra DB.

Part of the code handling connection to the public Tangle relies on a node, that either 
needs to be deployed on the same machine, or whose address must be specified in the Pdp.java class
 

----USEFUL NOTES FOR PROGRAMMING -----

This project uses the standard implementation developed by Sun (now Oracle) 
as a basis for components. Two components, the Context Handler and the Session Manager, 
not in the standard developments, are built from scratch.

In our architecture, the Context Handler centralises the calls of all components, and 
as a consequence partially replaces the functions of the PDP in the Oracle standard.

All the details to build basic components using Sun-Oracle XACML implementation are given
in the following tutorial: http://sunxacml.sourceforge.net/guide.html#using-pep

About databases:

We use JDBC to interact with databases, which is 
natively supported by Java, but to avoid writing complicated SQL 
request, it might be better to use Hibernate ( see https://www.marcobehler.com/guides/java-databases).
When writing in the database using JDBC, a significant overhead is introduced, it is not used in 
performance tests.

Issues/solutions : “References to interface static methods are allowed only at source level 1.8” or above when I already have a 1.8 JRE"

Using Maven in Eclipse, I had this issue preventing compilation. Can be solved by Right click on Project --> Properties --> set Java Compiler 1.8.

After shutting down your computer, you may have a connection refusal 

Connection error: ('Unable to connect to any servers', {'127.0.0.1:9042': ConnectionRefusedError(111,) etc.

while having :  sudo sysctl status cassandra 
showing that cassandra is running.

In that case, I restarted ./bin/cassandra from cassandra installation directory. Java version may be an issue leading 
to the same problem as well.

To run the main class outside Eclipse, use the following Command:

mvn exec:java -Dexec.mainClass="iotawucon.App" -e
------------- IOTA ------------------

Using the shell and not java, it is possible to interact with the IOTA Node using curl and the api.
Ex:  curl localhost:14265/api/v1/tips to get the last tips on our local node.

You can use the faucet to get iotas on your addresses' balance, that you can then send
to the data broker before checking the payment.

https://faucet.chrysalis-devnet.iota.cafe/

You can interact with the faucet using curl, which is useful for 
funding several addresses. You need to open port 8080 for it to 
work using : $ sudo ufw allow 8080

curl --location --request POST 'http://localhost:8081/faucet' \
--header 'Content-Type: application/json' \
--data-raw '{
    "address": "target address",
    "accessManaPledgeID": "nodeID",
    "consensusManaPledgeID": "nodeID",
  "nonce": 50
}'


$ sudo hornet -c config_devnet.json --- launch a hornet node on devnet without docker ----

----CASSANDRA TABLE CREATE ------
To generate the temperature table on Cassandra, use the following command:

create table simplekeyspace.temperatures (key int PRIMARY KEY, temperatureVal float,
latitude float, longitude float, genTime text, iotaPrice float);

To create the users table on Cassandra, use the following command:

create table simplekeyspace.users (key int PRIMARY KEY, iota_address text,
role text);

To generate the humidity levels table on Cassandra, use the following command:

create table simplekeyspace.humiditylevels (key int PRIMARY KEY, humidityLevel int,
latitude float, longitude float, genTime text, iotaPrice float);

To create the transactions table on Cassandra:

create table simplekeyspace.transactions (key int PRIMARY KEY, resource_id int,buyer_address text,
borker_address text, iotaPrice float);

-----

