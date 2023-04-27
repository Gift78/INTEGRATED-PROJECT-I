import { createRouter, createWebHistory } from "vue-router";
import AnnouncmentListingComponent from "../components/AnnouncmentListingComponent.vue";

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
      component: AnnouncmentListingComponent,
    },
  ],
});

export default router;
