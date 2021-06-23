package nextstep.subway.line;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionCreateReq;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Optional;

import static nextstep.subway.LineStationFixture.노선정보세팅_return_dto;
import static nextstep.subway.LineStationFixture.지하철역세팅_return_dto;
import static org.assertj.core.api.Assertions.assertThat;

public class SectionAcceptanceTest extends AcceptanceTest {

	@Autowired
	private StationRepository stationRepository;

	@Autowired
	private ObjectMapper objectMapper;

	LineResponse lineResponse;

	@BeforeEach
	public void setUp() throws JsonProcessingException {
		// given, line 생성 <- 상행약, 하행역 station 등록, 종점 및 거리 세팅
		StationRequest stationRequest1 = new StationRequest();
		stationRequest1.setName("잠실역");
		StationResponse stationResponse1 = 지하철역세팅_return_dto(stationRequest1);

		StationRequest stationRequest2 = new StationRequest();
		stationRequest2.setName("강남역");
		StationResponse stationResponse2 = 지하철역세팅_return_dto(stationRequest2);

		LineRequest lineRequest = new LineRequest("2호선", "yellow", stationResponse1.getId(), stationResponse2.getId(), 7);
		lineResponse = 노선정보세팅_return_dto(lineRequest);
	}

	@Test
	public void 종점사이의_새로운역_상행연결_등록_성공() throws JsonProcessingException {
		// given 상행역정보
		Long upStationId = lineResponse.getUpStationId();

		Station upStation = stationRepository.findById(upStationId).get();
//		Station newStation = Station.create("새로운 역"); // 내부동작 호도리
//		Section section = Section.create(upStation, newStation, 3);

		// when + 새로운 station 생성 및 등록, 양 좀점보다 짧은 구간정보 역은 1m(?)차지
		SectionCreateReq sectionCreateReq = new SectionCreateReq(upStation.getId(), "새로운역", 3);
		String req = objectMapper.writeValueAsString(sectionCreateReq);

		ExtractableResponse<Response> response = RestAssured.given().log().all()
				.body(req)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.post("/line/sections")
				.then().log().all()
				.extract();
		// then 상행 - 새로운 구간 -

		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	@Test
	public void 종점사이의_새로운역_하행연결_등록_성공() {
		// given 하행역정보
		// when + 새로운 station 생성 및 등록 , 양 좀점보다 짧은 구간정보 역은 1m(?)차지
		// then
	}

	@Test
	public void 종점사이의_새로운역_등록_실패_연결지점없음() {
		// when 새로운 station 생성 및 등록 2 , 양 좀점보다 짧은 구간정보 역은 1m(?)차지
		// then Exception
	}

	@Test
	public void 종점사이의_새로운역_등록_실패_길이_초과() {
		// given 상행역정보
		// when + 새로운 station 생성 및 등록, 양 좀점보다 긴 구간정보 역은 1m(?)차지
		// then
	}

	@Test
	public void 종점사이의_새로운역_등록_실패_길이_같음() {
		// given 상행역정보
		// when + 새로운 station 생성 및 등록, 양 좀점과 같은 구간정보 역은 1m(?)차지 역길이 고려쫌...
		// then
	}

	@Test
	public void 새로운_상행_종점_추가_성공() {
		// given 상행역정보
		// when + 새로운 station 생성 및 상행선으로 등록 , 양 좀점보다 짧은 구간정보 역은 1m(?)차지
		// then
	}

	@Test
	public void 새로운_하행_종점_추가_성공() {
		// given 하행역정보
		// when + 새로운 station 생성 및 하행선으로 등록 , 양 좀점보다 짧은 구간정보 역은 1m(?)차지
		// then
	}

	@Test
	public void 새로운_종점_추가_실패_상행_하행_모두_겹침() {
		// when 상행선 및 하행선 정보로 등록 , 양 좀점보다 짧은 구간정보 역은 1m(?)차지
		// then
	}
}