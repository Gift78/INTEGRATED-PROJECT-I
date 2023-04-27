import { createRouter, createWebHistory } from "vue-router";
import AnnouncmentListingComponent from "../components/AnnouncmentListingComponent.vue";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: "/",
      name: "Home",
      component: AnnouncmentListingComponent,
    },
  ],
});

export default router;
