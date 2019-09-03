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
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;
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
public class LogFactory extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b50610c15806100206000396000f3006080604052600436106100775763ffffffff7c0100000000000000000000000000000000000000000000000000000000600035041663014a5ef5811461007c57806361f1257b146100ed578063634c6109146101a3578063dda25d0114610239578063e79899bd146102c3578063fd4dc7e5146103b9575b600080fd5b34801561008857600080fd5b5061009d600160a060020a03600435166103ed565b60408051602080825283518183015283519192839290830191858101910280838360005b838110156100d95781810151838201526020016100c1565b505050509050019250505060405180910390f35b3480156100f957600080fd5b506040805160206004803580820135601f810184900484028501840190955284845261018f94369492936024939284019190819084018382808284375050604080516020601f89358b018035918201839004830284018301909452808352979a99988101979196509182019450925082915084018382808284375094975050509235600160a060020a031693506104b892505050565b604080519115158252519081900360200190f35b3480156101af57600080fd5b506101c4600160a060020a036004351661070a565b6040805160208082528351818301528351919283929083019185019080838360005b838110156101fe5781810151838201526020016101e6565b50505050905090810190601f16801561022b5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6040805160206004803580820135601f810184900484028501840190955284845261018f94369492936024939284019190819084018382808284375050604080516020601f89358b018035918201839004830284018301909452808352979a9998810197919650918201945092508291508401838280828437509497506107a39650505050505050565b3480156102cf57600080fd5b506102db6004356109d1565b604051808060200180602001838103835285818151815260200191508051906020019080838360005b8381101561031c578181015183820152602001610304565b50505050905090810190601f1680156103495780820380516001836020036101000a031916815260200191505b50838103825284518152845160209182019186019080838360005b8381101561037c578181015183820152602001610364565b50505050905090810190601f1680156103a95780820380516001836020036101000a031916815260200191505b5094505050505060405180910390f35b3480156103c557600080fd5b506103d1600435610b1d565b60408051600160a060020a039092168252519081900360200190f35b6060806000806003600086600160a060020a0316600160a060020a0316815260200190815260200160002054604051908082528060200260200182016040528015610442578160200160208202803883390190505b50925060009150600090505b6000548110156104af57600081815260016020526040902054600160a060020a03868116911614156104a75780838381518110151561048957fe5b602090810290910101526104a482600163ffffffff610b3816565b91505b60010161044e565b50909392505050565b600060606000806000876040518082805190602001908083835b602083106104f15780518252601f1990920191602091820191016104d2565b6001836020036101000a0380198251168184511680821785525050505050509050019150506040518091039020600019166002600088600160a060020a0316600160a060020a0316815260200190815260200160002060405180828054600181600116156101000203166002900480156105a25780601f106105805761010080835404028352918201916105a2565b820191906000526020600020905b81548152906001019060200180831161058e575b505091505060405180910390206000191614156106fa576105c2866103ed565b935060009250600091505b83518210156106fa5783828151811015156105e457fe5b9060200190602002015192506000838154811015156105ff57fe5b90600052602060002090600202019050866040518082805190602001908083835b6020831061063f5780518252601f199092019160209182019101610620565b6001836020036101000a0380198251168184511680821785525050505050509050019150506040518091039020600019168160010160405180828054600181600116156101000203166002900480156106cf5780601f106106ad5761010080835404028352918201916106cf565b820191906000526020600020905b8154815290600101906020018083116106bb575b505091505060405180910390206000191614156106ef57600194506106ff565b6001909101906105cd565b600094505b505050509392505050565b600260208181526000928352604092839020805484516001821615610100026000190190911693909304601f810183900483028401830190945283835291929083018282801561079b5780601f106107705761010080835404028352916020019161079b565b820191906000526020600020905b81548152906001019060200180831161077e57829003601f168201915b505050505081565b604080518082019091528281526020808201839052600080546001818101808455838052855180519496879693959294909360029091027f290decd9548b62a8d60345a988386fc84ba6bc95484008f6362f93160ef3e563019261080a9284920190610b4e565b5060208281015180516108239260018501920190610b4e565b505050036000818152600160209081526040808320805473ffffffffffffffffffffffffffffffffffffffff1916339081179091558352600282529091208651929350610874929091870190610b4e565b503360009081526003602052604090205461089690600163ffffffff610b3816565b6003600033600160a060020a0316600160a060020a03168152602001908152602001600020819055507fc7faebf256735d6ef8efaab10d3aac8eb9f536ea7260dbc5ec514a6d2e82c7c0818585604051808481526020018060200180602001838103835285818151815260200191508051906020019080838360005b8381101561092a578181015183820152602001610912565b50505050905090810190601f1680156109575780820380516001836020036101000a031916815260200191505b50838103825284518152845160209182019186019080838360005b8381101561098a578181015183820152602001610972565b50505050905090810190601f1680156109b75780820380516001836020036101000a031916815260200191505b509550505050505060405180910390a15060019392505050565b60008054829081106109df57fe5b60009182526020918290206002918202018054604080516001831615610100026000190190921693909304601f810185900485028201850190935282815290935091839190830182828015610a755780601f10610a4a57610100808354040283529160200191610a75565b820191906000526020600020905b815481529060010190602001808311610a5857829003601f168201915b505050505090806001018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610b135780601f10610ae857610100808354040283529160200191610b13565b820191906000526020600020905b815481529060010190602001808311610af657829003601f168201915b5050505050905082565b600160205260009081526040902054600160a060020a031681565b600082820183811015610b4757fe5b9392505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610b8f57805160ff1916838001178555610bbc565b82800160010185558215610bbc579182015b82811115610bbc578251825591602001919060010190610ba1565b50610bc8929150610bcc565b5090565b610be691905b80821115610bc85760008155600101610bd2565b905600a165627a7a7230582050d321577b9e8bc39b6897b1bc6413621d0ae4d755d61224b23128b8f8d02d9d0029";

    public static final String FUNC_GETLOGIDBYOWNER = "getLogIdByOwner";

    public static final String FUNC_COMPARELOGS = "compareLogs";

    public static final String FUNC_SIGNTOMESSAGE = "signToMessage";

    public static final String FUNC_SAVELOG = "saveLog";

    public static final String FUNC_LOGS = "logs";

    public static final String FUNC_LOGTOOWNER = "logToOwner";

    public static final Event NEWLOG_EVENT = new Event("NewLog", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    @Deprecated
    protected LogFactory(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected LogFactory(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected LogFactory(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected LogFactory(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
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
    public static LogFactory load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new LogFactory(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static LogFactory load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new LogFactory(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static LogFactory load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new LogFactory(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static LogFactory load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new LogFactory(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<LogFactory> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(LogFactory.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<LogFactory> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(LogFactory.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<LogFactory> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(LogFactory.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<LogFactory> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(LogFactory.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class NewLogEventResponse {
        public Log log;

        public BigInteger logId;

        public String signature;

        public String hashOfMessage;
    }
}
