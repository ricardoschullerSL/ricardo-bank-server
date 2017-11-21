# Ricardo Bank Server

![Build passing](https://circleci.com/gh/ricardoschullerSL/ricardo-bank-server.svg?style=shield&circle-token=:-circle-token)
[![codecov](https://codecov.io/gh/ricardoschullerSL/ricardo-bank-server/branch/master/graph/badge.svg)](https://codecov.io/gh/ricardoschullerSL/ricardo-bank-server)

Under construction. Currently on v0.11.0 BETA.
Javadocs can be found at : http://ricardoschullerSL.github.io/ricardo-bank-server


Just a hacky implementation of the Open Banking APIs so a phone app can be tested against it.

It saves and retrieves user account info from a MySQL database. Third Party Providers (TPPs) can dynamically register by presenting a correct software statement.
This will return client credentials that can be used to retrieve access/refresh tokens.

To get it working you need a `resources` folder in `main` with `application.properties` , `ec256-key-pair.pem`, and `keystore.p12`.
The `ec256-key-pair.pem` is used to sign and verify JWTs. 
`keystore.p12` is a SSL certificate used for HTTPS. 

In `application.properties` add these lines:

```
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.dll=create
spring.datasource.url=jdbc:{MySQL data url}
spring.datasource.username={MySQL username}
spring.datasource.password={MySQL password}
accesstoken.expirationtime=600
refreshtoken.expirationtime=2592000

server.port=8443
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password={keystore password}
server.ssl.keyStoreType=PKCS12
server.ssl.keyAlias={keystore alias}
```

Entries in `{}` need to be filled in. 


### Consuming the endpoints

For a TPP to get client credentials, it needs to POST a valid JWT to the `/register` endpoint, which will send a JSON back with
```json
{
    "id": "clientid",
    "secret": "randomsecret"
}
```

These credentials can now be used with 'TPPClientAuthenticated' endpoints. The TPP should first set up an agreement with the user
and create an `AccountRequest` object with the agreed upon permissions. 
```json
{
    "Data": {
    "Permissions":["ReadAccountBasic", "ReadAccountDetail"],
    "ExpirationDateTime": "0000",
    "TransactionFromDateTime": "0000",
    "TransactionToDateTime": "0000"
    },
    "Risk": {}
}
```

`Risk` should be left empty for now, this is reserved by Open Banking for later. This object should be in the POST request body
to the `/account-requests` endpoint with the credentials base64 encoded in the `Authorization` header.

The server will then respond with an AccountRequestId, which the TPP should use to redirect the user
to the servers website with endpoint `/login/{AccountRequestId}`. The user will then authenticate with the server,
and gets presented with the permissions that is required by the TPP. If the user agrees, it will be 
redirected to the redirect url given by the TPP with an authorization code added to the end.
This authorization code can be exhanged for a refresh token at the `/access-token/{authorizationCode}` endpoint.
The refresh token should be kept secret, as this will be the token that is required for the TPP to get access tokens to access the resources.
Access tokens are short-lived, refresh tokens will live as long as the `ExpirationDateTime` set by the user.
When an access token is expired, the TPP can use the refresh token to request a new access token at the 
`/access-token/refresh/{refreshToken}` endpoint.




