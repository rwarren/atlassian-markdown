package com.atlassian.labs.markdown;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This will white-list the tags allowed in the input
 */
public class MarkdownSanitizer
{
    public static String sanitizeHtml(final String html)
    {
        Whitelist whitelist = Whitelist.basicWithImages();
        String safeHtml = Jsoup.clean(html, whitelist);
        return safeHtml;
    }
}
