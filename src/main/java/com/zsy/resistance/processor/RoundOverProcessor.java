package com.zsy.resistance.processor;

import com.zsy.resistance.dto.Game;
import com.zsy.resistance.dto.Round;


/**
 * @Project resistance
 * @Description: 
 * @Company youku
 * @Create 2016年2月14日下午11:30:31
 * @author zhoushaoyu
 * @version 1.0
 * Copyright (c) 2016 youku, All Rights Reserved.
 */

public interface RoundOverProcessor
{
	public void process(Game game, Round round);
}
