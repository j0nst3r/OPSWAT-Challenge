package main.java.com;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Util {

	
	
	/**
	 * Convers the given byte array into hex string
	 * @param bytes byte array
	 * @return String representation of the byte array
	 */
	public String toHex(byte[] bytes) {
        return DatatypeConverter.printHexBinary(bytes);
    }
 

    
	
	
    /**
     * Parse the string into a map
     * @param resultJsonString String
     * @return Map<String, Object> A key/value pair of the given JSON string
     */
    public Map<String, Object> getResultMap(String resultJsonString){
		Map<String, Object> result = null;
		if (!resultJsonString.isEmpty()) {
		    ObjectMapper mapper = new ObjectMapper();
		    try {
		    	result = mapper.readValue(resultJsonString, Map.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
    
	
    
    
    
	/**
	 * Util used for extracting nested JSON
	 * @param jsonStr - String
	 * @param key - String
	 * @return JsonNode - the nested JSON associated to the key 
	 */
	public JsonNode getJsonNode(String jsonStr, String key) {
		JsonNode node = null;
		try {
			node = new ObjectMapper().readTree(jsonStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return getJsonNode(node, key);
	}

	
	
	
	/**
	 * Util used for extracting nested JSON
	 * @param curNode - JsonNode
	 * @param key - String
	 * @return JsonNode - the nested JSON associated to the key 
	 */
	public JsonNode getJsonNode(JsonNode curNode, String key) {
		return curNode.get(key);
	}
    
    
	
	
	
	
	/**
	 * Checks for error code within the response body
	 * @param responseString
	 * @return boolean - true-> has error
	 * 					 false-> no error
	 */
	public boolean containsApiError(String responseString) {
		Map<String, Object> result = getResultMap(responseString);
		
		if(result.containsKey("error")) {	
			//handle api error message
			System.err.println("API call has encountered an error");
			System.err.println(result.get("error"));
			System.exit(-1);
		}
		return false;
	}
	
	
	
	
	
	/**
	 * 
	 * @param header String - special formated header
	 * @param scanResult List<Result>
	 */
    public void printResult(String header, List<Result> scanResult) {
    	System.out.println(header);
		for(Result res : scanResult) {
			System.out.println(res.toString());
		}
    }
    
}
