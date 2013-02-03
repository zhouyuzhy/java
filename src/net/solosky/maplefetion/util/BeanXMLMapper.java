 /*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

 /**
 * Project  : MapleFetion2
 * Package  : net.solosky.maplefetion.util
 * File     : BeanXMLMapper.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-2-13
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.util;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;


/**
 *
 * 这个类实现了从XML格式的数据解析到Bean对象，如FetionBuddy, Member等等
 * 是基于配置的映射文件和反射来实现映射的
 *
 * @author solosky <solosky772@qq.com>
 */
public class BeanXMLMapper
{
	/**
	 * 所对应的Bean类
	 */
	private Class beanClass;
	
	/**
	 * 映射规则
	 */
	private ArrayList<FieldRule> fieldRuleList;
	
	/**
	 * LOGGER
	 */
	private static Logger logger = Logger.getLogger(BeanXMLMapper.class);
	
	/**
	 * 构造函数
	 */
	public BeanXMLMapper(Class beanClass)
	{
		this.beanClass = beanClass;
		this.fieldRuleList = new ArrayList<FieldRule>();
	}
	
	
	/**
	 * 加载映射XML
	 * 只需调用一次
	 * @throws FileNotFoundException 
	 * @throws ParseException 
	 * @throws ClassNotFoundException 
	 */
	public void loadMapXML(InputStream in) throws FileNotFoundException, ParseException, ClassNotFoundException
	{
		Element root = XMLHelper.build(in);
		String clazz = root.getAttributeValue("class");
		if(clazz.equals(beanClass.getName())) {
			List fields = root.getChildren("field");
			Iterator it = fields.iterator();
			while(it.hasNext()) {
				Element e = (Element) it.next();
				this.fieldRuleList.add(new FieldRule(e));
			}
		}else {
			throw new IllegalArgumentException("Unmatched class type");
		}
	}
	
	/**
	 * 把XML节点解析到Bean
	 * @param bean
	 * @param node
	 * @throws ParseException 
	 */
	public void toBean( Object bean, Element node) throws ParseException
	{
		this.checkClass(bean);
		Iterator <FieldRule> it = this.fieldRuleList.iterator();
		while(it.hasNext()) {
			FieldRule rule =  it.next();
			String value   = node.getAttributeValue(rule.getXmlName());
			if(rule!=null && value!=null && value.length()>0) {
					this.setValue(rule, bean, this.parseValue(rule, value));
			}
		}
}
	
	/**
	 * 这个方法把Bean中非只读属性生成可以发送给服务器的更新XML
	 * @param bean
	 * @param nodeName
	 * @throws ParseException 
	 */
	public String toUpdateXML(Object bean, NodeBuilder builder) throws ParseException
	{
		this.checkClass(bean);
		Iterator <FieldRule> it = this.fieldRuleList.iterator();
		while(it.hasNext()) {
			FieldRule rule = it.next();
			if( rule!=null && !rule.isReadonly()) {
				String value =  this.toValue(rule, this.getValue(rule, bean));
				if(value!=null)
					builder.add(rule.getXmlName(), value);
			}
		}
		return builder.toXML();
	}
	/**
	 * 把Bean的所有属性生成可供保存的XML
	 * @param bean
	 * @return
	 * @throws ParseException 
	 */
	public NodeBuilder toFullXML(Object bean, NodeBuilder builder) throws ParseException
	{
		this.checkClass(bean);
		Iterator <FieldRule> it = this.fieldRuleList.iterator();
		while(it.hasNext()) {
			FieldRule rule = it.next();
			if(rule!=null) {
				String value =  this.toValue(rule, this.getValue(rule, bean));
				builder.add(rule.getXmlName(), value==null?"null":value);
			}
		}
		return builder;
	}
	
	/**
	 * 检查传递过来的bean对象是否和这个Mapper映射的类对应或者兼容
	 * @return
	 * @throws ParseException 
	 */
	private void checkClass(Object bean) throws ParseException
	{
		Class c = bean.getClass();
		do {
			if(c.getName().equals(this.beanClass.getName())) {
				return;
			}else {
				c = c.getSuperclass();
			}
		}while(c!=null);
		
		throw new ParseException("Mapper type and bean type doesn't compatible:" +
				"mapper="+this.beanClass.getName()+", bean="+bean.getClass().getName());
		
	}
	
	
	/**
	 * 返回字段
	 * @param name
	 * @return
	 */
	private Field findField(String name)
	{
		return this.findField(name, this.beanClass);
	}
	
	/**
	 * 查找字段，包括公共字段和私有字段
	 * @param name
	 * @param c
	 * @return
	 */
	private Field findField(String name, Class c)
	{
		Field f = getField(name, c);
		if(f!=null) {
			f.setAccessible(true);
			return f;
		}
		return f;
		
	}
	
	/**
	 * 返回类属性，包含任何访问限制字段
	 * @param name
	 * @param c
	 * @return
	 */
	private Field getField(String name, Class c)
	{
		Field[] fields = c.getDeclaredFields();
		for(Field f: fields) {
			if(f.getName().equals(name))
				return f;
		}
		logger.warn("Cannot find field "+name+" in Class "+c.getName());
		return null;
	}
	
	
	/**
	 * 设置一个字段的值
	 * @param field
	 * @param o
	 * @throws ParseException 
	 */
	private void setValue(FieldRule rule, Object bean, Object value) throws ParseException
	{
		try {
	        if(rule.getSetter()!=null) {
	        	Method setter = rule.getSetter();
	        	setter.invoke(bean, value);
	        }else {
	        	Field field = this.findField(rule.getBeanName());
	        	if(field!=null)
	        		field.set(bean, value);
	        }
        } catch (Exception e) {
        	throw new ParseException(e);
        }
	}
	
	/**
	 * 返回字段的值
	 * @param field
	 * @param bean
	 * @return
	 * @throws ParseException 
	 */
	private Object getValue(FieldRule rule, Object bean) throws ParseException
	{
		try {
	        if(rule.getGetter()!=null) {
	        	Method getter = rule.getGetter();
	        	return getter.invoke(bean, null);
	        }else {
	        	Field field = this.findField(rule.getBeanName());
	        	if(field!=null)
	        		return field.get(bean);
	        	else
	        		return null;
	        }
        } catch (Exception e) {
        	throw new ParseException(e);
        }
	}
	
	
	/**
	 * 使用ParseHelper，把字符串转化为值对象
	 * @param rule		字段规则
	 * @param value		字符串值
	 * @return
	 * @throws ParseException 
	 */
	private Object parseValue(FieldRule rule, String value) throws ParseException
	{
		try {
			Method parser = rule.getParser();
	        return parser.invoke(null, value);
        } catch (Exception e) {
        	throw new ParseException(e);
        }
	}
	
	
	/**
	 * 把对象转化为字符串
	 * @param rule 字段规则
	 * @param o
	 * @return
	 * @throws ParseException 
	 */
	private String toValue(FieldRule rule, Object bean) throws ParseException
	{
		try {
			Method toer = rule.getToer();
	        return (String) toer.invoke(null, bean);
        } catch (Exception e) {
        	throw new ParseException(e);
        }
	}
	
	/**
	 * 查找方法，如果没有或者出差就返回null
	 * @param clazz
	 * @param name
	 * @param params
	 * @return
	 */
	private static Method getMethod(Class clazz, String name, Class ... params )
	{
		Method method  = null;
		try {
	        method = clazz.getMethod(name, params);
        } catch (SecurityException e) {
        	logger.warn("Cannot find Method for security reason.", e);
        } catch (NoSuchMethodException e) {
        	logger.warn("Cannot find Method "+name+" in "+clazz.getName());
	        method = null;
        }
        
        return method;
	}

	/**
	 * 
	 * 内部类，定义了一个属性的映射规则
	 *
	 * @author solosky <solosky772@qq.com>
	 */
	private class FieldRule
	{
		/**
		 * Bean属性名字
		 */
		private String beanNme;
		
		/**
		 * XML属性名字
		 */
		private String xmlName;
		
		/**
		 * 值类型
		 */
		private Class type;
		
		/**
		 * 是否只读
		 */
		private boolean isReadonly;
		
		/**
		 * getter
		 */
		private Method getter;
		
		/**
		 * setter
		 */
		private Method setter;
		
		/**
		 * toer 这个方法把一个属性转化为字符串
		 */
		private Method toer;
		
		/**
		 * parser 这个方法从一个字符串解析出属性
		 */
		private Method parser;
		/**
		 * 构造函数
		 * @param e
		 * @throws ClassNotFoundException 
		 * @throws NoSuchMethodException 
		 * @throws SecurityException 
		 */
		public FieldRule(Element e) throws ClassNotFoundException
		{
			this.beanNme    = e.getAttributeValue("bean");
			this.xmlName    = e.getAttributeValue("xml");
			this.isReadonly = (e.getAttributeValue("readonly")!=null && e.getAttributeValue("readonly").equals("true"))?true:false;

			String clazz = e.getAttributeValue("type");
			this.type = Class.forName(clazz);
			
			String g = e.getAttributeValue("getter");
			String s = e.getAttributeValue("setter");
			String t = e.getAttributeValue("toer");
			String p = e.getAttributeValue("parser");
			
			if(t==null)	 t = "to"+this.type.getSimpleName();
			if(p==null) p = "parse"+this.type.getSimpleName();
			
			if(g!=null) this.getter = BeanXMLMapper.getMethod(beanClass, g, this.type);
			if(s!=null) this.setter = BeanXMLMapper.getMethod(beanClass, s, this.type);
			if(t!=null) this.toer   = BeanXMLMapper.getMethod(ParseHelper.class, t, this.type);
			if(p!=null) this.parser = BeanXMLMapper.getMethod(ParseHelper.class, p, String.class);
			
		}


		/**
         * @return the beanNme
         */
        public String getBeanName()
        {
        	return beanNme;
        }

		/**
         * @return the xmlName
         */
        public String getXmlName()
        {
        	return xmlName;
        }

		/**
         * @return the isReadonly
         */
        public boolean isReadonly()
        {
        	return isReadonly;
        }

		/**
         * @return the type
         */
        public Class getType()
        {
        	return type;
        }

		/**
         * @return the getter
         */
        public Method getGetter()
        {
        	return getter;
        }

		/**
         * @return the setter
         */
        public Method getSetter()
        {
        	return setter;
        }
        
        /**
         * @return the toer
         */
        public Method getToer()
        {
        	return toer;
        }

		/**
         * @return the parser
         */
        public Method getParser()
        {
        	return parser;
        }
	}
}