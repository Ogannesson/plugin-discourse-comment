package run.halo.discourse;

import org.pf4j.PluginWrapper;
import org.springframework.stereotype.Component;
import run.halo.app.plugin.BasePlugin;

/**
 * Discourse评论嵌入插件的主类
 */
@Component
public class DiscourseCommentPlugin extends BasePlugin {

    public DiscourseCommentPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public void start() {
        System.out.println("Discourse评论嵌入插件已启动");
    }

    @Override
    public void stop() {
        System.out.println("Discourse评论嵌入插件已停止");
    }
}