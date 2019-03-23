/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.test.manytomany.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.test.manytomany.entity.StudentCourse;

/**
 * 学生课程记录MAPPER接口
 * @author lgf
 * @version 2018-06-12
 */
@MyBatisMapper
public interface StudentCourseMapper extends BaseMapper<StudentCourse> {
	
}