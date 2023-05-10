<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router'
import CancelRounded from './icons/CancelRounded.vue';
import InformationCircle from './icons/InformationCircle.vue';

const router = useRouter()

// Example for use this Component
// method 1 : Cancel modal
// <ErrorModalComponent :checkCondition="open" :typeError="'cancel'" @close="click()" />
// method 2 : Delete modal
// <ErrorModalComponent :checkCondition="open" :typeError="'delete'" :method="testMethod" :parameter="'its work'" @close="click()" />
// method 3 : problem modal
// <ErrorModalComponent v-if="isModalOpen" :checkCondition="isModalOpen" :typeError="'problem'" :status="errors?.status" :message="errors?.message"/>

const emit = defineEmits(['close'])
const props = defineProps({
    // true => show modal || false => close modal
    checkCondition: {
        type: Boolean,
        required: true,
    },
    //  type of error e.g. 'cancel','delete','problem'
    typeError: {
        type: String,
        required: true
    },
    // (optional) message error from backend
    status: {
        type: Number,
        required: false
    },
    message: {
        type: String,
        required: false
    },

    // (optional) function to do  
    method: {
        required: false,
    },
    // (optional) if function need parameter
    parameter: {
        type: [String, Number],
        required: false
    },

    // (optional) name page to go | If not defined,link to the announcement listing page.
    goToPage: {
        type: String,
        required: false,
        default: 'Announcement'
    }
})

const getParameter = props.parameter
const messageHeader = ref('')
const messageDesc = ref('')
const showCancel = ref(false)
const type = ref('')

if (props.typeError == 'cancel') {
    type.value = 'cancel'
    messageHeader.value = 'Are you sure to cancel?'
    messageDesc.value = 'Do you really want to leave this section? This process will discard all changes.'
    showCancel.value = true
}
if (props.typeError == 'delete') {
    type.value = 'delete'
    messageHeader.value = 'Are you sure to delete?'
    messageDesc.value = 'Do you really want to leave this section? This process will discard all changes.'
    showCancel.value = true
}
if (props.typeError == 'problem') {
    type.value = 'problem'
    messageHeader.value = `Error ${props.status} `
    messageDesc.value = props.message
    console.log('test : ' + props.status + ' ' + props.message)

}

const changePage = (name, id) => {
    if (id !== undefined) {
        router.push({ name: name, params: { id: id } })
    } else {
        router.push({ name: name })
    }
}

</script>
 
<template>
    <div class="fixed overflow-x-hidden inset-0 flex justify-center z-50 top-56 h-96" v-if="checkCondition">
        <!-- content -->
        <div class="relative mx-auto max-w-2xl rounded-2xl px-3 pb-5 bg-white" style="width: 40em;">
            <CancelRounded v-if="type === 'cancel'" class="text-red-500 bg-white rounded-full mx-auto" />
            <InformationCircle v-if="type === 'problem' || type === 'delete'"
                class="text-red-500 bg-white rounded-full mx-auto" />
            <p class="text-3xl font-bold text-center pt-2">{{ messageHeader }}</p>
            <p class="py-5 text-center">{{ messageDesc }}</p>
            <div class="modal-action flex justify-center">
                <label for="my-modal" class="btn shadow-xl border-none w-24 bg-zinc-300 hover:bg-zinc-400" v-if="showCancel"
                    @click="$emit('close')">Cancel</label>
                <label v-if="method == undefined" for="my-modal" class="btn shadow-xl text-white border-none w-24 bg-red-500 hover:bg-red-700"
                    @click="changePage(goToPage), $emit('close')">OK</label>
                <label v-if="method !== undefined" for="my-modal"
                    class="btn shadow-xl text-white border-none w-24 bg-red-500 hover:bg-red-700"
                    @click="method(parameter), $emit('close')">Delete</label>
            </div>
        </div>
    </div>
    <div class="absolute inset-0 z-40 opacity-25 bg-black" v-if="checkCondition"></div>
</template>
 
<style scoped></style>

<!-- changePage -->