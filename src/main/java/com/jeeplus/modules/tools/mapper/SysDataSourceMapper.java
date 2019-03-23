/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tools.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.tools.entity.DbTable;
import com.jeeplus.modules.tools.entity.DbTableColumn;
import com.jeeplus.modules.tools.entity.SysDataSource;

import java.util.List;

/**
 * 多数据源MAPPER接口
 * @author liugf
 * @version 2017-07-27
 */
@MyBatisMapper
public interface SysDataSourceMapper extends BaseMapper<SysDataSource> {

    public SysDataSource getByEnname(String enname);

}