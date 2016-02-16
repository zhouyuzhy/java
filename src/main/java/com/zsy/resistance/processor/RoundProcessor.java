package com.zsy.resistance.processor;

import com.zsy.resistance.dto.Actor;
import com.zsy.resistance.dto.Game;
import com.zsy.resistance.dto.Round;


/**
 * @Project resistance
 * @Description: 
 * @Company youku
 * @Create 2016年2月14日下午6:31:23
 * @author zhoushaoyu
 * @version 1.0
 * Copyright (c) 2016 youku, All Rights Reserved.
 */

public interface RoundProcessor
{
	public Round process(Game game, int roundNum, Actor leader);
}
