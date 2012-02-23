package com.atlassian.labs.markdown;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.RendererManager;
import com.atlassian.jira.issue.fields.renderer.IssueRenderContext;
import com.atlassian.jira.issue.fields.renderer.wiki.AtlassianWikiRenderer;
import com.atlassian.jira.util.JiraKeyUtils;
import com.petebevin.markdown.MarkdownProcessor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The code to do the actual markdown generation
 */
public class Markdown
{
    public String markdown(String text, IssueRenderContext issueRenderContext)
    {
        MarkdownProcessor processor = new MarkdownProcessor();
        String markdown = processor.markdown(text);
        markdown = JiraKeyUtils.linkBugKeys(markdown);
        markdown = replaceMentionsWithNames(markdown, issueRenderContext);
        return new StringBuilder()
                .append("\n<div class=\"jira-markdown-field-view\">")
                .append(markdown)
                .append("\n</div>").toString();
    }

    private static final Pattern USER_PROFILE_WIKI_MARKUP_LINK_PATTERN = Pattern.compile("(\\[[~@]*[^\\\\,]+?\\])");

    private String replaceMentionsWithNames(String content, IssueRenderContext issueRenderContext)
    {
        Matcher wikiMatcher = USER_PROFILE_WIKI_MARKUP_LINK_PATTERN.matcher(content);
        int cursor = 0;
        StringBuffer sb = new StringBuffer();
        while (wikiMatcher.find())
        {
            sb.append(content.substring(cursor, wikiMatcher.start()));
            cursor = wikiMatcher.end();

            String markup = content.substring(wikiMatcher.start(), wikiMatcher.end());
            if (markup.endsWith("("))
            {
                sb.append(markup); // its a markdown [xxx](link)
            }
            else
            {
                renderUserLink(sb, markup, issueRenderContext);
            }
        }
        sb.append(content.substring(cursor));
        return sb.toString();
    }

    private void renderUserLink(StringBuffer sb, String markup, IssueRenderContext issueRenderContext)
    {
        // in order to get full user profile link rendering we end up using the wiki render to turn [~xxxxx] into a user profile link
        // freaky eh?  wiki in markup inside wiki?
        RendererManager jiraRendererManager = ComponentAccessor.getRendererManager();
        String wikiLink = jiraRendererManager.getRendererForType(AtlassianWikiRenderer.RENDERER_TYPE).render(markup, issueRenderContext);
        sb.append(wikiLink);
    }
}
