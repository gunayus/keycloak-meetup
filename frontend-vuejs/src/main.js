import {createApp} from 'vue'
import App from './App.vue'
import Keycloak from 'keycloak-js'

const app = createApp(App);

// JSONVIEWER
import JsonViewer from "vue3-json-viewer";
import "vue3-json-viewer/dist/index.css";
app.use(JsonViewer);

// EVENTBUS
import mitt from 'mitt';
const emitter = mitt();
app.config.globalProperties.emitter = emitter;

// VUEX
import store from "@/store/user";
app.use(store)

const initOptions = {
    url: process.env.VUE_APP_KEYCLOAK_API_URL,
    realm: process.env.VUE_APP_KEYCLOAK_REALM,
    clientId: process.env.VUE_APP_KEYCLOAK_CLIENT_ID
}

const keycloak = Keycloak(initOptions)

keycloak.init({onLoad: 'login-required'}).then(() => {

    store.commit('setUserName',keycloak.tokenParsed.preferred_username)
    store.commit('setKeycloakJsonData',keycloak.tokenParsed)

    console.log('-------------keycloak token-------------')
    console.log(keycloak.tokenParsed)

    emitter.on('keycloak-logout', () => {
        keycloak.logout()
    })

    // Token Refresh
    setInterval(() => {
        keycloak
            .updateToken(70)
            .then((refreshed) => {
                if (refreshed) {
                    store.commit('setUserName',keycloak.tokenParsed.preferred_username)
                    store.commit('setKeycloakJsonData',keycloak.tokenParsed)
                    console.info('Token refreshed' + refreshed)
                } else {
                    console.warn('Token not refreshed, valid for ' + Math.round(keycloak.tokenParsed.exp + keycloak.timeSkew - new Date().getTime() / 1000) + ' seconds')
                }
            })
            .catch(() => {
                keycloak.logout()
            })
    }, 60 * 1000)
})

app.mount('#app')
