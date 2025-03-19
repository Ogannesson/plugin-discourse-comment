package run.halo.discourse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IAttribute;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import reactor.core.publisher.Mono;
import run.halo.app.plugin.ReactiveSettingFetcher;
import run.halo.app.theme.dialect.CommentWidget;

/**
 * Discourse评论组件实现
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DiscourseCommentWidget implements CommentWidget {

    private final ReactiveSettingFetcher settingFetcher;

    @Override
    public void render(ITemplateContext context,
                      IProcessableElementTag tag,
                      IElementTagStructureHandler structureHandler) {
        IAttribute groupAttribute = tag.getAttribute("group");
        IAttribute kindAttribute = tag.getAttribute("kind");
        IAttribute nameAttribute = tag.getAttribute("name");

        // 验证必要的属性
        if (kindAttribute == null || StringUtils.isBlank(kindAttribute.getValue())) {
            log.warn("Comment widget tag attributes 'kind' is missing.");
            structureHandler.replaceWith("<p style=\"color:red\">Comment widget attributes 'kind' is required but missing found.</p>", false);
            return;
        }
        if (nameAttribute == null || StringUtils.isBlank(nameAttribute.getValue())) {
            log.warn("Comment widget tag attributes 'name' is missing.");
            structureHandler.replaceWith("<p style=\"color:red\">Comment widget attributes 'name' is required but missing found.</p>", false);
            return;
        }

        // 获取设置 - 注意这里使用新的设置组名称
        DiscourseSettings settings = settingFetcher.fetch("discourse-comment", DiscourseSettings.class)
            .defaultIfEmpty(new DiscourseSettings())
            .block();

        // 检查是否已配置嵌入代码
        if (settings == null || StringUtils.isBlank(settings.getEmbedCode())) {
            structureHandler.replaceWith("<p style=\"color:red\">请在插件设置中配置Discourse嵌入代码</p>", false);
            return;
        }

        // 构建唯一的DOM ID
        String group = getGroup(groupAttribute);
        String domId = domIdFrom(group, kindAttribute.getValue(), nameAttribute.getValue());

        // 获取文章URL
        String fullUrl = "";
        
        if (context.containsVariable("post")) {
            Object post = context.getVariable("post");
            // 尝试获取文章的URL
            if (post != null) {
                try {
                    fullUrl = post.toString();
                } catch (Exception e) {
                    log.warn("Failed to get post URL: {}", e.getMessage());
                }
            }
        }

        // 替换占位符
        String embedCode = settings.getEmbedCode()
            .replace("{{POST_URL}}", fullUrl)
            .replace("{{DOM_ID}}", domId);

        // 输出嵌入代码
        structureHandler.replaceWith(embedCode, false);
    }

    private String domIdFrom(String group, String kind, String name) {
        Assert.notNull(name, "The name must not be null.");
        Assert.notNull(kind, "The kind must not be null.");
        String groupKindNameAsDomId = String.join("-", group, kind, name);
        return "discourse-" + groupKindNameAsDomId.replaceAll("[^\\-_a-zA-Z0-9\\s]", "-")
            .replaceAll("(-)+", "-");
    }

    private String getGroup(IAttribute groupAttribute) {
        return groupAttribute == null || groupAttribute.getValue() == null ? ""
            : StringUtils.defaultString(groupAttribute.getValue());
    }
}