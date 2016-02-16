package com.zsy.resistance.processor.impl;

import org.springframework.stereotype.Service;

import com.zsy.resistance.dto.Actor;
import com.zsy.resistance.dto.Game;
import com.zsy.resistance.processor.DarkProcessor;

/**
 * @Project resistance
 * @Description:
 * @Company youku
 * @Create 2016年2月16日下午2:49:21
 * @author zhoushaoyu
 * @version 1.0 Copyright (c) 2016 youku, All Rights Reserved.
 */
@Service
public class DarkProcessorImpl implements DarkProcessor
{

	/**
	 * 方法用途: <br>
	 * 实现步骤: <br>
	 * 
	 * @param game
	 */
	@Override
	public void process(Game game)
	{
		if (!game.getCurrentActor().isRed())
		{
			System.out.println("天黑闭眼");
			return;
		}
		for (Actor actor : game.getActors())
		{
			if (actor.isRed())
			{
				System.out.println("red:" + actor.getId());
			}
		}
	}

}
