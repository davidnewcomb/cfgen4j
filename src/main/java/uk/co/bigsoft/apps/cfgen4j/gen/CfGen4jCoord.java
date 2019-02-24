package uk.co.bigsoft.apps.cfgen4j.gen;

import com.contentful.java.cda.CDAEntry;

class CfGen4jCoord {

	private Double lon;
	private Double lat;

	CfGen4jCoord(CDAEntry entry) {
		lon = entry.getField("lon");
		lat = entry.getField("lat");
	}

	Double getLon() {
		return lon;
	}

	void setLon(Double lon) {
		this.lon = lon;
	}

	@Override
	public String toString() {
		return "ContentfulCoord{lon:" + lon + ",lat:" + lat + "}";
	}
}
