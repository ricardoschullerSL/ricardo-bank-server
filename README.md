# Ricardo Bank Server

![Build passing](https://circleci.com/gh/ricardoschullerSL/ricardo-bank-server.svg?style=shield&circle-token=:-circle-token)

Under construction.

Just a hacky implementation of the Open Banking APIs so a phone app can be tested against it.

It saves users to a mySQL database. Third Party Providers (TPPs) can then dynamically register by presenting a correct software statement.
This will return client credentials that can be used to retrieve access/refresh tokens.

To get it working you need a `resources` folder in `main` with `application.properties` , `ec256-key-pair.pem`, and `keystore.p12`.
The `ec256-key-pair.pem` is used to sign and encrypt/decrypt JWTs. 
`keystore.p12` is a SSL certificate used for HTTPS. 

In `application.properties` add these lines:

```properties
spring.jpa.hibernate.dll=create
spring.datasource.url=jdbc:{mySQL data url}
spring.datasource.username={mySQL username}
spring.datasource.password={mySQL password}
accesstoken.expirationtime=3600

server.port=8443
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password={keystore password}
server.ssl.keyStoreType=PKCS12
server.ssl.keyAlias=tomcat
```

Entries in `{}` need to be filled in. 

