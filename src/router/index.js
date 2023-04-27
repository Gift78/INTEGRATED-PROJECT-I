import { createRouter, createWebHistory } from "vue-router";
import AnnouncmentsListingComponent from "../components/AnnouncmentsListingComponent.vue";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: "/",
      redirect: "/announcments",
    },
    {
      path: "/announcments",
      name: "Announcments",
      component: AnnouncmentsListingComponent,
    },
  ],
});

export default router;
