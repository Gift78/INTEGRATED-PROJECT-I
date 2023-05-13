<script setup>
import { ref, onMounted, watch } from 'vue';
import { useRouter } from 'vue-router';
import TimezoneComponent from '../base/TimezoneComponent.vue';
import Published from '../icons/Published.vue'
import Unpublished from '../icons/Unpublished.vue'
import { getDataByPage } from '../../composable/getData';
import formatDatetime from '../../composable/formatDatetime.js'
import { useMode } from '../../stores/mode';
import { storeToRefs } from 'pinia'

const router = useRouter()
const modeStore = useMode()
const { mode } = storeToRefs(modeStore)
const { toggleMode } = modeStore
const activeButton = ref('text-white bg-emerald-light')
const closedButton = ref('')
const data = ref([])

const currentPage = ref(0)

onMounted(async () => {
    data.value = await getDataByPage(mode.value, currentPage.value, 5);
});

watch(currentPage, async (newVal) => {
    data.value = await getDataByPage(mode.value, newVal, 5);
})


const changeMode = async (modeName) => {
    toggleMode(modeName)
    data.value = await getDataByPage(mode.value, currentPage.value, 5);
    if (mode.value == 'active') {
        activeButton.value = 'text-white bg-emerald-light'
        closedButton.value = ''
    } else if (mode.value == 'close') {
        closedButton.value = 'text-white bg-red-500'
        activeButton.value = ''
    }
}

const changePage = (name, id) => {
    if (id !== undefined) {
        router.push({ name: name, params: { id: id } })
    } else {
        router.push({ name: name })
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
                    <button class="p-1 px-5 text-black rounded-lg flex" @click="changeMode('active')" :class="activeButton">
                        <Published class="mr-2" />ACTIVE
                    </button>
                    <button class="p-1 px-5 text-black rounded-lg flex" @click="changeMode('close')" :class="closedButton">
                        <Unpublished class="mr-2" />CLOSED
                    </button>
                </div>
            </div>
            <hr class="mt-4 border-2">
            <!-- head table -->
            <div class="grid grid-cols-9 my-5">
                <div class="text-center text-zinc-400">No.</div>
                <div class=" text-zinc-400 indent-10" :class="mode == 'active' ? 'col-span-7' : 'col-span-5'">
                    Title</div>
                <div class=" text-zinc-400 col-span-2 text-center" v-if="mode == 'close'">Close Date</div>
                <div class="text-center text-zinc-400 ">Category</div>
            </div>
            <!-- content -->
            <div class="text-center items-center justify-center text-gray-400 mt-48 text-2xl"
                v-if="data === undefined || data.length === 0">No Announcement
            </div>
            <div v-else>
                <div v-for="(announce, index) in data.content" :key="data.id"
                    class="ann-item grid grid-cols-9 bg-white hover:bg-slate-100 my-5 py-7 h-20 rounded-xl shadow-md cursor-pointer"
                    @click="changePage('UserAnnouncementDetail', announce.id)">
                    <div class="text-center"> {{ index + 1 + (currentPage * 5) }}</div>
                    <div class="ann-title underline overflow-hidden"
                        :class="mode == 'active' ? 'col-span-7' : 'col-span-5'">
                        {{ announce.announcementTitle }}
                    </div>
                    <div class="ann-close-date col-span-2 text-center" v-if="mode == 'close'">{{
                        formatDatetime(announce.closeDate) }}</div>
                    <div class="ann-category text-center">{{ announce.announcementCategory }}</div>
                </div>
            </div>
            <!-- pagination -->
            <div class="w-full my-10 flex justify-start" v-if="data.totalElements > 5">
                <!-- v-if="data.totalElements < 5" -->
                <button class="pr-5 py-2 bg-" @click="currentPage != 0 ? currentPage-- : ''">&lt; Prev</button>
                <div v-for="(page, index) in data.totalPages" class="flex">
                    <button class="px-6" :class="currentPage == index ? 'bg-emerald-light text-white' : 'bg-zinc-300 hover:bg-zinc-200',
                        index == 0 ? 'rounded-l-lg' : '', index == data.totalPages - 1 ? 'rounded-r-lg' : ''"
                        @click="currentPage = index">{{ page }}
                    </button>
                </div>
                <button class="px-5 py-2" @click="currentPage < data.totalPages - 1 ? currentPage++ : ''">Next &gt;</button>
            </div>
        </div>
    </div>
</template>
 
<style scoped></style>