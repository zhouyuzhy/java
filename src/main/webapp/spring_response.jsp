<!DOCTYPE unspecified PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html; charset=UTF-8"  import="com.zsy.learn.springwebsocket.constant.SpringWebsocketConstant"%>
<%=SpringWebsocketConstant.map.get("1")==null %>
<%=SpringWebsocketConstant.map.get("1").isOpen() %>
<%if(SpringWebsocketConstant.map.get("1").isOpen()){ %>
<%SpringWebsocketConstant.map.get("1").sendMessage(new org.springframework.web.socket.TextMessage("this is from jsp!!"));}%>