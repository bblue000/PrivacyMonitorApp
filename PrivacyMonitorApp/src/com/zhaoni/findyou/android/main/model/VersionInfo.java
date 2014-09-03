package com.zhaoni.findyou.android.main.model;

public class VersionInfo {
	int id;
	int version_code;
	String version_name;
	String download_url;
	long datatime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVersion_code() {
		return version_code;
	}

	public void setVersion_code(int version_code) {
		this.version_code = version_code;
	}

	public String getVersion_name() {
		return version_name;
	}

	public void setVersion_name(String version_name) {
		this.version_name = version_name;
	}

	public String getDownload_url() {
		return download_url;
	}

	public void setDownload_url(String download_url) {
		this.download_url = download_url;
	}

	public long getDatatime() {
		return datatime;
	}

	public void setDatatime(long datatime) {
		this.datatime = datatime;
	}

	@Override
	public String toString() {
		return "VersionInfo [id=" + id + ", version_code=" + version_code
				+ ", version_name=" + version_name + ", download_url="
				+ download_url + ", datatime=" + datatime + "]";
	}

}
