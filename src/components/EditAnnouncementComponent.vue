<script setup>
import { ref, onMounted, watch } from 'vue';
import { useRoute } from 'vue-router';
import { useRouter } from 'vue-router';
import { getDataById } from '../composable/getData';
import TimezoneComponent from './TimezoneComponent.vue';
import CancelRounded from './icons/CancelRounded.vue'

const { params } = useRoute();
const router = useRouter();
const data = ref({});
const isModalOpen = ref(false);
const isFieldEdit = ref(false);
const categoryItem = [
    { categoryId: 1, categoryName: 'ทั่วไป' },
    { categoryId: 2, categoryName: 'ทุนการศึกษา' },
    { categoryId: 3, categoryName: 'หางาน' },
    { categoryId: 4, categoryName: 'ฝึกงาน' }
]

const editedAnnounce = ref({
    announcementTitle: '',
    announcementDescription: '',
    publishDate: null,
    closeDate: null,
    announcementDisplay: 'N',
    categoryId: null,
    announcementCategory: ''
})

const changePage = (name, id) => {
    if (id !== undefined) {
        router.push({ name: name, params: { id: id } })
    } else {
        router.push({ name: name })
    }
}

const datetimeFormatter = (datetime) => {
    if (!datetime) {
        return null;
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

const datetimeFormatterISO = (dateString) => {
    if (!dateString) {
        return null;
    }

    const atIndex = dateString.search('at')
    const dateSliced = dateString.slice(0, atIndex)
    const timeSliced = dateString.slice(atIndex + 2, 30)
    const newDateString = dateSliced + ',' + timeSliced
    const dateObj = new Date(newDateString).toISOString().slice(0, -5) + "Z";
    return dateObj
}

watch(editedAnnounce, () => {
    if (editedAnnounce.value.closeDate === '') {
        editedAnnounce.value.closeDate = null
    }
    if (editedAnnounce.value.publishDate === '') {
        editedAnnounce.value.publishDate = null
    }

    if (editedAnnounce.value.announcementTitle !== data.value.announcementTitle ||
        editedAnnounce.value.announcementDescription !== data.value.announcementDescription ||
        editedAnnounce.value.publishDate !== datetimeFormatter(data.value.publishDate) ||
        editedAnnounce.value.closeDate !== datetimeFormatter(data.value.closeDate) ||
        editedAnnounce.value.announcementDisplay !== data.value.announcementDisplay ||
        editedAnnounce.value.announcementCategory !== data.value.announcementCategory) {
        isFieldEdit.value = true;
    } else {
        isFieldEdit.value = false;
    }
}, { deep: true })

onMounted(async () => {
    try {
        data.value = await getDataById(params?.id);

        if (data.value === undefined) {
            isModalOpen.value = true;
        }

        editedAnnounce.value.announcementTitle = data.value.announcementTitle
        editedAnnounce.value.announcementDescription = data.value.announcementDescription
        editedAnnounce.value.publishDate = datetimeFormatter(data.value.publishDate)
        editedAnnounce.value.closeDate = datetimeFormatter(data.value.closeDate)
        editedAnnounce.value.announcementDisplay = data.value.announcementDisplay
        editedAnnounce.value.announcementCategory = data.value.announcementCategory

        categoryItem.forEach((item) => {
            if (item.categoryName === editedAnnounce.value.announcementCategory) {
                editedAnnounce.value.categoryId = item.categoryId
            }
        })
    } catch (error) {
        console.log(error);
    }
});

const editAnnouncement = async (updateAnnounce, announceId) => {
    let foundMatchingCategory = false;
    for (const item of categoryItem) {
        if (item.categoryName === data.value.announcementCategory) {
            updateAnnounce.categoryId = item.categoryId;
            foundMatchingCategory = true;
            break;
        }
    }
    if (!foundMatchingCategory) {
        return;
    }

    updateAnnounce.closeDate = datetimeFormatterISO(updateAnnounce.closeDate)
    updateAnnounce.publishDate = datetimeFormatterISO(updateAnnounce.publishDate)

    try {
        console.log(updateAnnounce)
        const res = await fetch(import.meta.env.VITE_ROOT_API + "/api/announcements/" + announceId,
            {
                method: 'PUT',
                headers: {
                    'content-type': 'application/json'
                },
                body: JSON.stringify({
                    announcementTitle: updateAnnounce.announcementTitle,
                    announcementDescription: updateAnnounce.announcementDescription,
                    publishDate: updateAnnounce.publishDate,
                    closeDate: updateAnnounce.closeDate,
                    announcementDisplay: updateAnnounce.announcementDisplay,
                    categoryId: updateAnnounce.categoryId
                })
            })
        if (res.status === 200) {
            changePage('AnnouncementDetail', params?.id)
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
                <div class="w-52 text-cyan-800 font-bold pt-2">Title</div>
                <input type="text" class="ann-title h-10 w-full bg-slate-100 rounded-lg pl-4 border"
                    v-model="editedAnnounce.announcementTitle" />
            </div>
            <div class="flex mt-5">
                <div class="w-52 text-cyan-800 font-bold pt-2">Category</div>
                <input type="text" class="ann-category h-10 w-full bg-slate-100 rounded-lg pl-4 border"
                    v-model="editedAnnounce.announcementCategory" />
            </div>
            <div class="flex mt-5">
                <div class="w-52 text-cyan-800 font-bold pt-2">Description</div>
                <textarea class="ann-description pt-2 w-full bg-slate-100 rounded-lg pl-4 border" rows="5"
                    v-model="editedAnnounce.announcementDescription"></textarea>
            </div>
            <div class="flex mt-5">
                <div class="w-52 text-cyan-800 font-bold pt-2">Publish Date</div>
                <input v-model="editedAnnounce.publishDate" type="text"
                    class="ann-publish-date ann-publish-time h-10 w-full bg-slate-100 rounded-lg pl-4 border"
                    placeholder="e.g : 1 jan 2023 at 06:00" />
            </div>
            <div class=" flex mt-5">
                <div class="w-52 text-cyan-800 font-bold pt-2">Close Date</div>
                <input v-model="editedAnnounce.closeDate" type="text"
                    class="ann-close-date ann-close-time h-10 w-full bg-slate-100 rounded-lg pl-4 border"
                    placeholder="e.g : 1 jan 2023 at 06:00" />
            </div>
            <div class="flex mt-5">
                <div class="w-52 text-cyan-800 font-bold pt-2">Display</div>
                <input v-model="editedAnnounce.announcementDisplay" type="text"
                    class="ann-display h-10 w-full bg-slate-100 rounded-lg pl-4 border" />
            </div>
        </div>

        <!-- button -->
        <div class="flex justify-end mt-3 space-x-3 " v-if="!isModalOpen">
            <button
                class="ann-button text-black bg-slate-100 text-center rounded-lg shadow-md cursor-pointer px-5 py-2 w-20 h-10"
                @click="changePage('AnnouncementDetail', params?.id)">Back</button>

            <button class="ann-button text-white bg-emerald-plus text-center rounded-lg shadow-md px-5 py-2 w-20 h-10"
                :class="{ 'opacity-50 cursor-not-allowed': !isFieldEdit, 'cursor-pointer': isFieldEdit }"
                :disabled="!isFieldEdit" @click="editAnnouncement(editedAnnounce, params?.id)">Edit</button>
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
                    @click="changePage('AnnouncementDetail', params?.id)">OK</label>
            </div>
        </div>
    </div>
</template>
 
<style scoped></style>
