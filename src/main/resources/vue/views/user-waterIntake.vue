<template id="user-water">
  <app-layout>
    <h4>Water Intake Tracker</h4>
    <br>
    <div class="chart-container"
         style="position: relative; height:40vh; width:85vw; border: 1px solid lightgrey; padding: 25px">
      <canvas id="myChart"></canvas>
    </div>
    <br>
    <p v-if="water.length === 0">No Data Found! Add your water intake now to track health status</p>
    <br>
    <div class="card bg-light mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6"> Timeline</div>
          <div class="col" align="right">
            <button rel="tooltip" title="Add"
                    class="btn shadow-none border-0" data-bs-toggle="modal" data-bs-target="#myModal">
              <i class="fa fa-plus" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>

      <!-- Modal -->
      <div class="modal fade" id="myModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="modalTitle">Today</h5>
              <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
              <form ref="myForm">
                <div class="form-group">
                  <label for="inpWater">Water Intake(Litres)</label>
                  <input type="text" class="form-control" id="inpWater" v-model="formData.litres"
                         placeholder="Enter your water intake...">
                </div>
              </form>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
              <button type="button" data-bs-dismiss="modal" class="btn btn-primary" @click="addWater();">Save changes
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Modal for Updating Water Intake -->
      <div class="modal fade" id="updateModal" tabindex="-1" role="dialog" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered" role="document">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="modalTitle">{{
                  new Date(currentDate).toLocaleDateString('en-us', {
                    weekday: "long",
                    year: "numeric",
                    month: "short",
                    day: "numeric"
                  })
                }}</h5> &nbsp;&nbsp;&nbsp;
              <button type="button" class="close" data-bs-dismiss="modal">
                <span aria-hidden="true"><i class="fa-solid fa-x" style="color: #63E6BE;"></i></span>
              </button>
            </div>
            <div class="modal-body">
              <form>
                <div class="form-group">
                  <label for="inpWater">Enter New Water Intake (Litres)</label>
                  <input type="text" class="form-control" id="inpWater" v-model="formData.litres">
                </div>
              </form>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
              <button type="button" data-bs-dismiss="modal" class="btn btn-primary"
                      @click="updateWater(currentId, currentDate);">Save changes
              </button>
            </div>
          </div>
        </div>
      </div>

      <div class="card-body" v-for="(w, index) in water" v-bind:key="index">
        <table class="table">
          <tbody>
          <tr>
            <th scope="row">{{ w.litres }} L</th>
            <td>{{
                new Date(w.dateofdrinking).toLocaleDateString('en-us', {
                  year: "numeric",
                  month: "short",
                  day: "numeric"
                })
              }}
            </td>
            <td class="float-right">
              <button rel="tooltip" title="Update" @click="getData(w)" data-bs-toggle="modal"
                      data-bs-target="#updateModal"
                      class="btn btn-info btn-simple btn-link">
                <i class="far fa-save" aria-hidden="true"></i>
              </button>
              <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link"
                      @click="deleteWater(w, index)">
                <i class="fas fa-trash" aria-hidden="true"></i>
              </button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </app-layout>
</template>

<script>

app.component("user-water", {
  template: "#user-water",
  data: () => ({
    water: [],
    dates: [],
    litres: [],
    formData: [],
    currentId: null,
    currentDate: null
  }),
  created() {
    const userId = this.$javalin.pathParams["user-id"];
    axios.get(`/api/water/users/${userId}`)
        .then(res => {
          this.water = res.data;
          if (this.water.length) {
            const ctx = document.getElementById('myChart');
            for (item of this.water) {
              this.dates.push(new Date(item.dateofdrinking).toLocaleDateString('en-us', {
                year: "numeric",
                month: "short",
                day: "numeric"
              }));
              this.litres.push(item.litres);
            }
            this.dates.sort(function (a, b) {
              return new Date(a) - new Date(b);
            });
            new Chart(ctx, {
              type: 'line',
              data: {
                labels: this.dates,
                datasets: [{
                  label: 'Water Intake',
                  data: this.litres,
                  borderWidth: 1
                }]
              },
              options: {
                scales: {
                  y: {
                    beginAtZero: false
                  },
                  x: {
                    grid: {
                      display: false,
                    }
                  },
                  y: {
                    grid: {
                      display: false
                    }
                  },
                },
                plugins: {
                  legend: {
                    display: false
                  }
                }
              }
            });
          }
        })
        .catch(() => console.log("Error while fetching water intake data"));
  },
  methods: {
    getData: function (w) {
      this.currentId = w.id;
      this.currentDate = w.dateofdrinking;
      this.formData.litres = w.litres;
    },
    addWater: function () {
      const userId = this.$javalin.pathParams["user-id"];
      const url = `/api/water`;
      axios.post(url, {
        litres: parseFloat(this.formData.litres),
        dateofdrinking: new Date().toISOString(),
        userid: userId
      })
          .then(response => {
            this.water.push(response.data);
            this.formData.litres = "";
            $('#myModal').modal('hide');
          })
          .catch(error => {
            console.log(error)
          })

    },
    updateWater: function (id, date) {
      const userId = this.$javalin.pathParams["user-id"];
      const url = `/api/water/${id}`;
      axios.patch(url, {
        id: id,
        litres: parseFloat(this.formData.litres),
        dateofdrinking: date,
        userid: userId

      })
          .then(response => {
            const index = this.water.findIndex(w => w.id === id);
            if (index !== -1) {
              this.water[index].litres = response.data.litres;
              this.water[index].dateofdrinking = response.data.dateofdrinking;
            }
            this.formData.litres = "";
            $('#updateModal').modal('hide');

          })
          .catch(error => {
            console.log(error)
          });
    },
    deleteWater: function (water, index) {
      if (confirm('Are you sure you want to delete this record?')) {
        const url = `/api/water/${water.id}`;
        axios.delete(url)
            .catch(function (error) {
              console.log(error)
            });
      }
    },
  },
  resetForm: function () {
    this.formData.litres= null;
  },

});
</script>
<style>
.card-header {
  background-color: #e0bcf8 !important;
}
</style>