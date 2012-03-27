package com.atlassian.labs.markdown.confluence;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.content.render.xhtml.macro.annotation.Format;
import com.atlassian.confluence.content.render.xhtml.macro.annotation.RequiresFormat;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.renderer.v2.macro.MacroException;

import java.util.Map;

/**
 * A macro for Confluence that provides Markdown support
 */
public class ConfluenceMarkdownXhtmlMacro implements Macro
{

    private final ConfluenceMarkdownMacro oldMarkdownMacro;

    public ConfluenceMarkdownXhtmlMacro()
    {
        oldMarkdownMacro = new ConfluenceMarkdownMacro();
    }

    @Override
    public String execute(Map<String, String> parameters, String body, ConversionContext context) throws MacroExecutionException
    {
        try
        {
            return oldMarkdownMacro.execute(parameters, body, context != null ? context.getPageContext() : null);
        }
        catch (MacroException e)
        {
            throw new MacroExecutionException(e);
        }
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
