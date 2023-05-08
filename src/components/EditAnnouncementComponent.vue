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

// to set date and time
const publishDate = ref('')
const publishTime = ref('')
const closeDate = ref('')
const closeTime = ref('')

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

    if (minute < 10 && hour < 10) {
        return `${day} ${month} ${year} at 0${hour}:0${minute}`;
    } else if (minute < 10) {
        return `${day} ${month} ${year} at ${hour}:0${minute}`;
    } else if (hour < 10) {
        return `${day} ${month} ${year} at 0${hour}:${minute}`;
    } else {
        return `${day} ${month} ${year} at ${hour}:${minute}`;
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

const getDate = (dateString) => {
    if (!dateString) {
        return null;
    }
    const atIndex = dateString.search('at')
    const dateSliced = dateString.slice(0, atIndex - 1)
    // console.log('input : ' + dateSliced)
    let newDate = new Date(dateSliced).toLocaleDateString('en-GB', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
    })
    // console.log('only date : ' + newDate)
    let [day, month, year] = newDate.split("/"); // แยกค่าวันที่ออกเป็นปี เดือน วัน
    if (day.length == 1) {
        day = '0' + day
    }
    if (month.length == 1) {
        month = '0' + month
    }
    const formattedDate = `${year}-${month}-${day}`
    // console.log('re format : ' + formattedDate)
    return formattedDate
}


const getTime = (dateString) => {
    if (!dateString) {
        return null;
    }
    const atIndex = dateString.search('at')
    const timeSliced = dateString.slice(atIndex + 3, 30)
    // console.log('get time : '+timeSliced)
    return timeSliced
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

        console.log('on mounted : ' + editedAnnounce.value.publishDate)

        // console.log('get date result : ' + getDate(editedAnnounce.value.publishDate))
        publishDate.value = getDate(editedAnnounce.value.publishDate)
        closeDate.value = getDate(editedAnnounce.value.closeDate)

        publishTime.value = getTime(editedAnnounce.value.publishDate)
        closeDate.value = getTime(editedAnnounce.value.closeDate)

        console.log(publishDate.value + ' , ' + publishTime.value)


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
    
    updateAnnounce.publishDate = datetimeFormatterISO(publishDate.value, publishTime.value)
    updateAnnounce.closeDate = datetimeFormatterISO(closeDate.value, closeTime.value)

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
                <!-- <input type="text" class="ann-category h-10 w-full bg-slate-100 rounded-lg pl-4 border" v-model="editedAnnounce.announcementCategory" /> -->

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
                    <input type="date" class="ann-publish-date bg-white mx-3  border-2 rounded-lg px-10 py-2"
                        v-model="publishDate">
                    <input type="time" class="ann-publish-time bg-white mx-3  border-2 rounded-lg px-10 py-2"
                        v-model="publishTime">
                </div>
            </div>
            <div class="flex mt-5">
                <div class="text-cyan-800 w-40 py-3 font-bold">Close Date</div>
                <div class="flex ml-2">
                    <input type="date" class="ann-close-date bg-white mx-3  border-2 rounded-lg px-10 py-2"
                        v-model="closeDate">
                    <input type="time" class="ann-close-time bg-white mx-3  border-2 rounded-lg px-10 py-2"
                        v-model="closeTime">
                </div>
            </div>



            <div class="flex mt-5">
                <div class="w-52 text-cyan-800 font-bold pt-2">Display</div>
                <!-- <input v-model="editedAnnounce.announcementDisplay" type="text" class="ann-display h-10 w-full bg-slate-100 rounded-lg pl-4 border" /> -->
                <label class="cursor-pointer label">
                    <input type="checkbox" id="display" class="ann-display checkbox checkbox-success"
                        v-model="editedAnnounce.announcementDisplay" />
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
 
<style scoped>
input {
    color: black;
}
</style>
