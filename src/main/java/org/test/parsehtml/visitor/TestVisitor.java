package org.test.parsehtml.visitor;

import org.jsoup.nodes.Element;

/**
 * Created by zhoushaoyu on 2017/1/7.
 */
public class TestVisitor implements Visitor
{

	@Override
	public void visit(Element element)
	{
		System.out.println(element.toString());
	}
}
