package com.zsy.resistance.dto;

/**
 * @Project resistance
 * @Description:
 * @Company youku
 * @Create 2016年2月14日下午6:21:08
 * @author zhoushaoyu
 * @version 1.0 Copyright (c) 2016 youku, All Rights Reserved.
 */

public class GameConfig
{

	/**
	 * 游戏人数
	 */
	private Integer actorCount;

	/**
	 * 好人个数
	 */
	private Integer blueActorCount;

	/**
	 * @param actorCount
	 * @param blueActorCount
	 */
	public GameConfig(Integer actorCount, Integer blueActorCount)
	{
		this.actorCount = actorCount;
		this.blueActorCount = blueActorCount;
	}

	/**
	 * @return the actorCount
	 */
	public Integer getActorCount()
	{
		return actorCount;
	}

	/**
	 * @return the blueActorCount
	 */
	public Integer getBlueActorCount()
	{
		return blueActorCount;
	}

}
