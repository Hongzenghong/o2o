package com.hzh.o2o.dao;

import java.util.List;

import com.hzh.o2o.entity.Area;

public interface AreaDao {
	/**
	 * 返回列出区域列表
	 * @return  areaList
	 */
    List<Area> queryArea();
}
