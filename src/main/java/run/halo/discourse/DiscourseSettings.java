package run.halo.discourse;

import lombok.Data;

/**
 * Discourse评论设置
 */
@Data
public class DiscourseSettings {
    /**
     * 用户粘贴的Discourse嵌入代码
     */
    private String embedCode = "";
    
    /**
     * 是否在文章中启用
     */
    private boolean enableOnPosts = true;
    
    /**
     * 评论区位置
     * 可选值: bottom(文章底部), after-content(内容之后)
     */
    private String position = "bottom";
    
    /**
     * 默认DOM ID
     * 当无法获取特定ID时使用的默认DOM ID
     */
    private String defaultDomId = "discourse-comments";
}