<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getAllData } from '../composable/getData.js';
import formatDatetime from '../composable/formatDatetime';
import TimezoneComponent from './TimezoneComponent.vue';

const router = useRouter()
const data = ref([])

const gotoDetail = (id) => {
    router.push({ name: 'AnnouncementDetail', params: { id: id } })
}

onMounted(async () => {
    data.value = await getAllData();
});
</script>

<template>
    <div>
        <div style="width: 80em;" class="mx-auto">

            <!-- header -->
            <div class="text-center text-3xl text-cyan-800 py-10">SIT Announcement System (SAS)</div>

            <!-- time zone bar -->
            <TimezoneComponent />

            <!-- head table -->
            <div class="grid grid-cols-10 my-5">
                <div class="text-center text-zinc-400">No.</div>
                <div class=" text-zinc-400 col-span-2">Title</div>
                <div class="text-center text-zinc-400">Category</div>
                <div class="text-center text-zinc-400 col-span-2">Publish Date</div>
                <div class="text-center text-zinc-400 col-span-2">Close Date</div>
                <div class="text-center text-zinc-400">Display</div>
                <div class="text-center text-zinc-400">Action</div>
            </div>

            <div class="text-center items-center justify-center text-gray-400 mt-48 text-2xl"
                v-if="data === undefined || data.length === 0">
                No Announcements
            </div>
            <div v-else>
                <!-- show data -->
                <div v-for="(announce, index) in data" :key="data.id"
                    class="ann-item grid grid-cols-10 bg-white my-5 h-20 rounded-xl shadow-md">
                    <div class="text-cyan-800 my-auto text-center">{{ index + 1 }} </div>
                    <div class="ann-title text-cyan-800 my-auto col-span-2">{{ announce.announcementTitle }} </div>
                    <div class="ann-category text-cyan-800 my-auto text-center capitalize">{{
                        announce.announcementCategory }}</div>
                    <div class="ann-publish-date text-cyan-800 my-auto text-center col-span-2">{{
                        formatDatetime(announce.publishDate) || '-'
                    }} </div>
                    <div class="ann-close-date text-cyan-800 my-auto text-center col-span-2">{{
                        formatDatetime(announce.closeDate) || '-'
                    }} </div>
                    <div class="ann-display text-cyan-800 pt-2 my-auto mx-auto text-center w-10 h-10 rounded-full"
                        :class="announce.announcementDisplay == 'Y' ? 'bg-emerald-100 text-emerald-400' : 'bg-red-100 text-red-400'">
                        {{ announce.announcementDisplay }}
                    </div>
                    <div class="ann-button text-cyan-400 my-auto text-center mx-auto bg-cyan-100 rounded-lg pt-2 w-20 h-10 shadow-md cursor-pointer"
                        @click="gotoDetail(announce.id)">view
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped></style>
