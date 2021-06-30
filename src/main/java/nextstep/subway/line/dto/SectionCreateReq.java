package nextstep.subway.line.dto;

public class SectionCreateReq {
	private Long stationId;
	private String lineName;
	private String stationName;
	private int distance;

	public SectionCreateReq(Long stationId, String lineName, String stationName, int distance) {
		this.stationId = stationId;
		this.lineName = lineName;
		this.stationName = stationName;
		this.distance = distance;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}
}
