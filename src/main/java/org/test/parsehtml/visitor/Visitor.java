package org.test.parsehtml.visitor;

import org.jsoup.nodes.Element;

/**
 * Created by zhoushaoyu on 2017/1/7.
 */
public interface Visitor
{

	public void visit(Element element);

}
