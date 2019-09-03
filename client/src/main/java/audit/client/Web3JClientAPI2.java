package audit.client;

import com.google.api.client.util.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.model.KeyFactory;
import org.web3j.model.LogFactory;
import org.web3j.model.MyContractsTest;
import org.web3j.model.SafeMath;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.admin.methods.response.PersonalListAccounts;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


@Service
@SpringBootApplication
public class Web3JClientAPI2 {

    private static  String password = "123";
    private static String fileName;
    private static File file;
    private static String address;

    private static  BigInteger GAS_PRICE = BigInteger.valueOf(3000000);
    private static  BigInteger GAS_LIMIT = BigInteger.valueOf(5201314);

    private static  String contractAddress;
    private static KeyFactory keyFactory;
    private static SafeMath safeMath;
    private static LogFactory logFactory;
    private static MyContractsTest myContractsTest; // a instance of combined contract

    private static Web3j web3;
    private static Admin admin;

    private Credentials credentials;
    private Web3ClientVersion web3ClientVersion;
    public static String version;

    @Autowired
    public Web3JClientAPI2() {//public TransactionService(AddressService addressService) {
        //  this.addressService = addressService;
    }

    public static void main(String[] args) throws Exception{

       // new Web3JClientAPI2().runAPI();

        SpringApplication app = new SpringApplication(Web3JClientAPI2.class);
        Map<String, Object> pro1 = Maps.newHashMap();
        pro1.put("server.port", "8333");

        app.setDefaultProperties(pro1);
        // //app.setDefaultProperties(Collections.singletonMap("server.port", "8091"));//////
        //Had to change this
        app.run(args);
        //new Web3JClientAPI2().runAPI();

    }
    public void runAPI() {
        /*accounts password for ethClient
         * [0] password:123, [1] password:123456, [2] password: password@1; [3] password: password@2
         * new others are '123'*/
        web3 = Web3j.build(new HttpService("http://127.0.0.1:8546/"));

        connectEthClient();
        /*-------------------call functions of accounts management in the ethereum client or nodes--------*/
        //admin = Admin.build(new HttpService("http://127.0.0.1:8546/"));
        //createNewAccount();
        //getAccountList();

        //wallet();

        //transaction();
        /*----------------call functions of smart contracts-----------------------------*/
        deployContract();
        //saveLog("xiaohu", "12");
        //saveLog("xiaohu", "123");
        //saveKey("xiaohu", "12gubv233f123g3", "workflow", BigInteger.valueOf(0));
        //saveKey("xiaohu", "test", "workflow1", BigInteger.valueOf(0));
        //saveKey("xiaohu","nodeKey","participant1", BigInteger.valueOf(1));
       //compareLog("xiaohu", "12", "0x48e46c23904a4785191719a43c889a3c8540011d");
        //getKey("xiaohu", "0x48e46c23904a4785191719a43c889a3c8540011d", "workflow1", BigInteger.valueOf(0));
    }

    public void connectEthClient() {
        try {
            /* connecting with ethereum client*/
            web3ClientVersion = web3.web3ClientVersion().send();
            String clientVersion = web3ClientVersion.getWeb3ClientVersion();
            //System.out.println("client version:" + clientVersion);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*-------------------------create a account by wallet and credentials---------------------------------*/
    public void wallet(){
        try {
            /* wallet creation - create a new account*/
            file = new File("/home/auditworkflow/private/geth/chaindata/keystore");
            //password = "password@1";
            fileName = WalletUtils.generateFullNewWalletFile(password, file);
            System.out.println("wallet name:" +fileName);

            /*load wallet */
            credentials = WalletUtils.loadCredentials(password, "/home/auditworkflow/private/geth/chaindata/keystore/"+fileName);
            System.out.println("credential address:" + credentials.getAddress());

            BigInteger publicKey = credentials.getEcKeyPair().getPublicKey();
            BigInteger privateKey = credentials.getEcKeyPair().getPrivateKey();
            System.out.println("publickey:"+publicKey);
            System.out.println("privateKey:"+privateKey);

            /* get wallet balance */
            address = credentials.getAddress();
            EthGetBalance balance = web3.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
            System.out.println("account balance:"+balance.getBalance());

        } catch (IOException | NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException | CipherException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /* -------------------------------------simple transaction test: transfer some ether--------------------------------*/
    public void transaction() {
        try {
            credentials = WalletUtils.loadCredentials(password, "/home/auditworkflow/private/geth/chaindata/keystore/UTC--2019-08-20T08-55-08.001386110Z--48e46c23904a4785191719a43c889a3c8540011d");
            TransactionReceipt transactionReceipt = Transfer.sendFunds(web3, credentials, "0x6a847d9c654020edbf7ada6c3e8ebfb5746265b1", BigDecimal.ONE, Convert.Unit.FINNEY).send();
            System.out.println("transaction hash:" + transactionReceipt.getTransactionHash() + "-from:" + transactionReceipt.getFrom() + "-to:" + transactionReceipt.getTo() + "-status:" + transactionReceipt.getStatus());
        } catch (IOException | CipherException | InterruptedException | TransactionException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /* -----------------------AdminManager for accounts------------create a new account and unlock it----------------------*/
    public void createNewAccount() {
        //password = "password@2";
        BigInteger unlockTime = BigInteger.valueOf(60L);
        try {
            NewAccountIdentifier newAccountIdentifier = admin.personalNewAccount(password).send();
            address = newAccountIdentifier.getAccountId();
            System.out.println("newAccountAddress:" +address);
            /* unlock this account: if the rpc is running, this new account cannot be unlocked because of default security setting. add --allow-insecure-unlock*/
            PersonalUnlockAccount personalUnlockAccount= admin.personalUnlockAccount(address, password, unlockTime).send();
            Boolean isLock = personalUnlockAccount.accountUnlocked();
            System.out.println("this account:"+address+"- isLock - " +isLock);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void getAccountList()  {
        try{
            PersonalListAccounts personalListAccounts = admin.personalListAccounts().send();
            List<String> addressList;
            addressList = personalListAccounts.getAccountIds();
            System.out.println("account size:"+addressList.size());
            for (String address : addressList) {
                System.out.println(address);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /*------------------------------ deploy smart contracts----------------------------------------------*/
    public void deployContract() {

        try {
            credentials = WalletUtils.loadCredentials(password, "/home/auditworkflow/private/geth/chaindata/keystore/UTC--2019-08-20T08-55-08.001386110Z--48e46c23904a4785191719a43c889a3c8540011d");

            safeMath = SafeMath.deploy(web3, credentials, GAS_PRICE, GAS_LIMIT).send();
            System.out.println("safeMath contractAddress: "+safeMath.getContractAddress());

            keyFactory = KeyFactory.deploy(web3, credentials, GAS_PRICE, GAS_LIMIT).send();
            keyFactory.isValid();
            System.out.println("keyfactory contractAddress:" +keyFactory.getContractAddress());

            logFactory = LogFactory.deploy(web3, credentials,GAS_LIMIT,GAS_PRICE).send();
            System.out.println("logfactory contract address:"+ logFactory.getContractAddress());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (CipherException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*-------------------------------------load all smart contracts----------APIs-----------------------*/

    /*new audit log into the blockchain by participants*/
    public void saveLog(String signature, String encryptedMessage) {
        contractAddress = logFactory.getContractAddress();
        if(contractAddress != null && credentials !=null) {
            try {
                //logFactory = LogFactory.load(contractAddress,web3,credentials,GAS_PRICE,GAS_LIMIT);
                TransactionReceipt transactionReceipt = logFactory.saveLog(signature, encryptedMessage, BigInteger.valueOf(30000)).send(); //trigger with smart contract saveKey()
                List<String> list = logFactory.getLogIdByOwner("0x48e46c23904a4785191719a43c889a3c8540011d").sendAsync().get(); //show all exiting logs id from blockchain

                //System.out.println("transaction hash:" +transactionReceipt.getTransactionHash());
                System.out.println("Log is Saved OK:"+transactionReceipt.isStatusOK());
                System.out.println("system log is:"+transactionReceipt.getLogs());
                System.out.println("logs id are:"+list);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("please check the smart contract deployment and credentials load.");
        }

    }
    /*save public keys of workflow and participants into the blockchain*/
    public  String saveKey(String signature, String keyChain, String keyName, BigInteger keyType) {
        contractAddress = keyFactory.getContractAddress(); //single contract
        if (contractAddress != null && credentials !=null) {
            try {
                //keyFactory = KeyFactory.load(contractAddress, web3, credentials, GAS_PRICE, GAS_LIMIT);
                TransactionReceipt transactionReceipt = keyFactory.saveKey(signature, keyChain, keyName, keyType, BigInteger.valueOf(30000)).send(); //trigger with saveKey in the smart contract

                //System.out.println("transaction hash:" +transactionReceipt.getTransactionHash());
                System.out.println("Key is Saved OK:"+transactionReceipt.isStatusOK());
                System.out.println("system log is:"+transactionReceipt.getLogs());

                List<String> l = keyFactory.getKeyIdByOwner("0x48e46c23904a4785191719a43c889a3c8540011d").sendAsync().get();
                System.out.println("keys id are:"+l);
                return "System log is:"+transactionReceipt.getLogs();
            } catch (IOException | CipherException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("please check the smart contract deployment and credentials load.");

        }
        return "please check the smart contract deployment and credentials load.";
    }
    /* compare received payload with audit trail*/
    public String compareLog(String signature, String payload, String owner) {
        contractAddress = logFactory.getContractAddress();
        if (contractAddress != null && credentials !=null) {
            try {
                //logFactory = LogFactory.load(contractAddress,web3,credentials,GAS_PRICE,GAS_LIMIT);
                Boolean b = logFactory.compareLogs(signature, payload, owner).sendAsync().get();  // trigger with verification
                System.out.println("is match ? "+b);
                return "it's a match";
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("please check the smart contract logFactory deployment and credentials load.");
        }
        return "it's not a match";
    }
    /*get public keys from bockchain*/
    public void getKey(String signature, String owner, String keyName,BigInteger keyType) {
        contractAddress = keyFactory.getContractAddress();  //single contract
        System.out.println("key contract address:"+contractAddress);
        if (contractAddress != null && credentials !=null) {
            try {
                keyFactory = KeyFactory.load(contractAddress,web3,credentials,GAS_PRICE,GAS_LIMIT);
                String result = keyFactory.getKey(signature, owner, keyName, keyType).sendAsync().get(); // get keys from blockchain
                System.out.println("the key is:" + result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("please check the smart contract KeyFactory deployment and credentials load");
        }
    }
}
