<script setup>
import PhGlobe from './icons/PhGlobe.vue';
import AnnouncmentDetailComponent from './AnnouncmentDetailComponent.vue';
import { ref, onMounted } from 'vue'
import { getData } from '../composable/getData.js';

const timezone = Intl.DateTimeFormat().resolvedOptions().timeZone;
const data = ref([])
const detailSelect = ref(undefined)
const currentPage = ref('announcePage')
const setPage = (page, detail) => {
    currentPage.value = page
    detailSelect.value = detail
}

onMounted(async () => {
    data.value = await getData();
    console.log(data.value)
});
</script>

<template>
    <div>
        <div style="width: 80em;" class="mx-auto">

            <!-- header -->
            <div class="text-center text-3xl text-cyan-800 py-10">SIT Announcement System(SAS)</div>

            <!-- time zone bar -->
            <div class="flex">
                <div class="flex p-1 pr-3 rounded-lg" style="background-color: #12C980;">
                    <PhGlobe class="pt-1" />
                    <div class="text-white text-lg">Time zone :</div>
                </div>
                <div class="text-cyan-800 p-1 pl-3 text-lg">{{ timezone }}</div>
            </div>

            <hr class="mt-4 border-2">

            <div v-if="currentPage === 'announcePage'">
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

                <div class="text-center items-center justify-center text-gray-400 mt-48 text-2xl" v-if="data === undefined">
                    No
                    Announcement</div>
                <div v-else>
                    <!-- show data -->
                    <div v-for="(announce, index) in data" :key="data.announcementId"
                        class="grid grid-cols-10 bg-white my-5 h-20 rounded-xl shadow-md">
                        <div class="text-cyan-800 my-auto text-center">{{ index + 1 }} </div>
                        <div class="text-cyan-800 my-auto col-span-2">{{ announce.announcementTitle }} </div>
                        <div class="text-cyan-800 my-auto text-center capitalize">{{ announce.categoriesObject.categoryName
                        }}</div>
                        <div class="text-cyan-800 my-auto text-center col-span-2">{{ announce.publishDate || '-' }} </div>
                        <div class="text-cyan-800 my-auto text-center col-span-2">{{ announce.closeDate || '-' }} </div>
                        <div class="text-cyan-800 pt-2 my-auto mx-auto text-center w-10 h-10 rounded-full"
                            :class="announce.announcementDisplay == 'Y' ? 'bg-emerald-100 text-emerald-400' : 'bg-red-100 text-red-400'">
                            {{ announce.announcementDisplay }}
                        </div>
                        <div class="text-cyan-400 my-auto text-center mx-auto bg-cyan-100 rounded-lg pt-2 w-20 h-10 shadow-md cursor-pointer"
                            @click="setPage('detailPage', announce)">View
                        </div>
                    </div>
                </div>
            </div>
            <div v-if="currentPage === 'detailPage'">
                <AnnouncmentDetailComponent :detail="detailSelect" />
                <div class="  mt-10 flex justify-end">
                    <div class="text-cyan-600 text-center rounded-xl p-5 bg-cyan-200 w-28 shadow-md cursor-pointer"
                        @click="setPage('announcePage')">Back</div>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped></style>
