package nextstep.subway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.station.dto.StationRequest;
import org.springframework.http.MediaType;

public class LineStationFixture {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static ExtractableResponse<Response> 노선정보세팅_return_response(LineRequest lineRequest) throws JsonProcessingException {
		String req = objectMapper.writeValueAsString(lineRequest);

		return RestAssured.given().log().all()
				.body(req)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.post("/lines")
				.then().log().all()
				.extract();
	}

	public static ExtractableResponse<Response> 지하철역세팅_return_response(StationRequest stationRequest) throws JsonProcessingException {
		String req = objectMapper.writeValueAsString(stationRequest);

		return RestAssured.given().log().all()
				.body(req)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.post("/stations")
				.then().log().all()
				.extract();
	}
}
