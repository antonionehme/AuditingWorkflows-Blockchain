pragma solidity >=0.4.21 <0.6.0;
import "./SafeMath.sol";

contract KeyFactory {
  using SafeMath for uint256; // prevent overflow or underflow
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
}
