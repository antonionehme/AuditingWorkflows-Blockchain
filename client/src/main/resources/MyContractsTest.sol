pragma solidity >=0.4.21 <0.6.0;

/**
 * @title SafeMath
 * @dev Math operations with safety checks that throw on error
 */
library SafeMath {

    /**
    * @dev Multiplies two numbers, throws on overflow.
    */
    function mul(uint256 a, uint256 b) internal pure returns (uint256) {
        if (a == 0) {
            return 0;
        }
        uint256 c = a * b;
        assert(c / a == b);
        return c;
    }

    /**
    * @dev Integer division of two numbers, truncating the quotient.
    */
    function div(uint256 a, uint256 b) internal pure returns (uint256) {
        // assert(b > 0); // Solidity automatically throws when dividing by 0
        uint256 c = a / b;
        // assert(a == b * c + a % b); // There is no case in which this doesn't hold
        return c;
    }

    /**
    * @dev Substracts two numbers, throws on overflow (i.e. if subtrahend is greater than minuend).
    */
    function sub(uint256 a, uint256 b) internal pure returns (uint256) {
        assert(b <= a);
        return a - b;
    }

    /**
    * @dev Adds two numbers, throws on overflow.
    */
    function add(uint256 a, uint256 b) internal pure returns (uint256) {
        uint256 c = a + b;
        assert(c >= a);
        return c;
    }
}


contract MyContractsTest {
    using SafeMath for uint256; // prevent overflow or underflow

    ///////////////////////key factory
    event NewKey (uint keyId, string keyChain, string signature, string keyName);

    struct Key {
        string signature;
        string keyChain;
        string keyName;
        uint8 keyType; // 0 means workflow key, 1 means entity key
    }

    Key[] public keys;

    mapping (uint => address) public keyToOwner;
    mapping (address => string) public signToKey;
    mapping (address => uint) ownerKeyCount;
    //mapping (string => address) public keyOfOwner;
    mapping(uint => string) public idToName;

    function saveKey(string memory _signature, string memory _keyChain, string memory _keyName, uint8 _keyType) public payable returns(bool) {

        uint id = keys.push(Key(_signature,_keyChain, _keyName, _keyType)) - 1;
        keyToOwner[id] = msg.sender; //allocate the key to owner by keyId
        //keyOfOwner[_signature] = msg.sender;
        signToKey[msg.sender] = _signature;
        idToName[id] = _keyName;
        ownerKeyCount[msg.sender] = ownerKeyCount[msg.sender].add(1);
        emit NewKey(id, _signature, _keyChain, _keyName);
        return true;
    }


    function getKeyIdByOwner(address _owner) public view returns(uint[] memory) {
        //address myAddress = keyOfOwner[_signature];
        uint[] memory result = new uint[](ownerKeyCount[_owner]);
        uint counter = 0;
        for (uint i = 0; i < keys.length; i++) {
            if (keyToOwner[i] == _owner) {
                result[counter] = i;
                counter = counter.add(1);
            } // end if
        } // end for
        return result;
    }

    function getKey(string memory _signature, address _owner, string memory _keyName, uint8 _keyType) public view returns(string memory) {

        //address myAddress = keyOfOwner[_signature];
        if (keccak256(bytes(signToKey[_owner])) == keccak256(bytes(_signature))) {
            uint[] memory result = getKeyIdByOwner(_owner);
            uint id = 0;
            for (uint i=0; i< result.length; i++) {
                id = result[i];
                Key storage myKey = keys[id];
                while(keccak256(bytes(myKey.keyName)) == keccak256(bytes(_keyName)) && myKey.keyType == _keyType) {
                    return myKey.keyChain;
                }
            } return "this is a wrong key type or name";
        } else {
            return "this is a wrong account or signature";
        }
    } // end function


    /////////////////////////////////////////auditLog
    event NewLog (uint logId, string signature, string hashOfMessage);

    struct Log {
        string signature;
        string hashOfMessage;
    }

    Log[] public logs;

    mapping (uint => address) public logToOwner;
    mapping (address => string) public signToMessage;
    mapping (address => uint) ownerLogCount;


    ///@dev save log
    function saveLog(string memory _signature, string memory _encryptedMessage) public payable returns(bool) {
        //bytes32 _hashOfMessage = sha256(bytes(_encryptedMessage));
        //uint id =logs.push(Log(_signature,_hashOfMessage)) -1;
        uint id = logs.push(Log(_signature,_encryptedMessage)) - 1;
        logToOwner[id] = msg.sender; //allocate the log to owner by logId

        signToMessage[msg.sender] = _signature;
        ownerLogCount[msg.sender] = ownerLogCount[msg.sender].add(1);
        //emit NewLog(id, _signature, _hashOfMessage);
        emit NewLog(id, _signature, _encryptedMessage);
        return true;
    }
///////////////////////////////////////////////verification
    ///@dev get logId of all logs from owner
    function getLogIdByOwner(address _owner) public view returns(uint[] memory) {
        uint[] memory result = new uint[](ownerLogCount[_owner]);
        uint counter = 0;
        for (uint i = 0; i < logs.length; i++) {
            if (logToOwner[i] == _owner) {
                result[counter] = i;
                //counter++;
                counter = counter.add(1);
            }
        }
        return result;
    }

    /// @dev verify the hashOfMessage from audit log and payload

    function compareLogs(string memory _signature, string memory _payload, address _owner) public view returns(bool) {
        //bytes32 payload = sha256(bytes(_payload));

        if (keccak256(bytes(signToMessage[_owner])) == keccak256(bytes(_signature))) {
            uint[] memory result = getLogIdByOwner(_owner);
            uint id = 0;
            for (uint i=0; i< result.length; i++) {
                id = result[i];
                Log storage preLog = logs[id];
                //while(preLog.hashOfMessage == payload)
                while(keccak256(bytes(preLog.hashOfMessage)) == keccak256(bytes(_payload)))
                {
                    return true;
                }
            } return false;
        } else {
            return false;
        }
    } // end function

}
