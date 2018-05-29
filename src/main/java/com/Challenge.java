package main.java.com;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Challenge {

	public static final String fileHash = "HASH";
	public static final String fileId = "ID";
	public static final String hashUrlKey = "GET_HASH_LOOKUP_URL";
	public static final String idUrlKey = "GET_ID_LOOKUP_URL";
	public static final String apiKey = "API_KEY";
	
	private Util myUtil = new Util();
	
	public List<Result> getResultList(String jsonString){
		List<Result> resultList = null;
		//get "scan_result" object and set as new node to extra info from nested structure
		JsonNode scanResults = this.getJsonNode(jsonString, "scan_results");
		
		//get "scan_detail" object
		JsonNode scanDetail = this.getJsonNode(scanResults, "scan_details");
		
		//parse the scan_detail object into a list of individual result detail
		resultList = new ArrayList<Result>();
		Iterator<Entry<String, JsonNode>> itr = scanDetail.fields();
		while (itr.hasNext()) {
			Map.Entry<String, JsonNode> curEle = (Map.Entry<String, JsonNode>) itr.next();
			String engine = curEle.getKey();
			JsonNode details = curEle.getValue();
			String threats = details.findValue("threat_found").asText();
			String scanResult = details.findValue("scan_result_i").asText();
			String defTime = details.findValue("def_time").asText();
			resultList.add(new Result(engine, threats, scanResult, defTime));
		}
		return resultList;
	}
	
	
	
	public JsonNode getJsonNode(String jsonStr, String key) {
		JsonNode node = null;
		try {
			node = new ObjectMapper().readTree(jsonStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return getJsonNode(node, key);
	}
	
	private JsonNode getJsonNode(JsonNode curNode, String key) {
		return curNode.get(key);
	}
	
	public boolean containsApiError(String responseString) {
		Map<String, Object> result = myUtil.getResultMap(responseString);
		
		if(result.containsKey("error")) {	
			//handle api error message
		}
		return false;
	}

	public String getOverallStatus(String resultJsonString) {
		JsonNode scanResult = this.getJsonNode(resultJsonString, "scan_results");
		return scanResult.get("scan_all_result_a").toString();
	}
}
