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
 * Package  : net.solosky.net.maplefetion
 * File     : FetionClientException.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-6
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion;

/**
 * 飞信异常，是所有飞信异常的基类
 *
 * @author solosky <solosky772@qq.com>
 */
public abstract class FetionException extends Exception
{
    private static final long serialVersionUID = -7066837853615016245L;
    
    public FetionException(Throwable e)
    {
    	super(e);
    }
    
    public FetionException(String msg)
    {
    	super(msg);
    }
    
    public FetionException()
    {
    	
    }

}
