package nextstep.subway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

public class LineStationFixture {

	private static ObjectMapper objectMapper = new ObjectMapper();

	public LineResponse 노선정보세팅_return_dto(LineRequest lineRequest) throws JsonProcessingException {
		String req = objectMapper.writeValueAsString(lineRequest);

		ExtractableResponse<Response> response = RestAssured.given().log().all()
				.body(req)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.post("/lines")
				.then().log().all()
				.extract();

		return objectMapper.readValue(response.response().asString(), LineResponse.class);
	}

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

	public StationResponse 지하철역세팅_return_dto(StationRequest stationRequest) throws JsonProcessingException {
		String req = objectMapper.writeValueAsString(stationRequest);

		ExtractableResponse<Response> response = RestAssured.given().log().all()
				.body(req)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.post("/stations")
				.then().log().all()
				.extract();

		return objectMapper.readValue(response.response().asString(), StationResponse.class);
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
