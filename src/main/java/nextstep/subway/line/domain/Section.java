package nextstep.subway.line.domain;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

/**
 * 라인 등록 시 양끝 종점 정보
 */
@Entity
public class Section extends BaseEntity {
	private static final String DISTANCE_VALID_EXCEPTION = "구간길이정보는 1이상이여합니다.";;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = LAZY)
	private Line line;

	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "up_station_id")
	private Station upStation;

	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "down_station_id")
	private Station downStation;

	private int distance;

	protected Section() {
	}

	public Section(Station upStation, Station downStation, int distance) {
		this.upStation = upStation;
		this.downStation = downStation;
		this.distance = distance;
	}

	public Long getId() {
		return id;
	}

	public Line getLine() {
		return line;
	}

	public Station getUpStation() {
		return upStation;
	}

	public void setUpStation(Station upStation) {
		this.upStation = upStation;
	}

	public Station getDownStation() {
		return downStation;
	}

	public void setDownStation(Station downStation) {
		this.downStation = downStation;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public static Section create(Station upStation, Station downStation, int distance) {
		if (distance < 1) {
			throw new IllegalArgumentException(DISTANCE_VALID_EXCEPTION);
		}
		return new Section(upStation, downStation, distance);
	}

	public Section setLine(Line line) {
		this.line = line;
		return this;
	}
}
