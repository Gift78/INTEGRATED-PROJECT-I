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
<<<<<<< HEAD
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
    categoryId: null
})
=======
const data = ref({});
>>>>>>> ab7c109860656831309322d9c10e8c8437cc56f3

const backToAnnouncements = () => {
    router.push({ name: 'Announcement' });
}

<<<<<<< HEAD
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
    console.log(dateString)
    if (!dateString) {
        return null;
    }

    const newDateString = dateString.slice(0, 11) + " , " + dateString.slice(15, 20)
    const dateObj = new Date(newDateString).toISOString().slice(0, -5) + "Z";
    console.log(dateObj)
    return dateObj
}

=======
>>>>>>> ab7c109860656831309322d9c10e8c8437cc56f3
onMounted(async () => {
    try {
        data.value = await getDataById(params?.id);
        data.value.publishDate = datetimeFormatter(data.value.publishDate)
        data.value.closeDate = datetimeFormatter(data.value.closeDate)
        console.log(data.value)
        if (data.value === undefined) {
            isModalOpen.value = true;
        }

        editedAnnounce.value.announcementTitle = data.value.announcementTitle
        editedAnnounce.value.announcementDescription = data.value.announcementDescription
        editedAnnounce.value.publishDate = datetimeFormatter(data.value.publishDate)
        editedAnnounce.value.closeDate = datetimeFormatter(data.value.closeDate)
        editedAnnounce.value.announcementDisplay = data.value.announcementDisplay

        categoryItem.forEach((item) => {
            if (item.categoryName === data.value.announcementCategory) {
                editedAnnounce.value.categoryId = item.categoryId
            }
        })
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
    let foundMatchingCategory = false;
    for (const item of categoryItem) {
        if (item.categoryName === data.value.announcementCategory) {
            updateAnnounce.categoryId = item.categoryId;
            foundMatchingCategory = true;
            break;
        }
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
                    publishDate: datetimeFormatIso(updateAnnounce.publishDate),
                    closeDate: datetimeFormatIso(updateAnnounce.closeDate),
                    announcementDisplay: updateAnnounce.announcementDisplay,
<<<<<<< HEAD
                    categoryId: updateAnnounce.categoryId
=======
                    categoryId: findCategoryName(updateAnnounce.announcementCategory)

>>>>>>> ab7c109860656831309322d9c10e8c8437cc56f3
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
<<<<<<< HEAD
                <input type="text" class="h-10 w-full bg-slate-100 rounded-lg pl-4"
                    v-model="editedAnnounce.announcementTitle" />
=======
                <input v-model="data.announcementTitle" type="text" class="h-10 w-full bg-slate-100 rounded-lg pl-4" />

>>>>>>> ab7c109860656831309322d9c10e8c8437cc56f3
            </div>
            <div class="flex mt-5">
                <div class="w-52 text-cyan-800 font-bold">Category</div>
                <input type="text" class="h-10 w-full bg-slate-100 rounded-lg pl-4" v-model="data.announcementCategory" />
            </div>
            <div class="flex mt-5">
                <div class="w-52 text-cyan-800 font-bold">Description</div>
<<<<<<< HEAD
                <textarea class="textarea h-10 w-full bg-slate-100 rounded-lg pl-4"
                    v-model="editedAnnounce.announcementDescription"></textarea>
            </div>
            <div class="flex mt-5">
                <div class="w-52 text-cyan-800 font-bold">Publish Date</div>
                <input v-model="editedAnnounce.publishDate" type="text" class="h-10 w-full bg-slate-100 rounded- pl-4" />
=======
                <textarea v-model="data.announcementDescription"
                    class="textarea h-10 w-full bg-slate-100 rounded-lg pl-4"></textarea>
            </div>
            <div class="flex mt-5">
                <div class="w-52 text-cyan-800 font-bold">Publish Date</div>
                <input v-model="data.publishDate" type="text" class="h-10 w-full bg-slate-100 rounded- pl-4" />
>>>>>>> ab7c109860656831309322d9c10e8c8437cc56f3
            </div>
            <div class=" flex mt-5">
                <div class="w-52 text-cyan-800 font-bold">Close Date</div>
<<<<<<< HEAD
                <input v-model="editedAnnounce.closeDate" type="text" class="h-10 w-full bg-slate-100 rounded-lg pl-4" />
            </div>
            <div class="flex mt-5">
                <div class="w-52 text-cyan-800 font-bold">Display</div>
                <input v-model="editedAnnounce.announcementDisplay" type="text"
                    class="h-10 w-full bg-slate-100 rounded-lg pl-4" />
=======
                <input v-model="data.closeDate" type="text" class="h-10 w-full bg-slate-100 rounded-lg pl-4" />
            </div>
            <div class="flex mt-5">
                <div class="w-52 text-cyan-800 font-bold">Display</div>
                <input v-model="data.announcementDisplay" type="text" class="h-10 w-full bg-slate-100 rounded-lg pl-4" />
>>>>>>> ab7c109860656831309322d9c10e8c8437cc56f3
            </div>
        </div>

        <!-- button -->
        <div class="flex justify-end mt-3 space-x-3 " v-if="!isModalOpen">
            <button
                class="ann-button text-black bg-slate-100 text-center rounded-lg shadow-md cursor-pointer px-5 py-2 w-20 h-10"
                @click="backToAnnouncements">Back</button>

            <button
                class="ann-button text-white bg-emerald-plus text-center rounded-lg shadow-md cursor-pointer px-5 py-2 w-20 h-10 "
<<<<<<< HEAD
                @click="editAnnouncement(editedAnnounce, params?.id)">Edit</button>
=======
                @click="editAnnouncement(data, params?.id)">Edit</button>
>>>>>>> ab7c109860656831309322d9c10e8c8437cc56f3
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
