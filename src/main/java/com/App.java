package main.java.com;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

public class App {
	
	public static void main(String[] args) throws InterruptedException {
		
		ApiController apiController = new ApiController();
		Challenge myChallenge = new Challenge();
		Util myUtil = new Util();
		
		//check for user supply parameter
		if(args.length != 1) {
			System.err.println("Missing parameters. Please make sure the file name is supplied");
			System.exit(0);
		}
		
		//Retrieve hashtype to use from application.properties
		Hash myHash = null;
		switch(Property.getProperties().getProperty("HASH")) {
			case "MD5":
				myHash = Hash.MD5;
				break;
			case "SHA1":
				myHash = Hash.SHA1;
				break;
			case "SHA256":
				myHash = Hash.SHA256;
				break;
			case "SHA512":
				myHash = Hash.SHA512;
				break;
			default:
				break;
		}
		if(myHash == null) {
			System.err.println("Invalid hash property value in the file.");
			return;
		}
		
		
		//Locate file and make sure its not a bad input
		File file = new File(args[0]);
		if(!file.exists()) {
			System.err.println("The given file does not exist. Please make sure path/name is correct.");
			return;
		}
		
		String fileName = file.getName();
		String fileHashValue = myUtil.toHex(myHash.getChecksum(file));
		
		String resultJsonString = apiController.resultLookup(Challenge.fileHash,fileHashValue);
		Map<String, Object> lookupResult = myUtil.getResultMap(resultJsonString);

		
		//hash lookup could either return hash# not found, error, or scan result as the response
		if(lookupResult.get(fileHashValue)!=null) {
			
			//upload the file
			resultJsonString = apiController.uploadFile(file);
			Map<String, Object> uploadResult = myUtil.getResultMap(resultJsonString);
			String fileKey = uploadResult.get("data_id").toString();
			
			//continue check for the result
			boolean scanCompleted = false;
			while(!scanCompleted) {
				Thread.sleep(5000);
				resultJsonString = apiController.resultLookup(Challenge.fileId, fileKey);
				JsonNode scanResult = myUtil.getJsonNode(resultJsonString, "scan_results");
				scanCompleted = scanResult.get("progress_percentage").asInt() == 100;
			}
		}
		
		
		String overallStatus = myChallenge.getOverallStatus(resultJsonString);
		
		StringBuilder header = new StringBuilder();
		header.append("filename: ");
		header.append(fileName);
		header.append("\n");
		header.append("overall_status: ");
		header.append(overallStatus);
		header.append("\n");
		
		List<Result> resultList = myChallenge.getResultList(resultJsonString);
		myUtil.printResult(header.toString(), resultList);	
		
	}
}
