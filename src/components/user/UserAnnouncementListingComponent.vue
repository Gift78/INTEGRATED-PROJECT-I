<script setup>
import { ref, onMounted } from 'vue';
import TimezoneComponent from '../base/TimezoneComponent.vue';
import Published from '../icons/Published.vue'
import Unpublished from '../icons/Unpublished.vue'
import { getAllData } from '../../composable/getData';
import formatDatetime from '../../composable/formatDatetime.js'

const mode = ref('active')
const activeButton = ref('text-white bg-emerald-light')
const closedButton = ref('')
const data = ref([])

onMounted(async () => {
    data.value = await getAllData(mode.value);
});

const button = async (modeName) => {
    mode.value = modeName
    data.value = await getAllData(mode.value);
    if (mode.value == 'active') {
        activeButton.value = 'text-white bg-emerald-light'
        closedButton.value = ''
    } else if (mode.value == 'close') {
        closedButton.value = 'text-white bg-red-500'
        activeButton.value = ''
    }
}
</script>
 
<template>
    <div class="text-cyan-800">
        <div style="width: 80em;" class="mx-auto">
            <!-- header -->
            <div class="text-center text-3xl text-cyan-800 py-10">SIT Announcement System (SAS)</div>
            <!-- time zone bar -->
            <div class="flex justify-between">
                <TimezoneComponent />
                <div class="flex bg-white p-1 rounded-xl mx-1 ">
                    <button class="p-1 px-5 text-black rounded-lg flex" @click="button('active')" :class="activeButton">
                        <Published class="mr-2" />ACTIVE
                    </button>
                    <button class="p-1 px-5 text-black rounded-lg flex" @click="button('close')" :class="closedButton">
                        <Unpublished class="mr-2" />CLOSED
                    </button>
                </div>
            </div>
            <hr class="mt-4 border-2">
            <!-- head table -->
            <div class="grid grid-cols-9 my-5">
                <div class="text-center text-zinc-400">No.</div>
                <div class=" text-zinc-400 indent-10" :class="mode == 'active' ? 'col-span-7' : 'col-span-5'">Title</div>
                <div class=" text-zinc-400 col-span-2 text-center" v-if="mode == 'close'">Close Date</div>
                <div class="text-center text-zinc-400 ">Category</div>
            </div>
            <!-- content -->
            <div class="text-center items-center justify-center text-gray-400 mt-48 text-2xl"
                v-if="data === undefined || data.length === 0">No Announcement
            </div>
            <div v-else>
                <div v-for="(announce, index) in data" :key="data.id"
                    class="grid grid-cols-9 bg-white my-5 py-7 h-20 rounded-xl shadow-md">
                    <div class="text-center "> {{ index + 1 }}</div>
                    <div :class="mode == 'active' ? 'col-span-7' : 'col-span-5'">{{ announce.announcementTitle }}</div>
                    <div class="col-span-2 text-center" v-if="mode == 'close'">{{
                        formatDatetime(announce.closeDate) }}</div>
                    <div class="text-center">{{ announce.announcementCategory }}</div>
                </div>
            </div>
        </div>
    </div>
</template>
 
<style scoped></style>