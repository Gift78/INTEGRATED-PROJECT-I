import { createRouter, createWebHistory } from "vue-router";
import AnnouncmentsListingComponent from "../components/AnnouncmentsListingComponent.vue";
import AnnouncmentsDetailComponent from "../components/AnnouncmentDetailComponent.vue";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: "/",
      redirect: "/announcements",
    },
    {
      path: "/announcements",
      name: "Announcements",
      component: AnnouncmentsListingComponent,
    },
    {
      path: "/detail",
      name: "detail",
      component: AnnouncmentsDetailComponent,
    },
  ],
});

export default router;
