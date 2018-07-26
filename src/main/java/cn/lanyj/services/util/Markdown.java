package cn.lanyj.services.util;

import cn.lanyj.services.support.web.markdown.MarkdownService;
import cn.lanyj.services.support.web.markdown.impl.FlexmarkMarkdownService;

/**
 * A Markdown processing util class
 *
 * @author Raysmond
 */
public class Markdown {

    private static final MarkdownService MARKDOWN_SERVICE = new FlexmarkMarkdownService();

    public static String markdownToHtml(String content) {
        return MARKDOWN_SERVICE.renderToHtml(content);
    }
}
