package com.zsy.resistance.dto;

import com.zsy.resistance.constant.IdentityEnum;

/**
 * @Project resistance
 * @Description:
 * @Company youku
 * @Create 2016年2月14日下午4:31:46
 * @author zhoushaoyu
 * @version 1.0 Copyright (c) 2016 youku, All Rights Reserved.
 */

public class Actor
{

	// 标识玩家序号
	private int id;

	// 玩家代表的身份
	private IdentityEnum identity;

	// 玩家下家
	private Actor next;

	/**
	 * @param id
	 * @param identity
	 * @param next
	 */
	public Actor(int id, IdentityEnum identity, Actor next)
	{
		super();
		this.id = id;
		this.identity = identity;
		this.next = next;
	}

	/**
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * @return the identity
	 */
	public IdentityEnum getIdentity()
	{
		return identity;
	}

	public boolean isBlue()
	{
		return identity != null ? identity == IdentityEnum.BLUE : false;
	}

	public boolean isRed()
	{
		return identity != null ? identity == IdentityEnum.RED : false;
	}

	/**
	 * @return the next
	 */
	public Actor getNext()
	{
		return next;
	}

	/**
	 * 方法用途: <br>
	 * 实现步骤: <br>
	 * 
	 * @return
	 */
	@Override
	public String toString()
	{
		return "Actor [id=" + id + ", identity=" + identity + "]";
	}

}
