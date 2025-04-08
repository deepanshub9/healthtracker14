<template id="user-bmi-overview">
  <app-layout>
    <div class="card bg-light mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6">
            Add BMI
          </div>
          <div class="col" align="right">
            <button rel="tooltip" title="Add"
                    class="btn btn-info btn-simple btn-link"
                    @click="hideForm = !hideForm">
              <i class="fa fa-plus" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="card-body" :class="{ 'd-none': hideForm }">
        <form>
          <div class="row">
            <div class="col-md-4" style="display: none;">
              <input type="hidden" class="form-control" v-model="userId" name="id" readonly placeholder="Id" />
            </div>
            <div class="col-md-4 mb-3">
              <label for="inputWeight">Weight (kg)</label>
              <input type="text" class="form-control" v-model="formData.weight" name="weight" placeholder="Weight (kg)" />
            </div>
            <div class="col-md-4 mb-3 ">
              <label for="inputHeight">Height (cm)</label>
              <input type="text" class="form-control" v-model="formData.height" name="height" placeholder="Height (cm)" />
            </div>
            <div class="col-md-4 mb-3 calbmi">
              <button type="button" @click="calculateBmi()" class="btn btn-primary">
                <i class="fas fa-calculator"></i> BMI Calculate
              </button>
            </div>
          </div>
        </form>
      </div>
    </div>
    <div class="list-group list-group-flush">
      <table class="table">
        <thead>
        <tr>
          <th scope="col">Weight</th>
          <th scope="col">Height</th>
          <th scope="col">BMI</th>
          <th scope="col">Date</th>
          <th scope="col">Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(bmiRecord, index) in bmiRecords" :key="index">
          <td>{{ bmiRecord.weight }}</td>
          <td>{{ bmiRecord.height }}</td>
          <td>{{ bmiRecord.bmiCalculator }}</td>
          <td>{{ bmiRecord.timestamp }}</td>
          <td>
            <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link" @click="deleteBmi(bmiRecord, index)">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </app-layout>
</template>

<script>
app.component("user-bmi-overview", {
  template: "#user-bmi-overview",
  data: () => ({
    bmiRecords: [],
    formData: { weight: '', height: '' },
    userId: null,
    hideForm: true,
  }),
  created() {
    const userId = this.$javalin.pathParams["user-id"];
    axios.get(`/api/bmi/users/${userId}`)
        .then(res => this.bmiRecords = res.data)
        .catch(() => alert("Error while fetching BMI records"));
  },
  methods: {
    deleteBmi: function (bmiRecord, index) {
      if (confirm('Are you sure you want to delete this BMI record?')) {
        const url = `/api/bmi/${bmiRecord.id}`;
        axios.delete(url)
            .then(() => this.bmiRecords.splice(index, 1))
            .catch(error => console.log(error));
      }
    },
    calculateBmi: function () {
      const userId = this.$javalin.pathParams["user-id"];
      const url = `/api/bmi`;

      axios.post(url, {
        weight: this.formData.weight,
        height: this.formData.height,
        timestamp: new Date().toISOString(),
        userId: userId,
      })
          .then(response => {
            const weight = parseFloat(response.data.weight);
            const height = parseFloat(response.data.height) / 100;
            response.data.bmiCalculator = (weight / (height * height)).toFixed(2);
            this.bmiRecords.push(response.data);
            this.hideForm = true;
          })
          .catch(error => console.log(error));
    },
  },
});
</script>
<style>
.card-header{
  background-color: #e0bcf8 !important;
}
.calbmi {
  padding-top: 23px !important;
}
.btn-primary:hover{
  background-color: #e0bcf8 !important;
  color: #171717;
}
</style>