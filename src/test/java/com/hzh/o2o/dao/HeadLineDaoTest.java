package com.hzh.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hzh.o2o.BaseTest;
import com.hzh.o2o.entity.HeadLine;

public class HeadLineDaoTest extends BaseTest{
	@Autowired
	private HeadLineDao headLineDao;

	@Test
	public void testSelectHeadLineList() {
		HeadLine headLineConditon = new HeadLine();
		headLineConditon.setEnableStatus(1);

		// 查询不可用的头条信息
		List<HeadLine> headLineList = headLineDao.queryHeadLine(headLineConditon);
		assertEquals(2, headLineList.size());
		
		
	}
}
