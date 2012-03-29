package com.atlassian.labs.markdown.confluence;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.content.render.xhtml.macro.annotation.Format;
import com.atlassian.confluence.content.render.xhtml.macro.annotation.RequiresFormat;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.labs.markdown.PageDownMarkdown;
import com.atlassian.renderer.v2.macro.MacroException;

import java.util.Map;

/**
 * A macro for Confluence that provides Markdown support
 */
public class ConfluenceMarkdownXhtmlMacro implements Macro
{

    public ConfluenceMarkdownXhtmlMacro()
    {
    }

    @Override
    public String execute(Map<String, String> parameters, String body, ConversionContext context) throws MacroExecutionException
    {
        String markdown = new PageDownMarkdown().markdown(body);
        return markdown;
    }

    @Override
    public BodyType getBodyType()
    {
        return BodyType.PLAIN_TEXT;
    }

    @Override
    public OutputType getOutputType()
    {
        return OutputType.BLOCK;
    }

}
