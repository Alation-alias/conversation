package com.alation.bitbucket;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.alation.api.models.BitBucketConfig;
import com.alation.api.utils.BitBucketConstants;
import com.alation.api.utils.ConfigHelper;

public class BitBucketRepositoryProvider {
	// Static Stuff
		private static final Logger logger = Logger.getLogger(BitBucketRepositoryProvider.class);
		
		// Configuration Helper
		private ConfigHelper configHelper = new ConfigHelper();
		
		private BitBucketConfig config=new BitBucketConfig();
		
		

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		logger.info(new Date() + " STARTING!");
		
		BitBucketRepositoryProvider repositoryProvider=new BitBucketRepositoryProvider();
		
		// Kick start!
				repositoryProvider.startRepositoryProcess();
		
		
				
		
	}
	private void startRepositoryProcess() {
		// TODO Auto-generated method stub
		// Setup configurations
		try
		{
				// This class builds AlationConfig object and abstracts from accessing directly!
				getConfigHelper().createConfig();

				// Verify basic Credentials
				getConfigHelper().verifyDefaults();
				
				kickstartThis();
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(),e);
		}

	}
	private void kickstartThis() throws Exception 
	{
		// TODO Auto-generated method stub
		logger.info("Bitbucket Repository process starts");
		
		processBitBucketConnector();
		
		
		
	}
	private void processBitBucketConnector() throws Exception 
	{
		// TODO Auto-generated method stub
		// Now process buckets by bucket
				processRepository();
		
	}
	private void processRepository() throws Exception 
	{
		// TODO Auto-generated method stub
		
		JSONArray repositories = getRepository();
		LinkedHashMap<String,String> RepositorynamevsPath=new LinkedHashMap<String,String>();
		
		LinkedList<String> RepositoryName=getReponameList(repositories);
		
		logger.info("RepositoryName:::"+RepositoryName);
		
		RepositorynamevsPath=BitBucketConfig.getRepositorySourcePath(repositories,RepositoryName);
		
		logger.info("RepositorynamevsPath:::"+RepositorynamevsPath);
		
		BitBucketConfig.getRepositorySourceURL(RepositorynamevsPath);
		
		
		
		
		
		
	}
	private JSONArray getRepository() throws Exception 
	{
		// TODO Auto-generated method stub
		JSONArray repository = null;
        String BitBucket_Token="Bearer " + getConfigHelper().getAlationConfig().getBitbucketToken();
		String Reposiory_url = BitBucketConstants.REPOSITORY + BitBucketConstants.PATH_CHAR+getConfigHelper().getAlationConfig().getBitbucketOwner();
		logger.info("Reposiory_url::"+Reposiory_url);
				
		repository = config.get_all_repos(Reposiory_url,BitBucket_Token);

		return repository;
		
		
	}
	
	
	
	public static LinkedList<String> getReponameList(JSONArray repos) {
		LinkedList<String> reponm = new LinkedList<String>();
		for (int i = 0; i < repos.size(); i++) {
			JSONObject repo = (JSONObject) repos.get(i);
			String reponame = repo.get(BitBucketConstants.NAME).toString();
			reponm.add(reponame);
		}
		
		return reponm;
	}
	
	
	public ConfigHelper getConfigHelper() {
		return configHelper;
	}
	public void setConfigHelper(ConfigHelper configHelper) {
		this.configHelper = configHelper;
	}
	

}
