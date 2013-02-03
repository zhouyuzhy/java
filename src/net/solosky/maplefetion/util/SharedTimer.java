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
 * File     : SharedTimer.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-5-15
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.util;

import java.util.Timer;

/**
 *
 * 共享的定时器，可以在多个飞信实例之间共享
 *
 * @author solosky <solosky772@qq.com>
 */
public class SharedTimer extends ThreadTimer
{

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.util.ThreadTimer#startTimer()
     */
    @Override
    public synchronized void startTimer()
    {
    	if(this.timer==null) {
    		this.timer = new Timer("MapleFetionSharedTimer");
    	}
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.util.ThreadTimer#stopTimer()
     */
    @Override
    public synchronized void stopTimer()
    {
	    //这里不停止这个计时器,什么也不做
    }
    
    /**
     * 这个才是真正的关闭定时器
     */
    public void reallyStopTimer()
    {
    	super.stopTimer();
    }

}
