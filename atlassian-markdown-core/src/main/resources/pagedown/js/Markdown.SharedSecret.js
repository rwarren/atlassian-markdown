(function ()
{
    /**
     * This top level method instantiates a new shared secret per run and then invokes markdown with it
     *
     * @param text the markdown text to be converted into html
     */
    Markdown.getSharedSecretConverter = function ()
    {
        /*
         This invoker script wraps up the PageDown code into something that can be more easily invoked from Java.  It was of course
         never part of the standard PageDown code base.
         */

        var sharedSecret = '' + new Date().getTime() + '-' + Math.random();

        converter = Markdown.getSanitizingConverter(sharedSecret);

        /*
         Its really important to the Markdown Sanitizer that there be no extra spaces and the right attributes
         it will strip them otherwise
         */
        var toAttr = function (attrName, attrValue)
        {
            return attrValue ? ' ' + attrName + '="' + attrValue + '"' : "";
        };

        converter.hooks.set("generateImageHTML", function (obj)
        {
            return '<img' +
                    toAttr('src', obj.url) +
                    toAttr('alt', obj.alt_text) +
                    toAttr('title', obj.title) +
                    toAttr('class', 'markdownImg') +
                    toAttr('data-shared-secret', sharedSecret) +
                    '/>';
        });
        converter.hooks.set("generateLinkHTML", function (obj)
        {
            return '<a' +
                    toAttr('href', obj.url) +
                    toAttr('title', obj.title) +
                    toAttr('class', 'markdownLink') +
                    toAttr('data-shared-secret', sharedSecret) +
                    '>' + obj.link_text + '</a>';
        });

        converter.hooks.set("generateCodeBlockHTML", function (obj)
        {
            return '<pre' +
                    toAttr('class', 'prettyprint') +
                    toAttr('data-shared-secret', sharedSecret) +
                    '><code>' + obj.codeblock + '</code></pre>';
        });


        converter.hooks.set("generateCodeSpanHTML", function (obj)
        {
            return '<code' +
                    toAttr('class', 'prettyprint') +
                    toAttr('data-shared-secret', sharedSecret) +
                    '>' + obj.codeblock + '</code>';
        });


        return converter;
    };

})
();

Markdown.getSharedSecretConverter();
