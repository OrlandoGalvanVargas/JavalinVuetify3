<template id="form-page">
  <app-frame>
    <v-sheet class="mx-auto" width="300">
      <v-form ref="form">
        <v-text-field
            v-model="name"
            :counter="10"
            :rules="nameRules"
            label="Name"
            required
        ></v-text-field>

        <v-select
            v-model="select"
            :items="items"
            :rules="selectRules"
            label="Item"
            required
        ></v-select>

        <v-checkbox
            v-model="checkbox"
            :rules="checkboxRules"
            label="Do you agree?"
            required
        ></v-checkbox>

        <div class="d-flex flex-column">
          <v-btn
              class="mt-4"
              color="success"
              block
              @click="validate"
          >
            Validate
          </v-btn>

          <v-btn
              class="mt-4"
              color="error"
              block
              @click="reset"
          >
            Reset Form
          </v-btn>

          <v-btn
              class="mt-4"
              color="warning"
              block
              @click="resetValidation"
          >
            Reset Validation
          </v-btn>
        </div>
      </v-form>
    </v-sheet>
  </app-frame>
</template>

<script>
app.component("form-page", {
  template: "#form-page",
  data: () => ({
    form: null,
    items: [
      'Item 1',
      'Item 2',
      'Item 3',
      'Item 4',
    ],
    name: '',
    nameRules: [
      v => !!v || 'Name is required',
      v => (v && v.length <= 10) || 'Name must be 10 characters or less',
    ],
    select: null,
    selectRules: [v => !!v || 'Item is required'],
    checkbox: false,
    checkboxRules: [v => !!v || 'You must agree to continue!']
  }),
  methods: {
    validate() {
      this.$refs.form.validate().then(valid => {
        if (valid.valid) {
          alert('Form is valid');
        }
      });
    },
    reset() {
      this.$refs.form.reset();
    },
    resetValidation() {
      this.$refs.form.resetValidation();
    }
  }
})
</script>