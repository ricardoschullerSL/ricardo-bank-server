package atmbranchfinderspring.resourceserver.validation.accountrequests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IncomingRequestBody {

	@JsonProperty(value = "Data",required = true)
	private IncomingAccountRequest data;
	@JsonProperty(value = "Risk")
	private Map<String,String> risk;

	public IncomingRequestBody () {}


	public IncomingAccountRequest getData() {
		return data;
	}

	public void setData(IncomingAccountRequest data) {
		this.data = data;
	}
}
