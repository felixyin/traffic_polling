/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.iim.mapper;

import java.util.List;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.iim.entity.ChatHistory;

/**
 * 聊天记录MAPPER接口
 * @author jeeplus
 * @version 2015-12-29
 */
@MyBatisMapper
public interface ChatHistoryMapper extends BaseMapper<ChatHistory> {
	
	
	/**
	 * 查询列表数据
	 * @param entity
	 * @return
	 */
	public List<ChatHistory> findLogList(ChatHistory entity);
	

	/**
	 * 查询群组列表数据
	 * @param entity
	 * @return
	 */
	public List<ChatHistory> findGroupLogList(ChatHistory entity);
	
	public int findUnReadCount(ChatHistory chatHistory);
	
}