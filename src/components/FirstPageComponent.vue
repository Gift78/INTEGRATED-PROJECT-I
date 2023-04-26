<script setup>
import { ref, onMounted } from 'vue';
import { getData } from '../composable/getData.js';


const data = ref()
onMounted(async () => {
    data.value = await getData();

    console.log(data.value)
});
</script>
 
<template>
    <div>
        <!-- head table -->
        <div class="grid grid-cols-10 my-7">
            <div class="text-center text-zinc-400">No.</div>
            <div class=" text-zinc-400 col-span-2">Title</div>
            <div class="text-center text-zinc-400">Category</div>
            <div class="text-center text-zinc-400 col-span-2">Publish Date</div>
            <div class="text-center text-zinc-400 col-span-2">Close Date</div>
            <div class="text-center text-zinc-400">Display</div>
            <div class="text-center text-zinc-400">Action</div>
        </div>

        <div class="text-center items-center justify-center text-gray-400 mt-48 text-2xl" v-if="data === undefined">No
            Announcement</div>
        <div v-else>
            <div v-for="(announce, index) in data" :key="data.announcementId"
                class="grid grid-cols-10 bg-white my-5 h-20 rounded-xl shadow-md">
                <div class="text-cyan-800 my-auto text-center">{{ index + 1 }} </div>
                <div class="text-cyan-800 my-auto col-span-2">{{ announce.announcementTitle }} </div>
                <div class="text-cyan-800 my-auto text-center capitalize">{{ announce.categoriesObject.categoryName }}</div>
                <div class="text-cyan-800 my-auto text-center col-span-2">{{ announce.publishDate }} </div>
                <div class="text-cyan-800 my-auto text-center col-span-2">{{ announce.closeDate }} </div>
                <diV class="text-cyan-800 pt-2 my-auto mx-auto text-center w-10 h-10 rounded-full"
                    :class="announce.announcementDisplay == 'Y' ? 'bg-emerald-100 text-emerald-400' : 'bg-red-100 text-red-400'">
                    {{
                        announce.announcementDisplay }}
                </diV>
                <div class="text-cyan-400 my-auto text-center mx-auto bg-cyan-100 rounded-lg pt-2 w-20 h-10 shadow-md">
                    View
                </div>
            </div>
        </div>
    </div>
</template>
 
<style scoped></style>