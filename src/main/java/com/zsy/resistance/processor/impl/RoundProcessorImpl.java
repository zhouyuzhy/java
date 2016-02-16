package com.zsy.resistance.processor.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zsy.resistance.dto.Actor;
import com.zsy.resistance.dto.Game;
import com.zsy.resistance.dto.Round;
import com.zsy.resistance.dto.Task;
import com.zsy.resistance.dto.VoteResult;
import com.zsy.resistance.processor.RoundProcessor;

/**
 * @Project resistance
 * @Description:
 * @Company youku
 * @Create 2016年2月16日下午4:46:08
 * @author zhoushaoyu
 * @version 1.0 Copyright (c) 2016 youku, All Rights Reserved.
 */
@Service
public class RoundProcessorImpl implements RoundProcessor
{

	/**
	 * 方法用途: <br>
	 * 实现步骤: <br>
	 * 
	 * @param game
	 * @param roundNum
	 * @param leader
	 * @return
	 */
	@Override
	public Round process(Game game, int roundNum, Actor leader)
	{
		List<Actor> participants = selectTaskParticipants(game, roundNum, leader);

		discuss(game, leader, participants);

		participants = decideTaskParticipants(game, roundNum, leader);

		VoteResult voteParticipantsResult = voteParticipants(game, roundNum, leader, participants);

		Round round = new Round(roundNum, leader, game.getGameConfig().getActorCount());
		round.setParticipants(participants);
		round.setDoTask(voteParticipantsResult.isVoteSuccess());

		if (!voteParticipantsResult.isVoteSuccess())
		{
			return round;
		}

		VoteResult voteDoTaskResult = voteDoTask(game, roundNum, leader, participants);
		round.setApproveCount(voteDoTaskResult.getApproveCount());

		Task task = game.getRoundConfig().getTaskParticipants().get(roundNum);
		if (task == null)
		{
			throw new IllegalStateException(String.format("未找到第%d轮任务配置", roundNum));
		}
		if (voteDoTaskResult.getApproveCount() <= participants.size() - task.getFailOnDenyCount())
		{
			round.setTaskSuccess(false);
		} else
		{
			round.setTaskSuccess(true);
		}

		logGameHistory(game, round);
		return round;
	}

	/**
	 * 方法用途: <br>
	 * 实现步骤: <br>
	 * 
	 * @param game
	 * @param round
	 */
	private void logGameHistory(Game game, Round round)
	{
		game.getHistory().getHistoryRounds().put(round.getRoundNum(), round);
	}

	/**
	 * 方法用途: <br>
	 * 实现步骤: <br>
	 * 
	 * @param game
	 * @param roundNum
	 * @param leader
	 * @param participants
	 * @return
	 */
	private VoteResult voteParticipants(Game game, int roundNum, Actor leader, List<Actor> participants)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 方法用途: <br>
	 * 实现步骤: <br>
	 * 
	 * @param game
	 * @param roundNum
	 * @param leader
	 * @param participants
	 * @return
	 */
	private VoteResult voteDoTask(Game game, int roundNum, Actor leader, List<Actor> participants)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 方法用途: <br>
	 * 实现步骤: <br>
	 * 
	 * @param game
	 * @param roundNum
	 * @param leader
	 * @return
	 */
	private List<Actor> decideTaskParticipants(Game game, int roundNum, Actor leader)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 方法用途: <br>
	 * 实现步骤: <br>
	 * 
	 * @param game
	 * @param leader
	 * @param participants
	 */
	private void discuss(Game game, Actor leader, List<Actor> participants)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * 方法用途: <br>
	 * 实现步骤: <br>
	 * 
	 * @param game
	 * @param roundNum
	 * @param leader
	 * @return
	 */
	private List<Actor> selectTaskParticipants(Game game, int roundNum, Actor leader)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
