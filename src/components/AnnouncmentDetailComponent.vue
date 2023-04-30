<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getDataById } from '../composable/getData';
import { useRouter } from 'vue-router'
import TimezoneComponent from './TimezoneComponent.vue';
import formatDatetime from '../composable/formatDatetime';

const router = useRouter()
const { params } = useRoute()
const data = ref({})

const backToAnnouncements = () => {
    router.push({ name: 'Announcement' })
}

onMounted(async () => {
    data.value = await getDataById(parseInt(params.id))
})
</script>
 
<template>
    <div style="width: 80em;" class="mx-auto">
        <!-- header -->
        <h1 class="text-cyan-800 text-3xl py-10">Announcement Detail</h1>

        <TimezoneComponent />

        <!-- content -->
        <div class="bg-white flex-col rounded-lg p-10 shadow-lg mt-5">
            <div class="flex">
                <div class="w-52 text-cyan-800 font-bold">Title</div>
                <div class="text-cyan-800 w-full">{{ data.announcementTitle }}</div>
            </div>
            <div class="flex mt-5">
                <div class="w-52 text-cyan-800 font-bold">Category</div>
                <div class="text-cyan-800 w-full">{{ data.announcementCategory }}</div>
            </div>
            <div class="flex mt-5">
                <div class="w-52 text-cyan-800 font-bold">Description</div>
                <div class="text-cyan-800 w-full">{{ data.announcementDescription }}</div>
            </div>
            <div class="flex mt-5">
                <div class="w-52 text-cyan-800 font-bold">Publish Date</div>
                <div class="text-cyan-800 w-full">{{ formatDatetime(data.publishDate) || '-' }}</div>
            </div>
            <div class="flex mt-5">
                <div class="w-52 text-cyan-800 font-bold">Close Date</div>
                <div class="text-cyan-800 w-full">{{ formatDatetime(data.closeDate) || '-' }}</div>
            </div>
            <div class="flex mt-5">
                <div class="w-52 text-cyan-800 font-bold">Display</div>
                <div class="text-cyan-800 w-full">{{ data.announcementDisplay }}</div>
            </div>
        </div>

        <!-- button -->
        <div class="flex justify-end mt-3">
            <button class="text-cyan-400 bg-cyan-100 text-center rounded-lg shadow-md cursor-pointer px-5 py-2 w-20 h-10"
                @click="backToAnnouncements">Back</button>
        </div>
    </div>
</template>
 
<style scoped></style>