<script setup>
import TimezoneComponent from '../base/TimezoneComponent.vue';
import { computed, onMounted, onUpdated, ref, watch } from 'vue';
import { useRouter } from 'vue-router'
import { getAllCategories } from '../../composable/getData';
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css';
import ArrowRight from '../icons/ArrowRight.vue'
import ErrorModalComponent from '../base/ErrorModalComponent.vue';
const router = useRouter()

const errors = ref({})
const displayed = ref(false)
const isFormValid = ref(false)
const isModalOpen = ref(false)
const haveError = ref(false)
const publishDate = ref('')
const publishTime = ref('')
const closeDate = ref('')
const closeTime = ref('')
const haveFieldError = ref(false)
const fieldErrorMsg = ref('')
const enablePublishTime = ref(false)
const enableCloseTime = ref(false)

const categoryItem = ref([])
onMounted(async () => {
    categoryItem.value = await getAllCategories()
})

const newAnnouncement = ref({
    announcementTitle: '',
    announcementDescription: '',
    publishDate: null,
    closeDate: null,
    announcementDisplay: 'N',
    categoryId: null
})

watch([newAnnouncement, displayed, publishDate, closeDate], () => {
    if (newAnnouncement.value.announcementTitle == '' || newAnnouncement.value.announcementDescription == '' || newAnnouncement.value.categoryId == undefined) {
        isFormValid.value = false
    } else {
        isFormValid.value = true
    }

    if (displayed.value) {
        newAnnouncement.value.announcementDisplay = 'Y'
    } else {
        newAnnouncement.value.announcementDisplay = 'N'
    }

    if (publishTime.value == '') {
        publishTime.value = publishDate.value !== '' ? '06:00' : ''
    }
    if (closeTime.value == '') {
        closeTime.value = closeDate.value !== '' ? '18:00' : ''
    }

}, { deep: true })

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

const changePage = (name) => {
    router.push({ name: name })
}

const addNewAnnouncement = async (annonuce) => {
    if (annonuce.announcementTitle === '') {
        haveFieldError.value = true
        fieldErrorMsg.value = 'Announcement title is required'
        return
    }

    if (annonuce.announcementDescription === '') {
        haveFieldError.value = true
        fieldErrorMsg.value = 'Announcement description is required'
        return
    }

    if (annonuce.categoryId === undefined) {
        haveFieldError.value = true
        fieldErrorMsg.value = 'Category is required'
        return
    }

    if (annonuce.announcementTitle.length > 200) {
        haveFieldError.value = true
        fieldErrorMsg.value = 'Announcement title must not exceed 200 characters'
        return
    }

    if (annonuce.announcementDescription.length > 10000) {
        haveFieldError.value = true
        fieldErrorMsg.value = 'Announcement description must not exceed 10000 characters'
        return
    }

    if (publishDate.value !== '' && publishTime.value !== '') {
        annonuce.publishDate = new Date(`${publishDate.value} ${publishTime.value}`).toISOString().slice(0, 19) + 'Z'
    } else if (publishDate.value !== '' && publishTime.value === '') {
        haveFieldError.value = true
        fieldErrorMsg.value = 'Both of publish date and time must be filled or empty'
        return
    } else if (publishDate.value === '' && publishTime.value !== '') {
        haveFieldError.value = true
        fieldErrorMsg.value = 'Both of publish date and time must be filled or empty'
        return
    } else {
        annonuce.publishDate = null
    }

    if (closeDate.value !== '' && closeTime.value !== '') {
        annonuce.closeDate = new Date(`${closeDate.value} ${closeTime.value}`).toISOString().slice(0, 19) + 'Z'
    } else if (closeDate.value !== '' && closeTime.value === '') {
        haveFieldError.value = true
        fieldErrorMsg.value = 'Both of close date and time must be filled or empty'
        return
    } else if (closeDate.value === '' && closeTime.value !== '') {
        haveFieldError.value = true
        fieldErrorMsg.value = 'Both of close date and time must be filled or empty'
        return
    } else {
        annonuce.closeDate = null
    }

    // validate date
    const currentTime = new Date().toISOString();
    if (annonuce.closeDate < currentTime) {
        haveFieldError.value = true
        fieldErrorMsg.value = 'close date must be a future date'
        return
    }
    if (annonuce.publishDate > annonuce.closeDate) {
        haveFieldError.value = true
        fieldErrorMsg.value = 'close date must be a future date'
        return
    }

    try {
        const res = await fetch(import.meta.env.VITE_ROOT_API + "/api/announcements", {
            method: 'POST',
            headers: {
                'content-type': 'application/json'
            },
            body: JSON.stringify({
                announcementTitle: annonuce.announcementTitle,
                announcementDescription: annonuce.announcementDescription,
                publishDate: annonuce.publishDate,
                closeDate: annonuce.closeDate,
                announcementDisplay: annonuce.announcementDisplay,
                categoryId: annonuce.categoryId
            })
        })
        if (res.status === 200) {
            console.log('add successfully')
            changePage('Announcement')
        } else {
            console.log('add failed')
            const errorData = await res.json()
            errors.value = errorData
            haveError.value = true
        }
    } catch (err) {
        console.log(err)
        errors.value = err
        haveError.value = true
    }
}
</script>
<template>
    <div style="width: 80em;" class="mx-auto pb-1">
        <!-- Error message -->
        <ErrorModalComponent v-if="haveError" :checkCondition="haveError" :typeError="'problem'" :status="errors?.status"
            :message="errors?.message" />

        <ErrorModalComponent v-if="haveFieldError" :checkCondition="haveFieldError" :typeError="'problem'"
            :message="fieldErrorMsg" />
        <!-- content -->
        <div :class="haveError ? 'blur-sm' : ''" :style="haveError ? 'pointer-events: none;' : ''">
            <!-- header -->
            <h1 class="text-cyan-800 text-3xl py-10">Announcement Detail</h1>
            <TimezoneComponent />
            <hr class="mt-4 border-2">
            <!-- Announcement Title & Category -->
            <div class="text-cyan-800 text-xl ml-10 mt-3">Announcement Title & Category</div>
            <div class="bg-white py-5 rounded-xl shadow-md mt-3 flex-col">
                <div class="flex">
                    <div class="ml-16 text-cyan-800 w-20 py-3">Title :</div>
                    <input type="text" v-model="newAnnouncement.announcementTitle" maxlength="200"
                        class="ann-title bg-white border-2 rounded-lg w-full mr-20 py-3 pl-5">
                </div>
                <div class="flex">
                    <div class="ml-16 mt-5 text-cyan-800 w-20 py-3">Category : </div>
                    <!-- dropdown button -->
                    <div class="flex pt-3 ">
                        <div class="flex-none p-3 relative">
                            <select class="ann-category select select-bordered w-full max-w-xs font-normal"
                                v-model="newAnnouncement.categoryId">
                                <option v-for="category in categoryItem" :value="category.categoryId">
                                    {{ category.categoryName }}
                                </option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>
            <!-- Announcement Description -->
            <div class="text-cyan-800 text-xl ml-10 mt-3">Announcement Description</div>
            <div class="bg-white py-5 rounded-xl shadow-md mt-3 flex-col">
                <div class="flex">
                    <div class="ml-16 text-cyan-800 w-32 pt-3">Description :</div>
                    <div class="w-full px-5 mx-auto">
                        <QuillEditor theme="snow" toolbar="full" v-model:content="newAnnouncement.announcementDescription"
                            content-type="html" class="ann-description h-96 border rounded-2xl" />
                    </div>
                </div>
            </div>
            <!-- Date and Display -->
            <div class="text-cyan-800 text-xl ml-10 mt-3">Date & Display</div>
            <div class="bg-white py-5 rounded-xl shadow-md mt-3 flex-col  pb-10">
                <div class="flex justify-between">
                    <div class="felx-col w-full">
                        <div class="ml-16 mr-5 text-cyan-800 w-32 py-3">Publish Date</div>
                        <div class="flex w-full">
                            <input type="date" class="ann-publish-date bg-white mx-3 w-full border-2 rounded-lg px-10 py-2"
                                v-model="publishDate">
                            <input type="time" class="ann-publish-time bg-white mx-3 w-full border-2 rounded-lg px-10 py-2"
                                v-model="publishTime" :disabled="!enablePublishTime"
                                :class="!enablePublishTime ? 'cursor-not-allowed text-zinc-300' : ''">
                        </div>
                    </div>
                    <ArrowRight class="text-zinc-300 mt-8" />
                    <div class="felx-col w-full">
                        <div class="ml-16 mr-5 text-cyan-800 w-32 py-3">Close Date</div>
                        <div class="flex">
                            <input type="date" class="ann-close-date bg-white mx-3 w-full border-2 rounded-lg px-10 py-2"
                                v-model="closeDate">
                            <input type="time" class="ann-close-time bg-white mx-3 w-full border-2 rounded-lg px-10 py-2"
                                v-model="closeTime" :disabled="!enableCloseTime"
                                :class="!enableCloseTime ? 'cursor-not-allowed text-zinc-300' : ''">
                        </div>
                    </div>
                </div>
                <div class="ml-16 mr-5 text-cyan-800 w-32 py-3">Display</div>
                <div class="flex items-center">
                    <div class="form-control pl-20">
                        <label class="cursor-pointer label">
                            <input type="checkbox" id="display" class="ann-display checkbox checkbox-success"
                                v-model="displayed" />
                            <label for="display" class="ml-5 text-cyan-800">Check to show this announcement.</label>
                        </label>
                    </div>
                </div>
            </div>
            <!-- Submit or Cancel -->
            <div class="flex justify-end mt-5 mb-24">
                <button class="ann-button btn mx-5 w-32 bg-zinc-300  hover:bg-zinc-400 border-none"
                    @click="isModalOpen = true">cancel</button>

                <button class="ann-button bg-emerald-plus text-white w-32 py-3 rounded-lg"
                    :class="{ 'opacity-50 cursor-not-allowed': !isFormValid, 'cursor-pointer': isFormValid }"
                    :disabled="!isFormValid" @click="addNewAnnouncement(newAnnouncement)">Submit</button>
            </div>

        </div>
    </div>
    <!-- cancel modal -->
    <ErrorModalComponent :checkCondition="isModalOpen" :typeError="'cancel'" @close="isModalOpen = false" />
</template>
 
<style scoped>
input[type="time"]::-webkit-calendar-picker-indicator,
input[type="date"]::-webkit-calendar-picker-indicator {
    filter: invert(50%);
}

select {
    background-color: #fff;
}
</style>