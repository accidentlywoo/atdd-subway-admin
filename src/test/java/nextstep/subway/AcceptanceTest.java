package nextstep.subway;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {
	@LocalServerPort
	int port;

	@Autowired
	private DatabaseCleanup databaseCleanup;

	@BeforeEach
	public void setUp() throws JsonProcessingException {
		if (RestAssured.port == RestAssured.UNDEFINED_PORT) {
			RestAssured.port = port;
			databaseCleanup.afterPropertiesSet();
		}

		databaseCleanup.execute();
	}
}
