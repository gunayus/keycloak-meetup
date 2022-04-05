# keycloak-meetup frontend project

## Create Project
```
npm install -g @vue/cli
vue create hello-world
```

## Keycloak Setup & Client Configuration
- Add Keycloak dependency to package.json
```
npm i keycloak-js
```

- Import keycloak.js to main.js
```
import Keycloak from 'keycloak-js'
```

- Declare keycloak configuration object

```
const initOptions = {
    url: process.env.VUE_APP_KEYCLOAK_API_URL,          // URL to the Keycloak server
    realm: process.env.VUE_APP_KEYCLOAK_REALM,          // Name of the realm
    clientId: process.env.VUE_APP_KEYCLOAK_CLIENT_ID    // Client identifier
}
```

- Creates a new Keycloak client instance 
```
const keycloak = Keycloak(initOptions)
```

- Call function to initialize the keycloak adapter.
```
// Specifies an action to do on load
keycloak.init({ onLoad: 'login-required' }).then(() => {
    //do something after login
})

```
- keycloak.js useful functions.

```
keycloak.login()
keycloak.logout()
keycloak.updateToken()
keycloak.loadUserProfile()
```

### Run Project
```
cd frontend-vuejs
npm install
npm run serve
```

### Custom Login Page
```
copy login folder 
from https://github.com/keycloak/keycloak/tree/main/themes/src/main/resources/theme/keycloak
to ./keycloak-17.0.0/themes/{custom}

run keycloak with  'bin/kc.sh start-dev' command
```
