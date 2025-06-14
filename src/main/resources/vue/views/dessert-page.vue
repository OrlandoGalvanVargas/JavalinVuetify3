<template id="dessert-page">
  <app-frame>
  <v-data-table :headers="dessert.headers" :items="dessert.items">
    <template v-slot:top>
      <v-toolbar flat>
        <v-toolbar-title>My CRUD</v-toolbar-title>
        <v-divider class="mx-4" inset vertical></v-divider>
        <v-spacer></v-spacer>
        <v-dialog v-model="dessert.dialog" max-width="500px">
          <template v-slot:activator="{ props }">
            <v-btn dark class="mb-2" v-bind="props">
              New Item
            </v-btn>
          </template>
          <v-card>
            <v-card-title>
              <span class="text-h5">{{ formTitle }}</span>
            </v-card-title>

            <v-card-text>
              <v-container>
                <v-row>
                  <v-col cols="12" sm="6" md="4">
                    <v-text-field v-model="dessert.editedItem.name" label="Dessert name"></v-text-field>
                  </v-col>
                  <v-col cols="12" sm="6" md="4">
                    <v-text-field v-model="dessert.editedItem.calories" label="Calories"></v-text-field>
                  </v-col>
                  <v-col cols="12" sm="6" md="4">
                    <v-text-field v-model="dessert.editedItem.fat" label="Fat (g)"></v-text-field>
                  </v-col>
                  <v-col cols="12" sm="6" md="4">
                    <v-text-field v-model="dessert.editedItem.carbs" label="Carbs (g)"></v-text-field>
                  </v-col>
                  <v-col cols="12" sm="6" md="4">
                    <v-text-field v-model="dessert.editedItem.protein" label="Protein (g)"></v-text-field>
                  </v-col>
                </v-row>
              </v-container>
            </v-card-text>

            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue darken-1" variant="text" @click="close">
                Cancel
              </v-btn>
              <v-btn color="blue darken-1" variant="text" @click="save">
                Save
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
        <v-dialog v-model="dessert.dialogDelete" max-width="500px">
          <v-card>
            <v-card-title class="text-h5">Are you sure you want to delete this item?</v-card-title>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue darken-1" variant="text" @click="closeDelete">Cancel</v-btn>
              <v-btn color="blue darken-1" variant="text" @click="deleteItemConfirm">OK</v-btn>
              <v-spacer></v-spacer>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-toolbar>
    </template>
    <template v-slot:item.actions="{ item }">
      <v-icon size="small" class="me-2" @click="editItem(item)">
        mdi-pencil
      </v-icon>
      <v-icon size="small" @click="deleteItem(item)">
        mdi-delete
      </v-icon>
    </template>
    <template v-slot:no-data>
      <v-btn color="primary" @click="initialize">
        Reset
      </v-btn>
    </template>
  </v-data-table>
    </app-frame>
</template>

<script>
app.component("dessert-page", {
  template: "#dessert-page",
  data: () => ({
    dessert: {
      dialog: false,
      dialogDelete: false,
      headers: [
        { title: 'Dessert (100g serving)', align: 'start', sortable: false, key: 'name' },
        { title: 'Calories', key: 'calories' },
        { title: 'Fat (g)', key: 'fat' },
        { title: 'Carbs (g)', key: 'carbs' },
        { title: 'Protein (g)', key: 'protein' },
        { title: 'Actions', key: 'actions', sortable: false },
      ],
      items: [],
      editedIndex: -1,
      editedItem: { id: "", name: '', calories: 0, fat: 0, carbs: 0, protein: 0 },
      defaultItem: { id: "", name: '', calories: 0, fat: 0, carbs: 0, protein: 0 },
    }
  }),

  computed: {
    formTitle () {
      return this.dessert.editedIndex == -1 ? 'New Item' : 'Edit Item'
    },
  },

  watch: {
    dialog(val) {
      val || this.close()
    },
    dialogDelete(val) {
      val || this.closeDelete()
    },
  },

  created() {
    this.initialize()
  },

  methods: {
    initialize() {
      this.$fetch('api/desserts')
          .then(response => this.dessert.items = response)
          .catch(error => this.$toast.fire({ icon: "error", title: error }))
    },

    editItem(item) {
      this.$fetch(`api/desserts/${item.id}`)
          .then(response => {
            this.dessert.editedIndex = this.dessert.items.indexOf(item)
            this.dessert.editedItem = Object.assign({}, response)
            this.dessert.dialog = true
          })
    },

    deleteItem(item) {
      this.dessert.editedIndex = this.dessert.items.indexOf(item)
      this.dessert.editedItem = Object.assign({}, item)
      this.dessert.dialogDelete = true
    },

    deleteItemConfirm () {
      this.dessert.items.splice(this.dessert.editedIndex, 1)
      this.closeDelete()
    },

    close () {
      this.dessert.dialog = false
      this.$nextTick(() => {
        this.dessert.editedItem = Object.assign({}, this.dessert.defaultItem)
        this.dessert.editedIndex = -1
      })
    },

    closeDelete () {
      this.dessert.dialogDelete = false
      this.$nextTick(() => {
        this.dessert.editedItem = Object.assign({}, this.dessert.defaultItem)
        this.dessert.editedIndex = -1
      })
    },

    save () {
      const editing = this.dessert.editedIndex > -1
      const dessert = this.dessert.editedItem
      if (editing){
        this.$fetch(`api/desserts/${dessert.id}`, {
          method: 'PATCH',
          body: JSON.stringify(dessert)
        })
            .then(response => {
              if (response === 'success'){
              Object.assign(this.dessert.items[this.dessert.editedIndex], dessert)
            }
              this.close()

            })
      } else {
        this.dessert.items.push(this.dessert.editedItem)
      }
    },
  },
})
</script>