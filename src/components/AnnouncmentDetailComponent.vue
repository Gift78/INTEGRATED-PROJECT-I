<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { useRouter } from 'vue-router';
import { getDataById } from '../composable/getData';
import formatDatetime from '../composable/formatDatetime';
import TimezoneComponent from './TimezoneComponent.vue';

const { params } = useRoute();
const router = useRouter();
const data = ref({});
const isModalOpen = ref(false);

const backToAnnouncements = () => {
    router.push({ name: 'Announcement' });
}

onMounted(async () => {
    try {
        data.value = await getDataById(params?.id);

        if (data.value === undefined) {
            isModalOpen.value = true;
        }
    } catch (error) {
        console.log(error);
    }
});
</script>
 
<template>
    <div style="width: 80em;" class="mx-auto">
        <!-- header -->
        <h1 class="text-cyan-800 text-3xl py-10">Announcement Detail</h1>

        <TimezoneComponent />
        <hr class="mt-4 border-2">


        <!-- content -->
        <div class="ann-item bg-white flex-col rounded-lg p-10 shadow-lg mt-5" v-if="!isModalOpen">
            <div class="flex">
                <div class="w-52 text-cyan-800 font-bold">Title</div>
                <div class="ann-title text-cyan-800 w-full">{{ data?.announcementTitle }}</div>
            </div>
            <div class="flex mt-5">
                <div class="w-52 text-cyan-800 font-bold">Category</div>
                <div class="ann-category text-cyan-800 w-full">{{ data?.announcementCategory }}</div>
            </div>
            <div class="flex mt-5">
                <div class="w-52 text-cyan-800 font-bold">Description</div>
                <div class="ann-description text-cyan-800 w-full">{{ data?.announcementDescription }}</div>
            </div>
            <div class="flex mt-5">
                <div class="w-52 text-cyan-800 font-bold">Publish Date</div>
                <div class="ann-publish-date text-cyan-800 w-full">{{ formatDatetime(data?.publishDate) || '-' }}</div>
            </div>
            <div class="flex mt-5">
                <div class="w-52 text-cyan-800 font-bold">Close Date</div>
                <div class="ann-close-date text-cyan-800 w-full">{{ formatDatetime(data?.closeDate) || '-' }}</div>
            </div>
            <div class="flex mt-5">
                <div class="w-52 text-cyan-800 font-bold">Display</div>
                <div class="ann-display text-cyan-800 w-full">{{ data?.announcementDisplay }}</div>
            </div>
        </div>

        <!-- button -->
        <div class="flex justify-start mt-3" v-if="!isModalOpen">
            <button
                class="ann-button text-cyan-400 bg-cyan-100 text-center rounded-lg shadow-md cursor-pointer px-5 py-2 w-20 h-10"
                @click="backToAnnouncements">Back</button>
        </div>
    </div>

    <input type="checkbox" id="error-modal" class="modal-toggle modal-open" :checked="isModalOpen" />
    <div class="modal">
        <div class="modal-box bg-white">
            <h3 class="font-bold text-2xl text-black">Error!</h3>
            <p class="pt-4 text-black">The requested page is not available!</p>
            <div class="modal-action">
                <label for="error-modal" class="btn text-black bg-white hover:bg-gray-200"
                    @click="backToAnnouncements">OK</label>
            </div>
        </div>
    </div>
</template>
 
<style scoped></style>