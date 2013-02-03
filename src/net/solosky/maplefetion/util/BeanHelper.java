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
 * File     : BeanHelper.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-2-15
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.util;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.jdom.Element;


/**
 *
 * Bean工具类
 *
 * @author solosky <solosky772@qq.com>
 */
public class BeanHelper
{
	/**
	 * 缓存所有的Mapper
	 */
	private static Hashtable<String, BeanXMLMapper> MAPPER_CACHE = new Hashtable<String, BeanXMLMapper>();
	
	/**
	 * logger
	 */
	private static final Logger logger = Logger.getLogger(BeanHelper.class);
	/**
	 * 返回Mapper 如果没有就创建 
	 * @param beanClass
	 * @return
	 */
	private static synchronized BeanXMLMapper getMapper(Class beanClass)
	{
		BeanXMLMapper mapper = MAPPER_CACHE.get(beanClass.getName());
		if(mapper==null) {
			InputStream in = BeanHelper.class.getResourceAsStream("/resources/"+beanClass.getSimpleName()+".map.xml");
    		try {
    			mapper = new BeanXMLMapper(beanClass);
    	        mapper.loadMapXML(in);
    	        MAPPER_CACHE.put(beanClass.getName(), mapper);
            } catch (FileNotFoundException e) {
    	       logger.warn("Cannot find map file in classpath:"+beanClass.getName()+".map.xml",e);
            } catch (ParseException e) {
	            logger.warn("XML parse error.",e);
            } catch (ClassNotFoundException e) {
	            logger.warn("Bean field type is not a valid class.", e);
            }
		}
		
		return mapper;
	}
	
	/**
	 * 把XML数据转化为BEAN
	 * @param beanClass
	 * @param bean
	 * @param node
	 * @throws ParseException
	 */
	public static void toBean(Class beanClass, Object bean, Element node) throws ParseException
	{
		Iterator<Class> it = new ClassIterator(beanClass);
		while(it.hasNext()) {
			Class clazz = it.next();
			BeanXMLMapper mapper = getMapper(clazz);
			if(mapper!=null) {
				mapper.toBean(bean, node);
			}
		}
	}
	
	/**
	 * 把Bean中非只读属性的字段转化为可以发送给服务器更新的XML
	 * @param beanClass
	 * @param bean
	 * @param nodeBuilder
	 * @return
	 * @throws ParseException
	 */
	public static void toUpdateXML(Class beanClass, Object bean, NodeBuilder builder) throws ParseException
	{
		Iterator<Class> it = new ClassIterator(beanClass);
		while(it.hasNext()) {
			Class clazz = it.next();
			BeanXMLMapper mapper = getMapper(clazz);
			if(mapper!=null) {
				mapper.toUpdateXML(bean, builder);
			}
		}
	}
	
	/**
	 * 把Bean的所有属性转化为XML字符串
	 * @param beanClass
	 * @param bean
	 * @param nodeBuilder
	 * @throws ParseException
	 */
	public static void toFullXML(Class beanClass, Object bean, NodeBuilder builder) throws ParseException
	{
		Iterator<Class> it = new ClassIterator(beanClass);
		while(it.hasNext()) {
			Class clazz = it.next();
			BeanXMLMapper mapper = getMapper(clazz);
			if(mapper!=null) {
				mapper.toFullXML(bean, builder);
			}
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 类迭代器，可以用来迭代一个类对象的所有父类
	 */
	private static class ClassIterator implements Iterator<Class>
	{
		private Class clazz;
		public ClassIterator(Class clazz)
		{
			this.clazz = clazz;
		}
		/* (non-Javadoc)
         * @see java.util.Iterator#hasNext()
         */
        @Override
        public boolean hasNext()
        {
	       return !this.clazz.getSimpleName().equals(Object.class.getSimpleName());
        }

		/* (non-Javadoc)
         * @see java.util.Iterator#next()
         */
        @Override
        public Class next()
        {
	       Class ret = this.clazz;
	       this.clazz = this.clazz.getSuperclass();
	       return ret;
        }

		/* (non-Javadoc)
         * @see java.util.Iterator#remove()
         */
        @Override
        public void remove()
        {
        	throw new IllegalAccessError("Sorry, I couldn't break the Java law...");
        }

	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 查找字段，如果当前类没有，就去父类查找
	 * @param name
	 * @param c
	 * @return
	 */
	public static Field findField(String name, Class c)
	{
		Field[] fields = c.getDeclaredFields();
		Field field = null;
		for(Field f: fields) {
			if(f.getName().equals(name)) {
				field = f;
				break;
			}
		}
		
		if(field==null) {
        	if(c.getSuperclass()!=null) {
        		return findField(name, c.getSuperclass());
        	}else {
        		return null;
        	}
		}else {
			field.setAccessible(true);
			return field;
		}
		
	}
	
	
	/**
	 * 设置一个字段的值
	 * @param field
	 * @param o
	 */
	public static void setValue(Object bean, String fieldName, Object value)
	{
		try {
	        Field field = findField(fieldName, bean.getClass());
	        if(field!=null)
	        	field.set(bean, value);
        } catch (Exception e) {
        	logger.warn("Set bean value failed.", e);
        }
	}
	
	/**
	 * 返回字段的值
	 * @param field
	 * @param bean
	 * @return
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static Object getValue(Object bean,String fieldName) throws IllegalArgumentException, IllegalAccessException
	{
		Field field = findField(fieldName, bean.getClass());
		if(field!=null)
			return field.get(bean);
		else
			return null;
	}
}
