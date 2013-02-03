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
 * Project  : MapleSMS
 * Package  : net.solosky.maplesms.app
 * File     : WeatherApp.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-4-6
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.demo.robot.app;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import net.solosky.maplefetion.demo.robot.App;
import net.solosky.maplefetion.demo.robot.Gateway;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 *
 * 天气预报接口，使用webxml.com.cn提供的天气预报服务
 *
 * @author solosky <solosky772@qq.com>
 */
public class WeatherApp implements App
{

	/* (non-Javadoc)
     * @see net.solosky.maplesms.App#accept(java.lang.String)
     */
    @Override
    public boolean accept(String msg)
    {
    	return msg.startsWith("TQ");
    }

	/* (non-Javadoc)
     * @see net.solosky.maplesms.App#action(java.lang.String, java.lang.String, net.solosky.maplesms.Gateway)
     */
    @Override
    public void action(String msg, String uri, Gateway gateway)
            throws Exception
    {
    	String city = "成都";
    	if(msg.indexOf("#")!=-1) {
    		city = msg.substring(msg.indexOf("#")+1);
    	}
    	
    	String url ="http://webservice.webxml.com.cn/WebServices/WeatherWS.asmx/getWeather?theCityCode={city}&theUserID=";
    	url = url.replace("{city}", URLEncoder.encode(city, "UTF8"));
    	HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
    	if(conn.getResponseCode()==200) {
    		SAXBuilder builder = new SAXBuilder();
	        Document doc = builder.build(conn.getInputStream());
	        List strings = doc.getRootElement().getChildren();
	        String[] sugguestions = getText(strings.get(6)).split("\n");
	        
	        /*
	        四川 成都
            成都
            1117
            2010/04/06 20:05:49
            今日天气实况：气温：12.8℃；风向/风力：西北风 小于3级；湿度：90%；气压：956.8hPa
            空气质量：中；紫外线强度：最弱
            穿衣指数：建议着薄型套装或牛仔衫裤等春秋过渡装。年老体弱者宜着套装、夹克衫等。
            感冒指数：天气较凉，较易发生感冒，请适当增加衣服。体质较弱的朋友尤其应该注意防护。
            晨练指数：早晨气象条件较适宜晨练，但阴天导致树林夜晚释放的二氧化碳气体不易扩散，请避免在林中晨练。
            洗车指数：较不宜洗车，过去12小时有降雨，路面少量积水，如果执意擦洗汽车，要做好溅上泥水的心理准备。
            晾晒指数：天气阴沉，不利于水分的迅速蒸发，不太适宜晾晒。若非晾晒不可，请尽量选择通风的地点。
            旅游指数：阴天，温度适宜，总体来说还是好天气哦，这样的天气很适宜旅游，您可以尽情地享受大自然的风光。
            路况指数：阴天，条件适宜，路面比较干燥，路况较好。
            舒适度指数：白天不太热也不太冷，风力不大，相信您在这样的天气条件下，应会感到比较清爽和舒适。
            4月7日 阴转多云
            8℃/15℃
            无持续风向微风转南风微风
            2.gif
            1.gif
            4月8日 晴转多云
            10℃/20℃
            南风微风
            0.gif
            1.gif
            4月9日 阴转多云
            12℃/21℃
            南风微风
            2.gif
            1.gif
            4月10日 多云转阵雨
            11℃/20℃
            南风微风转北风微风
            1.gif
            3.gif
            4月11日 阴
            11℃/20℃
            北风微风
            2.gif
            2.gif
	         */
	        StringBuffer buffer = new StringBuffer();
	        buffer.append("欢迎使用MapleSMS的天气服务！\n");
	        buffer.append("你查询的是 "+getText(strings.get(1))+"的天气。\n");		//四川 成都
	        buffer.append(getText(strings.get(4))+"。\n");		//今日天气实况：气温：12.8℃；风向/风力：西北风 小于3级；湿度：90%；气压：956.8hPa
	        buffer.append(getText(strings.get(5))+"。\n");		//空气质量：中；紫外线强度：最弱
	        buffer.append(sugguestions[0]+"\n");				//穿衣指数：建议着薄型套装或牛仔衫裤等春秋过渡装。年老体弱者宜着套装、夹克衫等。
	        buffer.append(sugguestions[1]+"\n");				//感冒指数：天气较凉，较易发生感冒，请适当增加衣服。体质较弱的朋友尤其应该注意防护。
	        buffer.append(sugguestions[7]+"\n");				//舒适度指数：白天不太热也不太冷，风力不大，相信您在这样的天气条件下，应会感到比较清爽和舒适
	        
	        buffer.append("感谢你使用MapleSMS的天气服务！祝你愉快！");
	        
	        gateway.sendSMS(uri, buffer.toString());
    	}else {
    		gateway.sendSMS(uri, "对不起，你输入的城市格式有误，请检查后再试~");
    	}
    }
    
    public String getText(Object node)
    {
    	Element e = (Element) node;
    	return e.getText();
    }

	/* (non-Javadoc)
     * @see net.solosky.maplesms.App#getName()
     */
    @Override
    public String getName()
    {
	   return "天气预报";
    }

	/* (non-Javadoc)
     * @see net.solosky.maplesms.App#init()
     */
    @Override
    public void init()
    {
	    // TODO Auto-generated method stub
	    
    }

	/* (non-Javadoc)
     * @see net.solosky.maplesms.App#getFormat()
     */
    @Override
    public String getFormat()
    {
	    return "TQ#城市 默认为成都";
    }

	/* (non-Javadoc)
     * @see net.solosky.maplesms.App#getIntro()
     */
    @Override
    public String getIntro()
    {
	    return "可以查询城市的当天天气。";
    }
}
