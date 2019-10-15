/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.core.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.core.persistence.Page;

import javax.annotation.Resource;

/**
 * Service基类
 *
 * @author jeeplus
 * @version 2017-05-16
 */
@Transactional(readOnly = true)
public abstract class CrudService<M extends BaseMapper<T>, T extends DataEntity<T>> extends BaseService {


    /**
     * 持久层对象
     */
    @Autowired
    protected M mapper;

    /**
     * 获取单条数据
     *
     * @param id
     * @return
     */
    public T get(String id) {
        sleep();
        return mapper.get(id);
    }

    /**
     * 获取单条数据
     *
     * @param entity
     * @return
     */
    public T get(T entity) {
        sleep();
        return mapper.get(entity);
    }

    /**
     * 查询列表数据
     *
     * @param entity
     * @return
     */
    public List<T> findList(T entity) {
        sleep();
        dataRuleFilter(entity);
        return mapper.findList(entity);
    }


    /**
     * 查询所有列表数据
     *
     * @param entity
     * @return
     */
    public List<T> findAllList(T entity) {
        sleep();
        dataRuleFilter(entity);
        return mapper.findAllList(entity);
    }

    /**
     * 查询分页数据
     *
     * @param page   分页对象
     * @param entity
     * @return
     */
    public Page<T> findPage(Page<T> page, T entity) {
        sleep();
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findList(entity));
        return page;
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param entity
     */
    @Transactional(readOnly = false)
    public void save(T entity) {
        sleep();
        if (entity.getIsNewRecord()) {
            entity.preInsert();
            mapper.insert(entity);
        } else {
            entity.preUpdate();
            mapper.update(entity);
        }
    }


    /**
     * 删除数据
     *
     * @param entity
     */
    @Transactional(readOnly = false)
    public void delete(T entity) {
        sleep();
        mapper.delete(entity);
    }


    /**
     * 删除全部数据
     *
     * @param entitys
     */
    @Transactional(readOnly = false)
    public void deleteAll(Collection<T> entitys) {
        sleep();
        for (T entity : entitys) {
            mapper.delete(entity);
        }
    }

    /**
     * 删除全部数据
     *
     * @param entitys
     */
    @Transactional(readOnly = false)
    public void deleteAllByLogic(Collection<T> entitys) {
        sleep();
        for (T entity : entitys) {
            mapper.deleteByLogic(entity);
        }
    }


    /**
     * 获取单条数据
     *
     * @param propertyName, value
     * @return
     */
    public T findUniqueByProperty(String propertyName, Object value) {
        sleep();
        return mapper.findUniqueByProperty(propertyName, value);
    }

    /**
     * 动态sql
     *
     * @param sql
     */

    public List<Map<String, Object>> executeSelectSql(String sql) {
        sleep();
        return mapper.execSelectSql(sql);
    }

    @Transactional(readOnly = false)
    public void executeInsertSql(String sql) {
        sleep();
        mapper.execInsertSql(sql);
    }

    @Transactional(readOnly = false)
    public void executeUpdateSql(String sql) {
        sleep();
        mapper.execUpdateSql(sql);
    }

    @Transactional(readOnly = false)
    public void executeDeleteSql(String sql) {
        sleep();
        mapper.execDeleteSql(sql);
    }

    private void sleep() {
//        try {
            // 减慢速度，好收钱，收钱后，需要放开这个速度限制 fixme
//            int miao = 0;
//            Thread.sleep(1000 * miao);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

}
