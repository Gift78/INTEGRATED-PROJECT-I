<script setup>
import { useRoute } from 'vue-router';
import { ref, onMounted } from 'vue';
import { useMode } from '../../stores/mode';
import TimezoneComponent from '../base/TimezoneComponent.vue';
import Megaphone from '../icons/Megaphone.vue';
import formatDatetime from '../../composable/formatDatetime';
import ErrorModalComponent from '../base/ErrorModalComponent.vue';
import { storeToRefs } from 'pinia'
import { changePage } from '../../composable/changePage';
import { getDataById } from '../../composable/getData';

const params = useRoute().params;

const data = ref([]);
const isModalOpen = ref(false);
const errors = ref();
const modeStore = useMode();
const { mode } = storeToRefs(modeStore);
const QuillEditorOptions = {
    readOnly: true,
};


onMounted(async () => {
    data.value = await getDataById(params?.id,true)
})
</script>

<template>
    <div style="width: 80em;" class="mx-auto">
        <div class="text-center text-3xl text-cyan-800 py-10">SIT Announcement System (SAS)</div>

        <TimezoneComponent />
        <hr class="mt-4 border-2">

        <div class="ann-item bg-white flex-col rounded-lg p-10 shadow-lg mt-5">
            <div class="flex ml-12">
                <div class="bg-gray-200 p-4 rounded-lg">
                    <Megaphone class="mr-2" />
                </div>
                <div :class="mode === 'close' ? 'flex-col' : ''">
                    <div :class="mode === 'close' ? 'flex' : 'flex-col'">
                        <div class="ann-title text-cyan-800 font-bold text-3xl ml-5">{{ data?.announcementTitle }}</div>
                        <div class="ann-category text-white font-bold text-base mt-2"
                            :class="mode === 'close' ? 'ml-2' : 'ml-5'">
                            <span class="bg-green-400 py-1 px-2 rounded-lg">
                                {{ data?.announcementCategory }}
                            </span>
                        </div>
                    </div>
                    <div v-if="mode === 'close'" class="flex text-white font-semibold text-sm ml-5 mt-2">
                        <span class=" bg-red-400 py-1 px-2 rounded-lg">
                            Closed on :
                        </span>
                        <span class="ann-close-date py-1 px-2 text-cyan-800">
                            {{ formatDatetime(data?.closeDate) }}
                        </span>
                    </div>
                </div>
            </div>

            <!-- <hr class="mt-4 mx-12"> -->

            <div class="mt-14 mx-auto w-full border-t">
                <QuillEditor toolbar="#my-toolbar" v-model:content="data.announcementDescription" content-type="html"
                    class="ann-description" :options="QuillEditorOptions">
                    <template #toolbar>
                        <div id="my-toolbar" class="hidden"></div>
                    </template>
                </QuillEditor>
            </div>
        </div>

        <div class="flex justify-end mt-4">
            <button class="ann-button bg-cyan-800 text-white rounded-lg py-2 px-6" @click="changePage('UserAnnouncement')">
                Back
            </button>
        </div>
    </div>

    <ErrorModalComponent v-if="isModalOpen" :checkCondition="isModalOpen" :typeError="'problem'" :status="errors?.status"
        :message="errors?.message" goToPage="UserAnnouncement" />
</template>

<style scoped></style>
