package net.solosky.maplefetion.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import net.solosky.maplefetion.FetionContext;
import net.solosky.maplefetion.bean.ScheduleSMS;
import net.solosky.maplefetion.client.dialog.ServerDialog;
import net.solosky.maplefetion.event.ActionEvent;
import net.solosky.maplefetion.event.ActionEventType;
import net.solosky.maplefetion.event.action.ActionEventFuture;
import net.solosky.maplefetion.event.action.ActionEventListener;
import net.solosky.maplefetion.event.action.SuccessEvent;

public class GetScheduleListWork implements Runnable {
	/**
	 * 飞信上下文
	 */
	private FetionContext context;
	
	/**
	 * 结果监听器
	 */
	private ActionEventListener listener;
	
	/**
	 * 构造函数
	 * @param context
	 * @param listener
	 */
	public GetScheduleListWork(FetionContext context, ActionEventListener listener)
	{
		this.context = context;
		this.listener = listener;
	}
	
	/**
	 * 获取定时短信列表
	 * @return
	 */
	private ActionEvent getScheduleSMSList()
	{
		ActionEventFuture future = new ActionEventFuture();
		this.context.getDialogFactory().getServerDialog().getScheduleSMSList(future);
		return future.waitActionEventWithoutException();
	}
	
	/**
	 * 获取一组定时短信的详细信息
	 */
	private ActionEvent getScheduleSMSInfo(Collection<ScheduleSMS> list)
	{
		ActionEventFuture future = new ActionEventFuture();
		this.context.getDialogFactory().getServerDialog().getScheduleSMSInfo(list, future);
		return future.waitActionEventWithoutException();
	}
	
	/**
	 * 获取所有的定时短信
	 */
	private ActionEvent getScheduleSMSInfo()
	{
		Iterator<ScheduleSMS> it = this.context.getFetionStore().getScheduleSMSList().iterator();
		int pageSize = 20;
		ArrayList <ScheduleSMS> readyList = new ArrayList<ScheduleSMS>();
		ActionEventFuture future = new ActionEventFuture();
		ServerDialog dialog = this.context.getDialogFactory().getServerDialog();
		
		//迭代所有的定时短信列表，并在每20个发起一次获取定时短信详细内容的请求
		while(it.hasNext()){
			ScheduleSMS sc = it.next();
			readyList.add(sc);
			if(readyList.size()==20){
				future.clear();
				dialog.getScheduleSMSInfo(readyList, future);
				ActionEvent event = future.waitActionEventWithoutException();
				if(event.getEventType()==ActionEventType.SUCCESS){
					readyList.clear();		//成功继续下一次请求
				}else{
					return event;			//失败返回这个错误事件
				}
			}
		}
		
		//迭代完了，可能仍有部分短信没有请求，检查下请求列表，如果不为空，则继续请求
		if(readyList.size()>0){
			future.clear();
			dialog.getScheduleSMSInfo(readyList, future);
			return future.waitActionEventWithoutException();
		}else{
			return new SuccessEvent();		//成功
		}
	}
	
	@Override
	public void run() {
		ActionEvent event = this.getScheduleSMSList();
		if(event.getEventType()==ActionEventType.SUCCESS){
			event = this.getScheduleSMSInfo();
		}
		
		if(listener!=null)	listener.fireEevent(event);
	}

}
