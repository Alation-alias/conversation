package com.alation.api.utils;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;

import com.alation.api.models.AlationConfig;

public class ConfigHelper {

	// Logger
	private static final Logger logger = Logger.getLogger(ConfigHelper.class);

	// Alation Configuration Manager - Maps properties etc.
	private AlationConfig alationConfig = null;

	/*
	 * Method to setup configuration part
	 */
	public AlationConfig createConfig() throws Exception {

		// Get the value from PropHelper (Good candidate for a rewrite)
		// Removing unwanted white spaces
		String URL = StringUtils.strip(PropHelper.getHelper().getBaseURL());
		String token = StringUtils.strip(PropHelper.getHelper().getToken());
		String fileSystemId = StringUtils.strip(PropHelper.getHelper().getFilesystemid());
		
		String bitbucketBaseUrl = StringUtils.strip(PropHelper.getHelper().getBitbucketBaseURL());
		String bitbucketToken = StringUtils.strip(PropHelper.getHelper().getBitbucketToken());
		String bitbucketOwner = StringUtils.strip(PropHelper.getHelper().getBitbucketOwner());
		String propFileLocation=StringUtils.strip(PropHelper.getHelper().getPropfileLocation());

		String sampleSize = StringUtils.strip(PropHelper.getHelper().getSampleSize());
		String csvDelimiters = StringUtils.strip(PropHelper.getHelper().getCsvDelimiters());
		String requestRetryCount=StringUtils.strip(PropHelper.getHelper().getRequestRetryCount());
		

		// Create the configuration
		alationConfig = new AlationConfig();
		alationConfig.setBaseURL(URL);
		alationConfig.setToken(token);
		alationConfig.setFilesystemid(fileSystemId);
		alationConfig.setBitbucketBaseURL(bitbucketBaseUrl);
		alationConfig.setBitbucketToken(bitbucketToken);
		alationConfig.setBitbucketOwner(bitbucketOwner);
		alationConfig.setSampleSize(sampleSize);
		alationConfig.setPropfileLocation(propFileLocation);
		// Scan for sample size
		alationConfig.setCsvDelimiters(csvDelimiters);
		alationConfig.setRequestRetryCount(requestRetryCount);
		setAlationConfig(alationConfig);
		// Return - if anyone needs it?
		return alationConfig;
	}
	
	
	/**
	 * Method verifies all parameters Constructs necessary objects - if any errors
	 * it throws IllegalArgument runtime Exception.
	 * 
	 * @throws IllegalArgumentException - Please be ready to catch and process your
	 *                                  logic
	 */

public	void verifyDefaults() {

			// Check #1
			// Verify that the Alation Base URL is not empty
			if (StringUtils.isEmpty(getAlationConfig().getBaseURL())) {
				throw new IllegalArgumentException("Empty Alation URL - Please configure Alation Server URL");
			}

			// Check #2
			// Verify that the Alation auth token is not empty
			if (StringUtils.isEmpty(getAlationConfig().getToken())) {
				throw new IllegalArgumentException("Empty Alation auth token - Please configure Alation auth token");
			}

			// Check #2.1
			// Validate token
			if (!isValidAlationToken(getAlationConfig().getToken())) {
				throw new IllegalArgumentException("Unable to process with the given alation credentials");
			}

			// Check #3
			// Verify that the BitBuckets Base URL is not empty
			if (StringUtils.isEmpty(getAlationConfig().getBitbucketBaseURL())) {
				throw new IllegalArgumentException("Empty GitHubBase URL - Please configure GitHubBaseURL ");

			}
			// Check #4
			// Verify that the Bit bucket token  is not empty
			if (StringUtils.isEmpty(getAlationConfig().getBitbucketToken()
					)) {
				throw new IllegalArgumentException("Empty GitHub Token - Please configure GitHubToken ");

			}
			
			// Check #5
			// Verify that the Bit bucket owner Specified is not empty
			if (StringUtils.isEmpty(getAlationConfig().getBitbucketOwner())) {
				throw new IllegalArgumentException("Empty GitHubOwner - Please configure GithubOwner");

			}
			
			// Check #6
			// Verify that the File system is not empty
			if (StringUtils.isEmpty(getAlationConfig().getFilesystemid())) {
				throw new IllegalArgumentException("Empty Filesystem id - Please configure Alation FileSystem id");

			}
			
			// Check #7
			// Verify that the Property file Location is not empty
		/*
		 * if (StringUtils.isEmpty(getAlationConfig().getPropfileLocation())) { throw
		 * new
		 * IllegalArgumentException("Empty Property file Location - Please configure Property File Location"
		 * );
		 * 
		 * }
		 */
}
	



	public AlationConfig getAlationConfig() {
		return alationConfig;
	}

	public void setAlationConfig(AlationConfig alationConfig) {
		this.alationConfig = alationConfig;
	}
	
	/**
	 * Method to validate valid alation auth token
	 * 
	 * @param authToken
	 * @return
	 */

	private boolean isValidAlationToken(String authToken) {
		logger.info("Verifying Alation token");
		try {
			String partialURLString = "/api/v1/bulk_metadata/job/?id=1";
			ApiUtils apiUtils = new ApiUtils(getAlationConfig());
			HttpResponse response = apiUtils.doGET(partialURLString);
			if (response.getStatusLine().getStatusCode() == 200) {
				return true;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return false;
	}
	
	
/*	private boolean isValidGitHubToken(String authToken) {
		logger.info("Verifying Alation token");
		try {
			String partialURLString = "/api/v1/bulk_metadata/job/?id=1";
			ApiUtils apiUtils = new ApiUtils(getAlationConfig());
			HttpResponse response = apiUtils.doGET(partialURLString);
			if (response.getStatusLine().getStatusCode() == 200) {
				return true;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return false;
	}*/
	

}
