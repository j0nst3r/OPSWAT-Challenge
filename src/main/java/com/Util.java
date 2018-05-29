package main.java.com;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Util {

	public String toHex(byte[] bytes) {
        return DatatypeConverter.printHexBinary(bytes);
    }
 
    public void printResult(String header, List<Result> scanResult) {
    	System.out.println(header);
		for(Result res : scanResult) {
			System.out.println(res.toString());
		}
    }
    
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
    
}
