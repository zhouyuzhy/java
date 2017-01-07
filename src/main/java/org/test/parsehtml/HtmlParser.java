package org.test.parsehtml;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.test.httprequest.dto.HttpRequestContext;
import org.test.httprequest.service.HttpRequestImpl;
import org.test.parsehtml.visitor.TestVisitor;
import org.test.parsehtml.visitor.Visitor;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by zhoushaoyu on 2017/1/7.
 */
public class HtmlParser
{

	private String html;

	private Document doc;

	public HtmlParser(String html)
	{
		this.html = html;
		this.doc = Jsoup.parse(html);
	}

	public void parser(List<String> selectors, Visitor visitor)
	{
		Elements allElements = null;
		for (String selector : selectors)
		{
			Elements elements = doc.select(selector);
			if (allElements == null)
			{
				allElements = elements;
			} else
			{
				allElements.addAll(elements);
			}
		}
		Collections.sort(allElements, new Comparator<Element>()
		{

			@Override
			public int compare(Element o1, Element o2)
			{
				return o1.siblingIndex() - o2.siblingIndex();
			}
		});
		for (Element element : allElements)
		{
			visitor.visit(element);
		}
	}
}
