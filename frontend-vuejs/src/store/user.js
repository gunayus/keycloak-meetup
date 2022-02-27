import { createStore } from 'vuex'

const store = createStore({
    state () {
        return {
            username:null,
            keycloakToken:null
        }
    },
    mutations: {
        setUserName(state, val) {
            state.username = val
        },
        setKeycloakToken(state, val) {
            state.keycloakToken = val
        },
    },
    getters: {
        username(state) {
            return state.username
        },
        keycloakToken(state) {
            return state.keycloakToken
        }
    }
})

export default store;