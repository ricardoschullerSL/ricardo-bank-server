package atmbranchfinderspring.resourceserver.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ResponseBodyWriterTests {

	private ResponseBodyWriter responseBodyWriter;
	private ObjectMapper mapper;

	@BeforeEach
	void setup() {
		mapper = new ObjectMapper();
		responseBodyWriter = new ResponseBodyWriter(mapper);
	}

	@Test
	void writeResponseChecksForNullTest() {
		Map<String, String> o = new HashMap<String, String>();
		o.put("test", "value");
		assertThrows(RuntimeException.class, () -> responseBodyWriter.writeResponse(null, o), "HttpServletResponse object in ResponseBodyWriter is null.");
	}

	@Test
	void writeResponseWritesBody() throws Exception{
		MockHttpServletResponse response = new MockHttpServletResponse();
		Map<String, String> body = new HashMap<>();
		body.put("test","value");

		responseBodyWriter.writeResponse(response,body);
		assertThat(response.getContentAsString()).contains("{\"test\":\"value\"}");
	}
}
