<template>
  <div class="discourse-settings">
    <div class="bg-white dark:bg-black rounded-lg p-4">
      <h1 class="text-xl font-bold mb-4">Discourse评论设置</h1>
      <p class="text-gray-500 dark:text-gray-400 mb-4">
        将Discourse论坛评论嵌入到Halo博客中
      </p>

      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
          Discourse嵌入代码
        </label>
        <textarea
          v-model="formState.embedCode"
          rows="10"
          class="w-full px-3 py-2 text-gray-700 dark:text-gray-300 border rounded-lg focus:outline-none"
          placeholder='<div id="discourse-comments"></div>...'
        ></textarea>
        <p class="text-xs text-gray-500 mt-1">
          粘贴从Discourse管理面板获取的嵌入代码。您可以使用 {{postUrlPlaceholder}} 作为当前文章URL的占位符。您可以使用 {{domIdPlaceholder}} 作为评论区DOM ID的占位符。
        </p>
      </div>

      <div class="bg-gray-50 dark:bg-gray-800 p-4 rounded-lg mt-4">
        <h2 class="text-md font-medium mb-2">使用指南</h2>
        <ol class="list-decimal pl-5 text-sm">
          <li class="mb-1">访问您的Discourse论坛管理面板中的"自定义 > 嵌入"页面</li>
          <li class="mb-1">添加您的Halo博客域名为可嵌入主机</li>
          <li class="mb-1">复制提供的嵌入代码</li>
          <li class="mb-1">粘贴到上面的文本框中</li>
          <li class="mb-1">如需动态替换文章URL，请在代码中使用 {{postUrlPlaceholder}} 占位符</li>
          <li class="mb-1">如需动态生成唯一评论区ID，请使用 {{domIdPlaceholder}} 占位符</li>
        </ol>
      </div>

      <div class="flex justify-end mt-4">
        <button
          @click="handleSave"
          class="px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition-colors"
        >
          保存设置
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { consoleApiClient, axiosInstance } from "@halo-dev/api-client";
import { Toast } from "@halo-dev/components";

// 定义占位符变量，用于模板中显示
const postUrlPlaceholder = "{{POST_URL}}";
const domIdPlaceholder = "{{DOM_ID}}";

const formState = ref({
  embedCode: "",
});

onMounted(async () => {
  try {
    // 使用 axiosInstance 获取插件设置
    const { data } = await axiosInstance.get(
      "/apis/api.console.halo.run/v1alpha1/plugins/discourse-comment/settings"
    );
    
    if (data && data.forms) {
      const discourseForm = data.forms.find((form: any) => form.group === "discourse");
      
      if (discourseForm && discourseForm.formSchema) {
        const embedCodeField = discourseForm.formSchema.find(
          (field: any) => field.name === "embedCode"
        );
        
        if (embedCodeField && embedCodeField.value) {
          formState.value.embedCode = embedCodeField.value;
        }
      }
    }
  } catch (error) {
    console.error("Failed to fetch settings:", error);
  }
});

const handleSave = async () => {
  try {
    // 使用 axiosInstance 更新插件设置
    await axiosInstance.put(
      "/apis/api.console.halo.run/v1alpha1/plugins/discourse-comment/settings",
      {
        form: {
          group: "discourse",
          formSchema: [
            {
              $formkit: "textarea",
              name: "embedCode",
              value: formState.value.embedCode,
            },
          ],
        }
      }
    );
    
    Toast.success("设置已保存");
  } catch (error) {
    console.error("Failed to save settings:", error);
    Toast.error("保存设置失败");
  }
};
</script>