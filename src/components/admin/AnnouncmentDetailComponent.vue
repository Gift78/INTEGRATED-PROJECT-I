<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { useRouter } from 'vue-router';
import formatDatetime from '../../composable/formatDatetime';
import TimezoneComponent from '../base/TimezoneComponent.vue';
import ErrorModalComponent from '../base/ErrorModalComponent.vue';

const { params } = useRoute();
const router = useRouter();
const data = ref({});
const isModalOpen = ref(false);
const errors = ref();
const QuillEditorOptions = {
    readOnly: true,
};

const changePage = (name, id) => {
    if (id !== undefined) {
        router.push({ name: name, params: { id: id } })
    } else {
        router.push({ name: name })
    }
}

onMounted(async () => {
    try {
        const res = await fetch(import.meta.env.VITE_ROOT_API + "/api/announcements/" + params?.id)
        if (res.ok) {
            data.value = await res.json();
        } else {
            data.value = await res.json();
            isModalOpen.value = true
            const errorData = data.value
            errors.value = errorData
        }
    } catch (error) {
        errors.value = error
    }
})
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
                <div class="text-cyan-800 w-full border-4 border-gray-300">
                    <QuillEditor toolbar="#my-toolbar" v-model:content="data.announcementDescription" content-type="html"
                        class="ann-description" :options="QuillEditorOptions">
                        <template #toolbar>
                            <div id="my-toolbar" class="hidden"></div>
                        </template>
                    </QuillEditor>
                </div>
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
                @click="changePage('Announcement')">Back
            </button>
            <button
                class="ann-button text-orange-400 mx-3 bg-orange-200 text-center rounded-lg shadow-md cursor-pointer px-5 py-2 w-20 h-10"
                @click="changePage('EditAnnouncement', params?.id)">Edit
            </button>
        </div>
    </div>

    <ErrorModalComponent v-if="isModalOpen" :checkCondition="isModalOpen" :typeError="'problem'" :status="errors?.status"
        :message="errors?.message" />
</template>
 
<style scoped></style>
