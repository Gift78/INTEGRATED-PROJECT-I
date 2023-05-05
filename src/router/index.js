import { createRouter, createWebHistory } from "vue-router";
import AnnouncmentsListingComponent from "../components/AnnouncmentsListingComponent.vue";
import AnnouncmentsDetailComponent from "../components/AnnouncmentDetailComponent.vue";
import AddAnnouncementComponent from "../components/AddAnnouncementComponent.vue";
import EditAnnouncementComponent from "../components/EditAnnouncementComponent.vue"

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
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
    {
      path: "/admin/announcement/add",
      name: "AddAnnouncement",
      component: AddAnnouncementComponent,
    },
    {
      path: "/admin/announcement/:id/edit",
      name: "EditAnnouncement",
      component: EditAnnouncementComponent
},
  ],
});

export default router;
