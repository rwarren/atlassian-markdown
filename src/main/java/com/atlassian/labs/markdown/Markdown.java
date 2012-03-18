package com.atlassian.labs.markdown;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.fields.renderer.IssueRenderContext;
import com.atlassian.jira.issue.fields.renderer.wiki.AtlassianWikiRenderer;
import com.atlassian.jira.util.JiraKeyUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The code to do the actual markdown generation
 */
public class Markdown
{
    public String markdown(final String text, final IssueRenderContext issueRenderContext)
    {
        // markdown4j invocation
        //String markdown = new MarkdownProcessor().markdown(text);

        // PageDown invocation
        String markdown = new PageDownMarkdown().markdown(text);
        markdown = MarkdownSanitizer.sanitizeHtml(markdown);

        markdown = JiraKeyUtils.linkBugKeys(markdown);
        markdown = replaceMentionsWithNames(markdown, issueRenderContext);

        String markdownHTML = new StringBuilder()
                .append("\n<div class=\"jira-markdown-field-view\">")
                .append(markdown)
                .append("\n</div>").toString();

        return markdownHTML;
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
        String wikiLink = ComponentAccessor.getRendererManager().getRendererForType(AtlassianWikiRenderer.RENDERER_TYPE).render(markup, issueRenderContext);
        sb.append(wikiLink);
    }
}
