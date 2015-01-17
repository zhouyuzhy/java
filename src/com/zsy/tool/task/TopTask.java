package com.zsy.tool.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import android.util.Log;

import com.zsy.tool.dto.TopInfoDto;
import com.zsy.tool.util.ShellUtils;
import com.zsy.tool.util.ShellUtils.CommandResult;

public class TopTask {
	private LinkedBlockingQueue<List<TopInfoDto>> queue;
	private Comparator<TopInfoDto> comparator;

	public TopTask(LinkedBlockingQueue<List<TopInfoDto>> queue) {
		this.queue = queue;
	}

	public TopTask(LinkedBlockingQueue<List<TopInfoDto>> queue,
			Comparator<TopInfoDto> comparator) {
		this.queue = queue;
		this.comparator = comparator;
	}

	public void execute() {
		try {
			if (queue == null) {
				throw new IllegalArgumentException();
			}
			CommandResult result = ShellUtils.execCommand("top -n 1", true,
					true);
			Log.d("msg", result.successMsg.split("\n")[0]);
			if (result.successMsg != null
					&& result.successMsg.split("\n").length > 7) {
				List<TopInfoDto> topInfos = new ArrayList<TopInfoDto>();
				for (int i = 7; i < result.successMsg.split("\n").length; i++) {
					String msg = result.successMsg.split("\n")[i];
					String[] items = msg.trim().split(" ");
					String[] removeBlankItems = new String[10];
					int index = 0;
					for (String item : items) {
						if (item != null && !"".equals(item)) {
							removeBlankItems[index++] = item;
						}
					}
					topInfos.add(new TopInfoDto(removeBlankItems[0],
							removeBlankItems[1], removeBlankItems[2],
							removeBlankItems[3], removeBlankItems[4],
							removeBlankItems[5], removeBlankItems[6],
							removeBlankItems[7], removeBlankItems[8],
							removeBlankItems[9]));
				}
				if(comparator != null)
				{
					Collections.sort(topInfos, comparator);
				}
				try {
					queue.put(topInfos);
				} catch (InterruptedException e) {
					Log.e("errorMsg", e.getMessage(), e);
				}
			}
		} catch (Exception e) {
			Log.e("errorMsg", e.getMessage(), e);
		}

	}
}
