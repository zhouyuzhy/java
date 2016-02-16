package com.zsy.resistance.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project resistance
 * @Description:
 * @Company youku
 * @Create 2016年2月14日下午5:50:35
 * @author zhoushaoyu
 * @version 1.0 Copyright (c) 2016 youku, All Rights Reserved.
 */

public class Game
{

	/**
	 * 所有游戏参与者
	 */
	private List<Actor> actors = new ArrayList<Actor>();

	/**
	 * 当前参与者
	 */
	private Actor currentActor;

	/**
	 * 当前已成功任务数
	 */
	private int taskSuccessCount;

	/**
	 * 当前局已执行到第几轮
	 */
	private int roundNum;

	/**
	 * 当前局历史信息
	 */
	private History history;

	private GameConfig gameConfig;

	private RoundConfig roundConfig;

	/**
	 * 是否已经游戏结束
	 */
	private boolean gameOver;

	/**
	 * 是否好人已胜利
	 */
	private boolean blueSuccess;

	/**
	 * @return the actors
	 */
	public List<Actor> getActors()
	{
		return actors;
	}

	/**
	 * @param actors
	 *            the actors to set
	 */
	public void setActors(List<Actor> actors)
	{
		this.actors = actors;
	}

	/**
	 * @return the currentActor
	 */
	public Actor getCurrentActor()
	{
		return currentActor;
	}

	/**
	 * @param currentActor
	 *            the currentActor to set
	 */
	public void setCurrentActor(Actor currentActor)
	{
		this.currentActor = currentActor;
	}

	/**
	 * @return the roundNum
	 */
	public int getRoundNum()
	{
		return roundNum;
	}

	/**
	 * @param roundNum
	 *            the roundNum to set
	 */
	public void setRoundNum(int roundNum)
	{
		this.roundNum = roundNum;
	}

	/**
	 * @return the taskSuccessCount
	 */
	public int getTaskSuccessCount()
	{
		return taskSuccessCount;
	}

	/**
	 * @param taskSuccessCount
	 *            the taskSuccessCount to set
	 */
	public void setTaskSuccessCount(int taskSuccessCount)
	{
		this.taskSuccessCount = taskSuccessCount;
	}

	/**
	 * @return the history
	 */
	public History getHistory()
	{
		return history;
	}

	/**
	 * @param history
	 *            the history to set
	 */
	public void setHistory(History history)
	{
		this.history = history;
	}

	/**
	 * @return the gameConfig
	 */
	public GameConfig getGameConfig()
	{
		return gameConfig;
	}

	/**
	 * @param gameConfig
	 *            the gameConfig to set
	 */
	public void setGameConfig(GameConfig gameConfig)
	{
		this.gameConfig = gameConfig;
	}

	/**
	 * @return the roundConfig
	 */
	public RoundConfig getRoundConfig()
	{
		return roundConfig;
	}

	/**
	 * @param roundConfig
	 *            the roundConfig to set
	 */
	public void setRoundConfig(RoundConfig roundConfig)
	{
		this.roundConfig = roundConfig;
	}

	/**
	 * @return the gameOver
	 */
	public boolean isGameOver()
	{
		return gameOver;
	}

	/**
	 * @param gameOver
	 *            the gameOver to set
	 */
	public void setGameOver(boolean gameOver)
	{
		this.gameOver = gameOver;
	}

	/**
	 * @return the blueSuccess
	 */
	public boolean isBlueSuccess()
	{
		return blueSuccess;
	}

	/**
	 * @param blueSuccess
	 *            the blueSuccess to set
	 */
	public void setBlueSuccess(boolean blueSuccess)
	{
		this.blueSuccess = blueSuccess;
	}

}
