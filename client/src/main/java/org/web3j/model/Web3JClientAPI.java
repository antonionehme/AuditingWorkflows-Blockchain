//package org.web3j.model;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.web3j.crypto.CipherException;
//import org.web3j.crypto.Credentials;
//import org.web3j.crypto.WalletUtils;
//import org.web3j.protocol.Web3j;
//import org.web3j.protocol.admin.Admin;
//import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
//import org.web3j.protocol.admin.methods.response.PersonalListAccounts;
//import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
//import org.web3j.protocol.core.DefaultBlockParameterName;
//import org.web3j.protocol.core.methods.response.EthGetBalance;
//import org.web3j.protocol.core.methods.response.TransactionReceipt;
//import org.web3j.protocol.core.methods.response.Web3ClientVersion;
//import org.web3j.protocol.exceptions.TransactionException;
//import org.web3j.protocol.http.HttpService;
//import org.web3j.tx.Transfer;
//import org.web3j.utils.Convert;
//
//import java.io.File;
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.security.InvalidAlgorithmParameterException;
//import java.security.NoSuchAlgorithmException;
//import java.security.NoSuchProviderException;
//import java.util.List;
//
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//import com.google.api.client.util.Maps;
//
////import java.security.KeyFactory;
//
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//
//@Service
//@SpringBootApplication
//public class Web3JClientAPI {
//
//    private static  String password;
//    private static String fileName;
//    private static File file;
//    private static String address;
//
//    private static  BigInteger GAS_PRICE = BigInteger.valueOf(3000000);
//    private static  BigInteger GAS_LIMIT = BigInteger.valueOf(5201314);
//
//    private static  String contractAddress;
//    private static AuditLog auditLog;
//    private static KeyFactory keyFactory;
//    private static SafeMath safeMath;
//    private static Verification verification;
//
//    private static Web3j web3;
//    private static Admin admin;
//
//    private Credentials credentials;
//    private Web3ClientVersion web3ClientVersion;
//
//    public static String version;
//
//    @Autowired
//    public Web3JClientAPI() {//public TransactionService(AddressService addressService) {
//        //  this.addressService = addressService;
//    }
//
//    //String account = web3.ethAccounts().send().getAccounts().get(0);
//    //Credentials credentials = Credentials.create(account);
//    //String contractAddress = "0x345ca3e014aaf5dca488057592ee47305d9b3e10"; //The deployed contract address, taken from truffle console or ganache logs
//    public static void main(String[] args) throws Exception{
//
//        //new Web3JClientAPI().runAPI();
//
//        SpringApplication app = new SpringApplication(Web3JClientAPI.class);
//        Map<String, Object> pro1 = Maps.newHashMap();
//        pro1.put("server.port", "8333");
//
//        app.setDefaultProperties(pro1);
//        // //app.setDefaultProperties(Collections.singletonMap("server.port", "8091"));//////
//        //Had to change this
//        app.run(args);
//
//
//    }
//    public void runAPI() {
//        web3 = Web3j.build(new HttpService("http://127.0.0.1:8546/"));
//
//        connectEthClient();
//
//        admin = Admin.build(new HttpService("http://127.0.0.1:8546/"));
//        //createNewAccount();
//        //getAccountList();
//
//        //wallet();
//        //transaction();
//        //deployContract();
//        saveLog("xiaohu", "this is test");
//        saveKey("xiaohu", "12gubv233f123g3", "workflow", BigInteger.valueOf(0));
//    }
//
//    public void connectEthClient() {
//        try {
//            /* connecting with ethereum client*/
//            web3ClientVersion = web3.web3ClientVersion().send();
//            String clientVersion = web3ClientVersion.getWeb3ClientVersion();
//            System.out.println("client version:" + clientVersion);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//     public void wallet(){
//        try {
//            /* wallet creation*/
//            file = new File("/home/auditworkflow/private/geth/chaindata/keystore");
//            password = "password@1";
//            fileName = WalletUtils.generateFullNewWalletFile(password, file);
//            System.out.println("wallet name:" +fileName);
//
//            /*load wallet */
//            credentials = WalletUtils.loadCredentials(password, "/home/auditworkflow/private/geth/chaindata/keystore/"+fileName);
//            System.out.println("credential address:" + credentials.getAddress());
//
//            BigInteger publicKey = credentials.getEcKeyPair().getPublicKey();
//            BigInteger privateKey = credentials.getEcKeyPair().getPrivateKey();
//            System.out.println("publickey:"+publicKey);
//            System.out.println("privateKey:"+privateKey);
//
//            /* get wallet balance */
//            address = credentials.getAddress();
//            EthGetBalance balance = web3.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
//            System.out.println("account balance:"+balance.getBalance());
//
//        } catch (IOException | NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException | CipherException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    /* simple transaction: send some eth*/
//    public void transaction() {
//        try {
//            credentials = WalletUtils.loadCredentials("123", "/home/auditworkflow/private/geth/chaindata/keystore/UTC--2019-08-20T08-55-08.001386110Z--48e46c23904a4785191719a43c889a3c8540011d");
//            TransactionReceipt transactionReceipt = Transfer.sendFunds(web3, credentials, "0x6a847d9c654020edbf7ada6c3e8ebfb5746265b1", BigDecimal.ONE, Convert.Unit.FINNEY).send();
//            System.out.println("transaction hash:" + transactionReceipt.getTransactionHash() + "-from:" + transactionReceipt.getFrom() + "-to:" + transactionReceipt.getTo() + "-status:" + transactionReceipt.getStatus());
//        } catch (IOException | CipherException | InterruptedException | TransactionException e) {
//            e.printStackTrace();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//        /* create a new account and unlock it*/
//    public void createNewAccount() {
//        password = "password@2";
//        BigInteger unlockTime = BigInteger.valueOf(60L);
//        try {
//            NewAccountIdentifier newAccountIdentifier = admin.personalNewAccount(password).send();
//            address = newAccountIdentifier.getAccountId();
//            System.out.println("newAccountAddress:" +address);
//            /* unlock this account: it the rpc is running, this new account cannot be unlocked because of default security setting*/
//            PersonalUnlockAccount personalUnlockAccount= admin.personalUnlockAccount(address, password, unlockTime).send();
//            Boolean isLock = personalUnlockAccount.accountUnlocked();
//            System.out.println("this account:"+address+"- is - " +isLock);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    public void getAccountList()  {
//        try{
//            PersonalListAccounts personalListAccounts = admin.personalListAccounts().send();
//            List<String> addressList;
//            addressList = personalListAccounts.getAccountIds();
//            System.out.println("account size:"+addressList.size());
//            for (String address : addressList) {
//                System.out.println(address);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//    /* deploy smart contracts*/
//    public void deployContract() {
//
//        try {
//            credentials = WalletUtils.loadCredentials("123", "/home/auditworkflow/private/geth/chaindata/keystore/UTC--2019-08-20T08-55-08.001386110Z--48e46c23904a4785191719a43c889a3c8540011d");
//
//            safeMath = SafeMath.deploy(web3, credentials, GAS_PRICE, GAS_LIMIT).send();
//            System.out.println("safeMath contractAddress: "+safeMath.getContractAddress());
//
//            auditLog = AuditLog.deploy(web3, credentials, GAS_PRICE, GAS_LIMIT).send();
//            auditLog.isValid();
//            System.out.println("auditLog contractAddress:" +auditLog.getContractAddress());
//
//            verification = Verification.deploy(web3, credentials, GAS_PRICE, GAS_LIMIT).send();
//            verification.isValid();
//            System.out.println("verification contractAddress:"+ verification.getContractAddress());
//
//            keyFactory = KeyFactory.deploy(web3, credentials, GAS_PRICE, GAS_LIMIT).send();
//            keyFactory.isValid();
//            System.out.println("keyfactory contractAddress:" +keyFactory.getContractAddress());
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (CipherException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    /*load smart contracts
//     * auditLog address: 0xcbbfbaf0619d64dd5c6912f0e4930f32fca0b08c
//     * verification address: 0xbc026aa8d91cc7f913cbeb42aa61ddaf98587ad6
//     * keyfactory contract address:0xa7bf5003c158f1e7493730ba57312a1a8d64d3be
//     * safe math COntract: 0x2661d7099d9a8310c04d9eeefd19ac94fa2c104e
//     * */
//    public void saveLog(String signature, String encryptedMessage) {//API
//        try { /*new audit log into the blockchain by participants*/
//            credentials = WalletUtils.loadCredentials("123", "/home/auditworkflow/private/geth/chaindata/keystore/UTC--2019-08-20T08-55-08.001386110Z--48e46c23904a4785191719a43c889a3c8540011d");
//            //contractAddress = "0xcbbfbaf0619d64dd5c6912f0e4930f32fca0b08c";
//            auditLog = AuditLog.deploy(web3, credentials, GAS_PRICE, GAS_LIMIT).send();
//            auditLog.isValid();
//            System.out.println("auditLog contractAddress:" +auditLog.getContractAddress());
//
//            contractAddress = auditLog.getContractAddress();
//            auditLog = AuditLog.load(contractAddress, web3, credentials, GAS_PRICE, GAS_LIMIT);
//            TransactionReceipt transactionReceipt = auditLog.saveLog(signature, encryptedMessage,BigInteger.valueOf(3000)).send();
//            System.out.println("transaction hash:" +transactionReceipt.getTransactionHash()+ "-isSaved:"+transactionReceipt.getStatus());
//            System.out.println(transactionReceipt.getLogs());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    public  void saveKey(String signature, String keyChain, String keyName, BigInteger keyType) {////API
//        try { /*save public keys of workflow and participants into the blockchain*/
//            credentials = WalletUtils.loadCredentials("123", "/home/auditworkflow/private/geth/chaindata/keystore/UTC--2019-08-20T08-55-08.001386110Z--48e46c23904a4785191719a43c889a3c8540011d");
//            //contractAddress = "0xa7bf5003c158f1e7493730ba57312a1a8d64d3be";
//            keyFactory = KeyFactory.deploy(web3, credentials, GAS_PRICE, GAS_LIMIT).send();
//            keyFactory.isValid();
//            System.out.println("keyfactory contractAddress:" +keyFactory.getContractAddress());
//
//            contractAddress = keyFactory.getContractAddress();
//            keyFactory = KeyFactory.load(contractAddress, web3, credentials, GAS_PRICE, GAS_LIMIT);
//            TransactionReceipt transactionReceipt = keyFactory.saveKey(signature, keyChain, keyName, keyType, BigInteger.valueOf(3000)).send();
//            System.out.println("transaction hash:" +transactionReceipt.getTransactionHash()+ "-isSaved:"+transactionReceipt.getStatus());
//            System.out.println(transactionReceipt.getLogs());
//        } catch (IOException | CipherException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    public void compareLog(String signature, String payload, String owner) {
//        try {////API
//            /* compare received payload with audit trail*/
//            credentials = WalletUtils.loadCredentials("123", "/home/auditworkflow/private/geth/chaindata/keystore/UTC--2019-08-20T08-55-08.001386110Z--48e46c23904a4785191719a43c889a3c8540011d");
//            //contractAddress = "0xbc026aa8d91cc7f913cbeb42aa61ddaf98587ad6";
//
//            verification = Verification.deploy(web3, credentials, GAS_PRICE, GAS_LIMIT).send();
//            verification.isValid();
//            System.out.println("verification contractAddress:"+ verification.getContractAddress());
//
//            contractAddress = verification.getContractAddress();
//            verification = Verification.load(contractAddress, web3, credentials, GAS_PRICE, GAS_LIMIT);
//
//            //Transaction transaction = Transaction.createEthCallTransaction("");
//
//           // TransactionReceipt transactionReceipt = verification.compareLogs(signature, payload, owner).send();
//           // System.out.println("transaction hash:" +transactionReceipt.getTransactionHash()+ "-isMatched:"+transactionReceipt.getStatus());
//           // System.out.println(transactionReceipt.getLogs());
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (CipherException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
