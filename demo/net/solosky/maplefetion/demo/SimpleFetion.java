package net.solosky.maplefetion.demo;

import net.solosky.maplefetion.ClientState;
import net.solosky.maplefetion.FetionClient;
import net.solosky.maplefetion.LoginState;
import net.solosky.maplefetion.NotifyEventListener;
import net.solosky.maplefetion.bean.Message;
import net.solosky.maplefetion.event.ActionEvent;
import net.solosky.maplefetion.event.NotifyEvent;
import net.solosky.maplefetion.event.NotifyEventType;
import net.solosky.maplefetion.event.action.ActionEventFuture;
import net.solosky.maplefetion.event.action.FailureEvent;
import net.solosky.maplefetion.event.action.failure.RequestFailureEvent;
import net.solosky.maplefetion.event.action.success.SendChatMessageSuccessEvent;
import net.solosky.maplefetion.event.notify.ImageVerifyEvent;


/**
 * 这是个简单的飞信演示程序
 * 可以给指定的手机号码发送消息然后退出，前提是这个手机号码的飞信用户是你的好友，否则会发送失败。
 * 参数说明： SimpleFetion 手机号 密码 发送消息的手机号 消息内容
 * @author solosky
 *
 */
public class SimpleFetion {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		args = new String[]{"111", "111", "111", "testtestmaplefetion"};
		if(args.length<4){
			System.out.println("参数不正确。参数格式为：手机号 密码 发送消息的手机号 消息内容");
			System.out.println("说明：可以给指定的手机号码发送消息然后退出，前提是这个手机号码的飞信用户是你的好友，否则会发送失败。");
		}else{
			final FetionClient client = new FetionClient(args[0], args[1]);
			System.out.println("正在登录中，可能需要1分钟左右，请稍候...");
			
			//这里设置一个登录状态监听器，可以显示当前登录步骤，避免用户感到焦虑
			client.setNotifyEventListener(new NotifyEventListener() {
				public void fireEvent(NotifyEvent event)
				{
					System.err.println(event.toString());
					//如果出现验证码，取消登录
					if(event.getEventType()==NotifyEventType.IMAGE_VERIFY) {
						System.out.println("当前登录过程或者操作需要验证，操作失败。");
						client.cancelVerify((ImageVerifyEvent) event);
					}
				}
			});
			//禁用掉群，登录可以变得快一点
			client.enableGroup(false);		
			
			//这里为了编程方便使用了同步登录，当然推荐异步登录，使用登录状态回调函数来完成登录成功后的操作
			LoginState state = client.syncLogin();
			if(state==LoginState.LOGIN_SUCCESS){	//登录成功
				System.out.println("登录成功，正在发送消息至 "+args[2]+",请稍候...");											
					
				ActionEventFuture future = new ActionEventFuture();	//建立一个Future来等待操作事件			
				client.sendChatMessage(Long.parseLong(args[2]), new Message(args[3]), future);
				ActionEvent event = future.waitActionEventWithoutException();	//等待操作完成事件
				switch(event.getEventType()){
					
					case SUCCESS:
						SendChatMessageSuccessEvent evt = (SendChatMessageSuccessEvent) event;
						if(evt.isSendToMobile()){
							System.out.println("发送成功，消息已通过短信发送到对方手机！");
						}else if(evt.isSendToClient()){
							System.out.println("发送成功，消息已通过服务直接发送到对方客户端！");
						}
						break;
						
					case FAILURE:
						FailureEvent evt2 = (FailureEvent) event;
						switch(evt2.getFailureType()){
							case BUDDY_NOT_FOUND:
								System.out.println("发送失败, 该用户可能不是你好友，请尝试添加该用户为好友后再发送消息。");
								break;
							case USER_NOT_FOUND:
								System.out.println("发送失败, 该用户不是移动用户。");
								break;
							case SIPC_FAIL:
								System.out.println("发送失败, 服务器返回了错误的信息。");
								break;
							case UNKNOWN_FAIL:
								System.out.println("发送失败, 不知道错在哪里。");
								
							case REQEUST_FAIL:
								RequestFailureEvent evt3 = (RequestFailureEvent) event; 
								System.out.println("提示:"+evt3.getReason()+", 更多信息请访问:"+evt3.getReason());
								
							default:
								System.out.println("发送消息失败！"+event.toString());
						}
						break;
					
					/* 以下三个错误状态是在异步发送消息的情况才会发生，
					 * 为了方便处理，使用waitActionEventWithException()同步的情况下，这三个错误是通过异常来处理的
					 * 也就是在waitActionEvent的时候就会判断是否出现了这三个错误，如果出现了就会抛出相应的异常
					 * 而waitActionEventWithoutException()不会抛出异常，会把这些错误作为操作事件返回
					 */
					case SYSTEM_ERROR:
						System.out.println("发送失败, 客户端内部错误。");
						break;
					case TIMEOUT:
						System.out.println("发送失败, 超时");
						break;
					case TRANSFER_ERROR:
						System.out.println("发送失败, 超时");
				}	
	            
	            //无论发送成功还是失败，因为登录了，就必须退出，释放线程资源，否则客户端不会主动退出
	            //如果登录失败了，客户端会主动释放线程资源，不需要退出
	            System.out.print("正在退出客户端...");
	            client.logout();
	            System.out.println("已经退出。");
	            
			}else if(state==LoginState.SSI_AUTH_FAIL){
				System.out.println("你输入的手机号或者密码不对，请检查后重试!");
			}else if(state==LoginState.SSI_CONNECT_FAIL){
				System.out.println("SSI连接失败！！");
			}else if(state==LoginState.SIPC_CONNECT_FAIL){
				System.out.println("SIPC服务器连接失败！！");
			}else if(state==LoginState.LOGIN_CANCELED){
				//取消了登录
			}else {
				System.out.println("登录失败，原因："+state.name());
			}
		}
		
	}

}
