package org.test.httpclient;

import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * json工具类 - fastjson实现
 * 
 * @author zhengxiaohong(hzzhengxiaohong@corp.netease.com)
 * @version V 0.1 2013-6-2 下午3:01:03
 */
public class JSONUtils {
    private static Logger logger = Logger.getLogger(JSONUtils.class);

    public static <T> T fromJson(String jsonString, Class<T> clazz) {
        T t = null;
        try {
            t = JSON.parseObject(jsonString, clazz);
        } catch (JSONException ex) {
            logger.error("JSONException", ex);
        }
        return t;
    }

    public static Object fromJson(String jsonString) {
        Object obj = null;
        try {
            obj = JSON.parse(jsonString);
        } catch (JSONException ex) {
            logger.error("JSONException", ex);
        }
        return obj;
    }
    
    public static <T> List<T> parseArray(String jsonString, Class<T> clazz) {
        List<T> list = null;
        try {
            list = JSON.parseArray(jsonString, clazz);
        }catch(JSONException ex) {
            logger.error("JSONException", ex);
        }
        return list;
    }

    public static String getJson(Object obj) {
        String ret = null;
        try {
            ret = JSON.toJSONString(obj);
        } catch (JSONException ex) {
            logger.error("JSONException", ex);
        }
        return ret;
    }

    public static String getJsonTabAsSpecial(Object obj) {
        String ret = null;
        try {
            ret = JSON.toJSONString(obj, SerializerFeature.WriteTabAsSpecial);
        } catch (JSONException ex) {
            logger.error("JSONException", ex);
        }
        return ret;
    }

    public static JSONObject toJSONObject(String text) {
        JSONObject jsonObject = null;
        try {
            jsonObject = JSON.parseObject(text);
        } catch (JSONException ex) {
            logger.error("JSONException", ex);
        }
        return jsonObject;
    }
}
