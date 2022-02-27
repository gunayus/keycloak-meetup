<template>
  <img alt="Vue logo" src="./assets/logo.png">
  <h3>{{ 'username:' + username }}</h3>
  <button @click="logout"> Logout</button>
  <h1>Keycloak Token</h1>
  <json-viewer :value="keycloakTokenParsed" copyable boxed sort/>
  <h1>Backend Token</h1>
  <button @click="getBackendTokenInfo"> Auth Token Info</button>
  <json-viewer :value="backendToken" copyable boxed sort/>
</template>

<script>
import {mapGetters} from 'vuex'

export default {
  name: 'App',
  data() {
    return {
      backendToken:null
    }
  },
  computed: {
    ...mapGetters({
      username: 'username',
      keycloakToken: 'keycloakToken',
      keycloakTokenParsed: 'keycloakTokenParsed'
    })
  },
  methods: {
    logout() {
      this.emitter.emit('keycloak-logout')
    },
    getBackendTokenInfo() {
      const config = {
        headers:{
          Authorization: `Bearer ${this.keycloakToken}`
        }
      }
      this.axios.get("/demo/auth-token-info", config).then((response) => {
        this.backendToken = response.data
      })
    }
  }
}
</script>

<style>
</style>