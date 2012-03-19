package com.atlassian.labs.markdown;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
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
        StringWriter sw = new StringWriter();

        readResource(jsURL("js/pagedown/Markdown.Converter.js"), sw);
        readResource(jsURL("js/pagedown/Markdown.Sanitizer.js"), sw);

        sw.append("\n   Markdown.getSanitizingConverter();");
        return sw.toString();
    }

    private URL jsURL(String name)
    {
        return Thread.currentThread().getContextClassLoader().getResource(name);
    }

    private void readResource(URL url, StringWriter sw)
    {
        BufferedReader reader = null;
        try
        {
            InputStream is = url.openStream();
            reader = new BufferedReader(new InputStreamReader(is));
            String s;
            while ((s = reader.readLine()) != null)
            {
                sw.write(s);
                sw.append('\n');
            }
            sw.append('\n');
        }
        catch (IOException e)
        {
            throw runtimeAssertion(e);
        }
        finally
        {
            closeIt(reader);
        }
    }

    private void closeIt(Reader reader)
    {
        try
        {
            if (reader != null)
            {
                reader.close();
            }
        }
        catch (IOException ignored)
        {
        }
    }

    private RuntimeException runtimeAssertion(Exception e)
    {
        return new RuntimeException("This is really not expected and indicates something seriously wrong with this code", e);
    }


}
