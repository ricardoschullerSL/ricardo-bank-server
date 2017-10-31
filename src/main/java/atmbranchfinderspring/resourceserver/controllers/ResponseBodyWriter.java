package atmbranchfinderspring.resourceserver.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class ResponseBodyWriter {

	private ObjectMapper mapper;

	public ResponseBodyWriter(ObjectMapper mapper) {
		this.mapper = mapper;
	}

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
		mapper.writer().writeValue(response.getWriter(), responseBody);
	}
}
