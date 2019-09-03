pragma solidity >=0.4.21 <0.6.0;
import "./SafeMath.sol";
/*combined contract AuditLog with Verification*/
contract LogFactory {

    using SafeMath for uint256; // prevent overflow or underflow
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
        uint id = logs.push(Log(_signature,_encryptedMessage)) - 1;
        logToOwner[id] = msg.sender; //allocate the log to owner by logId

        signToMessage[msg.sender] = _signature;
        //ownerLogCount[msg.sender]++;
        ownerLogCount[msg.sender] = ownerLogCount[msg.sender].add(1);
        emit NewLog(id, _signature, _encryptedMessage);
        return true;
    }
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
                while(keccak256(bytes(preLog.hashOfMessage)) == keccak256(bytes(_payload))) {
                    return true;
                }
            } return false;
        } else {
            return false;
        }
    } // end function

}


