<template id="app-frame">
  <v-app>
    <v-navigation-drawer v-model="drawer" expand-on-hover rail>
      <v-list>
        <v-list-item
            prepend-avatar="https://cdn-icons-png.flaticon.com/512/6326/6326055.png"
            subtitle="orla.gamer@gmail.com"
            title="Orlando Galvan"
        ></v-list-item>
      </v-list>

      <v-divider></v-divider>

      <v-list density="compact" nav>
        <v-list-item prepend-icon="mdi-format-list-bulleted" title="List Desserts" value="main" @click="$router('/main')"></v-list-item>
        <v-list-item prepend-icon="mdi-star" title="Popular Mice" value="popular" @click="$router('/popular-mice')"></v-list-item>
        <v-list-item prepend-icon="mdi-controller-classic" title="Gaming" value="gaming" @click="$router('/gaming')"></v-list-item>
        <v-list-item prepend-icon="mdi-book-open-variant" title="Books" value="books" @click="$router('/books')"></v-list-item>
        <v-list-item prepend-icon="mdi-cupcake" title="Table Desserts" value="dessert-page" @click="$router('/dessert-page')"></v-list-item>
        <v-list-item prepend-icon="mdi-cupcake" title="Table Desserts" value="dessert-page" @click="$router('/form-page')"></v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-app-bar>
      <v-app-bar-nav-icon @click="drawer = !drawer"></v-app-bar-nav-icon>
      <v-app-bar-title>Mi Aplicación</v-app-bar-title>
    </v-app-bar>

    <v-main>
      <v-container fluid>
        <slot></slot>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
app.component("app-frame", {
  template: "#app-frame",
  data: () => ({
    drawer: true
  }),
  methods: {
    $router(path) {
      window.location.href = path;
    }
  }
})
</script>