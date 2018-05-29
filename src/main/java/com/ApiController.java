package main.java.com;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

public class ApiController {
	
	private HttpClient httpclient;
	private Properties props;
	
	public ApiController() {
		this.httpclient = new DefaultHttpClient();
		this.props = Property.getProperties();
	}
		
	
	/**
	 * http GET request to retrieve scan result by using file hash or assigned id
	 * @param type Challenge.fileHash | Challenge.fileId
	 * @param value the unique lookup key
	 * @return Response body of the GET request as a JSON String
	 */
	public String resultLookup(String type, String value) {
    	
		StringBuffer result = new StringBuffer();
		
		//determine the type of look up and build url accordingly
		String url = type==Challenge.fileHash ? Challenge.hashUrlKey : Challenge.idUrlKey;
		HttpGet request = new HttpGet(props.getProperty(url).concat(value));
		
		try {
			request.addHeader("apikey",props.getProperty(Challenge.apiKey));
			HttpResponse response = httpclient.execute(request);

			BufferedReader rd = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent()));
				
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result.toString();
    }
	
	/**
	 * http POST request to upload the file
	 * @param file Name of the file to be uploaded
	 * @return Response body of the POST request as a JSON String
	 */
	public String uploadFile(File file) {
    	
		StringBuffer result = new StringBuffer();
    	HttpPost request = new HttpPost(props.getProperty("FILE_UPLOAD_URL"));

    	HttpResponse response = null;
    	try {

			request.addHeader("apikey",props.getProperty(Challenge.apiKey));
        	FileBody uploadFilePart = new FileBody(file);
        	MultipartEntity reqEntity = new MultipartEntity();
        	reqEntity.addPart("upload-file", uploadFilePart);
        	request.setEntity(reqEntity);
			response = httpclient.execute(request);
			 
			BufferedReader rd = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent()));
				
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			 
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	return result.toString();
    }
}
