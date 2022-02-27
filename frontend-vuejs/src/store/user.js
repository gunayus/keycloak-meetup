import { createStore } from 'vuex'

const store = createStore({
    state () {
        return {
            username:null,
            keycloakJsonData:null
        }
    },
    mutations: {
        setUserName(state, val) {
            state.username = val
        },
        setKeycloakJsonData(state, val) {
            state.keycloakJsonData = val
        },
    },
    getters: {
        username(state) {
            return state.username
        },
        keycloakJsonData(state) {
            return state.keycloakJsonData
        }
    }
})

export default store;