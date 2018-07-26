package cn.lanyj.services.support.web.markdown.impl;

import java.io.FileOutputStream;
import java.io.OutputStream;

import org.springframework.stereotype.Service;

import com.vladsch.flexmark.Extension;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ext.anchorlink.AnchorLinkExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.profiles.pegdown.Extensions;
import com.vladsch.flexmark.profiles.pegdown.PegdownOptionsAdapter;
import com.vladsch.flexmark.util.options.DataHolder;

import cn.lanyj.services.support.web.markdown.MarkdownService;

/**
 * 使用flexmark-java解析markdown
 * 参考：https://github.com/vsch/flexmark-java
 *
 * @author Raysmond
 */
@Service("flexmark")
public class FlexmarkMarkdownService implements MarkdownService {
	static final DataHolder OPTIONS = PegdownOptionsAdapter.flexmarkOptions(true,
            Extensions.ALL & (~Extensions.ANCHORLINKS),  new Extension[] {
            		AnchorLinkExtension.create(),
	        		SampleExtension.create(),
	        	});

    static final Parser PARSER = Parser.builder(OPTIONS).build();
    static final HtmlRenderer RENDERER = HtmlRenderer.builder(OPTIONS).build();
	
    static final String HTML_START = "<html>\n" + 
    		"<head>\n" + 
    		"<link rel=\"stylesheet\" href=\"http://www.lanyj.cn/static/css/markdown/markdown-1.css\"/>\n" + 
    		"<link rel=\"stylesheet\" href=\"http://www.lanyj.cn/static/css/markdown/markdown-2.css\"/>\n" + 
    		"<link rel=\"stylesheet\" href=\"http://www.lanyj.cn/static/css/markdown/markdown-3.css\"/>\n" + 
    		"<link rel=\"stylesheet\" href=\"http://www.lanyj.cn/static/css/markdown/prism.css\"/>\n" + 
    		"<meta name=\"description\" content=\"weclome to use 'http://services.lanyj.cn'\">" +
    		"</head>\n" + 
    		"<body>\n" + 
    		"<div class=\"container new-discussion-timeline experiment-repo-nav\">\n" + 
    		"<div class=\"repository-content\">\n" + 
    		"<div class=\"wiki-wrapper page\">\n" + 
    		"<div class=\"wiki-content\">\n" + 
    		"<div id=\"wiki-body\" class=\"wiki-body gollum-markdown-content instapaper_body\">\n";
    static final String HTML_END = "</div>\n" + 
    		"</div>\n" + 
    		"</div>\n" + 
    		"</div>\n" + 
    		"</div>\n" + 
    		"<script src=\"http://www.lanyj.cn/static/js/markdown/prism.js\"></script>" +
    		"</body>\n" + 
    		"</html>";
    @Override
    public String renderToHtml(String content) {
    	Node document = PARSER.parse(content);
        StringBuilder html = new StringBuilder(HTML_START);
        html.append(RENDERER.render(document));
        html.append(HTML_END);
        return html.toString();
    }
    
    public static void main(String[] args) throws Exception {
		FlexmarkMarkdownService service = new FlexmarkMarkdownService();
		String input = "[TOC]: # \"Contents\"\n" + 
				"\n" + 
				"### Contents\n" + 
				"- [Configuring Options](#configuring-options)\n" + 
				"- [Parser](#parser)\n" + 
				"    - [List Parsing Options](#list-parsing-options)\n" + 
				"- [Renderer](#renderer)\n" + 
				"- [Formatting Module](#formatting-module)\n" + 
				"- [PDF Output Module](#pdf-output-module)\n" + 
				"- [Available Extensions](#available-extensions)\n" + 
				"- [Abbreviation](#abbreviation)\n" + 
				"- [Admonition](#admonition)\n" + 
				"- [AnchorLink](#anchorlink)\n" + 
				"- [Aside](#aside)\n" + 
				"- [Attributes](#attributes)\n" + 
				"- [Autolink](#autolink)\n" + 
				"- [Definition Lists](#definition-lists)\n" + 
				"- [Docx Converter](#docx-converter)\n" + 
				"- [Emoji](#emoji)\n" + 
				"- [Enumerated Reference](#enumerated-reference)\n" + 
				"- [Footnotes](#footnotes)\n" + 
				"- [Gfm-Issues](#gfm-issues)\n" + 
				"- [Gfm-Strikethrough/Subscript](#gfm-strikethroughsubscript)\n" + 
				"- [Gfm-TaskList](#gfm-tasklist)\n" + 
				"- [Gfm-Users](#gfm-users)\n" + 
				"- [Html To Markdown](#html-to-markdown)\n" + 
				"- [Ins](#ins)\n" + 
				"- [Jekyll Tags](#jekyll-tags)\n" + 
				"- [Jira-Converter](#jira-converter)\n" + 
				"- [Media Tags](#media-tags)\n" + 
				"- [Spec Example](#spec-example)\n" + 
				"- [Superscript](#superscript)\n" + 
				"- [Tables](#tables)\n" + 
				"- [Table of Contents](#table-of-contents)\n" + 
				"- [Typographic](#typographic)\n" + 
				"- [WikiLinks](#wikilinks)\n" + 
				"- [XWiki Macro Extension](#xwiki-macro-extension)\n" + 
				"- [YAML front matter](#yaml-front-matter)\n" + 
				"- [YouTrack-Converter](#youtrack-converter)\n" + 
				"- [YouTube Embedded Link Transformer](#youtube-embedded-link-transformer)\n" + 
				"\n" + 
				"\n" + 
				"Extensions need to extend the parser, or the HTML renderer, or both. To use an extension, the\n" + 
				"builder objects can be configured with a list of extensions. Because extensions are optional,\n" + 
				"they live in separate artifacts, so additional dependencies need to be added as well.\n" + 
				"\n" + 
				"Let's look at how to enable tables from GitHub Flavored Markdown or MultiMarkdown, which ever\n" + 
				"your prefer. First, add all modules as a dependency (see [Maven Central] for individual\n" + 
				"modules):\n" + 
				"\n" + 
				"```xml\n" + 
				"<dependency>\n" + 
				"    <groupId>com.vladsch.flexmark</groupId>\n" + 
				"    <artifactId>flexmark-all</artifactId>\n" + 
				"    <version>0.34.6</version>\n" + 
				"</dependency>\n" + 
				"```\n" + 
				"\n" + 
				"Configure the extension on the builders:\n" + 
				"\n" + 
				"```java\n" + 
				"import com.vladsch.flexmark.ext.tables.TablesExtension;\n" + 
				"import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;\n" + 
				"\n" + 
				"class SomeClass {\n" + 
				"    static final MutableDataHolder OPTIONS = new MutableDataSet()\n" + 
				"                .set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(), StrikethroughExtension.create()));\n" + 
				"\n" + 
				"    Parser parser = Parser.builder(options).build();\n" + 
				"    HtmlRenderer renderer = HtmlRenderer.builder(options).build();\n" + 
				"}\n" + 
				"```\n" + 
				"\n" + 
				"## Configuring Options\n" + 
				"\n" + 
				"A generic options API was added to allow easy configuration of the parser, renderer and\n" + 
				"extensions. It consists of `DataKey<T>` instances defined by various components. Each data key\n" + 
				"defines the type of its value and a default value.\n" + 
				"\n" + 
				"The values are accessed via the `DataHolder` and `MutableDataHolder` interfaces, with the former\n" + 
				"being a read only container. Since the data key provides a unique identifier for the data there\n" + 
				"is no collision for options.\n" + 
				"\n" + 
				"To configure the parser or renderer, pass a data holder to the `builder()` method with the\n" + 
				"desired options configured, including extensions.\n" + 
				"\n" + 
				"```java\n" + 
				"import com.vladsch.flexmark.html.HtmlRenderer;\n" + 
				"import com.vladsch.flexmark.parser.Parser;\n" + 
				"\n" + 
				"public class SomeClass {\n" + 
				"    static final MutableDataHolder OPTIONS = new MutableDataSet()\n" + 
				"            .set(Parser.REFERENCES_KEEP, KeepType.LAST)\n" + 
				"            .set(HtmlRenderer.INDENT_SIZE, 2)\n" + 
				"            .set(HtmlRenderer.PERCENT_ENCODE_URLS, true)\n" + 
				"\n" + 
				"            // for full GFM table compatibility add the following table extension options:\n" + 
				"            .set(TablesExtension.COLUMN_SPANS, false)\n" + 
				"            .set(TablesExtension.APPEND_MISSING_COLUMNS, true)\n" + 
				"            .set(TablesExtension.DISCARD_EXTRA_COLUMNS, true)\n" + 
				"            .set(TablesExtension.HEADER_SEPARATOR_COLUMN_MATCH, true)\n" + 
				"            .set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create()));\n" + 
				"\n" + 
				"    static final Parser PARSER = Parser.builder(OPTIONS).build();\n" + 
				"    static final HtmlRenderer RENDERER = HtmlRenderer.builder(OPTIONS).build();\n" + 
				"}\n" + 
				"```\n" + 
				"\n" + 
				"In the code sample above, `Parser.REFERENCES_KEEP` defines the behavior of references when\n" + 
				"duplicate references are defined in the source. In this case it is configured to keep the last\n" + 
				"value, whereas the default behavior is to keep the first value.\n" + 
				"\n" + 
				"The `HtmlRenderer.INDENT_SIZE` and `HtmlRenderer.PERCENT_ENCODE_URLS` define options to use for\n" + 
				"rendering. Similarly, extension options can be added at the same time. Any options not set, will\n" + 
				"default to their respective defaults as defined by their data keys.\n" + 
				"\n" + 
				"All markdown element reference types should be stored using a subclass of `NodeRepository<T>` as\n" + 
				"is the case for references, abbreviations and footnotes. This provides a consistent mechanism\n" + 
				"for overriding the default behavior of these references for duplicates from keep first to keep\n" + 
				"last.\n" + 
				"\n" + 
				"By convention, data keys are defined in the extension class and in the case of the core in the\n" + 
				"`Parser` or `HtmlRenderer`.\n" + 
				"\n" + 
				"Data keys are described in their respective extension classes and in `Parser` and\n" + 
				"`HtmlRenderer`.\n" + 
				"\n" + 
				"## Parser\n" + 
				"\n" + 
				"Unified options handling added which are also used to selectively disable loading of core\n" + 
				"parsers and processors.\n" + 
				"\n" + 
				"`Parser.builder()` now implements `MutableDataHolder` so you can use `get`/`set` to customize\n" + 
				"properties directly on it or pass it a DataHolder with predefined options.\n" + 
				"\n" + 
				"Defined in `Parser` class:\n" + 
				"\n" + 
				"<!-- @formatter:off -->\n" + 
				"\n" + 
				"- `ASTERISK_DELIMITER_PROCESSOR` : default `true` enable asterisk delimiter inline processing.\n" + 
				"- `BLANK_LINES_IN_AST` default `false`, set to `true` to include blank line nodes in the AST.\n" + 
				"- `BLOCK_QUOTE_ALLOW_LEADING_SPACE` : default `true`, when `true` leading spaces before `>` are allowed\n" + 
				"- `BLOCK_QUOTE_IGNORE_BLANK_LINE` : default `false`, when `true` block quotes will include blank lines between block quotes and treat them as if the blank lines are also preceded by the block quote marker\n" + 
				"- `BLOCK_QUOTE_INTERRUPTS_ITEM_PARAGRAPH` : default `true`, when `true` block quotes can interrupt list item text, else need blank line before to be included in list items\n" + 
				"- `BLOCK_QUOTE_INTERRUPTS_PARAGRAPH` : default `true`, when `true` block quote can interrupt a paragraph, else needs blank line before\n" + 
				"- `BLOCK_QUOTE_PARSER` : default `true`, when `true` parsing of block quotes is enabled\n" + 
				"- `BLOCK_QUOTE_TO_BLANK_LINE` : default `false`, when `true` block quotes extend to next blank line. Enables more customary block quote parsing than commonmark strict standard\n" + 
				"- `BLOCK_QUOTE_WITH_LEAD_SPACES_INTERRUPTS_ITEM_PARAGRAPH` : default `true`, when `true` block quotes with leading spaces can interrupt list item text, else need blank line before or no leading spaces\n" + 
				"- `CODE_BLOCK_INDENT` defaults to value of `LISTS_ITEM_INDENT`, allows for separate control of indented block setting from list item settings.\n" + 
				"- `CODE_SOFT_LINE_BREAKS` : default `false`, set to `true` to include soft line break nodes in code blocks\n" + 
				"- `CODE_STYLE_HTML_CLOSE` : default `(String)null` override HTML to use for wrapping style, if null then no override\n" + 
				"- `CODE_STYLE_HTML_OPEN` : default `(String)null` override HTML to use for wrapping style, if null then no override\n" + 
				"- `EMPHASIS_STYLE_HTML_CLOSE` : default `(String)null` override HTML to use for wrapping style, if null then no override\n" + 
				"- `EMPHASIS_STYLE_HTML_OPEN` : default `(String)null` override HTML to use for wrapping style, if null then no override\n" + 
				"- `EXTENSIONS` : default empty list list of extension to use for builders. Can use this option instead of passing extensions to parser builder and renderer builder.\n" + 
				"- `FENCED_CODE_BLOCK_PARSER` : default `true` enable parsing of fenced code blocks\n" + 
				"- `FENCED_CODE_CONTENT_BLOCK` : default `false` add `CodeBlock` as child to contain the code of a fenced block. Otherwise the code is contained in a `Text` node.\n" + 
				"- `HARD_LINE_BREAK_LIMIT` : default `false` only treat the last 2 spaces of a hard line break in the HardLineBreak node if set to true. Otherwise all spaces are used.\n" + 
				"- `HEADING_CAN_INTERRUPT_ITEM_PARAGRAPH` : default `true` allow headings to interrupt list item paragraphs\n" + 
				"- `HEADING_NO_ATX_SPACE` : default `false` allow headers without a space between `#` and the header text if true\n" + 
				"- `HEADING_NO_EMPTY_HEADING_WITHOUT_SPACE` default `false`, set to `true` to not recognize empty headings without a space following `#`\n" + 
				"- `HEADING_NO_LEAD_SPACE` : default `false` do not allow non-indent spaces before `#` for atx headers and text or `-`/`=` marker for setext, if true (pegdown and GFM), if false commonmark rules.\n" + 
				"- `HEADING_PARSER` : default `true` enable parsing of headings\n" + 
				"- `HEADING_SETEXT_MARKER_LENGTH` : default `1` sets the minimum number of `-` or `=` needed under a setext heading text before it being recognized as a heading.\n" + 
				"- `HTML_BLOCK_DEEP_PARSER` default `false` - enable deep HTML block parsing\n" + 
				"- `HTML_BLOCK_DEEP_PARSE_BLANK_LINE_INTERRUPTS_PARTIAL_TAG` default `true`, when true blank line interrupts partially open tag ie. `<TAG` without a corresponding `>` and having a blank line blank line before `>`\n" + 
				"- `HTML_BLOCK_DEEP_PARSE_BLANK_LINE_INTERRUPTS` default `true`, when `true` Blank line interrupts HTML block when not in raw tag, otherwise only when closed\n" + 
				"- `HTML_BLOCK_DEEP_PARSE_FIRST_OPEN_TAG_ON_ONE_LINE` default `false`, when `true` do not parse open tags unless they are contained on one line.\n" + 
				"- `HTML_BLOCK_DEEP_PARSE_INDENTED_CODE_INTERRUPTS` default `false`, when `true` Indented code can interrupt HTML block without a preceding blank line.\n" + 
				"- `HTML_BLOCK_DEEP_PARSE_MARKDOWN_INTERRUPTS_CLOSED` default `false`, when `true` Other markdown elements can interrupt a closed HTML block without an intervening blank line\n" + 
				"- `HTML_BLOCK_DEEP_PARSE_NON_BLOCK` default `true`, parse non-block tags inside HTML blocks\n" + 
				"- `HTML_BLOCK_PARSER` : default `true` enable parsing of html blocks\n" + 
				"- `HTML_BLOCK_START_ONLY_ON_BLOCK_TAGS`, default for deep html parser is `true` and regular parser `false`, but you can set your desired specific value and override the default. When `true` will not start an HTML block on a non-block tag, when `false` any tag will start an HTML block.\n" + 
				"- `HTML_BLOCK_TAGS`, default list: `address`, `article`, `aside`, `base`, `basefont`, `blockquote`, `body`, `caption`, `center`, `col`, `colgroup`, `dd`, `details`, `dialog`, `dir`, `div`, `dl`, `dt`, `fieldset`, `figcaption`, `figure`, `footer`, `form`, `frame`, `frameset`, `h1`, `h2`, `h3`, `h4`, `h5`, `h6`, `head`, `header`, `hr`, `html`, `iframe`, `legend`, `li`, `link`, `main`, `math`, `menu`, `menuitem`, `meta`, `nav`, `noframes`, `ol`, `optgroup`, `option`, `p`, `param`, `section`, `source`, `summary`, `table`, `tbody`, `td`, `tfoot`, `th`, `thead`, `title`, `tr`, `track`, `ul`, sets the HTML block tags\n" + 
				"- `HTML_COMMENT_BLOCKS_INTERRUPT_PARAGRAPH` : default `true` enables HTML comments to interrupt paragraphs, otherwise comment to be interpreted as HTML blocks need a blank line before.\n" + 
				"- `INDENTED_CODE_BLOCK_PARSER` : default `true` enable parsing of indented code block\n" + 
				"- `INDENTED_CODE_NO_TRAILING_BLANK_LINES` : default `true` enable removing trailing blank lines from indented code blocks\n" + 
				"- `INLINE_DELIMITER_DIRECTIONAL_PUNCTUATIONS` default `false`, set to `true` to use delimiter parsing rules that take bracket open/close into account\n" + 
				"- `INTELLIJ_DUMMY_IDENTIFIER` : default `false` add `'\\u001f'` to all parse patterns as an allowable character, used by plugin to allow for IntelliJ completion location marker\n" + 
				"- `LIST_BLOCK_PARSER` : default `true` enable parsing of lists\n" + 
				"- `LISTS_ITEM_PREFIX_CHARS` : default `\"*-+\"`, specify which characters mark a list item\n" + 
				"- `LIST_REMOVE_EMPTY_ITEMS`, default `false`. If `true` then empty list items or list items which only contain a `BlankLine` node are removed from the output.\n" + 
				"- `MATCH_CLOSING_FENCE_CHARACTERS` : default `true` whether the closing fence character has to match opening character, when false then back ticks can open and tildes close and vice versa. The number of characters in the opener and close still have to be the same.\n" + 
				"- `MATCH_NESTED_LINK_REFS_FIRST` : default `true` custom link ref processors that take tested `[]` have priority over ones that do not. ie. `[[^f]][test]` is a wiki link with `^f` as page ref followed by ref link `test` when this option is true. IF false then the same would be a ref link `test` with a footnote `^f` refernce for text\n" + 
				"- `PARSER_EMULATION_PROFILE` default `ParserEmulationProfile.COMMONMARK`, set to desired one of the `ParserEmulationProfile` values\n" + 
				"- `PARSE_INNER_HTML_COMMENTS` : default `false` when `true` will parse inner HTML comments in HTML blocks\n" + 
				"- `PARSE_JEKYLL_MACROS_IN_URLS` default `false`, set to `true` to allow any characters to appear between `{{` and `}}` in URLs, including spaces, pipes and backslashes\n" + 
				"- `PARSE_MULTI_LINE_IMAGE_URLS` : default `false` allows parsing of multi line urls:\n" + 
				"- `REFERENCES_KEEP` : default `KeepType.FIRST` which duplicates to keep.\n" + 
				"- `REFERENCES` : default new repository repository for document's reference definitions\n" + 
				"- `REFERENCE_PARAGRAPH_PRE_PROCESSOR` : default `true` enable parsing of reference definitions\n" + 
				"- `SPACE_IN_LINK_ELEMENTS` default `false`, set to `true` to allow whitespace between `![]` or `[]` and `()` of links or images.\n" + 
				"- `SPACE_IN_LINK_URLS` : default `false` will accept spaces in link address as long as they are not followed by `\"`\n" + 
				"- `STRONG_EMPHASIS_STYLE_HTML_CLOSE` : default `(String)null` override HTML to use for wrapping style, if null then no override\n" + 
				"- `STRONG_EMPHASIS_STYLE_HTML_OPEN` : default `(String)null` override HTML to use for wrapping style, if null then no override\n" + 
				"- `THEMATIC_BREAK_PARSER` : default `true` enable parsing of thematic breaks\n" + 
				"- `THEMATIC_BREAK_RELAXED_START` : default `true` enable parsing of thematic breaks which are not preceded by a blank line\n" + 
				"- `TRACK_DOCUMENT_LINES` default `false`. When `true` document lines are tracked in the document's `lineSegments` list and offset to line method can be used to get the 0-based line number for the given offset. When `false` these functions compute the line number by counting EOL sequences before the offset.\n" + 
				"- `UNDERSCORE_DELIMITER_PROCESSOR` : default `true` whether to process underscore delimiters\n" + 
				"\n" + 
				"<!-- @formatter:on -->\n" + 
				"\n" + 
				"### List Parsing Options\n" + 
				"\n" + 
				"Because list parsing is the greatest discrepancy between Markdown parser implementations. Before\n" + 
				"CommonMark there was no hard specification for parsing lists and every implementation took\n" + 
				"artistic license with how it determines what the list should look like.\n" + 
				"\n" + 
				"`flexmark-java` implements four parser families based on their list processing characteristics.\n" + 
				"In addition to `ParserEmulationProfile` setting, each of the families has a standard set of\n" + 
				"common options that control list processing, with defaults set by each but modifiable by the end\n" + 
				"user.\n" + 
				"\n" + 
				"There are a few ways to configure the list parsing options:\n" + 
				"\n" + 
				"1. the recommended way is to apply `ParserEmulationProfile` to options via\n" + 
				"   `MutableDataHolder.setFrom(ParserEmulationProfile)` to have all options configured for a\n" + 
				"   particular parser.\n" + 
				"2. start with the `ParserEmulationProfile.getOptions()` and modify defaults for the family and\n" + 
				"   then pass it to `MutableDataHolder.setFrom(MutableDataSetter)`\n" + 
				"3. by configuring an instance of `MutableListOptions` and then passing it to\n" + 
				"   `MutableDataHolder.setFrom(MutableDataSetter)`\n" + 
				"4. first via individual keys\n" + 
				"\n" + 
				"#### List Item Options\n" + 
				"\n" + 
				"<!-- @formatter:off -->\n" + 
				"\n" + 
				"- item indent columns: `Parser.ITEM_INDENT`, `ListOptions.itemIndent`\n" + 
				"- item code indent column: `Parser.CODE_INDENT`, `ListOptions.codeIndent`\n" + 
				"- new item code indent column: `Parser.NEW_ITEM_CODE_INDENT`, `ListOptions.newItemCodeIndent`\n" + 
				"- list items require explicit space after marker `Parser.LISTS_ITEM_MARKER_SPACE`, `ListOptions.itemMarkerSpace`\n" + 
				"- mismatch item type starts a new list: `Parser.LISTS_ITEM_TYPE_MISMATCH_TO_NEW_LIST`, `ListOptions.itemTypeMismatchToNewList`\n" + 
				"- mismatch item type start a sub-list: `Parser.LISTS_ITEM_TYPE_MISMATCH_TO_SUB_LIST`, `ListOptions.itemTypeMismatchToSubList`\n" + 
				"- bullet or ordered item delimiter mismatch starts a new list: `Parser.LISTS_DELIMITER_MISMATCH_TO_NEW_LIST`, `ListOptions.delimiterMismatchToNewList`\n" + 
				"- ordered items only with `.` after digit, otherwise `)` is also allowed: `Parser.LISTS_ORDERED_ITEM_DOT_ONLY`, `ListOptions.orderedItemDotOnly`\n" + 
				"- first ordered item prefix sets start number of list: `Parser.LISTS_ORDERED_LIST_MANUAL_START`, `ListOptions.orderedListManualStart`\n" + 
				"- item is loose if it contains a blank line after its item text: `Parser.LISTS_LOOSE_WHEN_BLANK_LINE_FOLLOWS_ITEM_PARAGRAPH`, `ListOptions.looseWhenBlankLineFollowsItemParagraph`\n" + 
				"- item is loose if it is first item and has trailing blank line or if previous item has a trailing blank line: `Parser.LISTS_LOOSE_WHEN_PREV_HAS_TRAILING_BLANK_LINE`, `ListOptions.looseWhenPrevHasTrailingBlankLine`\n" + 
				"- item is loose if it has loose sub-item: `Parser.LISTS_LOOSE_WHEN_HAS_LOOSE_SUB_ITEM`, `ListOptions.looseWhenHasLooseSubItem`\n" + 
				"- item is loose if it has trailing blank line in it or its last child: `Parser.LISTS_LOOSE_WHEN_HAS_TRAILING_BLANK_LINE`, `ListOptions.looseWhenHasTrailingBlankLine`\n" + 
				"- item is loose if it has non-list children: `Parser.LISTS_LOOSE_WHEN_HAS_NON_LIST_CHILDREN`, `ListOptions.looseWhenHasNonListChildren`\n" + 
				"- all items are loose if any in the list are loose: `Parser.LISTS_AUTO_LOOSE`, `ListOptions.autoLoose`\n" + 
				"- auto loose list setting `Parser.LISTS_AUTO_LOOSE` only applies to simple 1 level lists: `Parser.LISTS_AUTO_LOOSE_ONE_LEVEL_LISTS`, `ListOptions.autoLooseOneLevelLists`\n" + 
				"- list item marker suffixes, content indent computed after suffix: `Parser.LISTS_ITEM_MARKER_SUFFIXES`, `ListOptions.itemMarkerSuffixes`\n" + 
				"- list item marker suffixes apply to numbered items: `Parser.LISTS_NUMBERED_ITEM_MARKER_SUFFIXED`, `ListOptions.numberedItemMarkerSuffixed`\n" + 
				"- list item marker suffixes are not used to affect indent offset for child items: `Parser.LISTS_ITEM_CONTENT_AFTER_SUFFIX`, default `false`, set to `true` to treat the item suffix as part of the list item marker after which the content begins for indentation purposes. Gfm Task List uses the default setting.\n" + 
				"\n" + 
				"<!-- @formatter:on -->\n" + 
				"\n" + 
				":warning: If both `LISTS_ITEM_TYPE_MISMATCH_TO_NEW_LIST` and\n" + 
				"`LISTS_ITEM_TYPE_MISMATCH_TO_SUB_LIST` are set to true then a new list will be created if the\n" + 
				"item had a blank line, otherwise a sub-list is created.\n" + 
				"\n" + 
				"#### List Item Paragraph Interruption Options\n" + 
				"\n" + 
				"<!-- @formatter:off -->\n" + 
				"\n" + 
				"- bullet item can interrupt a paragraph: `Parser.LISTS_BULLET_ITEM_INTERRUPTS_PARAGRAPH`, `ListOptions.itemInterrupt.bulletItemInterruptsParagraph`\n" + 
				"- ordered item can interrupt a paragraph: `Parser.LISTS_ORDERED_ITEM_INTERRUPTS_PARAGRAPH`, `ListOptions.itemInterrupt.orderedItemInterruptsParagraph`\n" + 
				"- ordered non 1 item can interrupt a paragraph: `Parser.LISTS_ORDERED_NON_ONE_ITEM_INTERRUPTS_PARAGRAPH`, `ListOptions.itemInterrupt.orderedNonOneItemInterruptsParagraph`\n" + 
				"- empty bullet item can interrupt a paragraph: `Parser.LISTS_EMPTY_BULLET_ITEM_INTERRUPTS_PARAGRAPH`, `ListOptions.itemInterrupt.emptyBulletItemInterruptsParagraph`\n" + 
				"- empty ordered item can interrupt a paragraph: `Parser.LISTS_EMPTY_ORDERED_ITEM_INTERRUPTS_PARAGRAPH`, `ListOptions.itemInterrupt.emptyOrderedItemInterruptsParagraph`\n" + 
				"- empty ordered non 1 item can interrupt a paragraph: `Parser.LISTS_EMPTY_ORDERED_NON_ONE_ITEM_INTERRUPTS_PARAGRAPH`, `ListOptions.itemInterrupt.emptyOrderedNonOneItemInterruptsParagraph`\n" + 
				"- bullet item can interrupt a paragraph of a list item: `Parser.LISTS_BULLET_ITEM_INTERRUPTS_ITEM_PARAGRAPH`, `ListOptions.itemInterrupt.bulletItemInterruptsItemParagraph`\n" + 
				"- ordered item can interrupt a paragraph of a list item: `Parser.LISTS_ORDERED_ITEM_INTERRUPTS_ITEM_PARAGRAPH`, `ListOptions.itemInterrupt.orderedItemInterruptsItemParagraph`\n" + 
				"- ordered non 1 item can interrupt a paragraph of a list item: `Parser.LISTS_ORDERED_NON_ONE_ITEM_INTERRUPTS_ITEM_PARAGRAPH`, `ListOptions.itemInterrupt.orderedNonOneItemInterruptsItemParagraph`\n" + 
				"- empty bullet item can interrupt a paragraph of a list item: `Parser.LISTS_EMPTY_BULLET_ITEM_INTERRUPTS_ITEM_PARAGRAPH`, `ListOptions.itemInterrupt.emptyBulletItemInterruptsItemParagraph`\n" + 
				"- empty ordered non 1 item can interrupt a paragraph of a list item: `Parser.LISTS_EMPTY_ORDERED_ITEM_INTERRUPTS_ITEM_PARAGRAPH`, `ListOptions.itemInterrupt.emptyOrderedItemInterruptsItemParagraph`\n" + 
				"- empty ordered item can interrupt a paragraph of a list item: `Parser.LISTS_EMPTY_ORDERED_NON_ONE_ITEM_INTERRUPTS_ITEM_PARAGRAPH`, `ListOptions.itemInterrupt.emptyOrderedNonOneItemInterruptsItemParagraph`\n" + 
				"- empty bullet sub-item can interrupt a paragraph of a list item: `Parser.LISTS_EMPTY_BULLET_SUB_ITEM_INTERRUPTS_ITEM_PARAGRAPH`, `ListOptions.itemInterrupt.emptyBulletSubItemInterruptsItemParagraph`\n" + 
				"- empty ordered non 1 sub-item can interrupt a paragraph of a list item: `Parser.LISTS_EMPTY_ORDERED_SUB_ITEM_INTERRUPTS_ITEM_PARAGRAPH`, `ListOptions.itemInterrupt.emptyOrderedSubItemInterruptsItemParagraph`\n" + 
				"- empty ordered sub-item can interrupt a paragraph of a list item: `Parser.LISTS_EMPTY_ORDERED_NON_ONE_SUB_ITEM_INTERRUPTS_ITEM_PARAGRAPH`, `ListOptions.itemInterrupt.emptyOrderedNonOneSubItemInterruptsItemParagraph`\n" + 
				"\n" + 
				"<!-- @formatter:on -->\n" + 
				"\n" + 
				"## Renderer\n" + 
				"\n" + 
				"Unified options handling added, existing configuration options were kept but now they modify the\n" + 
				"corresponding unified option.\n" + 
				"\n" + 
				"Renderer `Builder()` now has an `indentSize(int)` method to set size of indentation for\n" + 
				"hierarchical tags. Same as setting `HtmlRenderer.INDENT_SIZE` data key in options.\n" + 
				"\n" + 
				"Defined in `HtmlRenderer` class:\n" + 
				"\n" + 
				"<!-- @formatter:off -->\n" + 
				"\n" + 
				"- `AUTOLINK_WWW_PREFIX` : default `\"http://\"` prefix to add to autolink if it starts with `www.`\n" + 
				"- `CODE_STYLE_HTML_CLOSE` default ``(String) null, custom inline code close HTML\n" + 
				"- `CODE_STYLE_HTML_OPEN` default `(String) null`, custom inline code open HTML\n" + 
				"- `DO_NOT_RENDER_LINKS` default `false`, Disable link rendering in the document. This will cause sub-contexts to also have link rendering disabled.\n" + 
				"- `EMPHASIS_STYLE_HTML_CLOSE` default `(String) null`, custom emphasis close HTML\n" + 
				"- `EMPHASIS_STYLE_HTML_OPEN` default `(String) null`, custom emphasis open HTML\n" + 
				"- `ESCAPE_HTML_BLOCKS` default value of `ESCAPE_HTML`, escape html blocks found in the document\n" + 
				"- `ESCAPE_HTML_COMMENT_BLOCKS` default value of `ESCAPE_HTML_BLOCKS`, escape html comment blocks found in the document.\n" + 
				"- `ESCAPE_HTML` default `false`, escape all html found in the document\n" + 
				"- `ESCAPE_INLINE_HTML_COMMENTS` default value of `ESCAPE_HTML_BLOCKS`, escape inline html found in the document\n" + 
				"- `ESCAPE_INLINE_HTML` default value of `ESCAPE_HTML`, escape inline html found in the document\n" + 
				"- `FENCED_CODE_LANGUAGE_CLASS_PREFIX` default `\"language-\"`, prefix used for generating the `<code>` class for a fenced code block, only used if info is not empty\n" + 
				"- `FENCED_CODE_NO_LANGUAGE_CLASS` default `\"\"`                            `,<code>` class for `<code>` tag of indented code or fenced code block without info (language) specification\n" + 
				"- `FORMAT_FLAGS` default `0`, Flags used for `FormattingAppendable` used for rendering HTML\n" + 
				"- `GENERATE_HEADER_ID` default `false`, Generate a header id attribute using the configured `HtmlIdGenerator` but not render it. The id may be used by another element such as an anchor link.\n" + 
				"- `HARD_BREAK` default `\"<br />\\n\"`, string to use for rendering hard breaks\n" + 
				"- `HEADER_ID_GENERATOR_NO_DUPED_DASHES` default `false`, When `true` duplicate `-` in id will be replaced by a single `-`\n" + 
				"- `HEADER_ID_GENERATOR_RESOLVE_DUPES` default `true`, When `true` will add an incrementing integer to duplicate ids to make them unique\n" + 
				"- `HEADER_ID_GENERATOR_TO_DASH_CHARS` default `\"_\"`, set of characters to convert to `-` in text used to generate id, non-alpha numeric chars not in set will be removed\n" + 
				"- `HTML_BLOCK_CLOSE_TAG_EOL` default `true`, When `false` will suppress EOL after HTML block tags which are automatically generated during html rendering.\n" + 
				"- `HTML_BLOCK_OPEN_TAG_EOL` default `true`, When `false` will suppress EOL before HTML block tags which are automatically generated during html rendering.\n" + 
				"- `INDENT_SIZE` default `0`, how many spaces to use for each indent level of nested tags\n" + 
				"- `INLINE_CODE_SPLICE_CLASS` default `(String) null`, used for splitting and splicing inline code spans for source line tracking\n" + 
				"- `MAX_TRAILING_BLANK_LINES` default `1`, Maximum allowed trailing blank lines for rendered HTML\n" + 
				"- `OBFUSCATE_EMAIL_RANDOM` default `true`, When `false` will not use random number generator for obfuscation. Used for tests\n" + 
				"- `OBFUSCATE_EMAIL` default `false`, When `true` will obfuscate `mailto` links\n" + 
				"- `PERCENT_ENCODE_URLS` default `false`, percent encode urls\n" + 
				"- `RECHECK_UNDEFINED_REFERENCES` default `false`, Recheck the existence of refences in `Parser.REFERENCES` for link and image refs marked undefined. Used when new references are added after parsing\n" + 
				"- `RENDER_HEADER_ID` default `false`, Render a header id attribute for headers using the configured `HtmlIdGenerator`\n" + 
				"- `SOFT_BREAK` default `\"\\n\"`, string to use for rendering soft breaks\n" + 
				"- `SOURCE_POSITION_ATTRIBUTE` default `\"\"`, name of the source position HTML attribute, source position is assigned as `startOffset + '-' + endOffset`\n" + 
				"- `SOURCE_POSITION_PARAGRAPH_LINES` default `false`, if true enables wrapping individual paragraph source lines in `span` with source position attribute set\n" + 
				"- `SOURCE_WRAP_HTML_BLOCKS` default value of SOURCE_WRAP_HTML, if generating source position attribute, then wrap HTML blocks in `div` with source position\n" + 
				"- `SOURCE_WRAP_HTML` default `false`, if generating source position attribute, then wrap HTML with source position\n" + 
				"- `STRONG_EMPHASIS_STYLE_HTML_CLOSE` default `(String) null`, custom strong emphasis close HTML\n" + 
				"- `STRONG_EMPHASIS_STYLE_HTML_OPEN` default `(String) null`, custom strong emphasis open HTML\n" + 
				"- `SUPPRESS_HTML_BLOCKS` default value of `SUPPRESS_HTML`, suppress html output for html blocks\n" + 
				"- `SUPPRESS_HTML_COMMENT_BLOCKS` default value of `SUPPRESS_HTML_BLOCKS`, suppress html output for html comment blocks\n" + 
				"- `SUPPRESS_HTML` default `false`, suppress html output for all html\n" + 
				"- `SUPPRESS_INLINE_HTML_COMMENTS` default value of `SUPPRESS_INLINE_HTML`, suppress html output for inline html comments\n" + 
				"- `SUPPRESS_INLINE_HTML` default value of `SUPPRESS_HTML`, suppress html output for inline html\n" + 
				"- `SUPPRESS_LINKS` default value of `\"javascript:.*\"`, a regular expression to suppress any\n" + 
				"  links that match. The test occurs before the link is resolved using a link resolver. Therefore\n" + 
				"  any link matching will be suppressed before it is resolved. Likewise, a link resolver\n" + 
				"  returning a suppressed link will not be suppressed since this is up to the custom link\n" + 
				"  resolver to handle.\n" + 
				"                                                         \n" + 
				"  Suppressed links will render only the child nodes, effectively `[Something\n" + 
				"  New](javascript:alert(1))` will render as if it was `Something New`.\n" + 
				"  \n" + 
				"  Link suppression based on URL prefixes does not apply to HTML inline or block elements. Use\n" + 
				"  HTML suppression options for this. \n" + 
				"- `TYPE` default `\"HTML\"`, renderer type. Renderer type extensions can add their own. `JiraConverterExtension` defines `JIRA`\n" + 
				"- `UNESCAPE_HTML_ENTITIES` default `true`, When `false` will leave HTML entities as is in the rendered HTML.\n" + 
				"\n" + 
				"<!-- @formatter:on -->\n" + 
				"\n" + 
				":exclamation: All the escape and suppress options have dynamic default value. This allows you to\n" + 
				"set the `ESCAPE_HTML` and have all html escaped. If you set a value of a specific key then the\n" + 
				"set value will be used for that key. Similarly, comment affecting keys take their values from\n" + 
				"the non-comment counterpart. If you want to exclude comments from being affected by suppression\n" + 
				"or escaping then you need to set the corresponding comment key to `false` and set the\n" + 
				"non-comment key to `true`.\n" + 
				"\n" + 
				"## Formatting Module\n" + 
				"\n" + 
				"Formatter renders the AST as markdown with various formatting options to clean up and make the\n" + 
				"source consistent and possibly convert from one indentation rule set to another. Formatter API\n" + 
				"allows extensions to provide and handle formatting options for custom nodes. \n" + 
				"\n" + 
				"See: [[Markdown Formatter]]\n" + 
				"\n" + 
				"The formatter module can also be used to help translate the markdown document to another\n" + 
				"language by extracting translatable strings, replacing non-translatable strings with an\n" + 
				"identifier and finally replacing the translatable text spans with translated versions.\n" + 
				"\n" + 
				"See: [[Translation Helper API]]\n" + 
				"\n" + 
				"## PDF Output Module\n" + 
				"\n" + 
				"HTML to PDF conversion is done using [Open HTML To PDF] library by `PdfConverterExtension` in\n" + 
				"`flexmark-pdf-converter` module.\n" + 
				"\n" + 
				"[[Usage PDF Output|Usage#pdf-output]]\n" + 
				"\n" + 
				"## Available Extensions\n" + 
				"\n" + 
				"The following extensions are developed with this library, each in their own artifact.\n" + 
				"\n" + 
				"Extension options are defined in their extension class.\n" + 
				"\n" + 
				"## Abbreviation\n" + 
				"\n" + 
				"Allows to create abbreviations which will be replaced in plain text into `<abbr></abbr>` tags or\n" + 
				"optionally into `<a></a>` with titles for the abbreviation expansion.\n" + 
				"\n" + 
				"Use class `AbbreviationExtension` from artifact `flexmark-ext-abbreviation`.\n" + 
				"\n" + 
				"The following options are available:\n" + 
				"\n" + 
				"Defined in `AbbreviationExtension` class:\n" + 
				"\n" + 
				"| Static Field              | Default Value            | Description                                          |\n" + 
				"|:--------------------------|:-------------------------|:-----------------------------------------------------|\n" + 
				"| `ABBREVIATIONS`           | new repository           | repository for document's abbreviation definitions   |\n" + 
				"| `ABBREVIATIONS_KEEP`      | `KeepType.FIRST`         | which duplicates to keep.                            |\n" + 
				"| `USE_LINKS`               | `false`                  | use `<a>` instead of `<abb>` tags for rendering html |\n" + 
				"| `ABBREVIATIONS_PLACEMENT` | `ElementPlacement.AS_IS` | formatting option see: [[Markdown Formatter]]        |\n" + 
				"| `ABBREVIATIONS_SORT`      | `ElementPlacement.AS_IS` | formatting option see: [[Markdown Formatter]]        |\n" + 
				"\n" + 
				"## Admonition\n" + 
				"\n" + 
				"To create block-styled side content. Based on [Admonition Extension, Material for MkDocs]\n" + 
				"(*Personal opinion: Material for MkDocs is eye-candy. If you have not taken a look at it, you\n" + 
				"are missing out on a visual treat.*). See [[Admonition Extension]]\n" + 
				"\n" + 
				"Use class `AbbreviationExtension` from artifact `flexmark-ext-admonition`.\n" + 
				"\n" + 
				"#### CSS and JavaScript must be included in your page \n" + 
				"\n" + 
				"Default CSS and JavaScript are contained in the jar as resources:\n" + 
				"\n" + 
				"* [admonition.css](../blob/master/flexmark-ext-admonition/src/main/resources/admonition.css)\n" + 
				"* [admonition.js](../blob/master/flexmark-ext-admonition/src/main/resources/admonition.js)\n" + 
				"\n" + 
				"Their content is also available by calling `AdmonitionExtension.getDefaultCSS()` and\n" + 
				"`AdmonitionExtension.getDefaultScript()` static methods.\n" + 
				"\n" + 
				"The script should be included at the bottom of the body of the document and is used to toggle\n" + 
				"open/closed state of collapsible admonition elements.\n" + 
				"\n" + 
				"## AnchorLink\n" + 
				"\n" + 
				"Automatically adds anchor links to heading, using GitHub id generation algorithm\n" + 
				"\n" + 
				":warning: This extension will only render an anchor link for headers that have an id attribute\n" + 
				"associated with them. You need to have the `HtmlRenderer.GENERATE_HEADER_ID` option to set to\n" + 
				"`true` so that header ids are generated.\n" + 
				"\n" + 
				"Use class `AnchorLinkExtension` from artifact `flexmark-ext-anchorlink`.\n" + 
				"\n" + 
				"The following options are available:\n" + 
				"\n" + 
				"Defined in `AnchorLinkExtension` class:\n" + 
				"\n" + 
				"| Static Field               | Default Value | Description                                                      |\n" + 
				"|:---------------------------|:--------------|:-----------------------------------------------------------------|\n" + 
				"| `ANCHORLINKS_SET_ID`       | `true`        | whether to set the id attribute to the header id, if true        |\n" + 
				"| `ANCHORLINKS_SET_NAME`     | `false`       | whether to set the name attribute to the header id, if true      |\n" + 
				"| `ANCHORLINKS_WRAP_TEXT`    | `true`        | whether to wrap the heading text in the anchor, if true          |\n" + 
				"| `ANCHORLINKS_TEXT_PREFIX`  | `\"\"`          | raw html prefix. Added before heading text, wrapped or unwrapped |\n" + 
				"| `ANCHORLINKS_TEXT_SUFFIX`  | `\"\"`          | raw html suffix. Added before heading text, wrapped or unwrapped |\n" + 
				"| `ANCHORLINKS_ANCHOR_CLASS` | `\"\"`          | class for the `a` tag                                            |\n" + 
				"\n" + 
				"## Aside\n" + 
				"\n" + 
				"Same as block quotes but uses `|` for prefix and generates `<aside>` tags. To make it compatible\n" + 
				"with the table extension, aside block lines cannot have `|` as the last character of the line,\n" + 
				"and if using this extension the tables must have the lines terminate with a `|` otherwise they\n" + 
				"will be treated as aside blocks.\n" + 
				"\n" + 
				"Use class `AsideExtension` from artifact `flexmark-ext-aside`.\n" + 
				"\n" + 
				"Defined in `AsideExtension` class:\n" + 
				"\n" + 
				"| Static Field           | Default Value | Description                                                                                                                                |\n" + 
				"|:-----------------------|:--------------|:-------------------------------------------------------------------------------------------------------------------------------------------|\n" + 
				"| `IGNORE_BLANK_LINE`    | `false`       | aside block will include blank lines between aside blocks and treat them as if the blank lines are also preceded by the aside block marker |\n" + 
				"| `EXTEND_TO_BLANK_LINE` | `false`       | aside blocks extend to blank line when true. Enables more customary a la block quote parsing than commonmark strict standard               |\n" + 
				"\n" + 
				"## Attributes\n" + 
				"\n" + 
				"Converts attributes `{...}` syntax into attributes AST nodes and adds an attribute provider to\n" + 
				"set attributes for immediately preceding sibling element during HTML rendering. See\n" + 
				"[[Attributes Extension]]\n" + 
				"\n" + 
				"Defined in `AttributeExtension` from artifact `flexmark-ext-attributes`\n" + 
				"\n" + 
				"* `AttributesExtension.ASSIGN_TEXT_ATTRIBUTES`, default `true`. When `false` attribute\n" + 
				"  assignment rules to nodes are changed not to allow text elements to get attributes.\n" + 
				"\n" + 
				"Use class `AttributesExtension` from artifact `flexmark-ext-attributes`.\n" + 
				"\n" + 
				"Full spec:\n" + 
				"[ext_attributes_ast_spec](../blob/master/flexmark-ext-attributes/src/test/resources/ext_attributes_ast_spec.md)\n" + 
				"\n" + 
				"## Autolink\n" + 
				"\n" + 
				"Turns plain links such as URLs and email addresses into links (based on [autolink-java]).\n" + 
				"\n" + 
				":warning: current implementation has significant performance impact on large files.\n" + 
				"\n" + 
				"Use class `AutolinkExtension` from artifact `flexmark-ext-autolink`.\n" + 
				"\n" + 
				"Defined in `AsideExtension` class:\n" + 
				"\n" + 
				"* `IGNORE_LINKS` , default `\"\"`, a regex expression to match link text which should not be\n" + 
				"  auto-linked. This can include full urls like `www.google.com` or parts by including wildcard\n" + 
				"  match patterns. Any recognized auto-link which matches the expression will be rendered as\n" + 
				"  text.\n" + 
				"\n" + 
				"## Definition Lists\n" + 
				"\n" + 
				"Converts definition syntax of [Php Markdown Extra Definition List] to `<dl></dl>` HTML and\n" + 
				"corresponding AST nodes.\n" + 
				"\n" + 
				"Definition items can be preceded by `:` or `~`, depending on the configured options.\n" + 
				"\n" + 
				"Use class `DefinitionExtension` from artifact `flexmark-ext-definition`.\n" + 
				"\n" + 
				"The following options are available:\n" + 
				"\n" + 
				"Defined in `DefinitionExtension` class:\n" + 
				"\n" + 
				"| Static Field                    | Default Value          | Description                                                                                               |\n" + 
				"|:--------------------------------|:-----------------------|:----------------------------------------------------------------------------------------------------------|\n" + 
				"| `COLON_MARKER`                  | `true`                 | enable use of `:` as definition item marker                                                               |\n" + 
				"| `MARKER_SPACES`                 | `1`                    | minimum number of spaces after definition item marker for valid definition item                           |\n" + 
				"| `TILDE_MARKER`                  | `true`                 | enable use of `~` as definition item marker                                                               |\n" + 
				"| `DOUBLE_BLANK_LINE_BREAKS_LIST` | `false`                | When true double blank line between definition item and next definition term will break a definition list |\n" + 
				"| `FORMAT_MARKER_SPACES`          | `3`                    | formatting option see: [[Markdown Formatter]]                                                             |\n" + 
				"| `FORMAT_MARKER_TYPE`            | `DefinitionMarker.ANY` | formatting option see: [[Markdown Formatter]]                                                             |\n" + 
				"\n" + 
				":information_source: this extension uses list parsing and indentation rules and will its best to\n" + 
				"align list item and definition item parsing according to selected options. For non-fixed indent\n" + 
				"family of parsers will use the definition item content indent column for sub-items, otherwise\n" + 
				"uses the `Parser.LISTS_ITEM_INDENT` value for sub-items.\n" + 
				"\n" + 
				"Wiki: [[Definition List Extension]]\n" + 
				"\n" + 
				"## Docx Converter\n" + 
				"\n" + 
				"Renders the parsed Markdown AST to docx format using the [docx4j] library.\n" + 
				"\n" + 
				"artifact: `flexmark-docx-converter`\n" + 
				"\n" + 
				"See the [DocxConverterCommonMark Sample] for code and [[Customizing Docx Rendering]] for an\n" + 
				"overview and information on customizing the styles.\n" + 
				"\n" + 
				"Pegdown version can be found in [DocxConverterPegdown Sample]\n" + 
				"\n" + 
				"For details see [[Docx Renderer Extension]]\n" + 
				"\n" + 
				"## Emoji\n" + 
				"\n" + 
				"Allows to create image link to emoji images from emoji shortcuts using [Emoji-Cheat-Sheet.com]\n" + 
				"and optionally to replace with its unicode equivalent character with mapping by Mark Wunsch\n" + 
				"found at [mwunsch/rumoji](https://github.com/mwunsch/rumoji)\n" + 
				"\n" + 
				"Use class `EmojiExtension` from artifact `flexmark-ext-emoji`.\n" + 
				"\n" + 
				"The following options are available:\n" + 
				"\n" + 
				"Defined in `EmojiExtension` class:\n" + 
				"\n" + 
				"* `ATTR_ALIGN`, default `\"absmiddle\"`, attributes to use for rendering in the absence of\n" + 
				"  attribute provider overrides\n" + 
				"* `ATTR_IMAGE_SIZE`, default `\"20\"` , attributes to use for rendering in the absence of\n" + 
				"  attribute provider overrides\n" + 
				"* `ATTR_IMAGE_CLASS`, default `\"\"`, if not empty will set the image tag class attribute to this\n" + 
				"  value.\n" + 
				"* `ROOT_IMAGE_PATH`, default `\"/img/\"` , root path for emoji image files. See\n" + 
				"  <https://github.com/arvida/emoji-cheat-sheet.com>\n" + 
				"* `USE_SHORTCUT_TYPE`, default `EmojiShortcutType.EMOJI_CHEAT_SHEET`, select type of shortcuts:\n" + 
				"  * `EmojiShortcutType.EMOJI_CHEAT_SHEET`\n" + 
				"  * `EmojiShortcutType.GITHUB`\n" + 
				"  * `EmojiShortcutType.ANY_EMOJI_CHEAT_SHEET_PREFERRED` use any shortcut from any source. If\n" + 
				"    image type options is not `UNICODE_ONLY`, will generate links to Emoji Cheat Sheet files or\n" + 
				"    GitHub URL, with preference given to Emoji Cheat Sheet files.\n" + 
				"  * `EmojiShortcutType.ANY_GITHUB_PREFERRED` - use any shortcut from any source. If image type\n" + 
				"    options is not `UNICODE_ONLY`, will generate links to Emoji Cheat Sheet files or GitHub URL,\n" + 
				"    with preference given to GitHub URL.\n" + 
				"* `USE_IMAGE_TYPE`, default `EmojiImageType.IMAGE_ONLY`, to select what type of images are\n" + 
				"  allowed.\n" + 
				"  * `EmojiImageType.IMAGE_ONLY`, only use image link\n" + 
				"  * `EmojiImageType.UNICODE_ONLY` convert to unicode and if there is no unicode treat as invalid\n" + 
				"    emoji shortcut\n" + 
				"  * `EmojiImageType.UNICODE_FALLBACK_TO_IMAGE` convert to unicode and if no unicode use image.\n" + 
				"    emoji shortcuts using Emoji-Cheat-Sheet.com http://www.emoji-cheat-sheet.com/\n" + 
				"\n" + 
				"## Enumerated Reference\n" + 
				"\n" + 
				"Used to create numbered references and numbered text labels for elements in the document.\n" + 
				"[[Enumerated References Extension]]\n" + 
				"\n" + 
				"Use class `EnumeratedReferenceExtension` from artifact `flexmark-ext-enumerated-reference`.\n" + 
				"\n" + 
				":exclamation: Note [Attributes](#attributes) extension is needed in order for references to be\n" + 
				"properly resolved for rendering.\n" + 
				"\n" + 
				"## Footnotes\n" + 
				"\n" + 
				"Creates footnote references in the document. Footnotes are placed at the bottom of the rendered\n" + 
				"document with links from footnote references to footnote and vice-versa. [[Footnotes Extension]] \n" + 
				"\n" + 
				"Converts: `[^footnote]` to footnote references and `[^footnote]: footnote definition` to\n" + 
				"footnotes in the document.\n" + 
				"\n" + 
				"## Gfm-Issues\n" + 
				"\n" + 
				"Enables Gfm issue reference parsing in the form of `#123`\n" + 
				"\n" + 
				"Use class `GfmIssuesExtension` in artifact `flexmark-ext-gfm-issues`.\n" + 
				"\n" + 
				"The following options are available:\n" + 
				"\n" + 
				"Defined in `GfmIssuesExtension` class:\n" + 
				"\n" + 
				"<!-- @formatter:off -->\n" + 
				"\n" + 
				"* `GIT_HUB_ISSUES_URL_ROOT` : default `\"issues\"` override root used for generating the URL\n" + 
				"* `GIT_HUB_ISSUE_URL_PREFIX` : default `\"/\"` override prefix used for appending the issue number to URL\n" + 
				"* `GIT_HUB_ISSUE_URL_SUFFIX` : default `\"\"` override suffix used for appending the issue number to URL\n" + 
				"* `GIT_HUB_ISSUE_HTML_PREFIX` : default `\"\"` override HTML to use as prefix for the issue text\n" + 
				"* `GIT_HUB_ISSUE_HTML_SUFFIX` : default `\"\"` override HTML to use as suffix for the issue text\n" + 
				"\n" + 
				"<!-- @formatter:on -->\n" + 
				"\n" + 
				"## Gfm-Strikethrough/Subscript\n" + 
				"\n" + 
				"Enables strikethrough of text by enclosing it in `~~`. For example, in `hey ~~you~~`, `you` will\n" + 
				"be rendered as strikethrough text.\n" + 
				"\n" + 
				"Use class `StrikethroughExtension` in artifact `flexmark-ext-gfm-strikethrough`.\n" + 
				"\n" + 
				"Enables subscript of text by enclosing it in `~`. For example, in `hey ~you~`, `you` will be\n" + 
				"rendered as subscript text.\n" + 
				"\n" + 
				"Use class `SubscriptExtension` in artifact `flexmark-ext-gfm-strikethrough`.\n" + 
				"\n" + 
				"To enables both subscript and strike through:\n" + 
				"\n" + 
				"Use class `StrikethroughSubscriptExtension` in artifact `flexmark-ext-gfm-strikethrough`.\n" + 
				"\n" + 
				":warning: Only one of these extensions can be included in the extension list. If you want both\n" + 
				"strikethrough and subscript use the `StrikethroughSubscriptExtension`.\n" + 
				"\n" + 
				"The following options are available:\n" + 
				"\n" + 
				"Defined in `StrikethroughSubscriptExtension` class:\n" + 
				"\n" + 
				"<!-- @formatter:off -->\n" + 
				"\n" + 
				"* `STRIKETHROUGH_STYLE_HTML_OPEN` : default `(String)null` override HTML to use for wrapping style, if null then no override\n" + 
				"* `STRIKETHROUGH_STYLE_HTML_CLOSE` : default `(String)null` override HTML to use for wrapping style, if null then no override\n" + 
				"* `SUBSCRIPT_STYLE_HTML_OPEN` : default `(String)null` override HTML to use for wrapping style, if null then no override\n" + 
				"* `SUBSCRIPT_STYLE_HTML_CLOSE` : default `(String)null` override HTML to use for wrapping style, if null then no override\n" + 
				"\n" + 
				"<!-- @formatter:on -->\n" + 
				"\n" + 
				"## Gfm-TaskList\n" + 
				"\n" + 
				"Enables list items based task lists using: `[ ]`, `[x]` or `[X]`\n" + 
				"\n" + 
				"Use class `TaskListExtension` in artifact `flexmark-ext-gfm-tasklist`.\n" + 
				"\n" + 
				"The following options are available:\n" + 
				"\n" + 
				"Defined in `TaskListExtension` class:\n" + 
				"\n" + 
				"| Static Field                 | Default Value                                                                                                                                                                                 | Description                                                                         |\n" + 
				"|:-----------------------------|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:------------------------------------------------------------------------------------|\n" + 
				"| `ITEM_DONE_MARKER`           | `<input`<br>&nbsp;&nbsp;&nbsp;`type=\"checkbox\"`<br>&nbsp;&nbsp;&nbsp;`class=\"task-list-item-checkbox\"`<br>&nbsp;&nbsp;&nbsp;`checked=\"checked\"`<br>&nbsp;&nbsp;&nbsp;`disabled=\"disabled\" />` | string to use for the item done marker html.                                        |\n" + 
				"| `ITEM_NOT_DONE_MARKER`       | `<input`<br>&nbsp;&nbsp;&nbsp;`type=\"checkbox\"`<br>&nbsp;&nbsp;&nbsp;`class=\"task-list-item-checkbox\"`<br>&nbsp;&nbsp;&nbsp;`disabled=\"disabled\" />`                                          | string to use for the item not done marker html.                                    |\n" + 
				"| `ITEM_CLASS`                 | `\"task-list-item\"`                                                                                                                                                                            | tight list item class attribute                                                     |\n" + 
				"| `LOOSE_ITEM_CLASS`           | value of `ITEM_CLASS`                                                                                                                                                                         | loose list item class attribute, if not set then will use value of tight item class |\n" + 
				"| `PARAGRAPH_CLASS`            | `\"\"`                                                                                                                                                                                          | p tag class attribute, only applies to loose list items                             |\n" + 
				"| `FORMAT_LIST_ITEM_CASE`      | `TaskListItemCase.AS_IS`                                                                                                                                                                      | formatting option see: [[Markdown Formatter]]                                       |\n" + 
				"| `FORMAT_LIST_ITEM_PLACEMENT` | `TaskListItemPlacement.AS_IS`                                                                                                                                                                 | formatting option see: [[Markdown Formatter]]                                       |\n" + 
				"\n" + 
				"* `TaskListItemCase`\n" + 
				"  *  `AS_IS`: no change\n" + 
				"  *  `LOWERCASE`: change `[X]` to `[x]`\n" + 
				"  *  `UPPERCASE`: change `[x]` to `[X]`\n" + 
				"\n" + 
				"* `TaskListItemPlacement`\n" + 
				"  * `AS_IS`: no change\n" + 
				"  * `INCOMPLETE_FIRST`: sort all lists to put incomplete task items first\n" + 
				"  * `INCOMPLETE_NESTED_FIRST`: sort all lists to put incomplete task items or items with\n" + 
				"    incomplete task sub-items first\n" + 
				"  * `COMPLETE_TO_NON_TASK`: sort all lists to put incomplete task items first and change\n" + 
				"    complete task items to non-task items\n" + 
				"  * `COMPLETE_NESTED_TO_NON_TASK`: sort all lists to put incomplete task items or items with\n" + 
				"    incomplete task sub-items first and change complete task items to non-task items\n" + 
				"\n" + 
				"## Gfm-Users\n" + 
				"\n" + 
				"Enables Gfm user reference parsing in the form of `#123`\n" + 
				"\n" + 
				"Use class `GfmUsersExtension` in artifact `flexmark-ext-gfm-users`.\n" + 
				"\n" + 
				"The following options are available:\n" + 
				"\n" + 
				"Defined in `GfmUsersExtension` class:\n" + 
				"\n" + 
				"<!-- @formatter:off -->\n" + 
				"\n" + 
				"* `GIT_HUB_USERS_URL_ROOT` : default `\"https://github.com\"` override root used for generating the URL\n" + 
				"* `GIT_HUB_USER_URL_PREFIX` : default `\"/\"` override prefix used for appending the user name to URL\n" + 
				"* `GIT_HUB_USER_URL_SUFFIX` : default `\"\"` override suffix used for appending the user name to URL\n" + 
				"* `GIT_HUB_USER_HTML_PREFIX` : default `\"<strong>\"` override HTML to use as prefix for the user name text\n" + 
				"* `GIT_HUB_USER_HTML_SUFFIX` : default `\"</strong>\"` override HTML to use as suffix for the user name text\n" + 
				"\n" + 
				"<!-- @formatter:on -->\n" + 
				"\n" + 
				"## Html To Markdown\n" + 
				"\n" + 
				"Not really and extension but useful if you need to convert HTML to Markdown.\n" + 
				"\n" + 
				"Converts HTML to Markdown, assumes all non-application specific extensions are going to be used:\n" + 
				"\n" + 
				"* abbreviations\n" + 
				"* aside\n" + 
				"* block quotes\n" + 
				"* bold, italic, inline code\n" + 
				"* bullet and numbered lists\n" + 
				"* definition\n" + 
				"* fenced code\n" + 
				"* strike through\n" + 
				"* subscript\n" + 
				"* superscript\n" + 
				"* tables\n" + 
				"* Gfm Task list item\n" + 
				"* will also handle conversion for multi-line URL images\n" + 
				"\n" + 
				"Use class `FlexmarkHtmlParser` in artifact `flexmark-html-parser`.\n" + 
				"\n" + 
				"options for output control, pass via `DataHolder` taking method `parse()` method. The following\n" + 
				"options are available:\n" + 
				"\n" + 
				"Defined in `FlexmarkHtmlParser` class:\n" + 
				"\n" + 
				"<!-- @formatter:off -->\n" + 
				"\n" + 
				"* `BR_AS_EXTRA_BLANK_LINES` default `true`, when true `<br>` encountered after a blank line is already in output or current output ends in `<br>` then will insert an inline HTML `<br>` into output to create extra blank lines in rendered result.\n" + 
				"* `BR_AS_PARA_BREAKS` default `true`, when true `<br>` encountered at the beginning of a new line will be treated as a paragraph break\n" + 
				"* `CODE_INDENT`, default 4 spaces, indent to use for indented code\n" + 
				"* `DEFINITION_MARKER_SPACES`, default `3`, min spaces after `:` for definitions\n" + 
				"* `DIV_AS_PARAGRAPH` default `false`, when true will treat `<div>` wrapped text as if it was `<p>` wrapped by adding a blank line after the text.\n" + 
				"* `DOT_ONLY_NUMERIC_LISTS`, default `true`. When set to false closing parenthesis as a list delimiter will be used in Markdown if present in MS-Word style list. Otherwise parenthesis delimited list will be converted to dot `.`.\n" + 
				"* `EOL_IN_TITLE_ATTRIBUTE`, default `\" \"`, string to use in place of EOL in image and link title attribute.\n" + 
				"* `LISTS_END_ON_DOUBLE_BLANK` default `false`, when set to `true` consecutive lists are separated by double blank line, otherwise by an empty HTML comment line.\n" + 
				"* `LIST_CONTENT_INDENT`, default `true`, continuation lines of list items and definitions indent to content column otherwise 4 spaces\n" + 
				"* `MIN_SETEXT_HEADING_MARKER_LENGTH`, default `3`, min 3, minimum setext heading marker length\n" + 
				"* `MIN_TABLE_SEPARATOR_COLUMN_WIDTH`, default `1`, min 1, minimum number of `-` in separator column, excluding alignment colons `:`\n" + 
				"* `MIN_TABLE_SEPARATOR_DASHES`, default `3`, min 3, minimum separator column width, including alignment colons `:`\n" + 
				"* `NBSP_TEXT`, default `\" \"`, string to use in place of non-break-space\n" + 
				"* `ORDERED_LIST_DELIMITER`, default `'.'`, delimiter for ordered items\n" + 
				"* `OUTPUT_UNKNOWN_TAGS`, default `false`, when true unprocessed tags will be output, otherwise they are ignored\n" + 
				"* `RENDER_COMMENTS`, default `false`. When set to true HTML comments will be rendered in the Markdown.\n" + 
				"* `SETEXT_HEADINGS`, default `true`, if true then use Setext headings for h1 and h2\n" + 
				"* `THEMATIC_BREAK`, default `\"*** ** * ** ***\"`, `<hr>` replacement\n" + 
				"* `UNORDERED_LIST_DELIMITER`, default `'*'`, delimiter for unordered list items\n" + 
				"* `OUTPUT_ATTRIBUTES_ID` is `true`, default `true`, id attribute will be rendered in [Attributes Extension](#attributes) syntax\n" + 
				"* `OUTPUT_ATTRIBUTES_NAMES_REGEX` default `\"\"`, if not empty then will output attributes extension syntax for attribute names that are matched by the regex.\n" + 
				"\n" + 
				"<!-- @formatter:on -->\n" + 
				"\n" + 
				"Uses the excellent [jsoup](https://jsoup.org/) HTML parsing library and emoji shortcuts using\n" + 
				"[Emoji-Cheat-Sheet.com](http://www.emoji-cheat-sheet.com/)\n" + 
				"\n" + 
				"## Ins\n" + 
				"\n" + 
				"Enables ins tagging of text by enclosing it in `++`. For example, in `hey ++you++`, `you` will\n" + 
				"be rendered as inserted text.\n" + 
				"\n" + 
				"Use class `InsExtension` in artifact `flexmark-ext-ins`.\n" + 
				"\n" + 
				"The following options are available:\n" + 
				"\n" + 
				"Defined in `InsExtension` class:\n" + 
				"\n" + 
				"<!-- @formatter:off -->\n" + 
				"\n" + 
				"* `INS_STYLE_HTML_CLOSE` : default `(String)null` override HTML to use for wrapping style, if null then no override\n" + 
				"* `INS_STYLE_HTML_OPEN` : default `(String)null` override HTML to use for wrapping style, if null then no override\n" + 
				"\n" + 
				"<!-- @formatter:on -->\n" + 
				"\n" + 
				"## Jekyll Tags\n" + 
				"\n" + 
				"Allows rendering of Jekyll `{% tag %}` with and without parameters.\n" + 
				"\n" + 
				"Use class `JekyllTagExtension` in artifact `flexmark-ext-jekyll-tag`.\n" + 
				"\n" + 
				":information_source: you can process the include tags and add the content of these files to the\n" + 
				"`INCLUDED_HTML` map so that the content is rendered. If the included content starts off as\n" + 
				"Markdown and may contain references that are used by the document which includes the file, then\n" + 
				"you can transfer the references from the included document to the including document using the\n" + 
				"`Parser.transferReferences(Document, Document)`\n" + 
				"\n" + 
				"The following options are available:\n" + 
				"\n" + 
				"Defined in `JekyllTagExtension` class:\n" + 
				"\n" + 
				"| Static Field         | Default Value                | Description                                                                                            |\n" + 
				"|:---------------------|:-----------------------------|:-------------------------------------------------------------------------------------------------------|\n" + 
				"| `ENABLE_INLINE_TAGS` | `true`                       | parse inline tags.                                                                                     |\n" + 
				"| `ENABLE_BLOCK_TAGS`  | `true`                       | parse block tags.                                                                                      |\n" + 
				"| `ENABLE_RENDERING`   | `false`                      | render tags as text                                                                                    |\n" + 
				"| `INCLUDED_HTML`      | `(Map<String, String>)null`  | map of include tag parameter string to HTML content to be used to replace the tag element in rendering |\n" + 
				"| `LIST_INCLUDES_ONLY` | `true`                       | only add include tags to TAG_LIST if true, else add all tags                                           |\n" + 
				"| `TAG_LIST`           | `new ArrayList<JekyllTag>()` | list of all jekyll tags in the document                                                                |\n" + 
				"\n" + 
				"## Jira-Converter\n" + 
				"\n" + 
				"Allows rendering of the markdown AST as JIRA formatted text\n" + 
				"\n" + 
				"Use class `JiraConverterExtension` in artifact `flexmark-jira-converter`.\n" + 
				"\n" + 
				"No options are defined. Extensions that do no support JIRA formatting will not generate any\n" + 
				"output for their corresponding nodes.\n" + 
				"\n" + 
				"## Media Tags\n" + 
				"\n" + 
				"Media link transformer extension courtesy Cornelia Schultz (GitHub @CorneliaXaos) transforms links using\n" + 
				"custom prefixes to Audio, Embed, Picture, and Video HTML5 tags.\n" + 
				"\n" + 
				"* `!A[Text](link|orLinks)` - audio\n" + 
				"* `!E[Text](link|orLinks)` - embed\n" + 
				"* `!P[Text](link|orLinks)` - picture\n" + 
				"* `!V[Text](link|orLinks)` - video\n" + 
				"\n" + 
				"Use class `MediaTagsExtension` in artifact `flexmark-ext-media-tags`.\n" + 
				"\n" + 
				"No options are defined. \n" + 
				"\n" + 
				"## Spec Example\n" + 
				"\n" + 
				"Parses the flexmark extended spec examples into example nodes with particulars broken and many\n" + 
				"rendering options. Otherwise these parse as fenced code. [[Flexmark Spec Example Extension]] \n" + 
				"\n" + 
				"This extension is used by [Markdown Navigator] plugin for JetBrains IDEs to facilitate work with\n" + 
				"flexmark-java test spec files.\n" + 
				"\n" + 
				"[Markdown Navigator]: http://vladsch.com/product/markdown-navigator\n" + 
				"\n" + 
				"## Superscript\n" + 
				"\n" + 
				"Enables ins tagging of text by enclosing it in `^`. For example, in `hey ^you^`, `you` will be\n" + 
				"rendered as superscript text.\n" + 
				"\n" + 
				"Use class `SuperscriptExtension` in artifact `flexmark-ext-superscript`.\n" + 
				"\n" + 
				"The following options are available:\n" + 
				"\n" + 
				"Defined in `SuperscriptExtension` class:\n" + 
				"\n" + 
				"<!-- @formatter:off -->\n" + 
				"\n" + 
				"* `SUPERSCRIPT_STYLE_HTML_CLOSE` : default `(String)null` override HTML to use for wrapping style, if null then no override\n" + 
				"* `SUPERSCRIPT_STYLE_HTML_OPEN` : default `(String)null` override HTML to use for wrapping style, if null then no override\n" + 
				"\n" + 
				"<!-- @formatter:on -->\n" + 
				"\n" + 
				"## Tables\n" + 
				"\n" + 
				"Enables tables using pipes as in [GitHub Flavored Markdown][gfm-tables]. With added options to\n" + 
				"handle column span syntax, ability to have more than one header row, disparate column numbers\n" + 
				"between rows, etc. [[Tables Extension]] \n" + 
				"\n" + 
				"## Table of Contents\n" + 
				"\n" + 
				"Table of contents extension is really two extensions in one: `[TOC]` element which renders a\n" + 
				"table of contents and a simulated `[TOC]:#` element which also renders a table of contents but\n" + 
				"is also intended for post processors that will append a table of contents after the element.\n" + 
				"Resulting in a source file with a table of contents element which will render on any markdown\n" + 
				"processor.\n" + 
				"\n" + 
				"Use class `TocExtension` or `SimTocExtension` in artifact `flexmark-ext-toc`.\n" + 
				"\n" + 
				"For details see [[Table of Contents Extension]]\n" + 
				"\n" + 
				"## Typographic\n" + 
				"\n" + 
				"Converts plain text to typographic characters. [[Typographic Extension]]\n" + 
				"\n" + 
				"* `'` to apostrophe `&rsquo;` &rsquo;\n" + 
				"* `...` and `. . .` to ellipsis `&hellip;` &hellip;\n" + 
				"* `--` en dash `&ndash;` &ndash;\n" + 
				"* `---` em dash `&mdash;` &mdash;\n" + 
				"* single quoted `'some text'` to `&lsquo;some text&rsquo;` &lsquo;some text&rsquo;\n" + 
				"* double quoted `\"some text\"` to `&ldquo;some text&rdquo;` &ldquo;some text&rdquo;\n" + 
				"* double angle quoted `<<some text>>` to `&laquo;some text&raquo;` &laquo;some text&raquo;\n" + 
				"\n" + 
				"## WikiLinks\n" + 
				"\n" + 
				"Enables wiki links `[[page reference]]` and optionally wiki images `![[image reference]]`\n" + 
				"\n" + 
				"To properly resolve wiki link to URL you will most likely need to implement a custom link\n" + 
				"resolver to handle the logic of your project. Please see:\n" + 
				"[PegdownCustomLinkResolverOptions](https://github.com/vsch/flexmark-java/blob/master/flexmark-java-samples/src/com/vladsch/flexmark/samples/PegdownCustomLinkResolverOptions.java)\n" + 
				"\n" + 
				"Use class `WikiLinkExtension` in artifact `flexmark-ext-wikilink`.\n" + 
				"\n" + 
				"The following options are available:\n" + 
				"\n" + 
				"Defined in `WikiLinkExtension` class:\n" + 
				"\n" + 
				"| Static Field            | Default Value           | Description                                                                                                                                             |\n" + 
				"|:------------------------|:------------------------|:--------------------------------------------------------------------------------------------------------------------------------------------------------|\n" + 
				"| `ALLOW_INLINES`         | `false`                 | to allow delimiter processing in text part when `|` is used, or when combined text and page ref in the contained text.                                  |\n" + 
				"| `DISABLE_RENDERING`     | `false`                 | disables wiki link rendering if true wiki links render the text of the node. Used to parse WikiLinks into the AST but not render them in the HTML       |\n" + 
				"| `IMAGE_LINKS`           | `false`                 | enables wiki image link format `![[]]` same as wiki links but with a `!` prefix, alt text is same as wiki link text and affected by `LINK_FIRST_SYNTAX` |\n" + 
				"| `IMAGE_PREFIX`          | `\"\"`                    | Prefix to add to the to generated link URL                                                                                                              |\n" + 
				"| `IMAGE_PREFIX_ABSOLUTE` | value of `IMAGE_PREFIX` | Prefix to add to the to generated link URL for absolute wiki links (starting with `/`)                                                                  |\n" + 
				"| `IMAGE_FILE_EXTENSION`  | `\"\"`                    | Extension to append to generated link URL                                                                                                               |\n" + 
				"| `LINK_FIRST_SYNTAX`     | `false`                 | When two part syntax is used `[[first|second]]`, true if the link part comes first, Creole syntax; or false when text comes first, GFM syntax           |\n" + 
				"| `LINK_PREFIX`           | `\"\"`                    | Prefix to add to the to generated link URL                                                                                                              |\n" + 
				"| `LINK_PREFIX_ABSOLUTE`  | value of `LINK_PREFIX`  | Prefix to add to the to generated link URL for absolute wiki links (starting with `/`)                                                                  |\n" + 
				"| `LINK_FILE_EXTENSION`   | `\"\"`                    | Extension to append to generated link URL                                                                                                               |\n" + 
				"| `LINK_ESCAPE_CHARS`     | `\" +/<>\"`               | characters to replace in page ref by replacing them with corresponding characters in `LINK_REPLACE_CHARS`                                               |\n" + 
				"| `LINK_REPLACE_CHARS`    | `\"-----\"`               | characters to replace in page ref by replacing corresponding characters in `LINK_ESCAPE_CHARS`                                                          |\n" + 
				"\n" + 
				"## XWiki Macro Extension\n" + 
				"\n" + 
				"Application provided macros of the form `{{macroName}}content{{/macroName}}` or the block macro\n" + 
				"form:\n" + 
				"\n" + 
				"```markdown\n" + 
				"{{macroName}}\n" + 
				"content\n" + 
				"{{/macroName}}\n" + 
				"```\n" + 
				"\n" + 
				"Use class `MacroExtension` in artifact `flexmark-ext-xwiki-macros`.\n" + 
				"\n" + 
				"The following options are available:\n" + 
				"\n" + 
				"Defined in `MacroExtension` class:\n" + 
				"\n" + 
				"| Static Field           | Default Value | Description                             |\n" + 
				"|:-----------------------|:--------------|:----------------------------------------|\n" + 
				"| `ENABLE_INLINE_MACROS` | `true`        | enable inline macro processing          |\n" + 
				"| `ENABLE_BLOCK_MACROS`  | `true`        | enable block macro processing           |\n" + 
				"| `ENABLE_RENDERING`     | `false`       | enable rendering of macro nodes as text |\n" + 
				"\n" + 
				"## YAML front matter\n" + 
				"\n" + 
				"Adds support for metadata through a YAML front matter block. This extension only supports a\n" + 
				"subset of YAML syntax. Here's an example of what's supported:\n" + 
				"\n" + 
				"```markdown\n" + 
				"---\n" + 
				"\n" + 
				"key: value list: - value 1 - value 2 literal: | this is literal value.\n" + 
				"\n" + 
				"literal values 2\n" + 
				"---\n" + 
				"\n" + 
				"document starts here\n" + 
				"```\n" + 
				"\n" + 
				"Use class `YamlFrontMatterExtension` in artifact `flexmark-ext-yaml-front-matter`. To fetch\n" + 
				"metadata, use `YamlFrontMatterVisitor`.\n" + 
				"\n" + 
				"## YouTrack-Converter\n" + 
				"\n" + 
				"Allows rendering of the markdown AST as YouTrack formatted text\n" + 
				"\n" + 
				"Use class `YouTrackConverterExtension` in artifact `flexmark-youtrack-converter`.\n" + 
				"\n" + 
				"No options are defined. Extensions that do no support YouTrack formatting will not generate any\n" + 
				"output for their corresponding nodes.\n" + 
				"\n" + 
				"## YouTube Embedded Link Transformer\n" + 
				"\n" + 
				"YouTube link transformer extension courtesy Vyacheslav N. Boyko (GitHub @bvn13) transforms\n" + 
				"simple links to youtube videos to embedded video iframe.\n" + 
				"\n" + 
				"Use class `YouTubeLinkExtension` in artifact `flexmark-ext-youtube-embedded`.\n" + 
				"\n" + 
				"No options are defined.\n" + 
				"\n" + 
				"Converts simple links to youtube videos to embedded video HTML code.\n" + 
				"\n" + 
				"[Admonition Extension, Material for MkDocs]: https://squidfunk.github.io/mkdocs-material/extensions/admonition/\n" + 
				"[autolink-java]: https://github.com/robinst/autolink-java\n" + 
				"[docx4j]: https://www.docx4java.org/trac/docx4j\n" + 
				"[DocxConverterCommonMark Sample]: https://github.com/vsch/flexmark-java/blob/master/flexmark-java-samples/src/com/vladsch/flexmark/samples/DocxConverterCommonMark.java\n" + 
				"[DocxConverterPegdown Sample]: https://github.com/vsch/flexmark-java/blob/master/flexmark-java-samples/src/com/vladsch/flexmark/samples/DocxConverterPegdown.java\n" + 
				"[Emoji-Cheat-Sheet.com]: http://www.emoji-cheat-sheet.com/\n" + 
				"[gfm-tables]: https://help.github.com/articles/organizing-information-with-tables/\n" + 
				"[Maven Central]: https://search.maven.org/#search|ga|1|g%3A%22com.vladsch.flexmark%22\n" + 
				"[Open HTML To PDF]: https://github.com/danfickle/openhtmltopdf\n" + 
				"[Php Markdown Extra Definition List]: https://michelf.ca/projects/php-markdown/extra/#def-list\n" + 
				"[CommonMark]: http://commonmark.org/\n" + 
				"[commonmark.js]: https://github.com/jgm/commonmark.js\n" + 
				"[Markdown]: https://daringfireball.net/projects/markdown/\n" + 
				"[Semantic Versioning]: http://semver.org/\n" + 
				"\n" + 
				"";
		String out = service.renderToHtml(input);
		OutputStream os = new FileOutputStream("C:/Users/Aidi/Desktop/t.html");
		os.write(out.getBytes());
		os.close();
	}
}
