package uk.co.bigsoft.apps.cfgen4j.cfg;

import com.contentful.java.cma.Constants;

public class Cfg {

	private String spaceId;
	private String packageName;
	private String folder;
	private String accessToken;
	private String coreEndpoint = Constants.ENDPOINT_CMA;
	private String uploadEndpoint = Constants.ENDPOINT_UPLOAD;
	private String version = new Version().get();
	private boolean addSetters = false;
	private boolean useDateType = false;
	private String cacheFile = null;

	public Cfg() {
		//
	}

	public boolean getUseDateType() {
		return useDateType;
	}

	public String getVersion() {
		return version;
	}

	/**
	 * Add setters to generated model classes.
	 *
	 * @return
	 */
	public boolean getAddSetters() {
		return addSetters;
	}

	public String getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(String spaceId) {
		this.spaceId = spaceId;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getCacheFile() {
		return cacheFile;
	}

	public void setAppAccessToken(String appAccessToken) {
		this.accessToken = appAccessToken;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[space=" + spaceId + ", package=" + packageName + ", folder=" + folder
				+ ", token=" + accessToken + "]";
	}

	public void setCoreEndpoint(String coreEndpoint) {
		this.coreEndpoint = coreEndpoint;
	}

	public String getCoreEndpoint() {
		return coreEndpoint;
	}

	public void setUploadEndpoint(String uploadEndpoint) {
		this.uploadEndpoint = uploadEndpoint;
	}

	public String getUploadEndpoint() {
		return uploadEndpoint;
	}

	public void setAddSetters(boolean addSetters) {
		this.addSetters = addSetters;
	}

	public void setUseDateType(Boolean useDateType) {
		this.useDateType = useDateType;
	}

	public void setCacheFile(String cacheFile) {
		this.cacheFile = somethingOrNull(cacheFile);
	}

	private String somethingOrNull(String s) {
		if (s == null) {
			return null;
		}
		s = s.trim();
		if (s.length() == 0) {
			return null;
		}
		return s;
	}
}
