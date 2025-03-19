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
}