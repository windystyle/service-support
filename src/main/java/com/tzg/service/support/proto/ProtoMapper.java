package com.tzg.service.support.proto;

import java.util.List;
import java.util.Map;

/**
 * 原型数据访问接口。提供基础的数据访问层增、删、改、查等数据访问操作。
 *
 * @author 曾林 2016/12/7.
 */
public interface ProtoMapper< T extends ProtoBean > {

    /**
     * 记录新增
     *
     * @param bean 需持久化的数据对象
     * @throws Exception 数据库访问异常
     */
    void insert( T bean ) throws Exception;

    /**
     * 记录删除
     *
     * @param id 要删除记录的主键
     * @throws Exception 数据库访问异常
     */
    void delete( Integer id ) throws Exception;

    /**
     * 记录批量删除
     *
     * @param map 查询条件映射
     * @throws Exception 数据库访问异常
     */
    void deleteList( Map< String, Object > map ) throws Exception;

    /**
     * 记录更新
     *
     * @param bean 需持久化的数据对象
     * @throws Exception 数据库访问异常
     */
    void update( T bean ) throws Exception;

    /**
     * 根据主键查询记录
     *
     * @param id 主键
     * @throws Exception 数据库访问异常
     */
    T selectById( Object id ) throws Exception;

    /**
     * 根据查询条件返回查询结果记录数。一般用于分页查询。
     *
     * @param map 查询条件映射
     * @throws Exception 数据库访问异常
     */
    Integer selectCount( Map< String, Object > map ) throws Exception;

    /**
     * 根据查询条件返回查询结果。
     *
     * @param map 查询条件映射
     * @throws Exception 数据库访问异常
     */
    List< T > selectList( Map< String, Object > map ) throws Exception;
}
