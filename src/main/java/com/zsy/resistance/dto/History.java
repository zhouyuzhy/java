package com.zsy.resistance.dto;

import java.util.LinkedHashMap;

/**
 * @Project resistance
 * @Description: 当前局历史轮次信息
 * @Company youku
 * @Create 2016年2月14日下午5:23:52
 * @author zhoushaoyu
 * @version 1.0 Copyright (c) 2016 youku, All Rights Reserved.
 */

public class History
{

	private LinkedHashMap<Integer, Round> historyRounds = new LinkedHashMap<Integer, Round>();

	/**
	 * @return the historyRounds
	 */
	public LinkedHashMap<Integer, Round> getHistoryRounds()
	{
		return historyRounds;
	}

	/**
	 * @param historyRounds
	 *            the historyRounds to set
	 */
	public void setHistoryRounds(LinkedHashMap<Integer, Round> historyRounds)
	{
		this.historyRounds = historyRounds;
	}

}