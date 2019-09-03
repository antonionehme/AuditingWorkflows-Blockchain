package org.web3j.model;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.2.0.
 */
public class MyContractsTest extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b506119cc806100206000396000f3006080604052600436106100c35763ffffffff7c0100000000000000000000000000000000000000000000000000000000600035041662e87da181146100c8578063014a5ef5146101a95780630cb6aaf11461021a5780633423d1e51461037f578063364410b31461049f57806361f1257b146104b7578063634c61091461055957806387e1ba611461057a578063a23c0918146105ae578063c07288e9146105cf578063dda25d01146105f0578063e79899bd1461067a578063fd4dc7e514610770575b600080fd5b6040805160206004803580820135601f810184900484028501840190955284845261019594369492936024939284019190819084018382808284375050604080516020601f89358b018035918201839004830284018301909452808352979a99988101979196509182019450925082915084018382808284375050604080516020601f89358b018035918201839004830284018301909452808352979a9998810197919650918201945092508291508401838280828437509497505050923560ff16935061078892505050565b604080519115158252519081900360200190f35b3480156101b557600080fd5b506101ca600160a060020a0360043516610a82565b60408051602080825283518183015283519192839290830191858101910280838360005b838110156102065781810151838201526020016101ee565b505050509050019250505060405180910390f35b34801561022657600080fd5b50610232600435610b4d565b6040805160ff83166060820152608080825286519082015285519091829160208084019284019160a08501918a019080838360005b8381101561027f578181015183820152602001610267565b50505050905090810190601f1680156102ac5780820380516001836020036101000a031916815260200191505b50848103835287518152875160209182019189019080838360005b838110156102df5781810151838201526020016102c7565b50505050905090810190601f16801561030c5780820380516001836020036101000a031916815260200191505b50848103825286518152865160209182019188019080838360005b8381101561033f578181015183820152602001610327565b50505050905090810190601f16801561036c5780820380516001836020036101000a031916815260200191505b5097505050505050505060405180910390f35b34801561038b57600080fd5b506040805160206004803580820135601f810184900484028501840190955284845261042a94369492936024939284019190819084018382808284375050604080516020601f818a01358b0180359182018390048302840183018552818452989b600160a060020a038b35169b909a9099940197509195509182019350915081908401838280828437509497505050923560ff169350610d3792505050565b6040805160208082528351818301528351919283929083019185019080838360005b8381101561046457818101518382015260200161044c565b50505050905090810190601f1680156104915780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b3480156104ab57600080fd5b5061042a6004356110c0565b3480156104c357600080fd5b506040805160206004803580820135601f810184900484028501840190955284845261019594369492936024939284019190819084018382808284375050604080516020601f89358b018035918201839004830284018301909452808352979a99988101979196509182019450925082915084018382808284375094975050509235600160a060020a0316935061115b92505050565b34801561056557600080fd5b5061042a600160a060020a03600435166113ad565b34801561058657600080fd5b50610592600435611415565b60408051600160a060020a039092168252519081900360200190f35b3480156105ba57600080fd5b506101ca600160a060020a0360043516611430565b3480156105db57600080fd5b5061042a600160a060020a03600435166114f2565b6040805160206004803580820135601f810184900484028501840190955284845261019594369492936024939284019190819084018382808284375050604080516020601f89358b018035918201839004830284018301909452808352979a9998810197919650918201945092508291508401838280828437509497506115589650505050505050565b34801561068657600080fd5b50610692600435611788565b604051808060200180602001838103835285818151815260200191508051906020019080838360005b838110156106d35781810151838201526020016106bb565b50505050905090810190601f1680156107005780820380516001836020036101000a031916815260200191505b50838103825284518152845160209182019186019080838360005b8381101561073357818101518382015260200161071b565b50505050905090810190601f1680156107605780820380516001836020036101000a031916815260200191505b5094505050505060405180910390f35b34801561077c57600080fd5b506105926004356118d4565b60408051608081018252858152602080820186905291810184905260ff831660608201526000805460018181018084558380528451805194968796939592946004027f290decd9548b62a8d60345a988386fc84ba6bc95484008f6362f93160ef3e56301926107fa9284920190611905565b5060208281015180516108139260018501920190611905565b506040820151805161082f916002840191602090910190611905565b50606091909101516003909101805460ff191660ff90921691909117905503600081815260016020908152604080832080543373ffffffffffffffffffffffffffffffffffffffff199091168117909155835260028252909120885192935061089c929091890190611905565b50600081815260046020908152604090912085516108bc92870190611905565b50336000908152600360205260409020546108de90600163ffffffff6118ef16565b6003600033600160a060020a0316600160a060020a03168152602001908152602001600020819055507fbf2613ed033e1ab990683f764ec4e76da473b83a70ed3a3abc9c0a76e411623a8187878760405180858152602001806020018060200180602001848103845287818151815260200191508051906020019080838360005b8381101561097757818101518382015260200161095f565b50505050905090810190601f1680156109a45780820380516001836020036101000a031916815260200191505b50848103835286518152865160209182019188019080838360005b838110156109d75781810151838201526020016109bf565b50505050905090810190601f168015610a045780820380516001836020036101000a031916815260200191505b50848103825285518152855160209182019187019080838360005b83811015610a37578181015183820152602001610a1f565b50505050905090810190601f168015610a645780820380516001836020036101000a031916815260200191505b5097505050505050505060405180910390a150600195945050505050565b6060806000806008600086600160a060020a0316600160a060020a0316815260200190815260200160002054604051908082528060200260200182016040528015610ad7578160200160208202803883390190505b50925060009150600090505b600554811015610b4457600081815260066020526040902054600160a060020a0386811691161415610b3c57808383815181101515610b1e57fe5b60209081029091010152610b3982600163ffffffff6118ef16565b91505b600101610ae3565b50909392505050565b6000805482908110610b5b57fe5b60009182526020918290206004919091020180546040805160026001841615610100026000190190931692909204601f810185900485028301850190915280825291935091839190830182828015610bf45780601f10610bc957610100808354040283529160200191610bf4565b820191906000526020600020905b815481529060010190602001808311610bd757829003601f168201915b505050505090806001018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610c925780601f10610c6757610100808354040283529160200191610c92565b820191906000526020600020905b815481529060010190602001808311610c7557829003601f168201915b50505060028085018054604080516020601f6000196101006001871615020190941695909504928301859004850281018501909152818152959695945090925090830182828015610d245780601f10610cf957610100808354040283529160200191610d24565b820191906000526020600020905b815481529060010190602001808311610d0757829003601f168201915b5050506003909301549192505060ff1684565b6060806000806000886040518082805190602001908083835b60208310610d6f5780518252601f199092019160209182019101610d50565b6001836020036101000a038019825116818451168082178552505050505050905001915050604051809103902060001916600260008a600160a060020a0316600160a060020a031681526020019081526020016000206040518082805460018160011615610100020316600290048015610e205780601f10610dfe576101008083540402835291820191610e20565b820191906000526020600020905b815481529060010190602001808311610e0c575b5050915050604051809103902060001916141561105457610e4088611430565b935060009250600091505b8351821015611017578382815181101515610e6257fe5b906020019060200201519250600083815481101515610e7d57fe5b90600052602060002090600402019050866040518082805190602001908083835b60208310610ebd5780518252601f199092019160209182019101610e9e565b6001836020036101000a038019825116818451168082178552505050505050905001915050604051809103902060001916816002016040518082805460018160011615610100020316600290048015610f4d5780601f10610f2b576101008083540402835291820191610f4d565b820191906000526020600020905b815481529060010190602001808311610f39575b5050915050604051809103902060001916148015610f745750600381015460ff8781169116145b1561100c5760018181018054604080516020600295841615610100026000190190931694909404601f8101839004830285018301909152808452908301828280156110005780601f10610fd557610100808354040283529160200191611000565b820191906000526020600020905b815481529060010190602001808311610fe357829003601f168201915b505050505094506110b4565b600190910190610e4b565b6040805190810160405280602081526020017f7468697320697320612077726f6e67206b65792074797065206f72206e616d6581525094506110b4565b606060405190810160405280602481526020017f7468697320697320612077726f6e67206163636f756e74206f72207369676e6181526020017f747572650000000000000000000000000000000000000000000000000000000081525094505b50505050949350505050565b60046020908152600091825260409182902080548351601f6002600019610100600186161502019093169290920491820184900484028101840190945280845290918301828280156111535780601f1061112857610100808354040283529160200191611153565b820191906000526020600020905b81548152906001019060200180831161113657829003601f168201915b505050505081565b600060606000806000876040518082805190602001908083835b602083106111945780518252601f199092019160209182019101611175565b6001836020036101000a0380198251168184511680821785525050505050509050019150506040518091039020600019166007600088600160a060020a0316600160a060020a0316815260200190815260200160002060405180828054600181600116156101000203166002900480156112455780601f10611223576101008083540402835291820191611245565b820191906000526020600020905b815481529060010190602001808311611231575b5050915050604051809103902060001916141561139d5761126586610a82565b935060009250600091505b835182101561139d57838281518110151561128757fe5b9060200190602002015192506005838154811015156112a257fe5b90600052602060002090600202019050866040518082805190602001908083835b602083106112e25780518252601f1990920191602091820191016112c3565b6001836020036101000a0380198251168184511680821785525050505050509050019150506040518091039020600019168160010160405180828054600181600116156101000203166002900480156113725780601f10611350576101008083540402835291820191611372565b820191906000526020600020905b81548152906001019060200180831161135e575b5050915050604051809103902060001916141561139257600194506113a2565b600190910190611270565b600094505b505050509392505050565b60076020908152600091825260409182902080548351601f6002600019610100600186161502019093169290920491820184900484028101840190945280845290918301828280156111535780601f1061112857610100808354040283529160200191611153565b600160205260009081526040902054600160a060020a031681565b6060806000806003600086600160a060020a0316600160a060020a0316815260200190815260200160002054604051908082528060200260200182016040528015611485578160200160208202803883390190505b50925060009150600090505b600054811015610b4457600081815260016020526040902054600160a060020a03868116911614156114ea578083838151811015156114cc57fe5b602090810290910101526114e782600163ffffffff6118ef16565b91505b600101611491565b600260208181526000928352604092839020805484516001821615610100026000190190911693909304601f81018390048302840183019094528383529192908301828280156111535780601f1061112857610100808354040283529160200191611153565b6040805180820190915282815260208082018390526005805460018181018084556000938452855180519496879693959294909360029091027f036b6384b5eca791c62761152d0c79bb0604c104a5fb6f4eb0703f3154bb3db001926115c19284920190611905565b5060208281015180516115da9260018501920190611905565b505050036000818152600660209081526040808320805473ffffffffffffffffffffffffffffffffffffffff191633908117909155835260078252909120865192935061162b929091870190611905565b503360009081526008602052604090205461164d90600163ffffffff6118ef16565b6008600033600160a060020a0316600160a060020a03168152602001908152602001600020819055507fc7faebf256735d6ef8efaab10d3aac8eb9f536ea7260dbc5ec514a6d2e82c7c0818585604051808481526020018060200180602001838103835285818151815260200191508051906020019080838360005b838110156116e15781810151838201526020016116c9565b50505050905090810190601f16801561170e5780820380516001836020036101000a031916815260200191505b50838103825284518152845160209182019186019080838360005b83811015611741578181015183820152602001611729565b50505050905090810190601f16801561176e5780820380516001836020036101000a031916815260200191505b509550505050505060405180910390a15060019392505050565b600580548290811061179657fe5b60009182526020918290206002918202018054604080516001831615610100026000190190921693909304601f81018590048502820185019093528281529093509183919083018282801561182c5780601f106118015761010080835404028352916020019161182c565b820191906000526020600020905b81548152906001019060200180831161180f57829003601f168201915b505050505090806001018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156118ca5780601f1061189f576101008083540402835291602001916118ca565b820191906000526020600020905b8154815290600101906020018083116118ad57829003601f168201915b5050505050905082565b600660205260009081526040902054600160a060020a031681565b6000828201838110156118fe57fe5b9392505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061194657805160ff1916838001178555611973565b82800160010185558215611973579182015b82811115611973578251825591602001919060010190611958565b5061197f929150611983565b5090565b61199d91905b8082111561197f5760008155600101611989565b905600a165627a7a72305820e22290aa0ec1768f283aa0063909a5f58fa3ee6b4fe7994122a01a3d89f509410029";

    public static final String FUNC_SAVEKEY = "saveKey";

    public static final String FUNC_GETLOGIDBYOWNER = "getLogIdByOwner";

    public static final String FUNC_KEYS = "keys";

    public static final String FUNC_GETKEY = "getKey";

    public static final String FUNC_IDTONAME = "idToName";

    public static final String FUNC_COMPARELOGS = "compareLogs";

    public static final String FUNC_SIGNTOMESSAGE = "signToMessage";

    public static final String FUNC_KEYTOOWNER = "keyToOwner";

    public static final String FUNC_GETKEYIDBYOWNER = "getKeyIdByOwner";

    public static final String FUNC_SIGNTOKEY = "signToKey";

    public static final String FUNC_SAVELOG = "saveLog";

    public static final String FUNC_LOGS = "logs";

    public static final String FUNC_LOGTOOWNER = "logToOwner";

    public static final Event NEWKEY_EVENT = new Event("NewKey", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event NEWLOG_EVENT = new Event("NewLog", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    @Deprecated
    protected MyContractsTest(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected MyContractsTest(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected MyContractsTest(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected MyContractsTest(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<TransactionReceipt> saveKey(String _signature, String _keyChain, String _keyName, BigInteger _keyType, BigInteger weiValue) {
        final Function function = new Function(
                FUNC_SAVEKEY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_signature), 
                new org.web3j.abi.datatypes.Utf8String(_keyChain), 
                new org.web3j.abi.datatypes.Utf8String(_keyName), 
                new org.web3j.abi.datatypes.generated.Uint8(_keyType)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<List> getLogIdByOwner(String _owner) {
        final Function function = new Function(FUNC_GETLOGIDBYOWNER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_owner)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint256>>() {}));
        return new RemoteCall<List>(
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteCall<Tuple4<String, String, String, BigInteger>> keys(BigInteger param0) {
        final Function function = new Function(FUNC_KEYS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint8>() {}));
        return new RemoteCall<Tuple4<String, String, String, BigInteger>>(
                new Callable<Tuple4<String, String, String, BigInteger>>() {
                    @Override
                    public Tuple4<String, String, String, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple4<String, String, String, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue());
                    }
                });
    }

    public RemoteCall<String> getKey(String _signature, String _owner, String _keyName, BigInteger _keyType) {
        final Function function = new Function(FUNC_GETKEY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_signature), 
                new org.web3j.abi.datatypes.Address(_owner), 
                new org.web3j.abi.datatypes.Utf8String(_keyName), 
                new org.web3j.abi.datatypes.generated.Uint8(_keyType)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> idToName(BigInteger param0) {
        final Function function = new Function(FUNC_IDTONAME, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<Boolean> compareLogs(String _signature, String _payload, String _owner) {
        final Function function = new Function(FUNC_COMPARELOGS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_signature), 
                new org.web3j.abi.datatypes.Utf8String(_payload), 
                new org.web3j.abi.datatypes.Address(_owner)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<String> signToMessage(String param0) {
        final Function function = new Function(FUNC_SIGNTOMESSAGE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> keyToOwner(BigInteger param0) {
        final Function function = new Function(FUNC_KEYTOOWNER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<List> getKeyIdByOwner(String _owner) {
        final Function function = new Function(FUNC_GETKEYIDBYOWNER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_owner)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint256>>() {}));
        return new RemoteCall<List>(
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteCall<String> signToKey(String param0) {
        final Function function = new Function(FUNC_SIGNTOKEY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> saveLog(String _signature, String _encryptedMessage, BigInteger weiValue) {
        final Function function = new Function(
                FUNC_SAVELOG, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_signature), 
                new org.web3j.abi.datatypes.Utf8String(_encryptedMessage)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<Tuple2<String, String>> logs(BigInteger param0) {
        final Function function = new Function(FUNC_LOGS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        return new RemoteCall<Tuple2<String, String>>(
                new Callable<Tuple2<String, String>>() {
                    @Override
                    public Tuple2<String, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<String, String>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue());
                    }
                });
    }

    public RemoteCall<String> logToOwner(BigInteger param0) {
        final Function function = new Function(FUNC_LOGTOOWNER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public List<NewKeyEventResponse> getNewKeyEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(NEWKEY_EVENT, transactionReceipt);
        ArrayList<NewKeyEventResponse> responses = new ArrayList<NewKeyEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            NewKeyEventResponse typedResponse = new NewKeyEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.keyId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.keyChain = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.signature = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.keyName = (String) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NewKeyEventResponse> newKeyEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, NewKeyEventResponse>() {
            @Override
            public NewKeyEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(NEWKEY_EVENT, log);
                NewKeyEventResponse typedResponse = new NewKeyEventResponse();
                typedResponse.log = log;
                typedResponse.keyId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.keyChain = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.signature = (String) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.keyName = (String) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NewKeyEventResponse> newKeyEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NEWKEY_EVENT));
        return newKeyEventFlowable(filter);
    }

    public List<NewLogEventResponse> getNewLogEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(NEWLOG_EVENT, transactionReceipt);
        ArrayList<NewLogEventResponse> responses = new ArrayList<NewLogEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            NewLogEventResponse typedResponse = new NewLogEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.logId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.signature = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.hashOfMessage = (String) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NewLogEventResponse> newLogEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, NewLogEventResponse>() {
            @Override
            public NewLogEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(NEWLOG_EVENT, log);
                NewLogEventResponse typedResponse = new NewLogEventResponse();
                typedResponse.log = log;
                typedResponse.logId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.signature = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.hashOfMessage = (String) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NewLogEventResponse> newLogEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NEWLOG_EVENT));
        return newLogEventFlowable(filter);
    }

    @Deprecated
    public static MyContractsTest load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new MyContractsTest(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static MyContractsTest load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new MyContractsTest(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static MyContractsTest load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new MyContractsTest(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static MyContractsTest load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new MyContractsTest(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<MyContractsTest> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(MyContractsTest.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<MyContractsTest> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(MyContractsTest.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<MyContractsTest> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(MyContractsTest.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<MyContractsTest> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(MyContractsTest.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class NewKeyEventResponse {
        public Log log;

        public BigInteger keyId;

        public String keyChain;

        public String signature;

        public String keyName;
    }

    public static class NewLogEventResponse {
        public Log log;

        public BigInteger logId;

        public String signature;

        public String hashOfMessage;
    }
}
