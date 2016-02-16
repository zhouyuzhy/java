package com.zsy.resistance.processor.impl;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.zsy.resistance.constant.GameConstant;
import com.zsy.resistance.constant.IdentityEnum;
import com.zsy.resistance.dto.Actor;
import com.zsy.resistance.dto.Game;
import com.zsy.resistance.dto.GameConfig;
import com.zsy.resistance.dto.History;
import com.zsy.resistance.dto.RoundConfig;
import com.zsy.resistance.dto.Task;
import com.zsy.resistance.processor.InitialProcessor;

/**
 * @Project resistance
 * @Description:
 * @Company youku
 * @Create 2016年2月16日下午2:37:03
 * @author zhoushaoyu
 * @version 1.0 Copyright (c) 2016 youku, All Rights Reserved.
 */
@Service
public class InitialProcessorImpl implements InitialProcessor
{

	/**
	 * 方法用途: <br>
	 * 实现步骤: <br>
	 * 
	 * @param actorCount
	 * @return
	 */
	@Override
	public Game process(int actorCount)
	{
		if (!GameConstant.blueCountMap.containsKey(actorCount))
		{
			throw new IllegalArgumentException("不支持的人数设置" + actorCount);
		}
		Game game = new Game();

		GameConfig gameConfig = new GameConfig(actorCount, GameConstant.blueCountMap.get(actorCount));
		game.setGameConfig(gameConfig);
		game.setHistory(new History());

		RoundConfig roundConfig = new RoundConfig();
		LinkedList<Task> tasks = GameConstant.taskParticipantsCountMap.get(actorCount);
		int i = 1;
		for (Task task : tasks)
		{
			roundConfig.getTaskParticipants().put(i++, task);
		}
		game.setRoundConfig(roundConfig);

		initalActors(game, gameConfig);

		showIdentity(game);
		
		return game;
	}

	/** 
	 * 方法用途: <br>
	 * 实现步骤: <br>
	 * @param game   
	 */
	private void showIdentity(Game game)
	{
		System.out.println(game.getCurrentActor());
	}

	/**
	 * 方法用途: <br>
	 * 实现步骤: <br>
	 * 
	 * @param game
	 * @param gameConfig
	 */
	private void initalActors(Game game, GameConfig gameConfig)
	{
		int actorCount = gameConfig.getActorCount();
		int blueCount = gameConfig.getBlueActorCount();

		// 产生随机的actorCount-blueCount个ID为坏人
		Set<Integer> redActorId = new HashSet<Integer>();
		Random random = new Random();
		while (redActorId.size() < actorCount - blueCount)
		{
			redActorId.add(random.nextInt(actorCount));
		}
		Actor nextActor = null;
		for (int i = actorCount - 1; i >= 0; i--)
		{
			Actor actor = new Actor(i, redActorId.contains(i) ? IdentityEnum.RED : IdentityEnum.BLUE, nextActor);
			if (i == 0)
			{
				game.setCurrentActor(actor);
			}
			game.getActors().add(actor);
			nextActor = actor;
		}

	}

}
