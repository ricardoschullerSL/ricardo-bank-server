# Ricardo Bank Server

![Build passing](https://circleci.com/gh/ricardoschullerSL/ricardo-bank-server.svg?style=shield&circle-token=:-circle-token)
[![codecov](https://codecov.io/gh/ricardoschullerSL/ricardo-bank-server/branch/master/graph/badge.svg)](https://codecov.io/gh/ricardoschullerSL/ricardo-bank-server)

Under construction.

Just a hacky implementation of the Open Banking APIs so a phone app can be tested against it.

It saves users to a MySQL database. Third Party Providers (TPPs) can then dynamically register by presenting a correct software statement.
This will return client credentials that can be used to retrieve access/refresh tokens.

To get it working you need a `resources` folder in `main` with `application.properties` , `ec256-key-pair.pem`, and `keystore.p12`.
The `ec256-key-pair.pem` is used to sign and encrypt/decrypt JWTs. 
`keystore.p12` is a SSL certificate used for HTTPS. 

In `application.properties` add these lines:

```
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.dll=create
spring.datasource.url=jdbc:{MySQL data url}
spring.datasource.username={MySQL username}
spring.datasource.password={MySQL password}
accesstoken.expirationtime=3600

server.port=8443
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password={keystore password}
server.ssl.keyStoreType=PKCS12
server.ssl.keyAlias={keystore alias}
```

Entries in `{}` need to be filled in. 

