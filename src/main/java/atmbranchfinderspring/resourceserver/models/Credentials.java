package atmbranchfinderspring.resourceserver.models;


public class Credentials implements ResponseObject {

    private String id;
    private String secret;

    public Credentials() {}

    public Credentials(String id, String secret) {
        this.id = id;
        this.secret = secret;
    }

    public String getId() {
        return id;
    }

    public String getSecret() {
        return secret;
    }

	public void setId(String id) {
		this.id = id;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

}

