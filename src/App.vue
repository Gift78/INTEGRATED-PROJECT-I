<script setup>
import PhGlobe from './components/icons/PhGlobe.vue';
import FirstPageComponant from './components/FirstPageComponent.vue'


const timezone = Intl.DateTimeFormat().resolvedOptions().timeZone;


</script>

<template>
  <div class=" h-screen flex-auto" style="background-color: #F3F5F7; ">
    <div style="width: 80em;" class="mx-auto">

      <!-- header -->
      <div class="text-center text-3xl text-cyan-800 py-10">SIT Announcement System(SAS)</div>

      <!-- time zone bar -->
      <div class="flex">
        <div class="bg-emerald-500 flex p-1 pr-3 rounded-lg">
          <PhGlobe class="pt-1" />
          <div class="text-white text-lg">Time zone :</div>
        </div>
        <div class="text-cyan-800 p-1 pl-3 text-lg">{{ timezone }}</div>
      </div>

      <hr class="mt-4 border-2">

      <FirstPageComponant />
      
    </div>

  </div>
</template>

<style scoped></style>
