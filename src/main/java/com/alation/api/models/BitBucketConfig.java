package com.alation.api.models;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.alation.api.utils.ApiUtils;
import com.alation.api.utils.BitBucketConstants;
import com.alation.api.utils.ConfigHelper;
import com.alation.api.utils.PropHelper;
import com.alation.bitbucket.BitBucketRepositoryProvider;

public class BitBucketConfig {
	// Static Stuff
	private static final Logger logger = Logger.getLogger(BitBucketConfig.class);
	

	public JSONArray get_all_repos(String reposiory_url , String bitbucketToken) throws Exception {
		// TODO Auto-generated method stub
		
        HttpResponse  response = ApiUtils.doGet(reposiory_url, bitbucketToken);
        
        return parseResponse(response, reposiory_url);
        
       
	
	}
	
	  private static JSONArray parseResponse(HttpResponse httpResponse, String partialURL) 
	  {
		  try
		  {
	        String jsonResponse = ApiUtils.convert(httpResponse.getEntity());
	        logger.info("jsonResponse  :::"+jsonResponse);
	        JSONParser parser = new JSONParser();
	        Object response = parser.parse(jsonResponse);
	        JSONObject responseJSON = (JSONObject) response;
	        if (responseJSON.containsKey(BitBucketConstants.VALUE)) {
	            Object RepositoryArray = responseJSON.get(BitBucketConstants.VALUE);
	            logger.info("RepositoryArray:::"+RepositoryArray);
	            if (RepositoryArray instanceof JSONArray) {
	                return (JSONArray) RepositoryArray;
	               
	            } else {
	                logger.error("\"value\" field in the response object of the url " + partialURL + " is not JSON Array!!");
	            }
	        } else {
	            logger.error("\"value\" field is not present in the response object of the url " + partialURL);
	        }
	       
		  }catch(Exception e)
		  {
			  logger.error(e.getMessage(),e);
		  }
		  return null;
	    }

	public static LinkedHashMap<String,String> getRepositorySourcePath(JSONArray repositories, LinkedList<String> repositoryName) 
	{
		// TODO Auto-generated method stub
		LinkedHashMap<String,String> PathvsSource=new LinkedHashMap<String,String>();
		for(int i=0;i<repositoryName.size();i++)
		{
		   for(int index=0;index<repositories.size();index++)
		    {
			  JSONObject RepositorySourcePath=(JSONObject)repositories.get(index);
			  String Reponame=RepositorySourcePath.get(BitBucketConstants.NAME).toString();
			  if(Reponame.equalsIgnoreCase(repositoryName.get(i)))
			  {
				 // String Link=RepositorySourcePath.get(BitBucketConstants.LINK).toString();
				  JSONObject Link=(JSONObject) RepositorySourcePath.get(BitBucketConstants.LINK);
				  JSONObject Sourceobj=(JSONObject) Link.get(BitBucketConstants.SOURCE);
				  String source=Sourceobj.get(BitBucketConstants.HREF).toString();
				  logger.info("source:::"+source);
				  PathvsSource.put("/"+Reponame, source);
				  logger.info("PathvsSource:::"+PathvsSource);
			  }
			
			 
			
		    }
		}
		return PathvsSource;
		
	}

	public static void getRepositorySourceURL(LinkedHashMap<String, String> repositorynamevsPath) throws Exception {
		// TODO Auto-generated method stub
		String PartialSourceURL=null;
		for(Map.Entry<String, String> entry:repositorynamevsPath.entrySet())
		{
			logger.info("Repository name and source Path");
			String RepositoryName=entry.getKey();
			String SourcePath=entry.getValue();
			logger.info("RepositoryName::"+ RepositoryName+" SourcePath:::"+SourcePath);
			PartialSourceURL=findPartialURL(SourcePath);
			logger.info("PartialSourceURL:::"+PartialSourceURL);
			
			getRepositoryPath(PartialSourceURL);
			
		}
		
		
		
	}
	
	private static void getRepositoryPath(String partialSourceURL) throws Exception {
		
		// TODO Auto-generated method stub
		JSONArray sourceURL=new JSONArray();
		 String BitBucket_Token="Bearer " + PropHelper.getHelper().getBitbucketToken();
		 
		 sourceURL = getSourcePath(partialSourceURL,BitBucket_Token); 
		 for(int i=0;i<sourceURL.size();i++)
		 {
			 JSONObject Source=(JSONObject)sourceURL.get(i);
			 String Path=Source.get(BitBucketConstants.PATH).toString();
			 String type=Source.get(BitBucketConstants.TYPE
					 ).toString();
			 JSONObject Link=(JSONObject) Source.get(BitBucketConstants.LINK);
			 JSONObject Sourceobj=(JSONObject) Link.get(BitBucketConstants.SELF);
			 String SourceReference= Sourceobj.get(BitBucketConstants.HREF).toString();
			logger.info("SourceReference:::"+SourceReference);
			 
		 }
		 
		 
		 logger.info("sourceURL::"+sourceURL);
		
		//return null;
	}

	private static JSONArray getSourcePath(String partialSourceURL, String bitBucket_Token) throws Exception 
	{
		// TODO Auto-generated method stub
		
		   HttpResponse  response = ApiUtils.doGet(partialSourceURL, bitBucket_Token);
	        
	        return parseResponse(response, partialSourceURL);
		
		//return null;
	}

	private static String findPartialURL(String url) {
		// TODO Auto-generated method stub
		String subdata = null;
		//Pattern regex = Pattern.compile("repositories/(.*?)/src");
		Pattern regex = Pattern.compile("repositories/.*");
		Matcher regexMatcher = regex.matcher(url);
		while (regexMatcher.find()) {
				 subdata = regexMatcher.group();
				logger.info("subdata:::"+subdata);
			
		}
		return subdata;
	}

	
	

}
