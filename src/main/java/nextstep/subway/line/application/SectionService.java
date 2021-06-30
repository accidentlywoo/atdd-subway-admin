package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.SectionCreateReq;
import nextstep.subway.line.exception.NoneExistLineException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.exception.NoneExistStationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SectionService {
	private static final String NONE_EXIST_STATION = "없는 역정보입니다.";
	private static final String NONE_EXIST_LINE = "없는 노선정보입니다.";

	@Autowired
	private StationRepository stationRepository;

	@Autowired
	private LineRepository lineRepository;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void create(Line line, Long upStationId, Long downStationId, int distance) {
		Station upStation = findJoinStation(upStationId);

		Station downStation = findJoinStation(downStationId);

		Section newSection = Section.create(upStation, downStation, distance);

		line.setSection(newSection);
	}

	public Section createSection(SectionCreateReq req) {
		Station joinStation = findJoinStation(req.getStationId());

		Optional<Line> line = lineRepository.findByName(req.getLineName());

		if (!line.isPresent()) {
			throw new NoneExistLineException(NONE_EXIST_LINE);
		}

	}

	private Station findJoinStation(Long stationId) {
		Optional<Station> upStation = stationRepository.findById(stationId);

		if (!upStation.isPresent()) {
			throw new NoneExistStationException(NONE_EXIST_STATION);
		}

		return upStation.get();
	}
}
