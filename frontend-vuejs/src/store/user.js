import { createStore } from 'vuex'

const store = createStore({
    state () {
        return {
            username:null,
            keycloakToken:null,
            keycloakTokenParsed:null,
        }
    },
    mutations: {
        setUserName(state, val) {
            state.username = val
        },
        setKeycloakToken(state, val) {
            state.keycloakToken = val
        },
        setKeycloakTokenParsed(state, val) {
            state.keycloakTokenParsed = val
        },
    },
    getters: {
        username(state) {
            return state.username
        },
        keycloakToken(state) {
            return state.keycloakToken
        },
        keycloakTokenParsed(state) {
            return state.keycloakTokenParsed
        }
    }
})

export default store;