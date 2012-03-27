package com.atlassian.labs.markdown.jira;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.RendererManager;
import com.atlassian.jira.issue.fields.renderer.IssueRenderContext;
import com.atlassian.jira.issue.fields.renderer.wiki.AtlassianWikiRenderer;
import com.atlassian.jira.util.JiraKeyUtils;
import com.atlassian.labs.markdown.PageDownMarkdown;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The code to do the actual markdown generation
 */
public class JiraMarkdownProcessor
{
    public String markdown(final String text, final IssueRenderContext issueRenderContext)
    {
        // PageDown invocation
        String markdown = new PageDownMarkdown().markdown(text);

        markdown = JiraKeyUtils.linkBugKeys(markdown);
        markdown = replaceMentionsWithNames(markdown, issueRenderContext);

        return markdown;
    }

    private static final Pattern USER_PROFILE_WIKI_MARKUP_LINK_PATTERN = Pattern.compile("(\\[[~@]*[^\\\\,]+?\\])");

    private String replaceMentionsWithNames(String content, IssueRenderContext issueRenderContext)
    {
        final Matcher wikiMatcher = USER_PROFILE_WIKI_MARKUP_LINK_PATTERN.matcher(content);
        final StringBuffer sb = new StringBuffer();
        int cursor = 0;
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
        String wikiLink = ComponentAccessor.getComponent(RendererManager.class).getRendererForType(AtlassianWikiRenderer.RENDERER_TYPE).render(markup, issueRenderContext);
        sb.append(wikiLink);
    }
}
