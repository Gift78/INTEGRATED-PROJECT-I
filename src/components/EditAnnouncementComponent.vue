<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { useRouter } from 'vue-router';
import { getDataById } from '../composable/getData';
import formatDatetime from '../composable/formatDatetime';
import TimezoneComponent from './TimezoneComponent.vue';
import CancelRounded from './icons/CancelRounded.vue'

const { params } = useRoute();
const router = useRouter();
const isModalOpen = ref(false);
const data = ref({});

const backToAnnouncements = () => {
    router.push({ name: 'Announcement' });
}

onMounted(async () => {
    try {
        data.value = await getDataById(params?.id);
        data.value.publishDate = datetimeFormatter(data.value.publishDate)
        data.value.closeDate = datetimeFormatter(data.value.closeDate)
        console.log(data.value)
        if (data.value === undefined) {
            isModalOpen.value = true;
        }
    } catch (error) {
        console.log(error);
    }
});

const findCategoryName = (categoryName) => {
    if (categoryName === 'ทั่วไป') {
        return 1
    } else if (categoryName === 'ทุนการศึกษา') {
        return 2
    } else if (categoryName === 'หางาน') {
        return 3
    } else if (categoryName === 'ฝึกงาน') {
        return 4
    }
}

const datetimeFormatter = (datetime) => {
    if (!datetime) {
        return '';
    }

    const date = new Date(datetime);
    const year = date.getFullYear();
    const month = date.toLocaleString('default', { month: 'short' });
    const day = date.getDate();
    const hour = date.getHours();
    const minute = date.getMinutes();

    if (minute < 10) {
        return `${day} ${month} ${year} at ${hour}:0${minute}`;
    } else {
        return `${day} ${month} ${year} at ${hour}:${minute}`;
    }
}

const datetimeFormatIso = (dateString) => {
    const dateObj = new Date(dateString);
    const isoString = dateObj.toISOString().slice(0, -5) + "Z";
    console.log(isoString)
    return isoString
}

const editAnnouncement = async (updateAnnounce, announceId) => {
    try {
        const res = await fetch(import.meta.env.VITE_ROOT_API + "/api/announcements/" + announceId,
            {
                method: 'PUT',
                headers: {
                    'content-type': 'application/json'
                },
                body: JSON.stringify({
                    announcementTitle: updateAnnounce.announcementTitle,
                    announcementDescription: updateAnnounce.announcementDescription,
                    publishDate: datetimeFormatIso(updateAnnounce.publishDate),
                    closeDate: datetimeFormatIso(updateAnnounce.closeDate),
                    announcementDisplay: updateAnnounce.announcementDisplay,
                    categoryId: findCategoryName(updateAnnounce.announcementCategory)

                })
            })
        if (res.status === 200) {
            backToAnnouncements()
            console.log('edit')
        } else {
            throw new Error('cannot edit')
        }
    }
    catch (err) {
        console.log(err)
    }
}
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
                <input v-model="data.announcementTitle" type="text" class="h-10 w-full bg-slate-100 rounded-lg pl-4" />

            </div>
            <div class="flex mt-5">
                <div class="w-52 text-cyan-800 font-bold">Category</div>
                <input type="text" class="h-10 w-full bg-slate-100 rounded-lg pl-4" v-model="data.announcementCategory" />
            </div>
            <div class="flex mt-5">
                <div class="w-52 text-cyan-800 font-bold">Description</div>
                <textarea v-model="data.announcementDescription"
                    class="textarea h-10 w-full bg-slate-100 rounded-lg pl-4"></textarea>
            </div>
            <div class="flex mt-5">
                <div class="w-52 text-cyan-800 font-bold">Publish Date</div>
                <input v-model="data.publishDate" type="text" class="h-10 w-full bg-slate-100 rounded- pl-4" />
            </div>
            <div class="flex mt-5">
                <div class="w-52 text-cyan-800 font-bold">Close Date</div>
                <input v-model="data.closeDate" type="text" class="h-10 w-full bg-slate-100 rounded-lg pl-4" />
            </div>
            <div class="flex mt-5">
                <div class="w-52 text-cyan-800 font-bold">Display</div>
                <input v-model="data.announcementDisplay" type="text" class="h-10 w-full bg-slate-100 rounded-lg pl-4" />
            </div>
        </div>

        <!-- button -->
        <div class="flex justify-end mt-3 space-x-3 " v-if="!isModalOpen">
            <button
                class="ann-button text-black bg-slate-100 text-center rounded-lg shadow-md cursor-pointer px-5 py-2 w-20 h-10"
                @click="backToAnnouncements">Back</button>

            <button
                class="ann-button text-white bg-emerald-plus text-center rounded-lg shadow-md cursor-pointer px-5 py-2 w-20 h-10 "
                @click="editAnnouncement(data, params?.id)">Edit</button>
        </div>
    </div>

    <input type="checkbox" id="error-modal" class="modal-toggle modal-open" :checked="isModalOpen" />
    <div class="modal flex items-center justify-center" v-if="isModalOpen">
        <CancelRounded class="text-red-500 absolute top-48 z-30 flex items-center justify-center bg-white rounded-full" />
        <div class="absolute top-72 bg-white px-40 pb-10 z-10 rounded-3xl shadow-xl">
            <p class="text-3xl font-bold text-center pt-16">Error!</p>
            <p class="py-5 text-center">The requested page is not available!</p>
            <div class="modal-action flex justify-center">
                <label class="btn text-white border-none w-24 bg-red-500 hover:bg-red-700"
                    @click="backToAnnouncements()">OK</label>
            </div>
        </div>
    </div>
</template>
 
<style scoped></style>
