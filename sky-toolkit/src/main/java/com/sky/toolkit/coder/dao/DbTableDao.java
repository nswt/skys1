package com.sky.toolkit.coder.dao;

import com.sky.core.page.Page;
import com.sky.toolkit.coder.model.DbTable;

import tk.mybatis.mapper.common.Mapper;

/**
 * 
 * @author Simon
 *
 */
public interface DbTableDao extends Mapper<DbTable> {
	/**
	 * 根据ID查询对象
	 * @param tableName
	 * @return
	 */
	DbTable findById(String tableName);

	/**
	 * 查询table表列表
	 * @param page
	 */
	void findForPageList(Page<DbTable> page);
}
