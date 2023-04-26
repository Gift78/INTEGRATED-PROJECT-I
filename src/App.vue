<script setup>
import { ref,onMounted, resolveDirective } from 'vue';
import PhGlobe from './components/icons/PhGlobe.vue';
import {getData} from './composable/getData.js';


const timezone = Intl.DateTimeFormat().resolvedOptions().timeZone;
const data =ref()

onMounted(async () => {
  data.value = await getData();

  console.log(data.value)
});

</script>

<template>
  <div class=" h-screen flex-auto" style="background-color: #F3F5F7; ">
    <div style="width: 70em; " class="mx-auto">
      <div class="text-center items-center justify-center">
        <h1 class="text-3xl">SIT Announcement System(SAS)</h1>
      </div>
      <div class="flex mt-5">
        <div class="flex bg-emerald-400 w-28 h-8 rounded-lg text-white ">
          <PhGlobe /> Time zone:
        </div>
        <div class="ml-2"> {{ timezone }}</div>
      </div>

      <hr class="mt-4 border-2">

      <div class="grid grid-cols-7 mt-3 text-gray-400">
        <div>No.</div>
        <div>Title</div>
        <div>Category</div>
        <div>Publish Date</div>
        <div>Close Date</div>
        <div>Display</div>
        <div>Action</div>
      </div>

      <div class="text-center items-center justify-center text-gray-400 mt-48 text-2xl" v-if="data === undefined">No Announcement</div>
      <div v-else>
        <div v-for="announce in data">
          <div class="grid grid-cols-7 bg-white mt-3"> 
            <div>{{ announce.announcementId }} </div>
            <div>{{ announce.announcementTitle }} </div>
            <div>{{ announce.categoriesObject.categoryName }}</div>
            <div>{{ announce.publishDate }} </div> 
            <div>{{ announce.closeDate }} </div>
            <diV>{{ announce.announcementDisplay}}</diV>

          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<style scoped></style>
