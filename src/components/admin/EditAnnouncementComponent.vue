<script setup>
import { ref, onMounted, watch, onUpdated } from 'vue';
import { useRoute } from 'vue-router';
import { useRouter } from 'vue-router';
import { getAllCategories, getDataById } from '../../composable/getData';
import TimezoneComponent from '../base/TimezoneComponent.vue';
import ErrorModalComponent from '../base/ErrorModalComponent.vue';
import { changePage } from '../../composable/changePage';

const { params } = useRoute();
const data = ref({});
const isModalOpen = ref(false);
const isCancel = ref(false)
const isFieldEdit = ref(false);
const publishDate = ref('')
const publishTime = ref('')
const closeDate = ref('')
const closeTime = ref('')
const display = ref(false)
const haveFieldError = ref(false)
const fieldErrorMsg = ref('')

const enablePublishTime = ref(true)
const enableCloseTime = ref(true)

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

onUpdated(() => {
    // enable or disable publish time
    if (publishDate.value !== '') {
        enablePublishTime.value = true
    }
    // enable or disable close time
    if (closeDate.value !== '') {
        enableCloseTime.value = true
    }
})

watch([editedAnnounce, display, publishDate, publishTime, closeDate, closeTime], () => {
    editedAnnounce.value.announcementCategory = categoryItem.value.find(item => item.categoryId === editedAnnounce.value.categoryId)?.categoryName

    if (publishTime.value == '') {
        publishTime.value = publishDate.value !== '' ? '06:00' : ''
    }
    if (closeTime.value == '') {
        closeTime.value = closeDate.value !== '' ? '18:00' : ''
    }

    if (publishDate.value == '') {
        enablePublishTime.value = false
    }
    if (closeDate.value == '') {
        enableCloseTime.value = false
    }

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
    if (updateAnnounce.announcementTitle === '') {
        haveFieldError.value = true
        fieldErrorMsg.value = 'Announcement title is required'
        return
    }

    if (updateAnnounce.announcementDescription === '') {
        haveFieldError.value = true
        fieldErrorMsg.value = 'Announcement description is required'
        return
    }

    if (updateAnnounce.categoryId === undefined) {
        haveFieldError.value = true
        fieldErrorMsg.value = 'Category is required'
        return
    }

    if (updateAnnounce.announcementTitle.length > 200) {
        haveFieldError.value = true
        fieldErrorMsg.value = 'Announcement title must not exceed 200 characters'
        return
    }

    if (updateAnnounce.announcementDescription.length > 10000) {
        haveFieldError.value = true
        fieldErrorMsg.value = 'Announcement description must not exceed 10000 characters'
        return
    }

    if (publishDate.value !== '' && publishTime.value !== '') {
        updateAnnounce.publishDate = new Date(`${publishDate.value} ${publishTime.value}`).toISOString().slice(0, 19) + 'Z'
    } else if (publishDate.value !== '' && publishTime.value === '') {
        haveFieldError.value = true
        fieldErrorMsg.value = 'Both of publish date and time must be filled or empty'
        return
    } else if (publishDate.value === '' && publishTime.value !== '') {
        haveFieldError.value = true
        fieldErrorMsg.value = 'Both of publish date and time must be filled or empty'
        return
    } else {
        updateAnnounce.publishDate = null
    }

    if (closeDate.value !== '' && closeTime.value !== '') {
        updateAnnounce.closeDate = new Date(`${closeDate.value} ${closeTime.value}`).toISOString().slice(0, 19) + 'Z'
    } else if (closeDate.value !== '' && closeTime.value === '') {
        haveFieldError.value = true
        fieldErrorMsg.value = 'Both of close date and time must be filled or empty'
        return
    } else if (closeDate.value === '' && closeTime.value !== '') {
        haveFieldError.value = true
        fieldErrorMsg.value = 'Both of close date and time must be filled or empty'
        return
    } else {
        updateAnnounce.closeDate = null
    }

    updateAnnounce.publishDate = datetimeFormatterISO(publishDate.value, publishTime.value)
    updateAnnounce.closeDate = datetimeFormatterISO(closeDate.value, closeTime.value)

    // validate date
    const currentTime = new Date().toISOString();
    if (updateAnnounce.closeDate < currentTime) {
        haveFieldError.value = true
        fieldErrorMsg.value = 'close date must be a future date'
        return
    }

    if (updateAnnounce.publishDate < currentTime) {
        haveFieldError.value = true
        fieldErrorMsg.value = 'publish date must be a future date'
        return
    }

    if (updateAnnounce.publishDate > updateAnnounce.closeDate) {
        haveFieldError.value = true
        fieldErrorMsg.value = 'publish date must be earlier than close date'
        return
    }



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
                <input type="text" class="ann-title h-10 w-full bg-slate-100 rounded-lg pl-4 border" maxlength="200"
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
            <!-- Description -->
            <div class="flex mt-5">
                <div class="w-52 text-cyan-800 font-bold pt-2">Description</div>
                <div class="w-full px-5 mx-auto">
                    <QuillEditor theme="snow" toolbar="full" v-model:content="editedAnnounce.announcementDescription"
                        content-type="html" class="ann-description h-96" />
                </div>
            </div>

            <!-- new date & time input -->
            <div class="flex mt-5">
                <div class="text-cyan-800 w-40 py-3 font-bold">Publish Date</div>
                <div class="flex ml-1">
                    <input type="date" class="ann-publish-date bg-white mx-3 border-2 rounded-lg px-10 py-2"
                        v-model="publishDate">
                    <input type="time" class="ann-publish-time bg-white mx-3 border-2 rounded-lg px-10 py-2"
                        v-model="publishTime" :disabled="!enablePublishTime"
                        :class="!enablePublishTime ? 'cursor-not-allowed text-zinc-300' : ''">
                </div>
            </div>
            <div class="flex mt-5">
                <div class="text-cyan-800 w-40 py-3 font-bold">Close Date</div>
                <div class="flex ml-1">
                    <input type="date" class="ann-close-date bg-white mx-3 border-2 rounded-lg px-10 py-2"
                        v-model="closeDate">
                    <input type="time" class="ann-close-time bg-white mx-3 border-2 rounded-lg px-10 py-2"
                        v-model="closeTime" :disabled="!enableCloseTime"
                        :class="!enableCloseTime ? 'cursor-not-allowed text-zinc-300' : ''">
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
                @click="isCancel = true">Back</button>

            <button class="ann-button text-white bg-emerald-plus text-center rounded-lg shadow-md px-5 py-2 w-20 h-10 mb-24"
                :class="{ 'opacity-50 cursor-not-allowed': !isFieldEdit, 'cursor-pointer': isFieldEdit }"
                :disabled="!isFieldEdit" @click="editAnnouncement(editedAnnounce, params?.id)">Edit</button>
        </div>
    </div>

    <!-- cancel modal -->
    <ErrorModalComponent :checkCondition="isCancel" :typeError="'cancel'" @close="isModalOpen = false" />
    <!-- error -->
    <ErrorModalComponent v-if="haveFieldError" :checkCondition="haveFieldError" :typeError="'problem'"
        :message="fieldErrorMsg" />
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
