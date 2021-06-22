package nextstep.subway.line;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.LineStationFixture.노선정보세팅_return_response;
import static nextstep.subway.LineStationFixture.지하철역세팅_return_response;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

	@Autowired
	private ObjectMapper objectMapper;

	@DisplayName("지하철 노선을 생성한다.")
	@Test
	void createLine() throws JsonProcessingException {
		// given, when 지하철_노선_생성_요청 + 상행, 하행 정보 요청 파라미터에 함께 추가
		StationRequest stationRequest = new StationRequest();
		stationRequest.setName("잠실역");
		ExtractableResponse<Response> stationRes = 지하철역세팅_return_response(stationRequest);

		StationResponse stationResponse = objectMapper.readValue(stationRes.response().asString(), StationResponse.class);

		// when
		LineRequest lineRequest = new LineRequest("2호선", "yellow", stationResponse.getId(), stationResponse.getId(), 1);
		String req = objectMapper.writeValueAsString(lineRequest);

		ExtractableResponse<Response> response = RestAssured.given().log().all()
				.body(req)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.post("/lines")
				.then().log().all()
				.extract();

		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		LineResponse lineResponse = objectMapper.readValue(response.response().asString(), LineResponse.class);

		assertThat("2호선").isEqualTo(lineResponse.getName());
		assertThat("yellow").isEqualTo(lineResponse.getColor());
	}

	@DisplayName("지하철 노선을 생성한다. 실패 - 없는 역정보")
	@Test
	void createLine_RED_NoneExistStationException() throws JsonProcessingException {
		// given, when 지하철_노선_생성_요청 + 상행, 하행 정보 요청 파라미터에 함께 추가
		// when
		LineRequest lineRequest = new LineRequest("2호선", "yellow", 1L, 1L, 1);
		String req = objectMapper.writeValueAsString(lineRequest);

		ExtractableResponse<Response> response = RestAssured.given().log().all()
				.body(req)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.post("/lines")
				.then().log().all()
				.extract();

		assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	@DisplayName("지하철 노선을 생성한다. 실패 - 잘못된 거리정보")
	@ParameterizedTest
	@ValueSource(ints = {0, -1})
	void createLine_RED_DISTANCE_VALID_EXCEPTION(int wrongDistanceValue) throws JsonProcessingException {
		// given, when 지하철_노선_생성_요청 + 상행, 하행 정보 요청 파라미터에 함께 추가
		StationRequest stationRequest = new StationRequest();
		stationRequest.setName("잠실역");
		ExtractableResponse<Response> stationRes = 지하철역세팅_return_response(stationRequest);

		StationResponse stationResponse = objectMapper.readValue(stationRes.response().asString(), StationResponse.class);

		// when
		LineRequest lineRequest = new LineRequest("2호선", "yellow", stationResponse.getId(), stationResponse.getId(), wrongDistanceValue);
		String req = objectMapper.writeValueAsString(lineRequest);

		ExtractableResponse<Response> response = RestAssured.given().log().all()
				.body(req)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.post("/lines")
				.then().log().all()
				.extract();

		assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	@DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
	@Test
	void createLine2() throws JsonProcessingException {
		// given 지하철_노선_등록되어_있음 + 상행, 하행 정보 요청 파라미터에 함께 추가
		StationRequest stationRequest = new StationRequest();
		stationRequest.setName("잠실역");
		지하철역세팅_return_response(stationRequest);

		String dupName = "2호선";
		String dupColor = "yellow";
		LineRequest lineRequest = new LineRequest(dupName, dupColor, 1L, 1L, 1);
		노선정보세팅_return_response(lineRequest);

		// when 중복 지하철_노선_생성_요청
		ExtractableResponse<Response> response = 노선정보세팅_return_response(lineRequest);

		// then
		// 지하철_노선_생성_실패됨
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	@DisplayName("지하철 노선 목록을 조회한다.")
	@Test
	void getLines() throws JsonProcessingException {
		// given
		// 지하철_노선_등록되어_있음 + 상행, 하행 정보 요청 파라미터에 함께 추가
		StationRequest stationRequest = new StationRequest();
		stationRequest.setName("잠실역");
		지하철역세팅_return_response(stationRequest);

		LineRequest lineRequest = new LineRequest("2호선", "yellow", 1L, 1L, 1);
		ExtractableResponse<Response> createdResponse1 = 노선정보세팅_return_response(lineRequest);

		// 지하철_노선_등록되어_있음 + 상행, 하행 정보 요청 파라미터에 함께 추가
		LineRequest lineRequest2 = new LineRequest("8호선", "yellow", 1L, 1L, 1);
		ExtractableResponse<Response> createdResponse2 = 노선정보세팅_return_response(lineRequest2);

		// when
		// 지하철_노선_목록_조회_요청 + 상행 -> 하행역 순으로 정렬되어야 함
		ExtractableResponse<Response> response = get메소드호출("/lines");

		// then
		// 지하철_노선_목록_응답됨
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

		// 지하철_노선_목록_포함됨
		List<Long> expectedLineIds = Arrays.asList(createdResponse1, createdResponse2).stream()
				.map(it -> Long.parseLong(it.header("Location").split("/")[2]))
				.collect(Collectors.toList());

		List<Long> resultLineIds = response.jsonPath().getList(".", LineResponse.class)
				.stream()
				.map(it -> it.getId())
				.collect(Collectors.toList());

		assertThat(resultLineIds).containsAll(expectedLineIds);
	}

	@DisplayName("지하철 노선을 조회한다.")
	@Test
	void getLine() throws JsonProcessingException {
		// given
		// 지하철_노선_등록되어_있음 + 상행, 하행 정보 요청 파라미터에 함께 추가
		StationRequest stationRequest = new StationRequest();
		stationRequest.setName("잠실역");
		지하철역세팅_return_response(stationRequest);

		String name = "2호선";
		LineRequest lineRequest = new LineRequest(name, "yellow", 1L, 1L, 1);
		노선정보세팅_return_response(lineRequest);

		// when
		// 지하철_노선_조회_요청
		ExtractableResponse<Response> response = get메소드호출("/lines/" + name);

		// then
		// 지하철_노선_응답됨
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

		String resultLineName = response.jsonPath().getString("name");

		assertThat(resultLineName).isEqualTo(name);
	}

	@DisplayName("지하철 노선을 수정한다.")
	@Test
	void updateLine() throws JsonProcessingException {
		// given
		// 지하철_노선_등록되어_있음 + 상행, 하행 정보 요청 파라미터에 함께 추가
		StationRequest stationRequest = new StationRequest();
		stationRequest.setName("잠실역");
		지하철역세팅_return_response(stationRequest);

		String name = "2호선";
		LineRequest lineRequest = new LineRequest(name, "yellow", 1L, 1L, 1);
		노선정보세팅_return_response(lineRequest);

		// when;
		// 지하철_노선_수정_요청
		String newColor = "puple";
		LineRequest lineChangeRequest = new LineRequest(name, newColor, 1L, 1L, 1);

		ExtractableResponse<Response> response = RestAssured.given().log().all()
				.body(lineChangeRequest)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.patch("/lines")
				.then().log().all()
				.extract();

		// then
		// 지하철_노선_수정됨
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	@DisplayName("지하철 노선을 제거한다.")
	@Test
	void deleteLine() throws JsonProcessingException {
		// given
		// 지하철_노선_등록되어_있음 + 상행, 하행 정보 요청 파라미터에 함께 추가
		StationRequest stationRequest = new StationRequest();
		stationRequest.setName("잠실역");
		지하철역세팅_return_response(stationRequest);

		String name = "2호선";
		LineRequest lineRequest = new LineRequest(name, "yellow", 1L, 1L, 1);
		노선정보세팅_return_response(lineRequest);

		// when
		// 지하철_노선_제거_요청
		ExtractableResponse<Response> response = RestAssured.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.delete("/lines/" + name)
				.then().log().all()
				.extract();

		// then
		// 지하철_노선_삭제됨
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	public ExtractableResponse<Response> get메소드호출(String path) {
		return RestAssured.given().log().all()
				.when()
				.get(path)
				.then().log().all()
				.extract();
	}
}
