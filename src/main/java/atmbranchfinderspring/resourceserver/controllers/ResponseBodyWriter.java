package atmbranchfinderspring.resourceserver.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * ResponseBodyWriter takes in the Data object and writes it to the correct JSON schema according to the Open Banking
 * spec. It's important to know that the writer immediately flushes the message, meaning the response is sent immediately
 * so make sure the response is ready when {@code writeResponse()} is called.
 */

@Component
public class ResponseBodyWriter {

	private ObjectMapper mapper;

	public ResponseBodyWriter(ObjectMapper mapper) {
		this.mapper = mapper;
	}


	public Map<String, Object> writeResponse(HttpServletRequest request, Object o) {
		Map<String, Object> responseBody = new HashMap<>();
		Map<String, Object> links = new HashMap<>();
		links.put("Self", request.getRequestURI());
		responseBody.put("Data", o);
		responseBody.put("Risk", "{}");
		responseBody.put("Links", links);
		responseBody.put("Meta", "{}");
		return responseBody;
	}

	@Deprecated
	public void writeResponse(HttpServletRequest request, HttpServletResponse response, Object o) throws IOException, RuntimeException {
		if (response == null) {
			throw new RuntimeException("HttpServletResponse object in ResponseBodyWriter is null.");
		}
		Map<String, Object> responseBody = new HashMap<>();
		Map<String, Object> links = new HashMap<>();
		links.put("Self", request.getRequestURI());
		responseBody.put("Data", o);
		responseBody.put("Risk", "{}");
		responseBody.put("Links", links);
		responseBody.put("Meta", "{}");
		response.setContentType("application/json");
		mapper.writer().writeValue(response.getWriter(), responseBody);
	}
}
