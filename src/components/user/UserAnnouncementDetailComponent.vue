<script setup>
import { useRoute, useRouter } from 'vue-router';
import { ref, onMounted } from 'vue';
import TimezoneComponent from '../base/TimezoneComponent.vue';
import Megaphone from '../icons/Megaphone.vue';
import formatDatetime from '../../composable/formatDatetime';
import ErrorModalComponent from '../base/ErrorModalComponent.vue';

const params = useRoute().params
const router = useRouter()
const data = ref([])
const isModalOpen = ref(false);
const errors = ref()
const isClosed = ref(false)

onMounted(async () => {
    try {
        const res = await fetch(import.meta.env.VITE_ROOT_API + "/api/announcements/" + params?.id)
        if (res.ok) {
            data.value = await res.json();
            checkCloseDate(data.value.closeDate)
        } else {
            data.value = await res.json();
            isModalOpen.value = true
            const errorData = data.value
            errors.value = errorData
        }
    } catch (error) {
        errors.value = error
    }
})

const checkCloseDate = (closeDate) => {
    if (closeDate === null) {
        isClosed.value = false
        return
    }

    const now = new Date()
    const close = new Date(closeDate)
    if (now.getTime() <= close.getTime()) {
        isClosed.value = false

    } else {
        isClosed.value = true
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
    <div style="width: 80em;" class="mx-auto">
        <div class="text-center text-3xl text-cyan-800 py-10">SIT Announcement System (SAS)</div>

        <TimezoneComponent />
        <hr class="mt-4 border-2">

        <div class="ann-item bg-white flex-col rounded-lg p-10 shadow-lg mt-5">
            <div class="flex ml-12">
                <div class="bg-gray-200 p-4 rounded-lg">
                    <Megaphone class="mr-2" />
                </div>
                <div :class="isClosed ? 'flex-col' : ''">
                    <div :class="isClosed ? 'flex' : 'flex-col'">
                        <div class="text-cyan-800 font-bold text-3xl ml-5">{{ data?.announcementTitle }}</div>
                        <div class="text-white font-bold text-base mt-2" :class="isClosed ? 'ml-2' : 'ml-5'">
                            <span class="bg-green-400 py-1 px-2 rounded-lg">
                                {{ data?.announcementCategory }}
                            </span>
                        </div>
                    </div>
                    <div v-if="isClosed" class="flex text-white font-bold text-sm ml-5 mt-2">
                        <span class="bg-red-400 py-1 px-2 rounded-lg">
                            Close on {{ formatDatetime(data?.closeDate) }}
                        </span>
                    </div>
                </div>
            </div>

            <hr class="mt-4 mx-12 border-2">

            <div class="mt-4">
                <div class="text-cyan-800 text-base ml-12 mt-2 indent-8">{{ data?.announcementDescription }}</div>
            </div>
        </div>

        <div class="flex justify-end mt-4">
            <button class="bg-cyan-800 text-white rounded-lg py-2 px-6" @click="changePage('UserAnnouncement')">
                Back
            </button>
        </div>
    </div>

    <ErrorModalComponent v-if="isModalOpen" :checkCondition="isModalOpen" :typeError="'problem'" :status="errors?.status"
        :message="errors?.message" goToPage="UserAnnouncement" />
</template>

<style scoped></style>