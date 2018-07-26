package cn.lanyj.services.support.web.markdown.impl;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.vladsch.flexmark.Extension;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ext.anchorlink.AnchorLink;
import com.vladsch.flexmark.ext.anchorlink.AnchorLinkExtension;
import com.vladsch.flexmark.ext.autolink.AutolinkExtension;
import com.vladsch.flexmark.html.AttributeProvider;
import com.vladsch.flexmark.html.AttributeProviderFactory;
import com.vladsch.flexmark.html.CustomNodeRenderer;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.html.HtmlWriter;
import com.vladsch.flexmark.html.IndependentAttributeProviderFactory;
import com.vladsch.flexmark.html.renderer.AttributablePart;
import com.vladsch.flexmark.html.renderer.LinkResolverContext;
import com.vladsch.flexmark.html.renderer.NodeRenderer;
import com.vladsch.flexmark.html.renderer.NodeRendererContext;
import com.vladsch.flexmark.html.renderer.NodeRendererFactory;
import com.vladsch.flexmark.html.renderer.NodeRenderingHandler;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.html.Attributes;
import com.vladsch.flexmark.util.options.DataHolder;
import com.vladsch.flexmark.util.options.MutableDataHolder;
import com.vladsch.flexmark.util.options.MutableDataSet;

class AttributeProviderSamples {
    

    static String commonMark(String markdown) {
        MutableDataHolder options = new MutableDataSet();
        options.set(Parser.EXTENSIONS, Arrays.asList(new Extension[] {
							        		AutolinkExtension.create(),
							        		AnchorLinkExtension.create(),
							        		SampleExtension.create(),
							        	}));

        options.set(HtmlRenderer.SOFT_BREAK, "<br/>");

        Parser parser = Parser.builder(options).build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        try {
        	final String html = renderer.render(document);
            return html;
        } catch (Exception e) {
        	e.printStackTrace();
		}
        return "";
    }
}
class SampleExtension implements HtmlRenderer.HtmlRendererExtension {
    @Override
    public void rendererOptions(final MutableDataHolder options) {
    }

    @Override
    public void extend(final HtmlRenderer.Builder rendererBuilder, final String rendererType) {
        rendererBuilder.attributeProviderFactory(SampleAttributeProvider.Factory());
        rendererBuilder.nodeRendererFactory(new MAnchorLinkNodeRenderer.Factory());
    }

    static SampleExtension create() {
        return new SampleExtension();
    }
}
class SampleAttributeProvider implements AttributeProvider {
    
	@Override
    public void setAttributes(final Node node, final AttributablePart part, final Attributes attributes) {
        if(node instanceof AnchorLink) {
        	attributes.replaceValue("class", "anchor");
        	attributes.addValue("aria-hidden", "true");
        }
    }

    static AttributeProviderFactory Factory() {
        return new IndependentAttributeProviderFactory() {
        	@SuppressWarnings("unused")
        	public AttributeProvider create(NodeRendererContext context) {
        		return create((LinkResolverContext) context);
			}
        	
			@Override
			public AttributeProvider create(LinkResolverContext context) {
				return new SampleAttributeProvider();
			}
        };
    }
}

class MAnchorLinkNodeRenderer implements NodeRenderer {
	AnchorLinkOptions options;

    public MAnchorLinkNodeRenderer(DataHolder options) {
		this.options = new AnchorLinkOptions(options);
	}

	@Override
    public Set<NodeRenderingHandler<?>> getNodeRenderingHandlers() {
        HashSet<NodeRenderingHandler<?>> set = new HashSet<NodeRenderingHandler<?>>();
        set.add(new NodeRenderingHandler<AnchorLink>(AnchorLink.class, new CustomNodeRenderer<AnchorLink>() {
            @Override
            public void render(AnchorLink node, NodeRendererContext context, HtmlWriter html) {
                MAnchorLinkNodeRenderer.this.render(node, context, html);
            }
        }));
        return set;
    }

    private void render(final AnchorLink node, final NodeRendererContext context, final HtmlWriter html) {
        String id = context.getNodeId(node.getParent());
        id = "<a id=\"" + id + "\" class=\"anchor\" href=\"#" + id + "\" aria-hidden=\"true\"><svg class=\"octicon octicon-link\" viewBox=\"0 0 16 16\" version=\"1.1\" width=\"16\" height=\"16\" aria-hidden=\"true\"><path fill-rule=\"evenodd\" d=\"M4 9h1v1H4c-1.5 0-3-1.69-3-3.5S2.55 3 4 3h4c1.45 0 3 1.69 3 3.5 0 1.41-.91 2.72-2 3.25V8.59c.58-.45 1-1.27 1-2.09C10 5.22 8.98 4 8 4H4c-.98 0-2 1.22-2 2.5S3 9 4 9zm9-3h-1v1h1c1 0 2 1.22 2 2.5S13.98 12 13 12H9c-.98 0-2-1.22-2-2.5 0-.83.42-1.64 1-2.09V6.25c-1.09.53-2 1.84-2 3.25C6 11.31 7.55 13 9 13h4c1.45 0 3-1.69 3-3.5S14.5 6 13 6z\"></path></svg></a>";
        html.raw(id);
        context.renderChildren(node);
    }

    public static class Factory implements NodeRendererFactory {
        @Override
        public NodeRenderer create(final DataHolder options) {
            return new MAnchorLinkNodeRenderer(options);
        }
    }
}
class AnchorLinkOptions {
    public final boolean wrapText;
    public final String textPrefix;
    public final String textSuffix;
    public final String anchorClass;
    public final boolean setName;
    public final boolean setId;
    public final boolean noBlockQuotes;

    public AnchorLinkOptions(DataHolder options) {
        this.wrapText = AnchorLinkExtension.ANCHORLINKS_WRAP_TEXT.getFrom(options);
        this.textPrefix = AnchorLinkExtension.ANCHORLINKS_TEXT_PREFIX.getFrom(options);
        this.textSuffix = AnchorLinkExtension.ANCHORLINKS_TEXT_SUFFIX.getFrom(options);
        this.anchorClass = AnchorLinkExtension.ANCHORLINKS_ANCHOR_CLASS.getFrom(options);
        this.setName = AnchorLinkExtension.ANCHORLINKS_SET_NAME.getFrom(options);
        this.setId = AnchorLinkExtension.ANCHORLINKS_SET_ID.getFrom(options);
        this.noBlockQuotes = AnchorLinkExtension.ANCHORLINKS_NO_BLOCK_QUOTE.getFrom(options);
    }
}
