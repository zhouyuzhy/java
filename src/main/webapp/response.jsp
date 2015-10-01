<!DOCTYPE unspecified PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html; charset=UTF-8"  import="com.zsy.learn.websocket.constant.WebsocketConstant"%>
<%=com.zsy.learn.websocket.constant.WebsocketConstant.map.get("1")==null %>
<%=com.zsy.learn.websocket.constant.WebsocketConstant.map.get("1").isOpen() %>
<%if(com.zsy.learn.websocket.constant.WebsocketConstant.map.get("1").isOpen()){ %>
<%WebsocketConstant.map.get("1").getBasicRemote().sendText("this is from jsp!!");}%>