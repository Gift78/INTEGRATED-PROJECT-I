<script setup>
import TimezoneComponent from './TimezoneComponent.vue';
import { ref } from 'vue';
import Arrowdown from './icons/arrowdown.vue'
import ArrowRight from './icons/ArrowRight.vue'


const categoryItem = [
    { categoryId: 1, categoryName: 'ทั่วไป' },
    { categoryId: 2, categoryName: 'ทุนการศึกษา' },
    { categoryId: 3, categoryName: 'หางาน' },
    { categoryId: 4, categoryName: 'ฝึกงาน' }
]
const categorySelect = ref(categoryItem[0])
const showItem = ref(true)
const showDropdownOptions = () => {
    showItem.value = !showItem.value
}
const selectItem = (item) => {
    categorySelect.value = item
    showItem.value = true
}

const displayed = ref(false)

const newAnnouncement = ref({
    announcementTitle: '',
    announcementDescription: '',
    publishDate: null,
    closeDate: null,
    announcementDisplay: 'N',
    announcementCategory: categorySelect.value
})

const publishDate = ref('')
const publishTime = ref('')
const closeDate = ref('')
const closeTime = ref('')

const setting = () => {
    if (newAnnouncement.value.announcementTitle == '' || newAnnouncement.value.announcementDescription == '') {
        return 'btn-disabled bg-smoke'
    } else {
        // set category
        newAnnouncement.value.announcementCategory = categorySelect.value
        // set display
        if (displayed.value) {
            newAnnouncement.value.announcementDisplay = 'Y'
        } else {
            newAnnouncement.value.announcementDisplay = 'N'
        }

        if(newAnnouncement.value.publishDate =""){

        }

        // set publish date
        newAnnouncement.value.publishDate = `${publishDate.value} ${publishTime.value}`
        newAnnouncement.value.closeDate = `${closeDate.value} ${closeTime.value}`

        return ''
    }
}

const addNewAnnouncement = async () => {
    setting()
    if (newAnnouncement.value.announcementTitle !== '' && newAnnouncement.value.announcementDescription !== '') {
        console.log(newAnnouncement.value)
        try {
            const res = await fetch(import.meta.env.VITE_ROOT_API + "/api/announcements", {
                method: 'POST',
                headers: {
                    'content-type': 'application/json'
                },
                body: JSON.stringify({
                    announcementTitle: newAnnouncement.value.announcementTitle,
                    announcementDescription: newAnnouncement.value.announcementDescription,
                    publishDate: newAnnouncement.value.publishDate,
                    closeDate: newAnnouncement.value.closeDate,
                    announcementDisplay: newAnnouncement.value.announcementDisplay,
                    categoriesObject: newAnnouncement.value.announcementCategory
                })
            })
            if (res.status === 201) {
                console.log('add successfully')
            } else {
                throw new Error('cannot add')
            }
        } catch (err) {
            console.log(err)
        }

        return ''
    }

}



</script>
 
<template>
    <div style="width: 80em;" class="mx-auto">
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
                    class="border-2 rounded-lg w-full mr-20 py-3 pl-5">
            </div>
            <div class="flex">
                <div class="ml-16 mt-5 text-cyan-800 w-20 py-3">Category : </div>
                <!-- dropdown button -->
                <div class="flex pt-3">
                    <div class="flex-none p-3">
                        <button @click="showDropdownOptions()"
                            class="flex justify-between w-48 px-2 py-2 bg-background rounded-md shadow border focus:outline-none focus:border-emerald-plus">
                            <span>{{ categorySelect.categoryName }}</span>
                            <arrowdown />
                        </button>
                        <div :hidden="showItem" class="w-56 py-3 mt-2 shadow-md bg-white border rounded-lg ">
                            <div class="block px-4 py-2 text-gray-500 hover:bg-emerald-light hover:text-white"
                                v-for="category in categoryItem" @click="selectItem(category)">
                                {{ category.categoryName }}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Announcement Description -->
        <div class="text-cyan-800 text-xl ml-10 mt-3">Announcement Description</div>
        <div class="bg-white py-5 rounded-xl shadow-md mt-3 flex-col">
            <div class="flex">
                <div class="ml-16 mr-5 text-cyan-800 w-32 py-3">Description :</div>
                <textarea rows="10" class="border-2 rounded-lg w-full mr-20"
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
                        <input type="date" class="mx-3 w-full border-2 rounded-lg px-10 py-2" v-model="publishDate">
                        <input type="time" class="mx-3 w-full border-2 rounded-lg px-10 py-2" v-model="publishTime">
                    </div>
                </div>
                <ArrowRight class="text-zinc-300 mt-8" />
                <div class="felx-col w-full">
                    <div class="ml-16 mr-5 text-cyan-800 w-32 py-3">Publish Date</div>
                    <div class="flex">
                        <input type="date" class="mx-3 w-full border-2 rounded-lg px-10 py-2" v-model="closeDate">
                        <input type="time" class="mx-3 w-full border-2 rounded-lg px-10 py-2" v-model="closeTime">
                    </div>
                </div>
            </div>

            <div class="ml-16 mr-5 text-cyan-800 w-32 py-3">Display</div>
            <div class="flex items-center">
                <input type="checkbox" id="display" class="ml-24 w-5 h-5" v-model="displayed">
                <label for="display" class="ml-5 text-cyan-800">Check to show this announcement.</label>
            </div>

        </div>

        <div>{{ newAnnouncement }}</div>

        <!-- Submit or Calcel -->
        <div class="flex justify-end mt-5 mb-24">
            <button class="mr-5 bg-red-400 text-white w-32 py-3 rounded-lg">Cancel</button>
            <button class="bg-emerald-plus text-white w-32 py-3 rounded-lg" :class="setting()"
                @click="addNewAnnouncement()">Confirm</button>
        </div>

    </div>
</template>
 
<style scoped></style>