package com.atlassian.labs.markdown;

import com.atlassian.jira.issue.fields.renderer.IssueRenderContext;
import com.atlassian.jira.issue.fields.renderer.JiraRendererPlugin;
import com.atlassian.jira.plugin.renderer.JiraRendererModuleDescriptor;

/**
 * A markdown field renderer
 */
public class MarkdownRenderer implements JiraRendererPlugin
{
    public static final String RENDERER_TYPE = "atlassian-jira-markdown-renderer";

    private JiraRendererModuleDescriptor jiraRendererModuleDescriptor;

    public String render(String value, IssueRenderContext context)
    {
        return new Markdown().markdown(value, context);
    }

    public String renderAsText(String value, IssueRenderContext context)
    {
        return value;
    }

    public String getRendererType()
    {
        return RENDERER_TYPE;
    }

    public Object transformForEdit(Object rawValue)
    {
        return rawValue;
    }

    public Object transformFromEdit(Object editValue)
    {
        return editValue;
    }

    public void init(JiraRendererModuleDescriptor jiraRendererModuleDescriptor)
    {
        this.jiraRendererModuleDescriptor = jiraRendererModuleDescriptor;
    }

    public JiraRendererModuleDescriptor getDescriptor()
    {
        return jiraRendererModuleDescriptor;
    }
}