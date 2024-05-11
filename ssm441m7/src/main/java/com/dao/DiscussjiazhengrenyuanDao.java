package com.dao;

import com.entity.DiscussjiazhengrenyuanEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.entity.vo.DiscussjiazhengrenyuanVO;
import com.entity.view.DiscussjiazhengrenyuanView;


/**
 * 家政人员评论表
 * 
 * @author 
 * @email 
 * @date 2022-03-08 09:16:35
 */
public interface DiscussjiazhengrenyuanDao extends BaseMapper<DiscussjiazhengrenyuanEntity> {
	
	List<DiscussjiazhengrenyuanVO> selectListVO(@Param("ew") Wrapper<DiscussjiazhengrenyuanEntity> wrapper);
	
	DiscussjiazhengrenyuanVO selectVO(@Param("ew") Wrapper<DiscussjiazhengrenyuanEntity> wrapper);
	
	List<DiscussjiazhengrenyuanView> selectListView(@Param("ew") Wrapper<DiscussjiazhengrenyuanEntity> wrapper);

	List<DiscussjiazhengrenyuanView> selectListView(Pagination page,@Param("ew") Wrapper<DiscussjiazhengrenyuanEntity> wrapper);
	
	DiscussjiazhengrenyuanView selectView(@Param("ew") Wrapper<DiscussjiazhengrenyuanEntity> wrapper);
	

}
