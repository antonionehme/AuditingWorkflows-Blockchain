

package audit.client.blockchainrest;
//import audit.client.Web3JClientAPI;
import audit.client.Web3JClientAPI2;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.math.BigInteger;
//import org.web3j.model.Web3JClientAPI;
import audit.client.Web3JClientAPI2;

@RestController()
@RequestMapping("Bclient")
public class BlockchainClientController {
    private static final Logger LOG = LoggerFactory.getLogger(BlockchainClientController.class);

    private final Web3JClientAPI2 BlockchainClient;//MsgService msgService;
    // private final NodeService nodeService;

    @Autowired
    public BlockchainClientController(Web3JClientAPI2 BlockchainClient) {//public MsgController(MsgService msgService) {//this is changed to Merge if we are to combine the classes for AuditRec verif.
        this.BlockchainClient = BlockchainClient;
        // this.nodeService = nodeService;
    }


    @RequestMapping(value= "ini", method = RequestMethod.POST)
    void runAPI(){
        BlockchainClient.runAPI();
    }

    @RequestMapping(value= "saveLog", method = RequestMethod.POST)
    void saveLog(@RequestParam(required = true) String signature, @RequestParam(required = true) String encryptedMessage) throws Exception { // HttpServletResponse
        //saveLog(String signature, String encryptedMessage)
        //This has been added for an orchestrator to call participants while passing recipients. It is only used for Brite evaluation.																						// response)
        // {
        // LOG.info("Add transaction " +
        // Base64.encodeBase64String(transaction.getHash()));
        encryptedMessage = DigestUtils.sha256Hex(encryptedMessage); //calculate hash of received payload
        BlockchainClient.saveLog(signature, encryptedMessage);//Had to throw exceptions after I added AuditRecordverification(message);
    }
    @RequestMapping(value= "compareLog", method = RequestMethod.POST)
        //void compareLog(@RequestBody String signature, @RequestParam(required = true) String payload, @RequestParam(required = true) String owner) throws Exception {
    String compareLog(@RequestParam(required = true) String signature, @RequestParam(required = true) String payload, @RequestParam(required = true) String owner) throws Exception {
        payload = DigestUtils.sha256Hex(payload);  //calculate hash of received payload before comparison
       return BlockchainClient.compareLog(signature, payload, owner);
    }

    @RequestMapping(value= "saveKey", method = RequestMethod.POST)
    //void saveKey(@RequestBody String signature, @RequestParam(required = true) String keyChain, @RequestParam(required = true) String keyName, BigInteger keyType) throws Exception {
    String saveKey(@RequestParam(required = true) String signature, @RequestParam(required = true) String keyChain, @RequestParam(required = true) String keyName) throws Exception {
        System.out.println(""+signature+ " "+ keyChain+" "+keyName);
       return BlockchainClient.saveKey(signature, keyChain, keyName, BigInteger.valueOf(0));
    }

    // public void getKey(String signature, String owner, String keyName, BigInteger keyType)
    @RequestMapping(value= "getKey", method = RequestMethod.POST)
    String getKey(@RequestParam(required = true) String signature, @RequestParam(required = true) String owner, @RequestParam(required = true) String keyName) throws Exception {
       return BlockchainClient.getKey(signature,owner,keyName, BigInteger.valueOf(0));
    }

    }
