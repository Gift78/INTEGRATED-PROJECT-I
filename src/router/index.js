import { createRouter, createWebHistory } from "vue-router";
import AnnouncmentsListingComponent from "../components/admin/AnnouncmentsListingComponent.vue";
import AnnouncmentsDetailComponent from "../components/admin/AnnouncmentDetailComponent.vue";
import AddAnnouncementComponent from "../components/admin/AddAnnouncementComponent.vue";
import EditAnnouncementComponent from "../components/admin/EditAnnouncementComponent.vue";
import UserAnnouncementListingComponent from "../components/user/UserAnnouncementListingComponent.vue";

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
      component: EditAnnouncementComponent,
    },
    {
      path: "/announcement",
      name: "UserAnnouncement",
      component: UserAnnouncementListingComponent,
    },
  ],
});

export default router;
