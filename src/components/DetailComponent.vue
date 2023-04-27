<script setup>
const props = defineProps({
    detail: {
        type: Object
    }
})
</script>
 
<template>
    <div>
        <div class="bg-white flex-col rounded-lg p-10 shadow-lg">
            <div class="flex mt-5">
                <div class="w-52 text-cyan-800 font-bold">Title</div>
                <div class="text-cyan-800 w-full">{{ detail?.announcementTitle }}</div>
            </div>
            <div class="flex mt-5">
                <div class="w-52 text-cyan-800 font-bold">Category</div>
                <div class="text-cyan-800 w-full">{{ detail?.categoriesObject?.categoryName }}</div>
            </div>
            <div class="flex mt-5">
                <div class="w-52 text-cyan-800 font-bold">Description</div>
                <div class="text-cyan-800 w-full">{{ detail?.announcementDescription }}</div>
            </div>
            <div class="flex mt-5">
                <div class="w-52 text-cyan-800 font-bold">Publish Date</div>
                <div class="text-cyan-800 w-full">{{ detail?.publishDate }}</div>
            </div>
            <div class="flex mt-5">
                <div class="w-52 text-cyan-800 font-bold">Close Date</div>
            <div class="text-cyan-800 w-full">{{ detail?.closeDate }}</div>
        </div>
            <div class="flex mt-5">
                <div class="w-52 text-cyan-800 font-bold">Display</div>
                <div class="text-cyan-800 w-full">{{ detail?.announcementDisplay }}</div>
            </div>
        </div>
        <!-- <div class="  mt-10 flex justify-end">
                <div class="text-cyan-600 text-center rounded-xl p-5 bg-cyan-200 w-28 shadow-md">Back</div>
            </div> -->
    </div>
</template>
 
<style scoped></style>