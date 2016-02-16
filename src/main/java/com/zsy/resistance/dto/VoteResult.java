package com.zsy.resistance.dto;


/**
 * @Project resistance
 * @Description: 任务投票成功失败结果
 * @Company youku
 * @Create 2016年2月16日下午7:22:28
 * @author zhoushaoyu
 * @version 1.0
 * Copyright (c) 2016 youku, All Rights Reserved.
 */

public class VoteResult
{
	private int voteCount;
	
	private int approveCount;
	
	private boolean voteSuccess;

	
	/**
	 * @return the voteCount
	 */
	public int getVoteCount()
	{
		return voteCount;
	}

	
	/**
	 * @param voteCount the voteCount to set
	 */
	public void setVoteCount(int voteCount)
	{
		this.voteCount = voteCount;
	}

	
	/**
	 * @return the approveCount
	 */
	public int getApproveCount()
	{
		return approveCount;
	}

	
	/**
	 * @param approveCount the approveCount to set
	 */
	public void setApproveCount(int approveCount)
	{
		this.approveCount = approveCount;
	}

	
	/**
	 * @return the voteSuccess
	 */
	public boolean isVoteSuccess()
	{
		return voteSuccess;
	}

	
	/**
	 * @param voteSuccess the voteSuccess to set
	 */
	public void setVoteSuccess(boolean voteSuccess)
	{
		this.voteSuccess = voteSuccess;
	}
	
	
}
