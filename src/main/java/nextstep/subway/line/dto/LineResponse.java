package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;

import java.time.LocalDateTime;
import java.util.List;

public class LineResponse {
	private Long id;
	private String name;
	private String color;
	private Long upStationId;
	private Long downStationId;
	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;

	public LineResponse() {
	}

	public LineResponse(Long id, String name, String color, LocalDateTime createdDate, LocalDateTime modifiedDate) {
		this.id = id;
		this.name = name;
		this.color = color;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
	}

	public static LineResponse of(Line line) {
		LineResponse lineResponse = new LineResponse(
				line.id(), line.name(), line.color(), line.getCreatedDate(), line.getModifiedDate()
		);

		lineResponse.setUpStationId(line.getUpStation().getId());
		lineResponse.setDownStationId(line.getDownStation().getId());

		return lineResponse;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColor() {
		return color;
	}

	public Long getUpStationId() {
		return upStationId;
	}

	public void setUpStationId(Long upStationId) {
		this.upStationId = upStationId;
	}

	public Long getDownStationId() {
		return downStationId;
	}

	public void setDownStationId(Long downStationId) {
		this.downStationId = downStationId;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
}
