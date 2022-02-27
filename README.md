# keycloak-meetup
We aim to explain how we have used keycloak open source identity management platform for providing Single Sign On feature for our customers. 

Keycloak is an Open Source Identity and Access Management tool.
You can use it to add authentication to applications and secure services with minimum effort.
No need to deal with storing users or authenticating users.

Keycloak provides user federation, strong authentication, user management, fine-grained authorization, and more.

for details and new versions, please visit https://www.keycloak.org/

## installing keycloak

for the sake of simplicity, we will use the development version of keycloak, you can simply download the keycloak from following address https://www.keycloak.org/downloads

we have used version 17.0.0. 

+ download the package via https://github.com/keycloak/keycloak/releases/download/17.0.0/keycloak-17.0.0.zip
+ extract the package 
+ run following command ```./bin/kc.sh start-dev```

this should start keycloak in development mode


