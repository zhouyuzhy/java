package com.zsy.resistance.dto;

import java.util.List;

/**
 * @Project resistance
 * @Description: 当前轮的游戏信息
 * @Company youku
 * @Create 2016年2月14日下午5:23:19
 * @author zhoushaoyu
 * @version 1.0 Copyright (c) 2016 youku, All Rights Reserved.
 */

public class Round
{

	/**
	 * 第几轮
	 */
	private int roundNum;

	/**
	 * 是否做了任务，为false时表示该局leader被流掉
	 */
	private boolean doTask;

	/**
	 * 当前局Leader是谁
	 */
	private Actor leader;

	/**
	 * leader选中的参与任务人是谁
	 */
	private List<Actor> participants;

	/**
	 * 当局参与任务人数
	 */
	private int participantCount;

	/**
	 * 同意任务人数
	 */
	private int approveCount;

	/**
	 * 任务是否成功
	 */
	private boolean taskSuccess;

	/**
	 * @param leader
	 * @param participantCount
	 */
	public Round(int roundNum, Actor leader, int participantCount)
	{
		this.roundNum = roundNum;
		this.leader = leader;
		this.participantCount = participantCount;
	}

	/**
	 * @return the roundNum
	 */
	public int getRoundNum()
	{
		return roundNum;
	}

	/**
	 * @return the doTask
	 */
	public boolean isDoTask()
	{
		return doTask;
	}

	/**
	 * @param doTask
	 *            the doTask to set
	 */
	public void setDoTask(boolean doTask)
	{
		this.doTask = doTask;
	}

	/**
	 * @return the leader
	 */
	public Actor getLeader()
	{
		return leader;
	}

	/**
	 * @return the participants
	 */
	public List<Actor> getParticipants()
	{
		return participants;
	}

	/**
	 * @param participants
	 *            the participants to set
	 */
	public void setParticipants(List<Actor> participants)
	{
		this.participants = participants;
	}

	/**
	 * @return the participantCount
	 */
	public int getParticipantCount()
	{
		return participantCount;
	}

	/**
	 * @return the approveCount
	 */
	public int getApproveCount()
	{
		return approveCount;
	}

	/**
	 * @param approveCount
	 *            the approveCount to set
	 */
	public void setApproveCount(int approveCount)
	{
		this.approveCount = approveCount;
	}

	/**
	 * @return the taskSuccess
	 */
	public boolean isTaskSuccess()
	{
		return taskSuccess;
	}

	/**
	 * @param taskSuccess
	 *            the taskSuccess to set
	 */
	public void setTaskSuccess(boolean taskSuccess)
	{
		this.taskSuccess = taskSuccess;
	}

}
