package atmbranchfinderspring.resourceserver.models;

import java.io.Serializable;

public class Admin implements Serializable{

    private String adminId;
    private byte[] hashedSecret;
    private String salt;

    public String getAdminId() {
        return adminId;
    }

    public byte[] getHashedSecret() {
        return hashedSecret;
    }

    public String getSalt() {
        return salt;
    }

    public Admin(String adminId, byte[] hashedSecret, String salt) {

        this.adminId = adminId;
        this.hashedSecret = hashedSecret;
        this.salt = salt;
    }
}
