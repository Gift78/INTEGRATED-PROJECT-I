<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getAllData } from '../../composable/getData.js';
import formatDatetime from '../../composable/formatDatetime';
import TimezoneComponent from '../base/TimezoneComponent.vue';
import ErrorModalComponent from '../base/ErrorModalComponent.vue';

const router = useRouter()
const data = ref([])
const errors = ref({})
const deleteId = ref(undefined)
const confirmDelete = ref(false)
const errorOnDelete = ref(false)

onMounted(async () => {
    data.value = await getAllData('admin');
});

const changePage = (name, id) => {
    if (id !== undefined) {
        router.push({ name: name, params: { id: id } })
    } else {
        router.push({ name: name })
    }
}

const deleteAnnouncement = async (id) => {
    try {
        const res = await fetch(import.meta.env.VITE_ROOT_API + "/api/announcements/" + id, {
            method: 'DELETE'
        })
        if (res.status === 200) {
            data.value = data.value.filter((ann) => {
                return ann.id !== id
            })
            console.log('delete successfully')
        } else {
            const errorData = await res.json()
            errors.value = errorData
            errorOnDelete.value = true
        }
    } catch (error) {
        errorOnDelete.value = true
        errors.value = error
    }
}
</script>

<template>
    <div style="width: 80em;" class="mx-auto">
        <!-- header -->
        <div class="text-center text-3xl text-cyan-800 py-10">SIT Announcement System (SAS)</div>

        <!-- time zone bar -->
        <div class="flex justify-between">
            <TimezoneComponent />
            <div class="ann-button text-center rounded-md text-white p-1 px-4 bg-emerald-plus hover:bg-emerald-light hover:scale-105 cursor-pointer"
                @click="changePage('AddAnnouncement', undefined)">
                + Add Announcement
            </div>

        </div>
        <hr class="mt-4 border-2">

        <!-- head table -->
        <div class="grid grid-cols-11 my-5">
            <div class="text-center text-zinc-400">No.</div>
            <div class=" text-zinc-400 col-span-2">Title</div>
            <div class="text-center text-zinc-400">Category</div>
            <div class="text-center text-zinc-400 col-span-2">Publish Date</div>
            <div class="text-center text-zinc-400 col-span-2">Close Date</div>
            <div class="text-center text-zinc-400">Display</div>
            <div class="text-center text-zinc-400 col-span-2">Action</div>
        </div>

        <div class="text-center items-center justify-center text-gray-400 mt-48 text-2xl"
            v-if="data === undefined || data.length === 0">
            No Announcement
        </div>
        <div v-else>
            <!-- show data -->
            <div v-for="(announce, index) in data" :key="data.id"
                class="ann-item grid grid-cols-11 bg-white my-5 h-20 rounded-xl shadow-md">
                <div class="text-cyan-800 my-auto text-center">{{ index + 1 }} </div>
                <div class="ann-title text-cyan-800 my-auto col-span-2 overflow-hidden">{{ announce.announcementTitle }}
                </div>
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
                <div class="flex ml-10 col-span-2">
                    <div class="ann-button mx-2 text-cyan-400 my-auto text-center bg-cyan-100 hover:bg-cyan-200 hover:scale-110  rounded-lg pt-2 w-16 h-10 shadow-sm cursor-pointer"
                        @click="changePage('AnnouncementDetail', announce.id)">View
                    </div>
                    <!-- delete -->
                    <button
                        class="ann-button mx-2 text-red-400 my-auto text-center bg-red-100 hover:bg-red-200 hover:scale-110  rounded-lg w-16 h-10 shadow-sm cursor-pointer"
                        @click="deleteId = announce.id, confirmDelete = true">Delete</button>
                </div>
            </div>
        </div>
        <!-- delete modal -->
        <ErrorModalComponent :checkCondition="confirmDelete" :typeError="'delete'" :method="deleteAnnouncement"
            :parameter="deleteId" @close="confirmDelete = false" />

        <!-- error when delete modal -->
        <ErrorModalComponent v-if="errorOnDelete" :checkCondition="errorOnDelete" :typeError="'problem'"
            :status="errors?.status" :message="errors?.message" />
    </div>
</template>

<style scoped></style>
