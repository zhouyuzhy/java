package com.zsy.resistance.constant;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.zsy.resistance.dto.Task;

/**
 * @Project resistance
 * @Description:
 * @Company youku
 * @Create 2016年2月16日上午10:participantsCount3:03
 * @author zhoushaoyu
 * @version 1.0 Copyright (c) 2016 youku, All Rights Reserved.
 */

public class GameConstant
{

	/**
	 * 当总人数为某个数时的任务参与人数，key为总人数，value为每轮所需任务人数
	 */
	public static final Map<Integer, LinkedList<Task>> taskParticipantsCountMap = new HashMap<>();

	/**
	 * 当局游戏根据总人数配置的好人人数，key为总人数，value为好人人数
	 */
	public static final Map<Integer, Integer> blueCountMap = new HashMap<>();

	static
	{
		// TODO 调整为properties配置
		int participantsCount = 5;
		taskParticipantsCountMap.put(participantsCount, new LinkedList<Task>());
		taskParticipantsCountMap.get(participantsCount).add(new Task(2));
		taskParticipantsCountMap.get(participantsCount).add(new Task(3));
		taskParticipantsCountMap.get(participantsCount).add(new Task(2));
		taskParticipantsCountMap.get(participantsCount).add(new Task(3));
		taskParticipantsCountMap.get(participantsCount).add(new Task(3));

		blueCountMap.put(participantsCount, 3);

		participantsCount = 6;
		taskParticipantsCountMap.put(participantsCount, new LinkedList<Task>());
		taskParticipantsCountMap.get(participantsCount).add(new Task(2));
		taskParticipantsCountMap.get(participantsCount).add(new Task(3));
		taskParticipantsCountMap.get(participantsCount).add(new Task(4));
		taskParticipantsCountMap.get(participantsCount).add(new Task(3));
		taskParticipantsCountMap.get(participantsCount).add(new Task(4));

		blueCountMap.put(participantsCount, 4);

		participantsCount = 7;
		taskParticipantsCountMap.put(participantsCount, new LinkedList<Task>());
		taskParticipantsCountMap.get(participantsCount).add(new Task(2));
		taskParticipantsCountMap.get(participantsCount).add(new Task(3));
		taskParticipantsCountMap.get(participantsCount).add(new Task(3));
		taskParticipantsCountMap.get(participantsCount).add(new Task(4, 2));
		taskParticipantsCountMap.get(participantsCount).add(new Task(4));

		blueCountMap.put(participantsCount, 4);

		for (int i = 8; i < 11; i++)
		{
			participantsCount = i;
			taskParticipantsCountMap.put(participantsCount, new LinkedList<Task>());
			taskParticipantsCountMap.get(participantsCount).add(new Task(3));
			taskParticipantsCountMap.get(participantsCount).add(new Task(4));
			taskParticipantsCountMap.get(participantsCount).add(new Task(4));
			taskParticipantsCountMap.get(participantsCount).add(new Task(5, 2));
			taskParticipantsCountMap.get(participantsCount).add(new Task(5));

			blueCountMap.put(participantsCount, (i + 3) / 2);
		}
	}
	
}
