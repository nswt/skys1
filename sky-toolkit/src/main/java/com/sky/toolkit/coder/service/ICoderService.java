package com.sky.toolkit.coder.service;

import com.sky.core.base.service.IBaseService;
import com.sky.core.page.Page;
import com.sky.toolkit.coder.model.Coder;
import com.sky.toolkit.coder.model.Column;

/**
 * 类名称：代码生成器接口类
 * 
 * @author Simon
 * @date 2017年10月24日
 * @version
 */
public interface ICoderService extends IBaseService<Coder> {

	/**
	 * 新增
	 * 
	 * @param coder
	 * @throws Exception
	 * @return
	 */
	public int save(Coder coder) throws Exception;

	/**
	 * 删除
	 * 
	 * @param coderId
	 * @throws Exception
	 * @return
	 */
	public int delete(String coderId) throws Exception;

	/**
	 * 通过id获取数据
	 * 
	 * @param coderId
	 * @throws Exception
	 * @return
	 */
	public Coder findById(String coderId) throws Exception;

	/**
	 * 列表(主表)
	 * 
	 * @param page
	 * @throws Exception
	 * @return
	 */
	public Page<Column> findColumnForList(Page<Column> page) throws Exception;

}
