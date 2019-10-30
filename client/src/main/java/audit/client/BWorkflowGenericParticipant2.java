package audit.client;


import audit.client.loadsimulation.LogNormalbasedDelayGeneration;
import audit.client.service.MsgService;
import audit.common.SignatureUtils;
import audit.common.domain.Address;
import audit.common.domain.Transaction;
import com.google.api.client.util.Maps;
import org.apache.commons.cli.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.*;
import java.util.concurrent.TimeUnit;
import org.apache.commons.codec.digest.DigestUtils;
import java.math.BigInteger;


/**
 * Simple class to help building REST calls for jBlockchain.
 * Just run it in command line for instructions on how to use it.
 *
 * Functions include:
 * - Generate Private/Public-Key
 * - Publish a new Address
 * - Publish a new Transaction
 */

@Service
@SpringBootApplication //Added this for the web service.
public class BWorkflowGenericParticipant2 {//Added the extension hoping to get the service variables
	 private static KeyStore clientKeyStore;
	  static private final String clientpassphrase = "clientpw";
	  static private final String serverpassphrase = "serverpw";
	  private static List<String> ReferenceofAuditRecsforReceivedMessages = new ArrayList<>(); //added for reference to prev records
	  private static List<String> AuditRecsforReceivedMessages = new ArrayList<>();
	  private static List<String> pulledAuditRecs = new ArrayList<>();
	  private static List<Long> pulledAuditRecsReportingTime = new ArrayList<>();
	  private static List<String> pulledAuditRecsReportingTime_str = new ArrayList<>(); //added for reference to prev records
	  private static Long mostRecentReportingTime;//of an audit record by any participant
	  private static String mostRecentAuditRecord; //published on the audit server by any participant. THis is because elements in a hashmap are not in order.
	  private static String mostRecentReportedLocalHash;// LocalHash Values Reported by other clients to the audit server.
	  private static Long epsilon=(long) 100.0;
	  // These are added for the command line options
	  static String file_send = "data_send.csv";
	  //static String file_recieve = "data_receive.csv";
	  static String file_combo = "data_combo.csv";
	  private static String addresstoPublish="";
	  private static String port="";
	  private static String name="Participant2";
	  private static String recipientPort= "";
	  static String file_recieve = "data_receive.csv";
      static double constant = 1;
      static double mu=0; static double sigma=0;
      static int SentMessageSize=0; static int ReceivedMessageSize=0;
      static int UnencryptedSentMsglength=0; static int encryptedSentMsglength=0;
    static JWTMsg globalmsg=new JWTMsg();
    static KeyPair senderPair, MsgSenderPair; static String keyPairName="";

   static {
        try {//This gets overriden later on. It's only for test cases.
            MsgSenderPair = globalmsg.getKeyPairFromFile("client3", "clientpw", clientpassphrase, "clientprivate");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws Exception {



    	/*String str="s1vjwh1gQl1scMrAFkM0KDV2E9m7hr7+oW2P1OCfVSk7NIEE9d\\/jPB2c\\/QV9QtMmpOqXl0\\/rIMEvaCSSO7+kM1gpScdW4b88V0HogFvbZbE91A9IwrOpz5qJCzWNd9H\\/zdVbv6AI4AS1AXInZMcuKjZukSQxCHUXuzO34GB445Ypti5mcyrnphW20nagBs\\/kPcYYAtEszb0MxkOpq2YSdw2lU\\/O\\/fO7qFz0Qruj3BlpuVXIrJ64s4R0rjMr9fLUenlHTgTEqKZDql+gwZT48IahtxAztwENa01rvJz2reWFluhlIJMeN6e2ZIT3JMvWUh6pOBgy\\/pINQmgqpd\\/pBbg==";
    	System.out.println("After Cleaning "+str.replaceAll("\\\\/", "/"));

    	JWTMsg msg=new JWTMsg("Data", "Issuer", "Recipient", "Label", new String[] {"Prev1", "Prev2"}, new String[] {"ParaPrev1", "ParaPrev2"});
    	JWTMsg msg2=new JWTMsg(msg.Plain_JWT(msg));
    	System.out.println("First: "+msg.Plain_JWT(msg));
    	System.out.println("Second: "+msg2.Plain_JWT(msg2));*/
    	  //Turning this to a service

        //this has to be uncommented to read arguments from config
    	CommandLineParser parser = new DefaultParser();
        Options options = getOptions();
        try {
            CommandLine line = parser.parse(options, args);
            executeCommand(line);//posting forAudit on the wall.
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("BlockchainClient", options , true);
        }

        SpringApplication app = new SpringApplication(BWorkflowGenericParticipant2.class);
        Map<String, Object> pro = Maps.newHashMap();
        pro.put("server.port", port);

        app.setDefaultProperties(pro);
        app.run(args);


        /*URL url = new URL("http://localhost:8080/transaction");
    	HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    	connection.connect();*/

        //SendingandVerifyingMessagesandAuditRecs();
       // SendingandVerifyingMessagesandAuditRecsWithKeyFiles(Paths.get("client.priv"),Paths.get("client.public"),Paths.get("key.private"),Paths.get("key.pub"));

       // publishAddress("key.pub", "Antonio Nehme");
        //UseCommandLineOptions(); Need to copy the body and copy it to the main if this is to be used.

       // pullAudits();

        switchOptions();
    }

    //Blockchain methods

    public static void ini(){
        String url = "http://localhost:8333/Bclient/ini";


        //keyChain=12&keyName=y&keyType=0
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();


        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<String> httpresponse =
                restTemplate.postForEntity( url, request , String.class );

    }

    public static void saveKey(String signature, String keychain, String keyname){
        String url = "http://localhost:8333/Bclient/saveKey";
    /*
    Path publicKey
    * Files.readAllBytes(publicKey)
    * */

        //keyChain=12&keyName=y&keyType=0
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("signature", signature);
        map.add("keyChain", keychain);
        map.add("keyName", keyname);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<String> httpresponse =
                restTemplate.postForEntity( url, request , String.class );
        System.out.println(httpresponse);
    }





    public static void getKey(String signature, String owner, String keyName){
        String url = "http://localhost:8333/Bclient/getKey";


        //keyChain=12&keyName=y&keyType=0
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("signature", signature);
        map.add("owner", owner);
        map.add("keyName", keyName);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<String> httpresponse =
                restTemplate.postForEntity( url, request , String.class );
        System.out.println(httpresponse);
    }


    public static void saveLog(String signature, JWTMsg msg) throws Exception {
        String url = "http://localhost:8333/Bclient/saveLog";
        // msg.toString()
        KeyPair auditPair =msg.getKeyPairFromFile("server", "serverpw", serverpassphrase, "serverprivate");
       // String forAudit=msg.ArraytoString(msg.encrypt_long(msg.Split_to_List(msg.Plain_JWT(msg)), auditPair.getPublic()));
        String JWTEncAudit= msg.ArraytoStringCleanCut(msg.encrypt_long(msg.Split_to_List(msg.Plain_JWT(msg)), auditPair.getPublic()));
        //String DecJWT= msg.Dec_JWT(JWTEncMsg, (RSAPrivateKey)auditPair.getPrivate());


        //keyChain=12&keyName=y&keyType=0
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("signature", signature);
        //map.add("encryptedMessage", encryptedMessage);
        map.add("encryptedMessage", JWTEncAudit);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<String> httpresponse =
                restTemplate.postForEntity( url, request , String.class );
        System.out.println(httpresponse);
    }

    public static void saveLogtoString(String signature, JWTMsg msg) throws Exception {
        String url = "http://localhost:8333/Bclient/saveLog";
        // msg.toString()
        KeyPair auditPair =msg.getKeyPairFromFile("server", "serverpw", serverpassphrase, "serverprivate");
        // String forAudit=msg.ArraytoString(msg.encrypt_long(msg.Split_to_List(msg.Plain_JWT(msg)), auditPair.getPublic()));
        String JWTEncAudit= msg.ArraytoStringCleanCut(msg.encrypt_long(msg.Split_to_List(msg.Plain_JWT(msg).toString()), auditPair.getPublic()));
        //String DecJWT= msg.Dec_JWT(JWTEncMsg, (RSAPrivateKey)auditPair.getPrivate());


        //keyChain=12&keyName=y&keyType=0
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("signature", signature);
        //map.add("encryptedMessage", encryptedMessage);
        map.add("encryptedMessage", JWTEncAudit);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<String> httpresponse =
                restTemplate.postForEntity( url, request , String.class );
        System.out.println(httpresponse);
    }
    public static void saveLogString(String signature, String s) throws Exception {
        String url = "http://localhost:8333/Bclient/saveLog";
        // msg.toString()
//        KeyPair auditPair =msg.getKeyPairFromFile("server", "serverpw", serverpassphrase, "serverprivate");
//        // String forAudit=msg.ArraytoString(msg.encrypt_long(msg.Split_to_List(msg.Plain_JWT(msg)), auditPair.getPublic()));
//        String JWTEncAudit= msg.ArraytoStringCleanCut(msg.encrypt_long(msg.Split_to_List(msg.Plain_JWT(msg).toString()), auditPair.getPublic()));
//        //String DecJWT= msg.Dec_JWT(JWTEncMsg, (RSAPrivateKey)auditPair.getPrivate());
//

        //keyChain=12&keyName=y&keyType=0
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("signature", signature);
        //map.add("encryptedMessage", encryptedMessage);
        map.add("encryptedMessage", s);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<String> httpresponse =
                restTemplate.postForEntity( url, request , String.class );
        System.out.println(httpresponse);
    }

    public static void compareLogString(String signature, String s, String owner) throws Exception {
//        JWTMsg m=new JWTMsg();
//        PublicKey auditPublic =m.getKeyPairFromFile("server", "serverpw", serverpassphrase, "serverprivate").getPublic();
        /*KeyPair receiverPair =m.getKeyPairFromFile("client3", "clientpw", clientpassphrase, "clientprivate");
        //String JWTEncMsg= msg.Enc_JWT(msg,(RSAPublicKey)receiverPair.getPublic());
        //There is a slight different between this and the generic one. Here we seem to to have the urge to clean the array.

        String[] receivedMsgArray=m.StringCleanCuttoArray(EncryptedReceivedMsg);///////////////////

        //System.out.println("1");
        //	String receivedMsgArrayDecrypt[]= m.decrypt_long(receivedMsgArray, (RSAPrivateKey)receiverPair.getPrivate());
        //System.out.println("2- Problem seems to be here.");
        String receivedMsg=m.ArraytoString(m.decrypt_long(receivedMsgArray, (RSAPrivateKey)receiverPair.getPrivate()));*/

        //String receivedMsg= m.Dec_JWT(EncryptedReceivedMsg, (RSAPrivateKey)receiverPair.getPrivate());
//        System.out.println("Received Msg After Decrypt:" +DecryptedReceivedMsg);
//        String VerifyAudit=m.ArraytoStringCleanCut(m.encrypt_long(m.Split_to_List(DecryptedReceivedMsg), auditPublic));

        ////
        String url = "http://localhost:8333/Bclient/compareLog";

        //keyChain=12&keyName=y&keyType=0
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("signature", "xiaohu");
        map.add("payload", s);// map.add("payload", "123");
        map.add("owner", "80bd8b0e1cd2f6c4fffdac470be4ab9c7006c7a8");//changed from 0x492444fd2216400ed15521a5f69d25262b73a288

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<String> httpresponse =
                restTemplate.postForEntity( url, request , String.class );
        System.out.println(httpresponse);
    }

    public static void compareLogHash(String signature, String s, String owner) throws Exception {
//        JWTMsg m=new JWTMsg();
//        PublicKey auditPublic =m.getKeyPairFromFile("server", "serverpw", serverpassphrase, "serverprivate").getPublic();
        /*KeyPair receiverPair =m.getKeyPairFromFile("client3", "clientpw", clientpassphrase, "clientprivate");
        //String JWTEncMsg= msg.Enc_JWT(msg,(RSAPublicKey)receiverPair.getPublic());
        //There is a slight different between this and the generic one. Here we seem to to have the urge to clean the array.

        String[] receivedMsgArray=m.StringCleanCuttoArray(EncryptedReceivedMsg);///////////////////

        //System.out.println("1");
        //	String receivedMsgArrayDecrypt[]= m.decrypt_long(receivedMsgArray, (RSAPrivateKey)receiverPair.getPrivate());
        //System.out.println("2- Problem seems to be here.");
        String receivedMsg=m.ArraytoString(m.decrypt_long(receivedMsgArray, (RSAPrivateKey)receiverPair.getPrivate()));*/

        //String receivedMsg= m.Dec_JWT(EncryptedReceivedMsg, (RSAPrivateKey)receiverPair.getPrivate());
//        System.out.println("Received Msg After Decrypt:" +DecryptedReceivedMsg);
//        String VerifyAudit=m.ArraytoStringCleanCut(m.encrypt_long(m.Split_to_List(DecryptedReceivedMsg), auditPublic));

        ////
        String url = "http://localhost:8333/Bclient/compareLogH";

        //keyChain=12&keyName=y&keyType=0
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //String h=DigestUtils.sha256Hex(s);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("signature", "xiaohu");
        map.add("payload", s);// map.add("payload", "123");
        map.add("owner", "80bd8b0e1cd2f6c4fffdac470be4ab9c7006c7a8");//changed from 0x492444fd2216400ed15521a5f69d25262b73a288

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<String> httpresponse =
                restTemplate.postForEntity( url, request , String.class );
        System.out.println(httpresponse);
    }

    public static void compareLog(String signature, String DecryptedReceivedMsg, String owner) throws Exception {
        JWTMsg m=new JWTMsg();
        PublicKey auditPublic =m.getKeyPairFromFile("server", "serverpw", serverpassphrase, "serverprivate").getPublic();
        /*KeyPair receiverPair =m.getKeyPairFromFile("client3", "clientpw", clientpassphrase, "clientprivate");
        //String JWTEncMsg= msg.Enc_JWT(msg,(RSAPublicKey)receiverPair.getPublic());
        //There is a slight different between this and the generic one. Here we seem to to have the urge to clean the array.

        String[] receivedMsgArray=m.StringCleanCuttoArray(EncryptedReceivedMsg);///////////////////

        //System.out.println("1");
        //	String receivedMsgArrayDecrypt[]= m.decrypt_long(receivedMsgArray, (RSAPrivateKey)receiverPair.getPrivate());
        //System.out.println("2- Problem seems to be here.");
        String receivedMsg=m.ArraytoString(m.decrypt_long(receivedMsgArray, (RSAPrivateKey)receiverPair.getPrivate()));*/

        //String receivedMsg= m.Dec_JWT(EncryptedReceivedMsg, (RSAPrivateKey)receiverPair.getPrivate());
        System.out.println("Received Msg After Decrypt:" +DecryptedReceivedMsg);
        String VerifyAudit=m.ArraytoStringCleanCut(m.encrypt_long(m.Split_to_List(DecryptedReceivedMsg), auditPublic));//Use this to broadcast.
        AuditRecsforReceivedMessages.add(VerifyAudit);
        ReferenceofAuditRecsforReceivedMessages.add(DigestUtils.sha256Hex(VerifyAudit));
       // compareLogHash("xiaohu", DigestUtils.sha256Hex("sss"), "0x492444fd2216400ed15521a5f69d25262b73a288");
        compareLogString("xiaohu", "sss", "80bd8b0e1cd2f6c4fffdac470be4ab9c7006c7a8");//changed
        ////
        String url = "http://localhost:8333/Bclient/compareLog";

        //keyChain=12&keyName=y&keyType=0
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("signature", "xiaohu");
        map.add("payload", VerifyAudit);// map.add("payload", "123");
        map.add("owner", "80bd8b0e1cd2f6c4fffdac470be4ab9c7006c7a8");//changed

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<String> httpresponse =
                restTemplate.postForEntity( url, request , String.class );
        System.out.println(httpresponse);
        //BroadcastfromReceiver(VerifyAudit);
    }

    //signature=xiao&payload=123&owner=0x48e46c23904a4785191719a43c889a3c8540011d
    public static void compareLogold(String signature, JWTMsg msg, String owner) throws Exception {
        String url = "http://localhost:8333/Bclient/compareLog";
        KeyPair auditPair =msg.getKeyPairFromFile("server", "serverpw", serverpassphrase, "serverprivate");
        String forAudit=msg.ArraytoString(msg.encrypt_long(msg.Split_to_List(msg.Plain_JWT(msg)), auditPair.getPublic()));

        //keyChain=12&keyName=y&keyType=0
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("signature", "xiaohu");
        map.add("payload", forAudit);// map.add("payload", "123");
        map.add("owner", "80bd8b0e1cd2f6c4fffdac470be4ab9c7006c7a8");//changed

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<String> httpresponse =
                restTemplate.postForEntity( url, request , String.class );
        System.out.println(httpresponse);
    }

    private static void executeCommand(CommandLine line) throws Exception {//This does not consider what's fed from the command line for the message.

        String p = line.getOptionValue("port");
        String n = line.getOptionValue("name");
        String rp= line.getOptionValue("recipientPort");
        String kn= line.getOptionValue("keyPairName");
        if (port == null || name == null) {
            throw new ParseException("Missing Arguments");
        }
        port=p;
        name=n;
        if(rp==null) {System.out.println("No Recipient Port set in Args. Going with the default RP.");
        int i = Integer.parseInt(port.trim());
        i=i+1;
        recipientPort=""+i;
        }
        else recipientPort= rp;

        if(kn==null){
            System.out.println("Default Key is client3");
            keyPairName="client3";
        }
        else {
            keyPairName=kn;
        }


        /*if (recipientPort.equals("")) {
        	int i = Integer.parseInt(port.trim());
            i=i+1;
            recipientPort=""+i;
        }
        else recipientPort= rp;*/
        //publishTransaction(new URL(node), Paths.get(privatekey), message, Base64.decodeBase64(sender), "This is the Local Hash");

}
    private static Options getOptions() {
        OptionGroup actions = new OptionGroup();
        actions.addOption(new Option("a", "arguments", false, "Please pass the arguments"));
        actions.setRequired(false);

        Options options = new Options();
        options.addOptionGroup(actions);
        options.addOption(Option.builder("p")
                .longOpt("port")
                .hasArg()
                .argName("Port Number")
                .desc("needed to run the service")
                .build());
        options.addOption(Option.builder("n")
                .longOpt("name")
                .hasArg()
                .argName("name of the sender")
                .desc("needed to recognize the sender- publish name on the server- print on receipt")
                .build());
        options.addOption(Option.builder("rp")
                .longOpt("recipientPort")
                .hasArg()
                .argName("recipientPort")
                .desc("For recipientPort")
                .build());
        options.addOption(Option.builder("kn")
                .longOpt("keyPairName")
                .hasArg()
                .argName("keyPairName")
                .desc("For keyPairName")
                .build());


        return options;
    }


    //////////////////////////From MSGService/////////////////////

    private final static Logger LOG = LoggerFactory.getLogger(MsgService.class);



     // private static Set<String> msgPool = new HashSet<>();//added the static to get from JWTClientandService
      private static List<String> msgPool = new ArrayList<>(); //Changing from Hashset to List.
      // This is where Messages are stored.
      private static List<String> postedAuditRecs = new ArrayList<>();
      //myposted audit recs
      private static List<String> storedAuditRecs = new ArrayList<>();//we're making a difference between posted, pulled, and stored audit recs.
      //Where audit recs are stored

      @Autowired
      public BWorkflowGenericParticipant2() {//public TransactionService(AddressService addressService) {
        //  this.addressService = addressService;
      }


      public static List<String> getmsgPool() {// Messages received from another participant.
          return msgPool;
      }
      
      public static List<String> getStoredAuditRecs() {//Audit records that a participant gets from the server, and adds when publishing a transaction.
          return storedAuditRecs;
      }
      
      public static List<String> getPostedAuditRecs() {//added the static to get from JWTClientandService
          return postedAuditRecs;
      }

      public static void setStoredAuditRecs(List<String> toset) {//added the static to get from JWTClientandService
      	storedAuditRecs=toset;
      }
      public static void addStoredAuditRec(String rec) {
      	storedAuditRecs.add(rec);
      }
      
      public static void addPostedAuditRec(String rec) {
      	postedAuditRecs.add(rec);
      }
      
      /**
       * Add a new Transaction to the pool
       * @param transaction Transaction to add
       * @return true if verification succeeds and Transaction was added
     * @throws Exception 
       */
      // public synchronized boolean add
      public synchronized boolean add(String message) throws Exception { //This is the method for a participant to receive a message.
    	  System.out.println("_____________Message Received by "+ name );
    	  ReceivedMessageSize=message.length()/343;
    	  System.out.println("ReceivedMsgSize= "+ message.length()+ " which is equivalent to "+  message.length()/343+ "Unit(s)");
        //added
          JWTMsg m=new JWTMsg();

    	  FileWriter fileWriter_recieve = new FileWriter(file_recieve,true);
    	//  FileWriter fileWriter_combo = new FileWriter(file_combo,true);
      	long startTime_recieve = System.currentTimeMillis();

          KeyPair receiverPair =m.getKeyPairFromFile("client3", "clientpw", clientpassphrase, "clientprivate");

          String[] receivedMsgArray=m.StringCleanCuttoArray(message);///////////////////
          String receivedMsg=m.ArraytoString(m.decrypt_long(receivedMsgArray, (RSAPrivateKey)receiverPair.getPrivate()));
        // m=new JWTMsg(receivedMsg);
         // m=new JWTMsg(receivedMsg, true);
          //System.out.println("Received "+m.toString());
          //System.out.println("Sig "+m.getSig());
         // System.out.println("Verifying the signature=" + m.verify(m.getData(), m.getSig(), MsgSenderPair.getPublic()));

    	 // boolean verif=EncryptedAuditRecordverification(message,"client2");//Here's where the problem is. When this is commented, the message gets received.
          compareLog("xiaohu", receivedMsg, "0x80bd8b0e1cd2f6c4fffdac470be4ab9c7006c7a8");
              msgPool.add(message);//

          //ReferenceofAuditRecsforReceivedMessages.add(DigestUtils.sha256Hex(receivedMsg));

         double delay=LogNormalbasedDelayGeneration.simulate_delay_time(constant, mu, sigma);
        		 
         long endTime_recieve = System.currentTimeMillis(); long duration_recieve = (endTime_recieve - startTime_recieve);//+(long)delay;
         //long duration_recieve_with_delay=(endTime_recieve - startTime_recieve)+(long)delay;
         //fileWriter_recieve.append(name+","+duration_recieve+","+duration_recieve_with_delay+"\n");
         fileWriter_recieve.append(name+","+duration_recieve+","+ReceivedMessageSize+","+"\n");
        //fileWriter_combo.append(name+","+duration_recieve+"\n");
        
        fileWriter_recieve.flush();//fileWriter_combo.flush();
        fileWriter_recieve.close();//fileWriter_combo.close();
             
              //Here, we trigger the audit rec verification. Added to automate the simulation.
              ///////////////////////////////////////This is case 4 //////////////////////////////////////
        		//may need to clean() here.
        		//check port number.
        
        /* Begin Comment to test if code still works for proof of concept
        if(!port.equals("8105")) {
              pullAudits();//Added; not essential
             // TimeUnit.SECONDS.sleep(1);
              Random rand = new Random();
              int n = rand.nextInt(500000) + 1;
              String dummyData = "Data"+n+""+System.currentTimeMillis();

              JWTMsg msg=new JWTMsg(dummyData, "Issuer", "Recipient", "Label", new String[] {mostRecentAuditRecord}, new String[] {"ParaPrev1"});
              
          	FileWriter fileWriter_send = new FileWriter(file_send,true);
          	long startTime_send = System.currentTimeMillis();
          	
              sendMessageToParticipant("http://localhost:"+recipientPort+"/participant?publish=true", msg, "key.priv", "HEWtNSfUAMKEitKc5MBThupdOTj98oV/VaLG9LbR5Ms=", "client2", "server");
             
              long endTime_send = System.currentTimeMillis();long duration_send = (endTime_send - startTime_send);
              
             // fileWriter_send.append(name+ " to "+recipientPort+","+duration_send+"\n");// this becomes like a loop...
              fileWriter_send.append(name+ " to "+recipientPort+","+duration_send+"\n");
              
              fileWriter_send.flush();
              fileWriter_send.close();
             //fileWriter_combo.flush();
              //fileWriter_combo.close();
              clean();
        } else {
            RestTemplate restTemplate = new RestTemplate();
             restTemplate.delete("http://localhost:8080/transaction");
             //TimeUnit.SECONDS.sleep(3);
             //RestTemplate restTemplate2 = new RestTemplate();
             //restTemplate2.delete("http://localhost:8101/startfresh");
             
             //restTemplate.
        }*/
              //Not having the chance to close
              
//////////////////////////////////////end of Case 4//////////////////////////////////////
              
              //Here, we can directly send the message to the next participant.
              //if(verif)  sendMessageToParticipant("http://localhost:8095", msg, "key.priv", "0xbdXETP8nmHznzg34Xzd9P3mNmRlIC+MQEXoqe1aGs=", "client2", "server");
              return true;
         
      }

      /**
       * Remove Transaction from pool
       * @param transaction Transaction to remove
       */
      public void remove(String transaction) {
          msgPool.remove(transaction);
      }

      /**
       * Does the pool contain all given Transactions?
       * @param transactions Collection of Transactions to check
       * @return true if all Transactions are member of the pool
       */
      public boolean containsAll(Collection<String> transactions) {
          return msgPool.containsAll(transactions);
      }
      
      ///////////////////////////////////////////////////////
      public static void switchOptions() throws Exception { try {
          senderPair = globalmsg.getKeyPairFromFile(keyPairName, "clientpw", clientpassphrase, "clientprivate");
      } catch (Exception e) {
          e.printStackTrace();
      }
          Scanner scan=new Scanner(System.in);
    System.out.println("0 to Add Address, 1 to VerifyServer, 2 to see last reported record on the audit server, 3 to Publish a message, 4 Send a message to another recipient, X to exit.");
    	String option=scan.nextLine();
    	while(option!="X") {
        switch(option) {
       /* case "AddRecord" :{ System.out.println("Adding an audit record to the client, but not the audit server");
        	addPostedAuditRec("Added Local Record");
           option=scan.nextLine();}
           break;*/
            case "ini" :{ini();
                System.out.println("0 to Add Address, 1 to VerifyServer, 2 to see last reported record on the audit server, 3 to Publish a message, 4 Send a message to another recipient, 5 to Override Recipient Port,6 Send with clear , X to exit.");
                saveLogString("xiaohu", "sss");
                option=scan.nextLine();}
            break;
        case "0" :{
            try {
                senderPair = globalmsg.getKeyPairFromFile(keyPairName, "clientpw", clientpassphrase, "clientprivate");
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(senderPair.getPublic().toString());
            saveKey("xiaohu", senderPair.getPublic().toString(), keyPairName);
            getKey("xiaohu", "0x80bd8b0e1cd2f6c4fffdac470be4ab9c7006c7a8", keyPairName);
        System.out.println("0 to Add Address, 1 to VerifyServer, 2 to see last reported record on the audit server, 3 to Publish a message, 4 Send a message to another recipient, 5 to Override Recipient Port, X to exit.");
        option=scan.nextLine();}
        break;
        case "1" :{ compareLogString("xiaohu", "sss", "80bd8b0e1cd2f6c4fffdac470be4ab9c7006c7a8");
            System.out.println("0 to Add Address, 1 to VerifyServer, 2 to see last reported record on the audit server, 3 to Publish a message, 4 Send a message to another recipient, 5 to Override Recipient Port, X to exit.");
        	option=scan.nextLine();}
            break;
        case "2" :{ 
        	System.out.println("mostRecentReportingTime "+mostRecentReportingTime+" mostRecentAuditRecord "+ mostRecentAuditRecord+ " mostRecentReportedLocalHash "+ mostRecentReportedLocalHash);
        	System.out.println("pulledAuditRecs "+pulledAuditRecs+" getStoredAuditRecs "+getStoredAuditRecs()+" getPostedAuditRecs "+getPostedAuditRecs());
        	System.out.println("pulledAuditRecsReportingTime "+pulledAuditRecsReportingTime+" pulledAuditRecsReportingTime_Str "+pulledAuditRecsReportingTime_str);
        	if(pulledAuditRecs.equals(getStoredAuditRecs()))System.out.println("pulledAuditRecs and StoredAuditRecs() are equal");
        	System.out.println("Equivalent AuditRecsforReceivedMessages"+AuditRecsforReceivedMessages);
        	System.out.println("Equivalent ReferenceofAuditRecsforReceivedMessages "+ReferenceofAuditRecsforReceivedMessages);
        	int sizeofAuditRecsforReceivedMessages=0;
        	for(int i=0; i<AuditRecsforReceivedMessages.size();i++) {
        		sizeofAuditRecsforReceivedMessages=sizeofAuditRecsforReceivedMessages+AuditRecsforReceivedMessages.get(i).length();
        	}
        	System.out.println("Size of Equivalent audit records"+sizeofAuditRecsforReceivedMessages);
        	System.out.println("Arrays.toString(calculateLocalHash()) "+ Arrays.toString(calculateLocalHash()));
        	System.out.println("0 to Add Address, 1 to VerifyServer, 2 to see last reported record on the audit server, 3 to Publish a message, 4 Send a message to another recipient, 5 to Override Recipient Port, X to exit.");
            option=scan.nextLine();}
        break;
        case "3": {
        	System.out.println("Add your record: "); String msg= scan.nextLine();
        System.out.println("Add your Address: "); String address= scan.nextLine();
        	publishAuditRecord("key.priv",msg,address);//
        	System.out.println("0 to Add Address, 1 to VerifyServer, 2 to see last reported record on the audit server, 3 to Publish a message, 4 Send a message to another recipient, 5 to Override Recipient Port, X to exit.");
            option=scan.nextLine();
        }break;
        case "4": {System.out.println("0 to Add Address, 1 to VerifyServer, 2 to see last reported record on the audit server, 3 to Publish a message, 4 Send a message to another recipient, 5 to Override Recipient Port, X to exit.");
        // publishAuditRecord("key.priv",postedAuditRecs.get(0),"HEWtNSfUAMKEitKc5MBThupdOTj98oV/VaLG9LbR5Ms=");
        
       // pullAudits();//Added; not essential<<<<<================================================Removed the pulls
        //TimeUnit.SECONDS.sleep(1); 
        Random rand = new Random();
        int n = rand.nextInt(500000) + 1;
        String dummyData = "Data"+n+""+System.currentTimeMillis();
        //JWTMsg msg=new JWTMsg(dummyData, name, "Recipient", "http://localhost:"+recipientPort, new String[] {mostRecentAuditRecord}, new String[] {"ParaPrev1", "ParaPrev2"});
       //Added to allow participant to send message without having to receive anything before(in case this should be considered.
        JWTMsg msg;
        if(AuditRecsforReceivedMessages.isEmpty()) {
        	 msg=new JWTMsg(dummyData, name, "Recipient", "http://localhost:"+recipientPort, new String[] {"Prev1"}, new String[] {"ParaPrev1"});//Removed ParaPrev2
        }
       // else { msg=new JWTMsg(dummyData, name, "Recipient", "http://localhost:"+recipientPort, ArraylistToArray(AuditRecsforReceivedMessages), new String[] {"ParaPrev1", "ParaPrev2"});}
        else { msg=new JWTMsg(dummyData, name, "Recipient", "http://localhost:"+recipientPort, ArraylistToArray(ReferenceofAuditRecsforReceivedMessages), new String[] {"ParaPrev1"});}
         
    	FileWriter fileWriter = new FileWriter(file_send,true);
    	long startTime = System.currentTimeMillis();
    	
        sendMessageToParticipant("http://localhost:"+recipientPort+"/participant?publish=true", msg, "key.priv", "HEWtNSfUAMKEitKc5MBThupdOTj98oV/VaLG9LbR5Ms=", "client2", "server");
       
        long endTime = System.currentTimeMillis();long duration = (endTime - startTime);
       // fileWriter.append(name+ " to "+recipientPort+","+duration+"\n");
        //fileWriter.append(name+ " to "+recipientPort+","+duration+","+SentMessageSize+","+AuditRecordsSize()+"\n");
        fileWriter.append(name+ " to "+recipientPort+","+duration+","+UnencryptedSentMsglength+","+encryptedSentMsglength+","+SentMessageSize+","+"\n");
        fileWriter.flush();
        fileWriter.close();
        	option=scan.nextLine();
        }break;
        
        case "5": {System.out.println("0 to Add Address, 1 to VerifyServer, 2 to see last reported record on the audit server, 3 to Publish a message, 4 Send a message to another recipient, 5 to Override Recipient Port, X to exit.");
        // pullAudits();//Added; not essential<<<<<================================================Removed the pulls
         //TimeUnit.SECONDS.sleep(1); 
        System.out.println("Enter Recipient Port: "); String Recipient= scan.nextLine();
        recipientPort=Recipient;
        
         Random rand = new Random();
         int n = rand.nextInt(500000) + 1;
         String dummyData = "Data"+n+""+System.currentTimeMillis();
         //JWTMsg msg=new JWTMsg(dummyData, name, "Recipient", "http://localhost:"+recipientPort, new String[] {mostRecentAuditRecord}, new String[] {"ParaPrev1", "ParaPrev2"});
       //Added to allow participant to send message without having to receive anything before(in case this should be considered.
         JWTMsg msg;
         //Why 2 prev? why the entire URL in the label's variable?
         if(AuditRecsforReceivedMessages.isEmpty()) {//if this or ReferenceofAuditRecsforReceivedMessages is empty
         	 msg=new JWTMsg(dummyData, name, "Recipient", "http://localhost:"+recipientPort, new String[] {"Prev1"}, new String[] {"ParaPrev1"});
         }
         else { msg=new JWTMsg(dummyData, name, "Recipient", "http://localhost:"+recipientPort, ArraylistToArray(ReferenceofAuditRecsforReceivedMessages), new String[] {"ParaPrev1"});}
         
        
     	FileWriter fileWriter = new FileWriter(file_send,true);
     	long startTime = System.currentTimeMillis();
     	
         sendMessageToParticipant("http://localhost:"+recipientPort+"/participant?publish=true", msg, "key.priv", "HEWtNSfUAMKEitKc5MBThupdOTj98oV/VaLG9LbR5Ms=", "client2", "server");
        
         long endTime = System.currentTimeMillis();long duration = (endTime - startTime);
         //fileWriter.append(name+ " to "+recipientPort+","+duration+"\n");
        // fileWriter.append(name+ " to "+recipientPort+","+duration+","+SentMessageSize+","+AuditRecordsSize()+"\n");
         fileWriter.append(name+ " to "+recipientPort+","+duration+","+UnencryptedSentMsglength+","+encryptedSentMsglength+","+SentMessageSize+","+AuditRecordsSize()+"\n");
         fileWriter.flush();
         fileWriter.close();
         	option=scan.nextLine();
         }break;
         
        case "6": {clean(); 
        	System.out.println("0 to Add Address, 1 to VerifyServer, 2 to see last reported record on the audit server, 3 to Publish a message, 4 Send a message to another recipient, 5 to Override Recipient Port, X to exit.");
       
        
        // publishAuditRecord("key.priv",postedAuditRecs.get(0),"HEWtNSfUAMKEitKc5MBThupdOTj98oV/VaLG9LbR5Ms=");
        
       // pullAudits();//Added; not essential<<<<<================================================Removed the pulls
        //TimeUnit.SECONDS.sleep(1); 
        Random rand = new Random();
        int n = rand.nextInt(500000) + 1;
        String dummyData = "Data"+n+""+System.currentTimeMillis();
        //JWTMsg msg=new JWTMsg(dummyData, name, "Recipient", "http://localhost:"+recipientPort, new String[] {mostRecentAuditRecord}, new String[] {"ParaPrev1", "ParaPrev2"});
       // JWTMsg msg=new JWTMsg(dummyData, name, "Recipient", "http://localhost:"+recipientPort, ArraylistToArray(AuditRecsforReceivedMessages), new String[] {"ParaPrev1", "ParaPrev2"});
        JWTMsg msg=new JWTMsg(dummyData, name, "Recipient", "http://localhost:"+recipientPort, ArraylistToArray(ReferenceofAuditRecsforReceivedMessages), new String[] {"ParaPrev1", "ParaPrev2"});
        
    	FileWriter fileWriter = new FileWriter(file_send,true);
    	long startTime = System.currentTimeMillis();
    	
        sendMessageToParticipant("http://localhost:"+recipientPort+"/participant?publish=true", msg, "key.priv", "HEWtNSfUAMKEitKc5MBThupdOTj98oV/VaLG9LbR5Ms=", "client2", "server");
       
        long endTime = System.currentTimeMillis();long duration = (endTime - startTime);
       // fileWriter.append(name+ " to "+recipientPort+","+duration+"\n");
       // fileWriter.append(name+ " to "+recipientPort+","+duration+","+SentMessageSize+","+AuditRecordsSize()+"\n");
        fileWriter.append(name+ " to "+recipientPort+","+duration+","+UnencryptedSentMsglength+","+encryptedSentMsglength+","+SentMessageSize+","+AuditRecordsSize()+"\n");
        fileWriter.flush();
        fileWriter.close();
        
        clean();
        	option=scan.nextLine();
        }break;
        
        case "test": {
    	System.out.println("0 to Add Address, 1 to VerifyServer, 2 to see last reported record on the audit server, 3 to Publish a message, 4 Send a message to another recipient, 5 to Override Recipient Port, X to exit.");
    	sendThroughURLCall("8104", "true");
    	/*RestTemplate restTemplate = new RestTemplate();
        
        restTemplate.postForLocation("http://localhost:8103/participant/call", "8104");
        */
    	
    	option=scan.nextLine();
    }break;
    
        case "test2": {
        	System.out.println("0 to Add Address, 1 to VerifyServer, 2 to see last reported record on the audit server, 3 to Publish a message, 4 Send a message to another recipient, 5 to Override Recipient Port, X to exit.");
        	 RestTemplate restTemplate = new RestTemplate();
        	//String http://localhost:8101/participant/call
              /*restTemplate.postForLocation(url, request);
               restTemplate.postForLocation("http://localhost:8101/participant/call", message);
               */
        	 
        	 HttpHeaders headers = new HttpHeaders();
        	 headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        	 MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        	 map.add("port", "8104");
        	 map.add("first", "true");
        	 

        	 HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        	 ResponseEntity<String> response = restTemplate.postForEntity( "http://localhost:8103/participant/call", request , String.class );
        	option=scan.nextLine();
        }break;
        
        default :{
           System.out.println("Invalid Option");
           option=scan.nextLine();}
     }
        }
     
  }


     
      
      public static void sendThroughURLCall(String RecipientPort, String first) throws Exception {//added to be used later by an orchestrator to call participants. Problem is, we need to use a single controller, which means that the method in the generic one would be called (unless we do some trich from the orchestrator, like pre-publishing from there if possible.
      	//We can add a flag in the request and a boolean here to let the orchestrator, knowing the first participant, require the participant to pre-publish.
    	 String RecipientPort_sub= RecipientPort.substring(5, 9);
    	  recipientPort=RecipientPort_sub;
          System.out.println("Recipient port: "+RecipientPort+" , first= "+first );
          Random rand = new Random();
          int n = rand.nextInt(500000) + 1;
          String dummyData = "Data"+n+""+System.currentTimeMillis();
          while(dummyData.length()<500) { 
          	dummyData+=""+n;
          }

          
          JWTMsg msg;//Changing this one to eliminate un-necessary msg length
    //String s=globalmsg.sign(ToSend.to_send, senderPair.getPrivate());
    //System.out.println("Signature "+s);
    //added the signature to the msg
          if(AuditRecsforReceivedMessages.isEmpty()) {//if this or ReferenceofAuditRecsforReceivedMessages is empty
          msg=new JWTMsg(ToSend.to_send, name, recipientPort, "THis is a label", new String[] {"Prev1"}, new String[] {"ParaPrev1"});
         }
         else { msg=new JWTMsg(ToSend.to_send, name, recipientPort,"THis is a label", ArraylistToArray(ReferenceofAuditRecsforReceivedMessages), new String[] {"ParaPrev1"});}

          //	msg=new JWTMsg(ToSend.to_send, name, recipientPort, "THis is a label", new String[] {"Prev1"}, new String[] {"ParaPrev1"});
          	//System.out.println("Before Sending "+msg.);
         msg.setSig(msg.sign(ToSend.to_send, senderPair.getPrivate()));
            System.out.println("Sig " +msg.sign(ToSend.to_send, senderPair.getPrivate()));

            //System.out.println("GetSig" + msg.getSig());
        	 // msg=new JWTMsg(ToSend.to_send, name, recipientPort, "", ArraylistToArray(ReferenceofAuditRecsforReceivedMessages), new String[] {"ParaPrev1"});
          

     	FileWriter fileWriter = new FileWriter(file_send,true);
      	long startTime = System.currentTimeMillis();
      	String URL="http://localhost:"+recipientPort+"/participant?publish=true";
      	System.out.println("URL: "+URL);
          sendMessageToParticipant(URL, msg, "key.priv", "HEWtNSfUAMKEitKc5MBThupdOTj98oV/VaLG9LbR5Ms=", "client3", "server");
         Broadcast(msg);
          long endTime = System.currentTimeMillis(); long duration = (endTime - startTime);
          //fileWriter.append(name+ " to "+recipientPort+","+duration+"\n");
          //fileWriter.append(name+ " to "+recipientPort+","+duration+","+SentMessageSize+","+AuditRecordsSize()+"\n");
          fileWriter.append(name+ " to "+recipientPort+","+duration+","+UnencryptedSentMsglength+","+encryptedSentMsglength+","+SentMessageSize+","+"\n");
      	fileWriter.flush();
          fileWriter.close();
      }

      //Add a method here and call it send to all.
    public static void Broadcast(JWTMsg msg) throws Exception {
        KeyPair auditPair =msg.getKeyPairFromFile("server", "serverpw", serverpassphrase, "serverprivate");
        // String forAudit=msg.ArraytoString(msg.encrypt_long(msg.Split_to_List(msg.Plain_JWT(msg)), auditPair.getPublic()));
        String JWTEncAudit= msg.ArraytoStringCleanCut(msg.encrypt_long(msg.Split_to_List(msg.Plain_JWT(msg)), auditPair.getPublic()));
        RestTemplate restTemplate = new RestTemplate();

        for(int i=8101; i<=8103;i++){
            if(i!=Integer.parseInt(port)){
        restTemplate.postForLocation("http://localhost:"+i+"/participant/audit", JWTEncAudit);}
        }
        //Applies to sender and receiver. Remove adding audit recs after verification for consistency. 387.

        //SendtoAll
    }

    public static void BroadcastfromReceiver(String EncAuditRec) throws Exception {
      //  KeyPair auditPair =msg.getKeyPairFromFile("server", "serverpw", serverpassphrase, "serverprivate");
        // String forAudit=msg.ArraytoString(msg.encrypt_long(msg.Split_to_List(msg.Plain_JWT(msg)), auditPair.getPublic()));
      //  String JWTEncAudit= msg.ArraytoStringCleanCut(msg.encrypt_long(msg.Split_to_List(msg.Plain_JWT(msg)), auditPair.getPublic()));
        RestTemplate restTemplate = new RestTemplate();
        for(int i=8101; i<=8103;i++){
            if(i!=Integer.parseInt(port)){
                restTemplate.postForLocation("http://localhost:"+i+"/participant/audit", EncAuditRec);}
        }

        //SendtoAll
    }

    public synchronized boolean addAuditRecord(String AuditRecord) throws Exception { //This is the method for a participant to receive a message.
          //expose this.
        //This includes verifying the record.
        System.out.println("Record Received by "+ name );

        JWTMsg m=new JWTMsg();


        String url = "http://localhost:8333/Bclient/compareLog";

        //keyChain=12&keyName=y&keyType=0
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("signature", "xiaohu");
        map.add("payload", AuditRecord);// map.add("payload", "123");
        map.add("owner", "80bd8b0e1cd2f6c4fffdac470be4ab9c7006c7a8");//changed

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<String> httpresponse =
                restTemplate.postForEntity( url, request , String.class );
        System.out.println(httpresponse);
        AuditRecsforReceivedMessages.add(AuditRecord);
        ReferenceofAuditRecsforReceivedMessages.add(DigestUtils.sha256Hex(AuditRecord));

        //ReferenceofAuditRecsforReceivedMessages.add(DigestUtils.sha256Hex(receivedMsg));


        return true;

    }



      public void testwithAdd(){
          /*  JWTMsg m=new JWTMsg();
          KeyPair receiverPair =m.getKeyPairFromFile("client3", "clientpw", clientpassphrase, "clientprivate");

          String[] receivedMsgArray=m.StringCleanCuttoArray(message);///////////////////
          String receivedMsg=m.ArraytoString(m.decrypt_long(receivedMsgArray, (RSAPrivateKey)receiverPair.getPrivate()));
    	  m=new JWTMsg(receivedMsg, true);
          System.out.println("Received "+m.toString());
    	  System.out.println("Verifying the sign+" + m.getSig()+ m.verify(m.getData(), m.getSig(), receiverPair.getPublic()));
    */
      }
      
      public static void clean() {
      	/*
    	  private static Long mostRecentReportingTime;//of an audit record by any participant
    	  private static String mostRecentAuditRecord; //published on the audit server by any participant. THis is because elements in a hashmap are not in order.
    	  private static String mostRecentReportedLocalHash;// LocalHash Values Reported by other clients to the audit server.
     */
      	pulledAuditRecs.clear();AuditRecsforReceivedMessages.clear();pulledAuditRecsReportingTime.clear();
      	postedAuditRecs.clear();
    	//Added after brite
      	mostRecentReportingTime=(long) 0;//of an audit record by any participant
   	   mostRecentAuditRecord=""; //published on the audit server by any participant. THis is because elements in a hashmap are not in order.
   	  mostRecentReportedLocalHash="";// LocalHash Values Reported by other clients to the audit server.
   	  msgPool.clear();
       postedAuditRecs.clear();
      storedAuditRecs.clear();
      
   // Added after Ref
  	ReferenceofAuditRecsforReceivedMessages.clear();
  	pulledAuditRecsReportingTime_str.clear();
  	pulledAuditRecsReportingTime.clear();
  	//
      }
      
      public static void Startfresh() throws Exception {clean();
    	  //Added only to be used by participant 1
      	//System.out.println("0 to Add Address, 1 to VerifyServer, 2 to see last reported record on the audit server, 3 to Publish a message, 4 Send a message to another recipient, 5 to Override Recipient Port, X to exit.");
        TimeUnit.SECONDS.sleep(1);System.out.println("Generic; recipientPort"+ recipientPort);
      		publishAuditRecord("key.priv","Prev1","HEWtNSfUAMKEitKc5MBThupdOTj98oV/VaLG9LbR5Ms=");
         // publishAuditRecord("key.priv","Prev2","HEWtNSfUAMKEitKc5MBThupdOTj98oV/VaLG9LbR5Ms=");
          publishAuditRecord("key.priv","ParaPrev1","HEWtNSfUAMKEitKc5MBThupdOTj98oV/VaLG9LbR5Ms=");
        //  publishAuditRecord("key.priv","ParaPrev2","HEWtNSfUAMKEitKc5MBThupdOTj98oV/VaLG9LbR5Ms=");
          TimeUnit.SECONDS.sleep(1);
          JWTMsg msg=new JWTMsg("Data", "Issuer", "Recipient", "Label", new String[] {"Prev1"}, new String[] {"ParaPrev1"});
          //Time this 
          
      	FileWriter fileWriter = new FileWriter(file_send,true);
          //FileWriter fileWriter_combo = new FileWriter(file_combo,true);
      	long startTime = System.currentTimeMillis();
          //sendMessageToParticipant("http://localhost:8102/participant?publish=true", msg, "key.priv", "HEWtNSfUAMKEitKc5MBThupdOTj98oV/VaLG9LbR5Ms=", "client2", "server");
          sendMessageToParticipant("http://localhost:"+recipientPort+"/participant?publish=true", msg, "key.priv", "HEWtNSfUAMKEitKc5MBThupdOTj98oV/VaLG9LbR5Ms=", "client2", "server");
          
          long endTime = System.currentTimeMillis();long duration = (endTime - startTime);
          //fileWriter.append(name+ " to "+recipientPort+","+duration+","+"\n");
          //fileWriter.append(name+ " to "+recipientPort+","+duration+","+SentMessageSize+","+AuditRecordsSize()+"\n");
          fileWriter.append(name+ " to "+recipientPort+","+duration+","+UnencryptedSentMsglength+","+encryptedSentMsglength+","+SentMessageSize+","+AuditRecordsSize()+"\n");
          //fileWriter_combo.append(name+ " to "+recipientPort+","+duration+","+"\n");
          
          /*///////////
          long startTime2 = System.currentTimeMillis();
          //sendMessageToParticipant("http://localhost:8102/participant?publish=true", msg, "key.priv", "HEWtNSfUAMKEitKc5MBThupdOTj98oV/VaLG9LbR5Ms=", "client2", "server");
          sendMessageToParticipant("http://localhost:8103/participant?publish=true", msg, "key.priv", "HEWtNSfUAMKEitKc5MBThupdOTj98oV/VaLG9LbR5Ms=", "client2", "server");
          
          long endTime2 = System.currentTimeMillis();long duration2 = (endTime2 - startTime2);
          fileWriter.append(name+","+duration2+","+"\n");
          ///////////////////*/
          
          
      	fileWriter.flush();//fileWriter_combo.flush();
          fileWriter.close();//fileWriter_combo.close();
          clean();
      }
      
    public static void UseCommandLineOptions() {
   /* CommandLineParser parser = new DefaultParser();
    Options options = getOptions();
    try {
        CommandLine line = parser.parse(options, args);
        executeCommand(line, "Audit data");//posting forAudit on the wall.
    } catch (ParseException e) {
        System.err.println(e.getMessage());
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("BlockchainClient", options , true);
    }*/
    }
    
    
    
    public static void SendingandVerifyingMessagesandAuditRecs() throws Exception { // This is only reference for JWT cipher methods and verifications. 
    	//We are calling JWT methods using the JWTMsg object.
    	// 
    	JWTMsg msg=new JWTMsg("Data", "Issuer", "Recipient", "Label", new String[] {"Prev1", "Prev2"}, new String[] {"ParaPrev1", "ParaPrev2"});
    	KeyPair receiverPair =msg.getKeyPairFromFile("client3", "clientpw", clientpassphrase, "clientprivate");
		KeyPair auditPair =msg.getKeyPairFromFile("server", "serverpw", serverpassphrase, "serverprivate");
		
		
		
		String JWTEncMsg= msg.Enc_JWT(msg,(RSAPublicKey)receiverPair.getPublic());
		String DecJWT= msg.Dec_JWT(JWTEncMsg, (RSAPrivateKey)receiverPair.getPrivate());
		System.out.println("Plain JWT: "+ msg.Plain_JWT(msg));
		if (msg.Plain_JWT(msg).equals(DecJWT))System.out.println("Plain and Dec are the same");
		else {
			System.out.println("They are not");	
		}
		
		System.out.println("PlainJWT "+ msg.Plain_JWT(msg).toString());
		if (msg.Plain_JWT(msg).equals(DecJWT))System.out.println("Bingo 1");
		
		//The same thing has to be adopted for sent messages between participants.
		//There is probably no need for that, since we're using different methods to encrypt clients' exchanged messages and audit recs.
		String forAudit=msg.ArraytoString(msg.encrypt_long(msg.Split_to_List(msg.Plain_JWT(msg)), auditPair.getPublic()));
		String VerifyAudit=msg.ArraytoString(msg.encrypt_long(msg.Split_to_List(DecJWT), auditPair.getPublic()));
		if (forAudit.equals(VerifyAudit))System.out.println("Bingo v");
    }
    
   //This has to be implemented in order to use keys from files, rather than from key store. THis is because we can't have the audit server's or the intended recipient's private key.
    //We would need to modify Enc_JWT and methods
   //It is OK for a proof of concept though.
/*public static void SendingandVerifyingMessagesandAuditRecsWithKeyFiles(Path privateKeyReceiver, Path publicKeyReceiver, Path privateKeyServer, Path publicKeyServer) throws Exception { //We are calling JWT methods using he JWTMsg object.
    	// 
    	JWTMsg msg=new JWTMsg("Data", "Issuer", "Recipient", "Label", new String[] {"Prev1", "Prev2"}, new String[] {"ParaPrev1", "ParaPrev2"});
    	//KeyPair receiverPair =msg.getKeyPairFromFile("client3", "clientpw", clientpassphrase, "clientprivate");
		//KeyPair auditPair =msg.getKeyPairFromFile("server", "serverpw", serverpassphrase, "serverprivate");
		
    	
    	
    	PublicKey ReceiverpublicKey = 
    		    KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Files.readAllBytes(publicKeyReceiver)));
    	PrivateKey ReceiverPrivateKey = 
    		    KeyFactory.getInstance("RSA").generatePrivate(new X509EncodedKeySpec(Files.readAllBytes(privateKeyReceiver)));
    	PublicKey AuditpublicKey = 
    		    KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Files.readAllBytes(publicKeyServer)));
    	PrivateKey AuditPrivateKey = 
    		    KeyFactory.getInstance("RSA").generatePrivate(new X509EncodedKeySpec(Files.readAllBytes(privateKeyServer)));
		
    	
    	
    	
		String JWTEncMsg= msg.Enc_JWT(msg,(RSAPublicKey) ReceiverpublicKey);
		String DecJWT= msg.Dec_JWT(JWTEncMsg, (RSAPrivateKey) ReceiverPrivateKey);
		if (msg.Plain_JWT(msg).equals(DecJWT))System.out.println("Plain and Dec are the same");
		else {
			System.out.println("They are not");	
		}
		
		System.out.println("PlainJWT "+ msg.Plain_JWT(msg).toString());
		if (msg.Plain_JWT(msg).equals(DecJWT))System.out.println("Bingo 1");
		
		//The same thing has to be adopted for sent messages between participants.
		//There is probably no need for that, since we're using different methods to encrypt clients' exchanged messages and audit recs.
		String forAudit=msg.ArraytoString(msg.encrypt_long(msg.Split_to_List(msg.Plain_JWT(msg)), AuditpublicKey));
		String VerifyAudit=msg.ArraytoString(msg.encrypt_long(msg.Split_to_List(DecJWT), AuditpublicKey));
		if (forAudit.equals(VerifyAudit))System.out.println("Bingo v");
    }*/
    
    
    
    private static void sendMessageToParticipant(String RecipientURL, JWTMsg msg, String SenderPrivateKey, String senderAddress, String receiverKeyPair, String auditKeyPair) throws Exception {//Maybe rename it. //This method sends a message from one participant to another following our protocol
    	//Renamed from sendTransaction
    	/* Work on SendingandVerifyingMessagesandAuditRecsWithKeyFiles to deal with the keys.
    	 * Steps: Publish audit record of the message, encrypted with the workflow public key, to the audit server
    	 * Send message, encrytped with the recipient's public key, to the intended recipient.
    	 * */
    	KeyPair receiverPair =msg.getKeyPairFromFile(receiverKeyPair, "clientpw", clientpassphrase, "clientprivate");
		KeyPair auditPair =msg.getKeyPairFromFile(auditKeyPair, "serverpw", serverpassphrase, "serverprivate");
		
		String JWTEncMsg= msg.ArraytoStringCleanCut(msg.encrypt_long(msg.Split_to_List(msg.Plain_JWT(msg)), receiverPair.getPublic()));//msg.Enc_JWT(msg,(RSAPublicKey)receiverPair.getPublic());
        //String receivedMsg=msg.ArraytoString(msg.decrypt_long(JWTEncMsg, (RSAPrivateKey)receiverPair.getPrivate()));
        //System.out.println("Encrypted String Sent to Next participant " +JWTEncMsg);
        System.out.println("Plain "+msg.Plain_JWT(msg).toString());
		System.out.println("UnencryptedMsgSize= "+ msg.Plain_JWT(msg).length()+" SentMsgSize= "+ JWTEncMsg.length()+ " which is equivalent to "+  JWTEncMsg.length()/343+ "Unit(s)");
		UnencryptedSentMsglength=msg.Plain_JWT(msg).length();encryptedSentMsglength=JWTEncMsg.length();
		SentMessageSize=JWTEncMsg.length()/343;
		//My guess is that when encrypting a string with a public key, its size changes.<=========
		String[] EncryptedArray=msg.encrypt_long(msg.Split_to_List(msg.Plain_JWT(msg)), receiverPair.getPublic());
		/////System.out.println("Printing Array: "+ EncryptedArray.toString());
		//maybe decrypt with private and re-encrypt with audit before logging.

		String JWTEncAudit= msg.ArraytoStringCleanCut(msg.encrypt_long(msg.Split_to_List(msg.Plain_JWT(msg)), auditPair.getPublic()));//msg.Enc_JWT(msg,(RSAPublicKey)auditPair.getPublic());
		
		System.out.println("PrePublish. ");
		//publishAuditRecord(SenderPrivateKey, JWTEncAudit, senderAddress);
		saveLog("xiaohu", msg);//
		////System.out.println("Publishing Passed. ");
		//sendHTTPMessage(RecipientURL,JWTEncMsg); It works if I type the string though.
		/*We have a problem here. For some readon, I cannot send JWTEncMsg.
		 * */
		////System.out.println("What I am trying to send: "+JWTEncMsg);
		sendHTTPMessage(RecipientURL,JWTEncMsg);
		//sendHTTPMessage(RecipientURL,"cZbLrbvnftn4pGNPU0PMpZTS0kQPTywNMDr3qY3GT78LEtjN4xP+kZcqVO3QtjDoDQiVT11KM7fwTvPDratEUyfHhY3JjskAgIpqaufmNBpSBNiJawcw9F+OZxpUwltUZQHfRrp0H9ZCnrMqeaCLegFFWAK8WKa4BH2UluBSbtviEgHqS1WO1P+Lf75MjdoKsYDDhVLjx6VGJHLI0gcWxBZvBkHBhy7FfwHKMdW0gtirNmuyQhBr8luWxYEp1M/wYNENhUKrYFuaJ5F2NLKblzL0g/K0SZNUcq2J9mqXNf6mWPepgXvNjT608nHMTNhNgYK7hQX2SI0B++ZzXD7XKw==");//Send msg from here, participant will on the other end verifies the record.
    }

    private static void sendHTTPMessage(String URL, String message) throws Exception {//Added throuws Exception
//This is to send messages from one participant to another. An audit record has to go in parallel with this action. 
    	//Messages have to be encrypted with the recipient's public key, and audit records with the workflow's public key.
        RestTemplate restTemplate = new RestTemplate();
        
        restTemplate.postForLocation(URL, message);
        
       // byte[] signature = SignatureUtils.sign(text.getBytes(), Files.readAllBytes(privateKey));
        //Here, the sender signs the text prior to sending it.
        //Transaction transaction = new Transaction(text, senderHash, signature, LocalHash);
       // restTemplate.post(node.toString() + "/interface?publish=true", message);
       // System.out.println("Hash of new transaction: " + Base64.encodeBase64String(transaction.getHash()));
    }
  



    public static void publishAddress(String PubKey, String name) throws MalformedURLException, IOException {
	 publishAddress(new URL("http://localhost:8080"), Paths.get(PubKey), name);
}

    
    
    private static void publishTransaction(URL node, Path privateKey, String text, byte[] senderHash, String LocalHash) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
       // System.out.println("What we are publishing "+text);
        byte[] signature = SignatureUtils.sign(text.getBytes(), Files.readAllBytes(privateKey));
        //Here, the sender signs the text prior to sending it.
        Transaction transaction = new Transaction(text, senderHash, signature, LocalHash);
        restTemplate.put(node.toString() + "/transaction?publish=true", transaction);
      //  System.out.println("Hash of new transaction: " + Base64.encodeBase64String(transaction.getHash()));
    }
    
    public static void publishAuditRecord(String publisheprivatekey, String auditRecord, String sender) throws MalformedURLException, Exception{// Uses publishTransaction()
	//First we need to update our audit records by pulling from the audit server, and performing the audit server verification simultaneously
	AuditServerVerificartion();
	//we first add the record to the local storage, and to the list of out audit records
	addPostedAuditRec(auditRecord);addStoredAuditRec(auditRecord);
	//THen we calculate the hash of the stored audit recs, and we post to the server.
	publishTransaction(new URL("http://localhost:8080"), Paths.get(publisheprivatekey), auditRecord, Base64.decodeBase64(sender), Arrays.toString(calculateLocalHash()));//change the type of calculateLocalHash();
//Right after publishTransaction, mostRecentAuditRecord and mostRecentReportedLocalHash have to be updated. This is why we are pulling.
pullAudits();// Verify that tthe audit record shows on the audit server/

    }
    
    

    private static void publishAddress(URL node, Path publicKey, String name) throws IOException { //This is for the client to register with the audit node (audit server).
    RestTemplate restTemplate = new RestTemplate();
    Address address = new Address(name, Files.readAllBytes(publicKey));
    restTemplate.put(node.toString() + "/address?publish=true", address);
    addresstoPublish= Base64.encodeBase64String(address.getHash());
    System.out.println("Hash of new address: " + Base64.encodeBase64String(address.getHash()));
}


   

    private static void generateKeyPair() throws NoSuchProviderException, NoSuchAlgorithmException, IOException { //This is to generate key pairs. It is used by the key manager. Not needed if we already have ones.
    	
        KeyPair keyPair = SignatureUtils.generateKeyPair();
        Files.write(Paths.get("key3.priv"), keyPair.getPrivate().getEncoded());
        Files.write(Paths.get("key3.pub"), keyPair.getPublic().getEncoded());
    }


    
    public static void SetRecentAuditRecord(String mostRecentAuditRecordtemp, Long mostRecentReportingTimetemp, String mostRecentReportedLocalHashtemp) {
    	//THis method is to set the most recent audit records on the audit server, pulled by this client.
    	mostRecentAuditRecord=mostRecentAuditRecordtemp;
    	mostRecentReportingTime=mostRecentReportingTimetemp;
    	mostRecentReportedLocalHash=mostRecentReportedLocalHashtemp;
    }
    
    public static String AuditRecordsSize() throws Exception {
        String url = "http://localhost:8080/transaction/size";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // optional default is GET
        con.setRequestMethod("GET");
        //add request header
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = con.getResponseCode();
       // System.out.println("\nSending 'GET' request to URL : " + url);
        //System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
        	response.append(inputLine);
        }
        in.close();
        //print in String
        String ResponseStr=response.toString();
        return ResponseStr;//.substring(1, ResponseStr.length()-1);
    }
    
    public static void pullAudits() throws Exception { //Filling the Hashmap pulledAuditRecs. It also locates the most recently published audit record, and assigns it to static variables using SetRecentAuditRecord(,,) 
    	}

  
    public static List<String> hashmapToArrayList(Set<String> Hashset) { //I think this is what is shuffling the elements
    	String[] temp=	 (String[]) Hashset.toArray(new String[Hashset.size()]);
    	List<String> arrayList = new ArrayList<String>(Arrays.asList(temp));
		return arrayList;
    	
    }
    
    public static String[] ArraylistToArray(List<String> list) { //I think this is what is shuffling the elements
    	String[] temp = new String[list.size()];
    	temp = list.toArray(temp);

    	return temp;
    	
    }
    
   /* public static long[] ArraylistToArray_long(List<long> list) { //I think this is what is shuffling the elements
    	long[] temp = new long[list.size()];
    	temp = list.toArray(temp);

    	return temp;
    	
    }*/

    public static String ArrayListtoString(List<String> strList) {
	String combine="";
	for(int i=0; i<strList.size(); i++) {
		combine+=strList.get(i);
	}
	return combine;
}
    
    
    private static byte[] calculateLocalHash() {
        String hashableData = ArrayListtoString(getStoredAuditRecs());
        //System.out.println("hashableData "+ hashableData);
        return DigestUtils.sha256(hashableData);
    }
    
    // We can test this by posting recs manually.
    public static boolean AuditServerVerificartion() throws Exception {//This is used to verify that an audit record has been published on the server. Used in Publish audit record method.
    	pullAudits();//This updates a hashmap, which means that its size remains the same if you run it multiple times.
//if audit server is clear, and no messages have been posted by client, return true.
    if(pulledAuditRecs.isEmpty()&&getStoredAuditRecs().isEmpty()&&getPostedAuditRecs().isEmpty()) {
	System.out.println("Nobody has posted yet.");
	return true;//did not work.
    	}
    	
for (int i = 0; i < getPostedAuditRecs().size(); i++) {
			if(!pulledAuditRecs.contains(getPostedAuditRecs().get(i))) {System.out.println("Audit Server Verification Failure. "+getPostedAuditRecs().get(i)+"Does not exist on the server");
				return false;
			}
		}
    	for (int i = 0; i < getStoredAuditRecs().size(); i++) {
			if(!pulledAuditRecs.contains(getStoredAuditRecs().get(i))) {System.out.println("Audit Server Verification Failure. "+getStoredAuditRecs().get(i)+"Does not exist on the server");
				return false;
			}
		}
    	
    	setStoredAuditRecs(pulledAuditRecs);//Local hash seems to be messed up after this.
    	// The resulting hash value is nnot correct
    	//This does not have the same order as the arraylist
    	//Maybe change the data structure in transactions to arraylist?  
    	//The pulled data from the server has a different order from what is stored locally in an arraylist.
    	
    	//Here, we need to calculate the hash of setStoredAuditRecs and compare it with mostRecentReportedLocalHash.
    	if(Arrays.toString(calculateLocalHash()).equals(mostRecentReportedLocalHash)) return true;
    	else {
    		System.out.println("Audit server Malicious behaviour. Your Local hash does not coincide with another participant's.");
    		return false;
    	}
    }

    public static boolean AuditRecordverification(String receivedMsg) throws Exception {// To Implement.
    	pullAudits();
    	JWTMsg m=new JWTMsg();
    	PublicKey auditPublic =m.getKeyPairFromFile("server", "serverpw", serverpassphrase, "serverprivate").getPublic();
    	String VerifyAudit=m.ArraytoString(m.encrypt_long(m.Split_to_List(receivedMsg), auditPublic));
		
    	//To implement every detail of this, we need to parse the message to obtain the lable. This has to be done in case it is used in production.
			if(!pulledAuditRecs.contains(VerifyAudit)) {System.out.println("Audit Record Verification Failure. "+"Audit Record of the message that you received was not reported.");
			return false;
			}
			else{
			//If this record has been reported, then:
				JWTMsg ReceivedJWTMsg=new JWTMsg(receivedMsg);
				if(ReceivedJWTMsg.getLabel().equalsIgnoreCase("ini")) {
					if(pulledAuditRecs.size()==1) return true;
				}
				if(ReceivedJWTMsg.getLabel().equalsIgnoreCase("ini,parallel")){
					for(int i=0; i<pulledAuditRecsReportingTime.size()-1; i++) {
						if(pulledAuditRecsReportingTime.get(i)-pulledAuditRecsReportingTime.get(i+1)>epsilon) return false;
					}return true;
				}
				if(ReceivedJWTMsg.getPrev().equals(null)&&ReceivedJWTMsg.getParaPrev().equals(null))return false;
				else {
					if(!ReceivedJWTMsg.getPrev().equals(null)) {
						for(int i=0; i<ReceivedJWTMsg.getPrev().length;i++) {
							if(!pulledAuditRecs.contains(ReceivedJWTMsg.getPrev()[i])) {
								System.out.println("Previous record not valid in the message");
								return false;
							}
						}
					}
					if(!ReceivedJWTMsg.getParaPrev().equals(null)) {
						for(int i=0; i<ReceivedJWTMsg.getParaPrev().length; i++) {
							if(!pulledAuditRecs.contains(ReceivedJWTMsg.getParaPrev()[i])) {
								System.out.println("Previous record not valid in the message");
								return false;
							}
						}
					}
				}
			
			}
			
			
			return true;
    }

    public static boolean EncryptedAuditRecordverification(String EncryptedReceivedMsg, String ClientPair) throws Exception {// To Implement.
    	//This is the method we're using for audit record verification.
    	//Need to do the same modifications for Workflow participant.
    	pullAudits();
    	JWTMsg m=new JWTMsg();
    	PublicKey auditPublic =m.getKeyPairFromFile("server", "serverpw", serverpassphrase, "serverprivate").getPublic();
    	KeyPair receiverPair =m.getKeyPairFromFile(ClientPair, "clientpw", clientpassphrase, "clientprivate");
		//String JWTEncMsg= msg.Enc_JWT(msg,(RSAPublicKey)receiverPair.getPublic());
    	//System.out.println("0");
    	//==========> Maybe try cleaning the encrypted message///////////// Does not harm but not useful
    	System.out.println("This is how the receiver got the message: "+EncryptedReceivedMsg);
    	/*String CleanedRecievedMessage=m.CleanReceivedPrevForVerification(EncryptedReceivedMsg);
    	String[] receivedMsgArray=m.StringCleanCuttoArray(CleanedRecievedMessage);*/
    	//
    	String[] receivedMsgArray=m.StringCleanCuttoArray(EncryptedReceivedMsg);///////////////////
    	
    	//System.out.println("1");
    //	String receivedMsgArrayDecrypt[]= m.decrypt_long(receivedMsgArray, (RSAPrivateKey)receiverPair.getPrivate());
    	//System.out.println("2- Problem seems to be here.");
    	String receivedMsg=m.ArraytoString(m.decrypt_long(receivedMsgArray, (RSAPrivateKey)receiverPair.getPrivate()));
    	//String receivedMsg= m.Dec_JWT(EncryptedReceivedMsg, (RSAPrivateKey)receiverPair.getPrivate());
		//System.out.println("Received Msg After Decrypt:" +receivedMsg+ "END");
    	String VerifyAudit=m.ArraytoStringCleanCut(m.encrypt_long(m.Split_to_List(receivedMsg), auditPublic));
		
    	//To implement every detail of this, we need to parse the message to obtain the lable. This has to be done in case it is used in production.
			if(!pulledAuditRecs.contains(VerifyAudit)) {System.out.println("Audit Record Verification Failure. "+"Audit Record of the message that you received was not reported.");
			return false;
			}
			else{ System.out.println("1");
				//////////This part has been added to find the reference (timestamp) of the received message on the server
			System.out.println("pulledAuditRecs"+pulledAuditRecs+" pulledAuditRecsReportingTime "+pulledAuditRecsReportingTime+" pulledAuditRecsReportingTime_Str "+pulledAuditRecsReportingTime_str);
        		
			int index_of_timestamp=pulledAuditRecs.indexOf(VerifyAudit);
			System.out.println("index_of_timestamp"+ index_of_timestamp);
				//int index_of_timestamp2=pulledAuditRecs.indexOf(o)
				System.out.println("Adding pulledAuditRecsReportingTime_str.get(index_of_timestamp)= "+ pulledAuditRecsReportingTime_str.get(index_of_timestamp));
				ReferenceofAuditRecsforReceivedMessages.add(pulledAuditRecsReportingTime_str.get(index_of_timestamp));
				
				//////////////
				AuditRecsforReceivedMessages.add(VerifyAudit);// adding the equivalent audit record to the storage of the participant for future use
				System.out.println("2");
				//If this record has been reported, then:
				System.out.println("Received Msg: "+receivedMsg);
				JWTMsg ReceivedJWTMsg=new JWTMsg(receivedMsg);
				if(ReceivedJWTMsg.getLabel().equalsIgnoreCase("ini")) {
					if(pulledAuditRecs.size()==1) {System.out.println("EncryptedAuditRecordverification Passed");
						return true;}
				}
				if(ReceivedJWTMsg.getLabel().equalsIgnoreCase("ini,parallel")){
					for(int i=0; i<pulledAuditRecsReportingTime.size()-1; i++) {
						if(pulledAuditRecsReportingTime.get(i)-pulledAuditRecsReportingTime.get(i+1)>epsilon) return false;
					} return true;
				}
				if(ReceivedJWTMsg.getPrev().equals(null)&&ReceivedJWTMsg.getParaPrev().equals(null))return false;
				else {
					if(ReceivedJWTMsg.getPrev().length!=0) {//if(!ReceivedJWTMsg.getPrev().equals(null)) {
						for(int i=0; i<ReceivedJWTMsg.getPrev().length;i++) {if(!ReceivedJWTMsg.getPrev()[i].equalsIgnoreCase("Prev1")) {
							//If I am to go for the ref instead of a full audit trail as an object, I need to find the correct object that I need to verify through the reference (timestamp in my case).
							/////Added to use the reference of the record instead of the actual previous one.
							System.out.println("Here we are. THis is the index of prev"+ pulledAuditRecsReportingTime_str.indexOf(ReceivedJWTMsg.getPrev()[i]));
							int index_of_timestamp_prev=pulledAuditRecsReportingTime_str.indexOf(ReceivedJWTMsg.getPrev()[i]);
							String ActualPrev=pulledAuditRecs.get(index_of_timestamp_prev);
							System.out.println("Here is the actual Prev"+ pulledAuditRecs.get(index_of_timestamp_prev));
							
							/////
							if(!pulledAuditRecs.contains(m.CleanReceivedPrevForVerification(ActualPrev))) {//Here is where things are going wrong.
								System.out.println("Prev Previous record with reference number "+m.CleanReceivedPrevForVerification(ReceivedJWTMsg.getPrev()[i])+ " is not valid in the message");
								return false;
							}
						}
						}
					}
					if(ReceivedJWTMsg.getParaPrev().length!=0) {//if(!ReceivedJWTMsg.getParaPrev().equals(null)) {
						for(int i=0; i<ReceivedJWTMsg.getParaPrev().length; i++) {if(!ReceivedJWTMsg.getParaPrev()[i].equalsIgnoreCase("ParaPrev1")) {
							//If I am to go for the ref instead of a full audit trail as an object, I need to find the correct object that I need to verify through the reference (timestamp in my case).
							/////Added to use the reference of the record instead of the actual previous one.
							int index_of_timestamp_ParaPrev=pulledAuditRecsReportingTime.indexOf(ReceivedJWTMsg.getParaPrev()[i]);
							String ActualParaPrev=pulledAuditRecs.get(index_of_timestamp_ParaPrev);
							/////
							if(!pulledAuditRecs.contains(m.CleanReceivedPrevForVerification(ActualParaPrev))) {
								System.out.println("ParaPrev Previous record with reference number "+ m.CleanReceivedPrevForVerification(ReceivedJWTMsg.getParaPrev()[i])+ " is not valid in the message");
								return false;
							}
						}
						}
					}
				}
			
			}
			
			System.out.println("EncryptedAuditRecordverification Passed");
			return true;
    }

    

}



