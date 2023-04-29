import { createRouter, createWebHistory } from "vue-router";
import AnnouncmentsListingComponent from "../components/AnnouncmentsListingComponent.vue";
import AnnouncmentsDetailComponent from "../components/AnnouncmentDetailComponent.vue";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: "/",
      redirect: "/admin/announcement",
    },
    {
      path: "/admin/announcement",
      name: "Announcement",
      component: AnnouncmentsListingComponent,
    },
    {
      path: "/admin/announcement/:id",
      name: "AnnouncementDetail",
      component: AnnouncmentsDetailComponent,
    },
  ],
});

export default router;
