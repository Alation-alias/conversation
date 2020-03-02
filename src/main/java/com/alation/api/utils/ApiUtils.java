package com.alation.api.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.alation.api.models.AlationConfig;
import com.google.common.net.HttpHeaders;

public class ApiUtils {
	
	private static Logger logger = Logger.getLogger(ApiUtils.class);

	private AlationConfig alationConfig = null;
	
	
	
	public ApiUtils(AlationConfig conf) {
		this.setAlationConfig(conf);
	}

	public static HttpResponse doPOST(List<String> entity, String partialURLString) throws Exception {
		StringBuffer buffer = new StringBuffer();
		for (Iterator<String> iterator = entity.iterator(); iterator.hasNext();) {
			buffer.append(iterator.next());
			buffer.append("\n"); // Add a new line indicating new record (Alation needs it)
		}

		// Sanity
		logger.info(buffer);

		// Now push it!
		return doPOST(new StringEntity(buffer.toString()), partialURLString);
	}
	
	public static HttpResponse doPOST(StringEntity entity, String partialURLString) throws Exception {
		logger.info(
				"STARTS : entity.getContentLength=" + entity.getContentLength() + ", partialURL=" + partialURLString);

		HttpResponse response = null;
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost(PropHelper.getHelper().getBaseURL() + partialURLString);
		httpPost.setHeader("token", PropHelper.getHelper().getToken());
		httpPost.setEntity(entity);
		httpPost.setHeader("Content-Type", "application/json");
		response = httpClient.execute(httpPost);

		logger.info("ENDS : entity.getContentLength=" + entity.getContentLength() + ", response=" + response);

		return response;
	}
	
	public static HttpResponse doPOST(String baseUrl, StringEntity entity, String partialURLString) throws Exception {
		HttpResponse response = null;
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost(baseUrl + partialURLString);
		httpPost.setHeader("token", PropHelper.getHelper().getToken());
		if (entity != null) {
			httpPost.setEntity(entity);
		}
		httpPost.setHeader("Content-Type", "application/json");
		response = httpClient.execute(httpPost);

		logger.info("ENDS : entity.getContentLength=" + entity + ", response=" + response);

		return response;
	}
	
	
	
	/**
	 * This method is specific to post files to server using curl in a specific
	 * format that the Java runtime curl accepts
	 *
	 * @param fileEntities     list of file names
	 * @param partialURLString url to append to base
	 * @throws Exception
	 */
	public static void doPOSTFiles(List<String> fileEntities, String partialURLString, boolean removeOld)
			throws Exception {
		logger.info("STARTS : filesLength=" + fileEntities.size() + ", partialURL=" + partialURLString);

		// Use Multipart entity to send in all the files as a single request
		MultipartEntityBuilder entity = MultipartEntityBuilder.create();
		HttpResponse response = null;

		// Loop through all the files and Build a multipart request
		for (int index = 0; index < fileEntities.size(); index++) {
			logger.info("Adding file to request object: " + fileEntities.get(index));
			entity.addPart("file", new FileBody(new File(fileEntities.get(index))));
		}

		HttpEntity httpEntity = entity.build();

		try {

			HttpClient httpClient = HttpClientBuilder.create().build();
			String postString = PropHelper.getHelper().getBaseURL() + partialURLString + "?remove_old=" + removeOld;
			logger.info("Posting to: " + postString);
			logger.info("Token: " + PropHelper.getHelper().getToken());
			HttpPost httpPost = new HttpPost(postString);
			httpPost.setHeader("token", PropHelper.getHelper().getToken());
			httpPost.setEntity(httpEntity);

			response = httpClient.execute(httpPost);
			logger.info("Response from server");
			logger.info(new JSONParser().parse(ApiUtils.convert(response.getEntity().getContent())));

		} catch (IOException e1) {
			logger.error("Error in posting the data to server");
			logger.error(e1.getMessage(), e1);
		}
	}
	
	public static HttpResponse doGET(String partialURLString) throws Exception {
		HttpResponse response = null;
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet(PropHelper.getHelper().getBaseURL() + partialURLString);
		httpGet.setHeader("token", PropHelper.getHelper().getToken());
		response = httpClient.execute(httpGet);
		return response;
	}
	
	
	public static HttpResponse doGet(String partialUrl, String token) throws Exception {
		// String baseUrl = GITHUB_SOURCE_PREFIX_URL;
		String baseUrl = PropHelper.getHelper().getBitbucketBaseURL();
		HttpResponse response =null;
		String result=null;
		 response = ApiUtils.doGET(baseUrl, token, partialUrl);
		
		return response;
	}
	
	public static HttpResponse doGET(String baseUrl, String token, String partialURLString) throws Exception {
		HttpResponse response = null;

		int retryCount = getRetryCount();

		DefaultHttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(retryCount, true);

		// Setting default retry handler
		HttpClient httpClient = HttpClients.custom().setRetryHandler(retryHandler).build();
		// Building Get request and setting headers
		logger.info("baseUrl + partialURLString::"+baseUrl + partialURLString);
		HttpGet httpGet = new HttpGet(baseUrl + partialURLString);
		httpGet.setHeader(HttpHeaders.AUTHORIZATION, token);

		response = httpClient.execute(httpGet);
		return response;
	}
	
	
	private static int getRetryCount() {
		int retryCount = 0;
		try {
			if (PropHelper.getHelper().getProperties().get(BitBucketConstants.RETRY_COUNT) != null) {
				retryCount = Integer.parseInt(PropHelper.getHelper().getProperties().get(BitBucketConstants.RETRY_COUNT).toString());
				logger.info("Setting retry count to: " + retryCount);
				return retryCount;
			}
		} catch (Exception ex) {
			logger.error("Error in fetching request.retry.count from Properties file. Setting it to default 0");
			logger.error(ex.getMessage(), ex);
		}
		logger.warn("request.retry.count not provided in properties file - Setting retry count to default: 0");
		return retryCount;
	}
	
	 public static String convert(HttpEntity entity) throws IOException {
	        return EntityUtils.toString(entity);
	    }

	
	 public static String convert(InputStream inputStream) throws IOException {
			try (Scanner scanner = new Scanner(inputStream)) {
				if (scanner.useDelimiter("\\A").hasNext()) {
					return scanner.useDelimiter("\\A").next();
				}
				return "";
			}
		}

	public AlationConfig getAlationConfig() {
		return alationConfig;
	}

	public void setAlationConfig(AlationConfig alationConfig) {
		this.alationConfig = alationConfig;
	}
	

}
