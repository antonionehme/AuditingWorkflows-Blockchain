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
public class KeyFactory extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b50610ffb806100206000396000f3006080604052600436106100815763ffffffff7c0100000000000000000000000000000000000000000000000000000000600035041662e87da181146100865780630cb6aaf1146101675780633423d1e5146102cc578063364410b3146103ec57806387e1ba6114610404578063a23c091814610438578063c07288e9146104a9575b600080fd5b6040805160206004803580820135601f810184900484028501840190955284845261015394369492936024939284019190819084018382808284375050604080516020601f89358b018035918201839004830284018301909452808352979a99988101979196509182019450925082915084018382808284375050604080516020601f89358b018035918201839004830284018301909452808352979a9998810197919650918201945092508291508401838280828437509497505050923560ff1693506104ca92505050565b604080519115158252519081900360200190f35b34801561017357600080fd5b5061017f6004356107c4565b6040805160ff83166060820152608080825286519082015285519091829160208084019284019160a08501918a019080838360005b838110156101cc5781810151838201526020016101b4565b50505050905090810190601f1680156101f95780820380516001836020036101000a031916815260200191505b50848103835287518152875160209182019189019080838360005b8381101561022c578181015183820152602001610214565b50505050905090810190601f1680156102595780820380516001836020036101000a031916815260200191505b50848103825286518152865160209182019188019080838360005b8381101561028c578181015183820152602001610274565b50505050905090810190601f1680156102b95780820380516001836020036101000a031916815260200191505b5097505050505050505060405180910390f35b3480156102d857600080fd5b506040805160206004803580820135601f810184900484028501840190955284845261037794369492936024939284019190819084018382808284375050604080516020601f818a01358b0180359182018390048302840183018552818452989b600160a060020a038b35169b909a9099940197509195509182019350915081908401838280828437509497505050923560ff1693506109ae92505050565b6040805160208082528351818301528351919283929083019185019080838360005b838110156103b1578181015183820152602001610399565b50505050905090810190601f1680156103de5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b3480156103f857600080fd5b50610377600435610d37565b34801561041057600080fd5b5061041c600435610dd2565b60408051600160a060020a039092168252519081900360200190f35b34801561044457600080fd5b50610459600160a060020a0360043516610ded565b60408051602080825283518183015283519192839290830191858101910280838360005b8381101561049557818101518382015260200161047d565b505050509050019250505060405180910390f35b3480156104b557600080fd5b50610377600160a060020a0360043516610eb8565b60408051608081018252858152602080820186905291810184905260ff831660608201526000805460018181018084558380528451805194968796939592946004027f290decd9548b62a8d60345a988386fc84ba6bc95484008f6362f93160ef3e563019261053c9284920190610f34565b5060208281015180516105559260018501920190610f34565b5060408201518051610571916002840191602090910190610f34565b50606091909101516003909101805460ff191660ff90921691909117905503600081815260016020908152604080832080543373ffffffffffffffffffffffffffffffffffffffff19909116811790915583526002825290912088519293506105de929091890190610f34565b50600081815260046020908152604090912085516105fe92870190610f34565b503360009081526003602052604090205461062090600163ffffffff610f1e16565b6003600033600160a060020a0316600160a060020a03168152602001908152602001600020819055507fbf2613ed033e1ab990683f764ec4e76da473b83a70ed3a3abc9c0a76e411623a8187878760405180858152602001806020018060200180602001848103845287818151815260200191508051906020019080838360005b838110156106b95781810151838201526020016106a1565b50505050905090810190601f1680156106e65780820380516001836020036101000a031916815260200191505b50848103835286518152865160209182019188019080838360005b83811015610719578181015183820152602001610701565b50505050905090810190601f1680156107465780820380516001836020036101000a031916815260200191505b50848103825285518152855160209182019187019080838360005b83811015610779578181015183820152602001610761565b50505050905090810190601f1680156107a65780820380516001836020036101000a031916815260200191505b5097505050505050505060405180910390a150600195945050505050565b60008054829081106107d257fe5b60009182526020918290206004919091020180546040805160026001841615610100026000190190931692909204601f81018590048502830185019091528082529193509183919083018282801561086b5780601f106108405761010080835404028352916020019161086b565b820191906000526020600020905b81548152906001019060200180831161084e57829003601f168201915b505050505090806001018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156109095780601f106108de57610100808354040283529160200191610909565b820191906000526020600020905b8154815290600101906020018083116108ec57829003601f168201915b50505060028085018054604080516020601f600019610100600187161502019094169590950492830185900485028101850190915281815295969594509092509083018282801561099b5780601f106109705761010080835404028352916020019161099b565b820191906000526020600020905b81548152906001019060200180831161097e57829003601f168201915b5050506003909301549192505060ff1684565b6060806000806000886040518082805190602001908083835b602083106109e65780518252601f1990920191602091820191016109c7565b6001836020036101000a038019825116818451168082178552505050505050905001915050604051809103902060001916600260008a600160a060020a0316600160a060020a031681526020019081526020016000206040518082805460018160011615610100020316600290048015610a975780601f10610a75576101008083540402835291820191610a97565b820191906000526020600020905b815481529060010190602001808311610a83575b50509150506040518091039020600019161415610ccb57610ab788610ded565b935060009250600091505b8351821015610c8e578382815181101515610ad957fe5b906020019060200201519250600083815481101515610af457fe5b90600052602060002090600402019050866040518082805190602001908083835b60208310610b345780518252601f199092019160209182019101610b15565b6001836020036101000a038019825116818451168082178552505050505050905001915050604051809103902060001916816002016040518082805460018160011615610100020316600290048015610bc45780601f10610ba2576101008083540402835291820191610bc4565b820191906000526020600020905b815481529060010190602001808311610bb0575b5050915050604051809103902060001916148015610beb5750600381015460ff8781169116145b15610c835760018181018054604080516020600295841615610100026000190190931694909404601f810183900483028501830190915280845290830182828015610c775780601f10610c4c57610100808354040283529160200191610c77565b820191906000526020600020905b815481529060010190602001808311610c5a57829003601f168201915b50505050509450610d2b565b600190910190610ac2565b6040805190810160405280602081526020017f7468697320697320612077726f6e67206b65792074797065206f72206e616d658152509450610d2b565b606060405190810160405280602481526020017f7468697320697320612077726f6e67206163636f756e74206f72207369676e6181526020017f747572650000000000000000000000000000000000000000000000000000000081525094505b50505050949350505050565b60046020908152600091825260409182902080548351601f600260001961010060018616150201909316929092049182018490048402810184019094528084529091830182828015610dca5780601f10610d9f57610100808354040283529160200191610dca565b820191906000526020600020905b815481529060010190602001808311610dad57829003601f168201915b505050505081565b600160205260009081526040902054600160a060020a031681565b6060806000806003600086600160a060020a0316600160a060020a0316815260200190815260200160002054604051908082528060200260200182016040528015610e42578160200160208202803883390190505b50925060009150600090505b600054811015610eaf57600081815260016020526040902054600160a060020a0386811691161415610ea757808383815181101515610e8957fe5b60209081029091010152610ea482600163ffffffff610f1e16565b91505b600101610e4e565b50909392505050565b600260208181526000928352604092839020805484516001821615610100026000190190911693909304601f8101839004830284018301909452838352919290830182828015610dca5780601f10610d9f57610100808354040283529160200191610dca565b600082820183811015610f2d57fe5b9392505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610f7557805160ff1916838001178555610fa2565b82800160010185558215610fa2579182015b82811115610fa2578251825591602001919060010190610f87565b50610fae929150610fb2565b5090565b610fcc91905b80821115610fae5760008155600101610fb8565b905600a165627a7a723058209f9164970e9897f312ab98042ddf86f150d07c583344674457ea2b5739af13e70029";

    public static final String FUNC_SAVEKEY = "saveKey";

    public static final String FUNC_KEYS = "keys";

    public static final String FUNC_GETKEY = "getKey";

    public static final String FUNC_IDTONAME = "idToName";

    public static final String FUNC_KEYTOOWNER = "keyToOwner";

    public static final String FUNC_GETKEYIDBYOWNER = "getKeyIdByOwner";

    public static final String FUNC_SIGNTOKEY = "signToKey";

    public static final Event NEWKEY_EVENT = new Event("NewKey", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    @Deprecated
    protected KeyFactory(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected KeyFactory(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected KeyFactory(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected KeyFactory(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
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

    @Deprecated
    public static KeyFactory load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new KeyFactory(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static KeyFactory load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new KeyFactory(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static KeyFactory load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new KeyFactory(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static KeyFactory load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new KeyFactory(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<KeyFactory> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(KeyFactory.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<KeyFactory> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(KeyFactory.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<KeyFactory> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(KeyFactory.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<KeyFactory> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(KeyFactory.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class NewKeyEventResponse {
        public Log log;

        public BigInteger keyId;

        public String keyChain;

        public String signature;

        public String keyName;
    }
}
