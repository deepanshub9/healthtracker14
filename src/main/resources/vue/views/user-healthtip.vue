<template id="health-tips">
  <app-layout>
    <div class="tips-container">
      <h3>Health Tips</h3>
      <div v-if="currentTip" class="tip-wrapper">
        <img :src="currentImage" alt="Health Tip" class="tip-image" />
        <p class="tip-text" style="font-family: Calibri">{{ currentTip }}</p>
      </div>
      <div class="d-flex justify-content-center mt-3">
        <button type="button"
                class="btn btn-info btn-primary"
                title="Refresh"
                @click="fetchRandomTip">
          <i class="fa fa-refresh me-2" aria-hidden="true"></i>Refresh
        </button>
      </div>
    </div>
  </app-layout>
</template>

<script>
app.component("health-tips", {
  template: "#health-tips",
  data: () => ({
    currentTip: "",
    currentImage: "",
  }),
  created() {
    this.fetchRandomTip();
  },
  methods: {
    fetchRandomTip() {
      axios.get("/api/healthTips")
          .then(res => {
            console.log("Response from server:", res);
            if (res.data.length > 0) {
              const randomIndex = Math.floor(Math.random() * res.data.length);
              this.currentTip = res.data[randomIndex].tips;
              this.fetchRandomImage();
            }
          })
          .catch(error => {
            console.error("Error while fetching health tips:", error);
            alert("Error while fetching health tips");
          });
    },
    fetchRandomImage() {
      axios.get("https://picsum.photos/400/300")
          .then(res => {
            this.currentImage = res.request.responseURL;
          })
          .catch(error => {
            console.error("Error while fetching health tip image:", error);

            this.currentImage = "https://via.placeholder.com/400x300?text=Health+Tip";
          });
    }
  }
});



</script>

<style scoped>
.tips-container {
  text-align: center;
  background-color: #e0bcf8 !important;
  padding: 20px;
  border-radius: 8px;
}

.tip-wrapper {
  margin-top: 20px;
}

.tip-image {
  max-width: 100%;
  height: auto;
  border-radius: 8px;
}

.tip-text {
  color: #000;
  text-decoration: none;
  font-weight: bold;
  margin-top: 10px;
  font-size: 1.25rem;
  font-family: 'Lora', serif;
}

button {
  color: #fff;
}
.btn-primary:hover{
  background-color: #e0bcf8 !important;
  color: #171717;
}
</style>
