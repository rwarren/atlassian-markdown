package com.atlassian.labs.markdown;

import javax.script.*;
import java.io.*;
import java.net.URL;

/**
 * This markdown generation code uses a JavaScript engine to invoke the same PageDown JS.
 * <p/>
 * This means the server side and client side are producing the same results.
 */
public class PageDownMarkdown
{

    public String markdown(final String markdownText)
    {
        final ScriptEngineManager factory = new ScriptEngineManager();
        final ScriptEngine engine = factory.getEngineByName("JavaScript");
        final Invocable invocableEngine = (Invocable) engine;
        try
        {
            final Bindings bindings = new SimpleBindings();
            String js = getPageDownJS();
            Object pageDownConverter = engine.eval(js, bindings);
            return invocableEngine.invokeMethod(pageDownConverter, "makeHtml", markdownText) + "";
        }
        catch (ScriptException e)
        {
            throw runtimeAssertion(e);
        }
        catch (NoSuchMethodException e)
        {
            throw runtimeAssertion(e);
        }
    }

    private String getPageDownJS()
    {
        final StringWriter sw = new StringWriter();

        new ResourceReader()
                .readResource("js/pagedown/Markdown.Converter.js", sw)
                .readResource("js/pagedown/Markdown.Sanitizer.js", sw);

        sw.append("\n   Markdown.getSanitizingConverter();");
        return sw.toString();
    }

    private RuntimeException runtimeAssertion(Exception e)
    {
        return new RuntimeException("This is really not expected and indicates something seriously wrong with this code", e);
    }


}
