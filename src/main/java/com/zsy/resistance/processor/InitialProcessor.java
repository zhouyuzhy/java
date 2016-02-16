package com.zsy.resistance.processor;

import com.zsy.resistance.dto.Game;

/**
 * @Project resistance
 * @Description: 当前局初始化，决定人数，初始配置每轮任务人数，洗牌，分配身份，
 * @Company youku
 * @Create 2016年2月14日下午5:19:02
 * @author zhoushaoyu
 * @version 1.0 Copyright (c) 2016 youku, All Rights Reserved.
 */

public interface InitialProcessor
{

	public Game process(int actorCount);
}
