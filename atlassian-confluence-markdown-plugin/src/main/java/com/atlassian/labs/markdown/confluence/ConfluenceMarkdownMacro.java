package com.atlassian.labs.markdown.confluence;

import com.atlassian.confluence.util.GeneralUtil;
import com.atlassian.renderer.RenderContext;
import com.atlassian.renderer.v2.RenderMode;
import com.atlassian.renderer.v2.macro.BaseMacro;
import com.atlassian.renderer.v2.macro.MacroException;

import java.util.Map;

/**
 * A macro for Confluence that provides Markdown support
 */
public class ConfluenceMarkdownMacro extends BaseMacro
{
    @Override
    public boolean hasBody()
    {
        return true;
    }

    @Override
    public RenderMode getBodyRenderMode()
    {
        return RenderMode.NO_RENDER;
    }

    @Override
    public String execute(Map parameters, String body, RenderContext renderContext) throws MacroException
    {
        return "<div>" +
                "<h2>This would be markdown generated</h2>" +
                "<pre><code>" + GeneralUtil.escapeXml(body) + "</code></pre>" +
                "</div>";
    }
}
