package com.atlassian.labs.markdown.confluence;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.util.GeneralUtil;
import com.atlassian.confluence.xhtml.api.XhtmlContent;

import java.util.Map;

/**
 * A macro for Confluence that provides Markdown support
 */
public class ConfluenceMarkdownXhtmlMacro implements Macro
{
    private final XhtmlContent xhtmlUtils;

    public ConfluenceMarkdownXhtmlMacro(final XhtmlContent xhtmlUtils)
    {
        this.xhtmlUtils = xhtmlUtils;
    }

    @Override
    public String execute(Map<String, String> parameters, String body, ConversionContext context) throws MacroExecutionException
    {
        return "<div>" +
                "<h2>This would be markdown generated</h2>" +
                "<pre><code>" + GeneralUtil.escapeXml(body) + "</code></pre>" +
                "</div>";
    }

    @Override
    public BodyType getBodyType()
    {
        return BodyType.RICH_TEXT;
    }

    @Override
    public OutputType getOutputType()
    {
        return OutputType.BLOCK;
    }

}
