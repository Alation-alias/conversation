package com.alation.api.utils;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropHelper {

	
	public static Logger logger = Logger.getLogger(PropHelper.class);
	
	private static PropHelper helper = null;
	
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
	
	
	
	
	public String getPropfileLocation() {
		return propfileLocation;
	}
	public void setPropfileLocation(String propfileLocation) {
		this.propfileLocation = propfileLocation;
	}
	public String getCsvDelimiters() {
		return csvDelimiters;
	}
	public void setCsvDelimiters(String csvDelimiters) {
		this.csvDelimiters = csvDelimiters;
	}


	public String getRequestRetryCount() {
		return requestRetryCount;
	}
	public void setRequestRetryCount(String requestRetryCount) {
		this.requestRetryCount = requestRetryCount;
	}

	
	public Properties getProperties() {
		return properties;
	}


	private Properties properties = new Properties();
	
	// Scans startup parameters and loads.
		// Configure your -Dconfig="/folder/config/loader.properties" to point out where
		// the startup is!!
		public static PropHelper getHelper() throws Exception {
			if (helper == null) {
				if (System.getProperty("config") == null) {
					throw new IllegalArgumentException("Missing startup parameter 'config'");
				}
				helper = new PropHelper(System.getProperty("config"));
			}
			return helper;
		}
		
		private void loadProperties(Properties props) {
			baseURL = props.getProperty("alation.baseUrl");
			token = props.getProperty("auth.token");
			filesystemid=props.getProperty("filesystem.id");
			sampleSize=props.getProperty("sample.size");
			csvDelimiters=props.getProperty("csv.delimiter");
			propfileLocation=props.getProperty("property.file.location");
			bitbucketToken=props.getProperty("bitbucket.token");
			bitbucketBaseURL=props.getProperty("bitbucket.base.url");
			bitbucketOwner=props.getProperty("bitbucket.owner");
			requestRetryCount=props.getProperty("request.retry.count");
		}
		
		
		private PropHelper(String filePath) throws Exception {

			FileInputStream file = null;
			try {
				file = new FileInputStream(filePath);
				properties.load(file);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			} finally {
				if (file != null) {
					file.close();
				}
			}

			// Load properties
			loadProperties(properties);
		}
		

}
