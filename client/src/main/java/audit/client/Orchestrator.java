package audit.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class Orchestrator {
	static int row=0;
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		//switchOptions();
		ExecuteTopology();
	}
	
	 public static void ExecuteTopology() throws Exception {
		 List<List<String>> records = new ArrayList<>();
		 String Readfrom="C:\\Users\\ID126219\\OneDrive - Birmingham City University\\Coding\\AuditProject-WithBlockchain\\AuditProject\\topologies\\20 Nodes\\test3.csv";
		 //"C:\\Users\\ID126219\\OneDrive - Birmingham City University\\Coding\\AuditProject-WithBlockchain\\AuditProject\\topologies\\20 Nodes\\BAn20m10_2-Topology.csv";
		 System.out.println(Readfrom);
		 try (BufferedReader br = new BufferedReader(new FileReader(Readfrom))) {
		  String line;
		     while ((line = br.readLine()) != null) {
		     	String first="false";
		         String[] values = line.split(",");
		         records.add(Arrays.asList(values));
		         if(row>=1) {if (row==1) first="true";
		         System.out.println("Sender: "+ TranslatetoPort(values[1])+" Receiver: "+  TranslatetoPort(values[2]));
		         SendRequests(TranslatetoPort(values[1]), TranslatetoPort(values[2]), first);
		         }row++;
		     }
		 } 
		 
	 }
	
	/* public static void ExecuteTopology_old() throws Exception {
		 Scanner scanner = new Scanner(new File("C:\\Users\\ID126219\\Dropbox\\PhD\\Readings\\BCU\\Papers and Drafts\\Authorization and Audit Framework for E-Government Workflows\\Evaluation\\Brite\\Output\\552.csv"));
	   //  Seems like scanner goes through the entire file here.   
		 scanner.useDelimiter(",");
		 while (scanner.hasNextLine()) {
			 String []record=new String[3]; int column=0;
			 //row++;
			 if(row>=1) {while(scanner.hasNext()){String value=scanner.next();//THis is reading the entire CSV, starting from the beginning of the file.
		 System.out.print(value+"|");
	            record[column]=value;
	            column++;
	            
	            //Send reques
	            String first;
	            if(row==2) {first="true";}
	            else {first="false";}
	            
	            SendRequests(TranslatetoPort(record[1]), TranslatetoPort(record[2]), first);
	        }
		}
		row++; }
	        scanner.close();
	    }*/
	 
	 
	 public static void SendRequests(String From, String p, String f) throws Exception {//Port and First
		 RestTemplate restTemplate = new RestTemplate();

     	 HttpHeaders headers = new HttpHeaders();
     	 headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

     	 MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
     	 map.add("port", p);
     	 map.add("first", f);
     	 

     	 HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

     	 ResponseEntity<String> response = restTemplate.postForEntity( "http://localhost:"+From+"/participant/call", request , String.class );	 
     	 //ResponseEntity<String> response = restTemplate.postForEntity( "http://localhost:8103/participant/call", request , String.class );	 
	 }
	 
	 
	 public static String TranslatetoPort(String str) { //finds the corresponding port for participants in the brite topology.
		 int partInt=Integer.parseInt(str);
		 int translationInt=partInt+8101;
		 return ""+translationInt;
	 }
	 
	 public static void switchOptions() throws Exception { Scanner scan=new Scanner(System.in);
	    System.out.println("0 to Add Address, 1 to VerifyServer, 2 to see last reported record on the audit server, 3 to Publish a message, 4 Send a message to another recipient, X to exit.");
	    	String option=scan.nextLine();
	    	while(option!="X") {
	        switch(option) { case "test2": {
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
}
