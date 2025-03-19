<template>
  <div>
    <h1 class="text-xl font-bold mb-4">Discourse 评论设置</h1>
    <div class="mb-4">
      <label class="block text-sm font-medium mb-1">Discourse嵌入代码</label>
      <textarea 
        v-model="embedCode" 
        rows="10" 
        class="w-full p-2 border rounded"
        placeholder="粘贴Discourse嵌入代码..."
      ></textarea>
    </div>
    <button 
      @click="saveSettings" 
      class="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
    >
      保存
    </button>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue';

export default defineComponent({
  name: 'SettingsView',
  setup() {
    const embedCode = ref('');

    const saveSettings = () => {
      console.log('保存设置', embedCode.value);
      // 这里仅打印日志，暂不实现实际保存功能
    };

    return {
      embedCode,
      saveSettings
    };
  }
});
</script>