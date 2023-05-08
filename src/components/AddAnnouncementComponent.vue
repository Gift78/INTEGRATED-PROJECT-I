<script setup>
import TimezoneComponent from './TimezoneComponent.vue';
import { ref } from 'vue';
import { useRouter } from 'vue-router'
import DropDown from './icons/DropDown.vue'
import ArrowRight from './icons/ArrowRight.vue'
import CancelRounded from './icons/CancelRounded.vue'
const router = useRouter()

const errors = ref({})
// category data
const categoryItem = [
    { categoryId: 1, categoryName: 'ทั่วไป' },
    { categoryId: 2, categoryName: 'ทุนการศึกษา' },
    { categoryId: 3, categoryName: 'หางาน' },
    { categoryId: 4, categoryName: 'ฝึกงาน' }
]

// for set display to Y / N
const displayed = ref(false)
// default announcement
const newAnnouncement = ref({
    announcementTitle: '',
    announcementDescription: '',
    publishDate: null,
    closeDate: null,
    announcementDisplay: 'N',
    categoryId: null
})

// to set date and time
const publishDate = ref('')
const publishTime = ref('')
const closeDate = ref('')
const closeTime = ref('')

// set all announcement should set
const setting = () => {
    if (newAnnouncement.value.announcementTitle == '' || newAnnouncement.value.announcementDescription == '' || newAnnouncement.value.categoryId == undefined) {
        return 'opacity-50 cursor-not-allowed'
    } else {
        // set category
        // set display
        if (displayed.value) {
            newAnnouncement.value.announcementDisplay = 'Y'
        } else {
            newAnnouncement.value.announcementDisplay = 'N'
        }
        // set publish date
        if (publishDate.value !== '' && publishTime.value !== '' && closeDate.value == '' && closeTime.value == '') {
            newAnnouncement.value.publishDate = new Date(`${publishDate.value} ${publishTime.value}`).toISOString().slice(0, 19) + 'Z'
            newAnnouncement.value.closeDate = null
        } else if (closeDate.value !== '' && closeTime !== '' && publishDate.value == '' && publishTime.value == '') {
            newAnnouncement.value.closeDate = new Date(`${closeDate.value} ${closeTime.value}`).toISOString().slice(0, 19) + 'Z'
            newAnnouncement.value.publishDate = null
        } else if (closeDate.value !== '' && closeTime !== '' && publishDate.value !== '' && publishTime.value !== '') {
            newAnnouncement.value.publishDate = new Date(`${publishDate.value} ${publishTime.value}`).toISOString().slice(0, 19) + 'Z'
            newAnnouncement.value.closeDate = new Date(`${closeDate.value} ${closeTime.value}`).toISOString().slice(0, 19) + 'Z'
        } else {
            newAnnouncement.value.publishDate = null
            newAnnouncement.value.closeDate = null
        }
        return ''
    }
}

const changePage = (name) => {
    router.push({ name: name })
}

// if have error will show pop up
const haveError = ref(false)
const addNewAnnouncement = async (annonuce) => {

    console.log('fetching...')
    // set before create
    setting()
    // Check if name and desc='' it will not be created.
    if (annonuce.announcementTitle !== '' && annonuce.announcementDescription !== '') {
        console.log(annonuce)
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
}
</script>
<template>
    <div style="width: 80em;" class="mx-auto">
        <!-- Error message -->
        <div class="flex items-center justify-center" v-if="haveError">
            <CancelRounded
                class="text-red-500 absolute top-48 z-30 flex items-center justify-center bg-white rounded-full" />
            <div class="absolute top-72 bg-white px-10 pb-10 z-10 rounded-3xl shadow-xl">
                <p class="text-3xl font-bold text-center pt-16">Error {{ errors.status }}!</p>
                <p class="py-5 text-center">{{ errors.message }}</p>
                <div class="modal-action flex justify-center">
                    <label class="btn text-white border-none w-24 bg-red-500 hover:bg-red-700"
                        @click="changePage('Announcement')">OK</label>
                </div>
            </div>
        </div>
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
                    <input type="text" v-model="newAnnouncement.announcementTitle"
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
                    <div class="ml-16 mr-5 text-cyan-800 w-32 py-3">Description :</div>
                    <textarea rows="10" maxlength="10000"
                        class="ann-description bg-white pl-5 pt-3 border-2 rounded-lg w-full mr-20"
                        v-model="newAnnouncement.announcementDescription"></textarea>
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
                                v-model="publishTime">
                        </div>
                    </div>
                    <ArrowRight class="text-zinc-300 mt-8" />
                    <div class="felx-col w-full">
                        <div class="ml-16 mr-5 text-cyan-800 w-32 py-3">Close Date</div>
                        <div class="flex">
                            <input type="date" class="ann-close-date bg-white mx-3 w-full border-2 rounded-lg px-10 py-2"
                                v-model="closeDate">
                            <input type="time" class="ann-close-time bg-white mx-3 w-full border-2 rounded-lg px-10 py-2"
                                v-model="closeTime">
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
                <label for="my-modal"
                    class="ann-button btn mx-5 w-32 bg-zinc-300  hover:bg-zinc-400 border-none ">Cancel</label>
                <input type="checkbox" id="my-modal" class="modal-toggle" />
                <div class="modal">
                    <CancelRounded class="text-red-500 absolute top-52 z-10 bg-white rounded-full" />
                    <div class="modal-box bg-white">
                        <p class="text-3xl font-bold text-center pt-16">Are you sure to cancel?</p>
                        <p class="py-5 text-center">Do you really want to leave this section? This process will discard all
                            changes.</p>
                        <div class="modal-action flex justify-center">
                            <!-- close modal -->
                            <label for="my-modal" class="btn border-none w-24 bg-zinc-300 hover:bg-zinc-400">Cancel</label>
                            <!-- cancel all change -->
                            <label for="my-modal" class="btn text-white border-none w-24 bg-red-500 hover:bg-red-700"
                                @click="changePage('Announcement')">OK</label>
                        </div>
                    </div>
                </div>
                <button class="ann-button bg-emerald-plus text-white w-32 py-3 rounded-lg " :class="setting()"
                    @click="addNewAnnouncement(newAnnouncement)">Submit</button>
            </div>
        </div>
    </div>
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