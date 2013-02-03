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
 * File     : HelloApp.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-4-6
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.demo.robot.app;

import net.solosky.maplefetion.demo.robot.App;
import net.solosky.maplefetion.demo.robot.Gateway;


/**
 *
 * 一个经典的hello应用
 *
 * @author solosky <solosky772@qq.com>
 */
public class HelloApp implements App
{

	/* (non-Javadoc)
     * @see net.solosky.maplesms.App#accept(java.lang.String)
     */
    @Override
    public boolean accept(String msg)
    {
	   return msg.indexOf("hello")!=-1;
    }

	/* (non-Javadoc)
     * @see net.solosky.maplesms.App#action(java.lang.String, java.lang.String, net.solosky.maplesms.gateway.Gateway)
     */
    @Override
    public void action(String msg, String uri, Gateway gateway)
            throws Exception
    {
    	gateway.sendSMS(uri, "Hello, this is greet from MapleSMS");
    }

	/* (non-Javadoc)
     * @see net.solosky.maplesms.App#getName()
     */
    @Override
    public String getName()
    {
	    return "每天的问候";
    }

	/* (non-Javadoc)
     * @see net.solosky.maplesms.App#init()
     */
    @Override
    public void init()
    {
    }

	/* (non-Javadoc)
     * @see net.solosky.maplesms.App#getFormat()
     */
    @Override
    public String getFormat()
    {
	    return "hello";
    }

	/* (non-Javadoc)
     * @see net.solosky.maplesms.App#getIntro()
     */
    @Override
    public String getIntro()
    {
	    return "这只是一个演示插件。";
    }


}
