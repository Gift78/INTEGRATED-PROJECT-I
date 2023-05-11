import { ref } from "vue";
import { defineStore } from "pinia";

export const useMode = defineStore("mode", () => {
  const mode = ref("active");
  const toggleMode = (modeName) => {
    mode.value = modeName;
  };

  return { mode, toggleMode };
});
