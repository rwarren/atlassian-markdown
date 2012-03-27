(function(window) {
    /*
     This invoker script wraps up the PageDown code into something that can be more easily invoked from Java.  It was of course
     never part of the standard PageDown code base.
    */

    converter = Markdown.getSanitizingConverter();
    converter.hooks.set("generateImageHTML", function (obj) {
        print('\nJS here calling out as generateImageHTML(' + obj +')');
        print('\nJS here calling out as generateImageHTML(' + obj.url + "," + obj.alt_text + ','+ obj.title +')');
        return "<img src='http://images.wikia.com/disney/images/0/00/Captain_Hook.png'/>";
    });

    /*
     Its really important to the Markdown Sanitizer that there be no extra spaces and the right attributes
     it will strip them otherwise
    */
    var toAttr = function(attrName, attrValue) {
        return attrValue ? ' ' + attrName +'="' + attrValue + '"' : "";
    };

    converter.hooks.set("generateImageHTML", function (obj) {
        return '<img' +
                toAttr('src', obj.url) +
                toAttr('alt',obj.alt_text) +
                toAttr('title', obj.title) +
                toAttr('class', 'markdownImg') +
                '/>';
    });
    converter.hooks.set("generateLinkHTML", function (obj) {
        return '<a' +
                toAttr('href',obj.url) +
                toAttr('title',obj.title) +
                toAttr('class','markdownLink') +
                '>' + obj.link_text + '</a>';
    });

    return converter;
})(this);
