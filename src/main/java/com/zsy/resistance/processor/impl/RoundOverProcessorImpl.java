package com.zsy.resistance.processor.impl;

import com.zsy.resistance.dto.Game;
import com.zsy.resistance.dto.Round;
import com.zsy.resistance.processor.RoundOverProcessor;

/**
 * @Project resistance
 * @Description:
 * @Company youku
 * @Create 2016年2月16日下午10:36:01
 * @author zhoushaoyu
 * @version 1.0 Copyright (c) 2016 youku, All Rights Reserved.
 */

public class RoundOverProcessorImpl implements RoundOverProcessor
{

	/**
	 * 方法用途: <br>
	 * 实现步骤: <br>
	 * 
	 * @param game
	 * @param round
	 */
	@Override
	public void process(Game game, Round round)
	{
		int taskSuccessCount = 0;
		int taskFailCount = 0;

		for (Round hisRound : game.getHistory().getHistoryRounds().values())
		{
			if (hisRound.isDoTask() && hisRound.isTaskSuccess())
			{
				taskSuccessCount++;
			} else if (hisRound.isDoTask() && !hisRound.isTaskSuccess())
			{
				taskFailCount++;
			}
		}
		if (taskSuccessCount >= 3 && taskFailCount >= 3)
		{
			throw new IllegalStateException(String.format("成功任务数和失败任务数都大于等于3,success=%d,fail=%d", taskSuccessCount,
					taskFailCount));
		}
		if (taskSuccessCount >= 3)
		{
			game.setGameOver(true);
			game.setBlueSuccess(true);
		}
		if (taskFailCount >= 3)
		{
			game.setGameOver(true);
			game.setBlueSuccess(false);
		}
	}
}
