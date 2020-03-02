package com.alation.api.models;

import org.apache.log4j.Logger;



public class AlationConfig {
	public static Logger loggerr = Logger.getLogger(AlationConfig.class);

	private String baseURL;
	private String token;
	private String sampleSize;
	private String filesystemid;
	private String bitbucketBaseURL;
	private String bitbucketToken;
	private String bitbucketOwner;
	private String csvDelimiters;
	private String propfileLocation;
	private String requestRetryCount;
	
	public AlationConfig() 
	{
	}

	public AlationConfig(String hostName, String token, String fileSystemId) {
		this.baseURL = hostName;
		this.token = token;
		this.filesystemid = fileSystemId;
	}

	
	
		
	public String getBaseURL() {
		return baseURL;
	}

	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSampleSize() {
		return sampleSize;
	}

	public void setSampleSize(String sampleSize) {
		this.sampleSize = sampleSize;
	}

	public String getFilesystemid() {
		return filesystemid;
	}

	public void setFilesystemid(String filesystemid) {
		this.filesystemid = filesystemid;
	}

	public String getBitbucketBaseURL() {
		return bitbucketBaseURL;
	}

	public void setBitbucketBaseURL(String bitbucketBaseURL) {
		this.bitbucketBaseURL = bitbucketBaseURL;
	}

	public String getBitbucketToken() {
		return bitbucketToken;
	}

	public void setBitbucketToken(String bitbucketToken) {
		this.bitbucketToken = bitbucketToken;
	}

	public String getBitbucketOwner() {
		return bitbucketOwner;
	}

	public void setBitbucketOwner(String bitbucketOwner) {
		this.bitbucketOwner = bitbucketOwner;
	}

	public String getCsvDelimiters() {
		return csvDelimiters;
	}

	public void setCsvDelimiters(String csvDelimiters) {
		this.csvDelimiters = csvDelimiters;
	}

	public String getPropfileLocation() {
		return propfileLocation;
	}

	public void setPropfileLocation(String propfileLocation) {
		this.propfileLocation = propfileLocation;
	}
	

	public String getRequestRetryCount() {
		return requestRetryCount;
	}

	public void setRequestRetryCount(String requestRetryCount) {
		this.requestRetryCount = requestRetryCount;
	}

	public static int parseInt(String val, int defaultValue) {
		try {
			return Integer.parseInt(val);
		} catch (Exception ex) {
			loggerr.warn("parseInt: Error processing value " + val + ", returning default=" + defaultValue);
			loggerr.error(ex.getMessage(),ex);
		}
		return defaultValue;
	}
	
	public static long parseLong(String val, long defaultValue) {
		try {
			return Long.parseLong(val);
		} catch (Exception ex) {
			loggerr.warn("parseInt: Error processing value " + val + ", returning default=" + defaultValue);
			loggerr.error(ex.getMessage(),ex);
		}
		return defaultValue;
	}

	


	
}
