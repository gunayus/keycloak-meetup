<template>
  <img alt="Vue logo" src="./assets/logo.png">
  <h3>{{'username:' + username}}</h3>
  <button @click="logout"> Logout </button>
  <h1>Keycloak Token</h1>
  <json-viewer :value="keycloakJsonData" copyable boxed sort />
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'App',
  computed:{
    ...mapGetters({
      username: 'username',
      keycloakJsonData: 'keycloakJsonData'
    })
  },
  methods: {
    logout(){
      this.emitter.emit('keycloak-logout')
    }
  }
}
</script>

<style>
</style>