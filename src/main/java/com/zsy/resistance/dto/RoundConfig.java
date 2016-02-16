package com.zsy.resistance.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * @Project resistance
 * @Description: 每轮游戏全局配置
 * @Company youku
 * @Create 2016年2月14日下午5:22:11
 * @author zhoushaoyu
 * @version 1.0 Copyright (c) 2016 youku, All Rights Reserved.
 */

public class RoundConfig
{

	/**
	 * 每轮任务需要参与的人数
	 */
	private Map<Integer, Task> taskParticipants = new HashMap<>();

	/**
	 * @return the taskParticipants
	 */
	public Map<Integer, Task> getTaskParticipants()
	{
		return taskParticipants;
	}

	/**
	 * @param taskParticipants
	 *            the taskParticipants to set
	 */
	public void setTaskParticipants(Map<Integer, Task> taskParticipants)
	{
		this.taskParticipants = taskParticipants;
	}

}
