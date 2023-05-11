<script setup>
import { ref, onMounted, watch } from 'vue';
import { useRoute } from 'vue-router';
import { useRouter } from 'vue-router';
import { getAllCategories, getDataById } from '../../composable/getData';
import TimezoneComponent from '../base/TimezoneComponent.vue';

const { params } = useRoute();
const router = useRouter();
const data = ref({});
const isModalOpen = ref(false);
const isFieldEdit = ref(false);
const publishDate = ref('')
const publishTime = ref('')
const closeDate = ref('')
const closeTime = ref('')
const display = ref(false)

const categoryItem = ref({})

const editedAnnounce = ref({
    announcementTitle: '',
    announcementDescription: '',
    publishDate: null,
    closeDate: null,
    announcementDisplay: 'N',
    categoryId: null,
    announcementCategory: ''
})

onMounted(async () => {
    try {
        categoryItem.value = await getAllCategories();
        data.value = await getDataById(params?.id);

        if (data.value === undefined) {
            isModalOpen.value = true;
        }

        if (data.value.publishDate !== null) {
            publishDate.value = getFormattedDate(data.value.publishDate)
            publishTime.value = getFormattedTime(data.value.publishDate)
        }

        if (data.value.closeDate !== null) {
            closeDate.value = getFormattedDate(data.value.closeDate)
            closeTime.value = getFormattedTime(data.value.closeDate)
        }

        editedAnnounce.value.announcementTitle = data.value.announcementTitle
        editedAnnounce.value.announcementDescription = data.value.announcementDescription
        editedAnnounce.value.publishDate = data.value.publishDate
        editedAnnounce.value.closeDate = data.value.closeDate
        editedAnnounce.value.announcementDisplay = data.value.announcementDisplay
        editedAnnounce.value.announcementCategory = data.value.announcementCategory

        if (editedAnnounce.value.announcementDisplay === 'Y') {
            display.value = true
        } else {
            display.value = false
        }

        categoryItem.value.forEach((item) => {
            if (item.categoryName === editedAnnounce.value.announcementCategory) {
                editedAnnounce.value.categoryId = item.categoryId
            }
        })
    } catch (error) {
        console.log(error);
    }
});

const changePage = (name, id) => {
    if (id !== undefined) {
        router.push({ name: name, params: { id: id } })
    } else {
        router.push({ name: name })
    }
}

const datetimeFormatterISO = (date, time) => {
    if (!date || !time) {
        return null;
    }
    const includeDateTime = date + ' , ' + time

    const dateObj = new Date(includeDateTime).toISOString().slice(0, -5) + "Z";
    return dateObj
}

const getFormattedDate = (date) => {
    if (date === null || date === undefined || date === '' || date === 'Invalid Date') {
        return '';
    }

    date = new Date(date)

    return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`
}

const getFormattedTime = (date) => {
    if (date === null || date === undefined || date === '' || date === 'Invalid Date') {
        return '';
    }

    date = new Date(date)

    return `${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`
}

watch([editedAnnounce, display, publishDate, publishTime, closeDate, closeTime], () => {
    editedAnnounce.value.announcementCategory = categoryItem.value.find(item => item.categoryId === editedAnnounce.value.categoryId)?.categoryName

    if (display.value === true) {
        editedAnnounce.value.announcementDisplay = 'Y'
    } else {
        editedAnnounce.value.announcementDisplay = 'N'
    }

    if (editedAnnounce.value.announcementTitle !== data.value.announcementTitle ||
        editedAnnounce.value.announcementDescription !== data.value.announcementDescription ||
        editedAnnounce.value.publishDate !== data.value.publishDate ||
        editedAnnounce.value.closeDate !== data.value.closeDate ||
        editedAnnounce.value.announcementDisplay !== data.value.announcementDisplay ||
        editedAnnounce.value.announcementCategory !== data.value.announcementCategory ||
        publishDate.value !== getFormattedDate(editedAnnounce.value.publishDate) ||
        publishTime.value !== getFormattedTime(editedAnnounce.value.publishDate) ||
        closeDate.value !== getFormattedDate(editedAnnounce.value.closeDate) ||
        closeTime.value !== getFormattedTime(editedAnnounce.value.closeDate)) {
        isFieldEdit.value = true;
    } else {
        isFieldEdit.value = false;
    }
}, { deep: true })

const editAnnouncement = async (updateAnnounce, announceId) => {
    updateAnnounce.publishDate = datetimeFormatterISO(publishDate.value, publishTime.value)
    updateAnnounce.closeDate = datetimeFormatterISO(closeDate.value, closeTime.value)

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
                    publishDate: updateAnnounce.publishDate,
                    closeDate: updateAnnounce.closeDate,
                    announcementDisplay: updateAnnounce.announcementDisplay,
                    categoryId: updateAnnounce.categoryId
                })
            })
        if (res.status === 200) {
            changePage('AnnouncementDetail', params?.id)
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
                <div class="w-44 text-cyan-800 font-bold pt-3">Category</div>

                <!-- dropdown button -->
                <select class="ann-category select select-bordered bg-slate-100" v-model="editedAnnounce.categoryId">
                    <option v-for="category in categoryItem" :value="category.categoryId">
                        {{ category.categoryName }}
                    </option>
                </select>
            </div>
            <div class="flex mt-5">
                <div class="w-52 text-cyan-800 font-bold pt-2">Description</div>
                <textarea class="ann-description pt-2 w-full bg-slate-100 rounded-lg pl-4 border" rows="5"
                    v-model="editedAnnounce.announcementDescription"></textarea>
            </div>

            <!-- new date & time input -->
            <div class="flex mt-5">
                <div class="text-cyan-800 w-40 py-3 font-bold">Publish Date</div>
                <div class="flex ml-1">
                    <input type="date" class="ann-publish-date bg-white mx-3 border-2 rounded-lg px-10 py-2"
                        v-model="publishDate">
                    <input type="time" class="ann-publish-time bg-white mx-3 border-2 rounded-lg px-10 py-2"
                        v-model="publishTime">
                </div>
            </div>
            <div class="flex mt-5">
                <div class="text-cyan-800 w-40 py-3 font-bold">Close Date</div>
                <div class="flex ml-1">
                    <input type="date" class="ann-close-date bg-white mx-3 border-2 rounded-lg px-10 py-2"
                        v-model="closeDate">
                    <input type="time" class="ann-close-time bg-white mx-3 border-2 rounded-lg px-10 py-2"
                        v-model="closeTime">
                </div>
            </div>

            <div class="flex mt-5">
                <div class="w-44 text-cyan-800 font-bold pt-2">Display</div>
                <label class="cursor-pointer label">
                    <input type="checkbox" id="display" class="ann-display checkbox checkbox-success" v-model="display"
                        :checked="display" />
                    <label for="display" class="ml-5 text-cyan-800">Check to show this announcement.</label>
                </label>
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
</template>
 
<style scoped>
input,
textarea,
select {
    color: black;
}

input[type="time"]::-webkit-calendar-picker-indicator,
input[type="date"]::-webkit-calendar-picker-indicator {
    filter: invert(50%);
}
</style>
