package com.atlassian.labs.markdown;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.fields.renderer.IssueRenderContext;
import com.atlassian.jira.issue.fields.renderer.wiki.AtlassianWikiRenderer;
import com.atlassian.renderer.RenderContext;
import com.atlassian.renderer.v2.RenderMode;
import com.atlassian.renderer.v2.macro.BaseMacro;
import com.atlassian.renderer.v2.macro.MacroException;

import java.util.Map;

/**
 * A wiki macro that can insert markdown text
 */
public class MarkdownMacro extends BaseMacro
{
    public boolean isInline()
    {
        return true;
    }

    public boolean hasBody()
    {
        return true;
    }

    public RenderMode getBodyRenderMode()
    {
        return RenderMode.NO_RENDER;
    }

    public String execute(Map map, String body, RenderContext renderContext) throws MacroException
    {
        return new Markdown().markdown(body, buildIssueRenderContext(renderContext));
    }

    private IssueRenderContext buildIssueRenderContext(RenderContext renderContext)
    {
        Issue issue = (Issue) renderContext.getParam(AtlassianWikiRenderer.ISSUE_CONTEXT_KEY);
        return new IssueRenderContext(issue);
    }
}
