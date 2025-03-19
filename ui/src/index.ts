import { definePlugin } from "@halo-dev/console-shared";
import SettingsView from "./views/SettingsView.vue";
import { IconMessage } from "@halo-dev/components";
import { markRaw } from "vue";

// 显式标记 SettingsView 类型
const settingsComponent = SettingsView as any;

export default definePlugin({
  components: {},
  routes: [
    {
      parentName: "Root",
      route: {
        path: "/discourse-comments",
        name: "DiscourseComments",
        component: settingsComponent,
        meta: {
          title: "Discourse评论",
          searchable: true,
          menu: {
            name: "Discourse评论",
            group: "设置",
            icon: markRaw(IconMessage),
            priority: 0,
          },
        },
      },
    },
  ],
  extensionPoints: {},
});