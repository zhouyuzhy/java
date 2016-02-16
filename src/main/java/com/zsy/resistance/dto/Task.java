package com.zsy.resistance.dto;

/**
 * @Project resistance
 * @Description:
 * @Company youku
 * @Create 2016年2月16日上午10:59:50
 * @author zhoushaoyu
 * @version 1.0 Copyright (c) 2016 youku, All Rights Reserved.
 */

public class Task
{

	/**
	 * 任务参与者人数
	 */
	private int participantsCount;

	/**
	 * 否决人数大于等于该数则任务失败
	 */
	private int failOnDenyCount;

	public Task(int participantsCount)
	{
		super();
		this.participantsCount = participantsCount;
		this.failOnDenyCount = 1;
	}
	
	/**
	 * @param participantsCount
	 * @param failOnDenyCount
	 */
	public Task(int participantsCount, int failOnDenyCount)
	{
		super();
		this.participantsCount = participantsCount;
		this.failOnDenyCount = failOnDenyCount;
	}

	/**
	 * @return the participantsCount
	 */
	public int getParticipantsCount()
	{
		return participantsCount;
	}

	/**
	 * @return the failOnDenyCount
	 */
	public int getFailOnDenyCount()
	{
		return failOnDenyCount;
	}

}
