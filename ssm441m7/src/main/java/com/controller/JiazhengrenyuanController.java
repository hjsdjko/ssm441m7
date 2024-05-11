package com.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import com.utils.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.annotation.IgnoreAuth;

import com.entity.JiazhengrenyuanEntity;
import com.entity.view.JiazhengrenyuanView;

import com.service.JiazhengrenyuanService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MD5Util;
import com.utils.MPUtil;
import com.utils.CommonUtil;
import com.service.StoreupService;
import com.entity.StoreupEntity;

/**
 * 家政人员
 * 后端接口
 * @author 
 * @email 
 * @date 2022-03-08 09:16:35
 */
@RestController
@RequestMapping("/jiazhengrenyuan")
public class JiazhengrenyuanController {
    @Autowired
    private JiazhengrenyuanService jiazhengrenyuanService;


    @Autowired
    private StoreupService storeupService;

    
	@Autowired
	private TokenService tokenService;
	
	/**
	 * 登录
	 */
	@IgnoreAuth
	@RequestMapping(value = "/login")
	public R login(String username, String password, String captcha, HttpServletRequest request) {
		JiazhengrenyuanEntity user = jiazhengrenyuanService.selectOne(new EntityWrapper<JiazhengrenyuanEntity>().eq("jiazhengzhanghao", username));
		if(user==null || !user.getMima().equals(password)) {
			return R.error("账号或密码不正确");
		}
		String token = tokenService.generateToken(user.getId(), username,"jiazhengrenyuan",  "家政人员" );
		return R.ok().put("token", token);
	}
	
	/**
     * 注册
     */
	@IgnoreAuth
    @RequestMapping("/register")
    public R register(@RequestBody JiazhengrenyuanEntity jiazhengrenyuan){
    	//ValidatorUtils.validateEntity(jiazhengrenyuan);
    	JiazhengrenyuanEntity user = jiazhengrenyuanService.selectOne(new EntityWrapper<JiazhengrenyuanEntity>().eq("jiazhengzhanghao", jiazhengrenyuan.getJiazhengzhanghao()));
		if(user!=null) {
			return R.error("注册用户已存在");
		}
		Long uId = new Date().getTime();
		jiazhengrenyuan.setId(uId);
        jiazhengrenyuanService.insert(jiazhengrenyuan);
        return R.ok();
    }

	
	/**
	 * 退出
	 */
	@RequestMapping("/logout")
	public R logout(HttpServletRequest request) {
		request.getSession().invalidate();
		return R.ok("退出成功");
	}
	
	/**
     * 获取用户的session用户信息
     */
    @RequestMapping("/session")
    public R getCurrUser(HttpServletRequest request){
    	Long id = (Long)request.getSession().getAttribute("userId");
        JiazhengrenyuanEntity user = jiazhengrenyuanService.selectById(id);
        return R.ok().put("data", user);
    }
    
    /**
     * 密码重置
     */
    @IgnoreAuth
	@RequestMapping(value = "/resetPass")
    public R resetPass(String username, HttpServletRequest request){
    	JiazhengrenyuanEntity user = jiazhengrenyuanService.selectOne(new EntityWrapper<JiazhengrenyuanEntity>().eq("jiazhengzhanghao", username));
    	if(user==null) {
    		return R.error("账号不存在");
    	}
    	user.setMima("123456");
        jiazhengrenyuanService.updateById(user);
        return R.ok("密码已重置为：123456");
    }


    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,JiazhengrenyuanEntity jiazhengrenyuan, 
		HttpServletRequest request){

        EntityWrapper<JiazhengrenyuanEntity> ew = new EntityWrapper<JiazhengrenyuanEntity>();
		PageUtils page = jiazhengrenyuanService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, jiazhengrenyuan), params), params));
        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,JiazhengrenyuanEntity jiazhengrenyuan, 
		HttpServletRequest request){
        EntityWrapper<JiazhengrenyuanEntity> ew = new EntityWrapper<JiazhengrenyuanEntity>();
		PageUtils page = jiazhengrenyuanService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, jiazhengrenyuan), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( JiazhengrenyuanEntity jiazhengrenyuan){
       	EntityWrapper<JiazhengrenyuanEntity> ew = new EntityWrapper<JiazhengrenyuanEntity>();
      	ew.allEq(MPUtil.allEQMapPre( jiazhengrenyuan, "jiazhengrenyuan")); 
        return R.ok().put("data", jiazhengrenyuanService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(JiazhengrenyuanEntity jiazhengrenyuan){
        EntityWrapper< JiazhengrenyuanEntity> ew = new EntityWrapper< JiazhengrenyuanEntity>();
 		ew.allEq(MPUtil.allEQMapPre( jiazhengrenyuan, "jiazhengrenyuan")); 
		JiazhengrenyuanView jiazhengrenyuanView =  jiazhengrenyuanService.selectView(ew);
		return R.ok("查询家政人员成功").put("data", jiazhengrenyuanView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        JiazhengrenyuanEntity jiazhengrenyuan = jiazhengrenyuanService.selectById(id);
		jiazhengrenyuan.setClicknum(jiazhengrenyuan.getClicknum()+1);
		jiazhengrenyuan.setClicktime(new Date());
		jiazhengrenyuanService.updateById(jiazhengrenyuan);
        return R.ok().put("data", jiazhengrenyuan);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        JiazhengrenyuanEntity jiazhengrenyuan = jiazhengrenyuanService.selectById(id);
		jiazhengrenyuan.setClicknum(jiazhengrenyuan.getClicknum()+1);
		jiazhengrenyuan.setClicktime(new Date());
		jiazhengrenyuanService.updateById(jiazhengrenyuan);
        return R.ok().put("data", jiazhengrenyuan);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody JiazhengrenyuanEntity jiazhengrenyuan, HttpServletRequest request){
    	jiazhengrenyuan.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(jiazhengrenyuan);
    	JiazhengrenyuanEntity user = jiazhengrenyuanService.selectOne(new EntityWrapper<JiazhengrenyuanEntity>().eq("jiazhengzhanghao", jiazhengrenyuan.getJiazhengzhanghao()));
		if(user!=null) {
			return R.error("用户已存在");
		}

		jiazhengrenyuan.setId(new Date().getTime());
        jiazhengrenyuanService.insert(jiazhengrenyuan);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody JiazhengrenyuanEntity jiazhengrenyuan, HttpServletRequest request){
    	jiazhengrenyuan.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(jiazhengrenyuan);
    	JiazhengrenyuanEntity user = jiazhengrenyuanService.selectOne(new EntityWrapper<JiazhengrenyuanEntity>().eq("jiazhengzhanghao", jiazhengrenyuan.getJiazhengzhanghao()));
		if(user!=null) {
			return R.error("用户已存在");
		}

		jiazhengrenyuan.setId(new Date().getTime());
        jiazhengrenyuanService.insert(jiazhengrenyuan);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody JiazhengrenyuanEntity jiazhengrenyuan, HttpServletRequest request){
        //ValidatorUtils.validateEntity(jiazhengrenyuan);
        jiazhengrenyuanService.updateById(jiazhengrenyuan);//全部更新
        return R.ok();
    }
    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        jiazhengrenyuanService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
    /**
     * 提醒接口
     */
	@RequestMapping("/remind/{columnName}/{type}")
	public R remindCount(@PathVariable("columnName") String columnName, HttpServletRequest request, 
						 @PathVariable("type") String type,@RequestParam Map<String, Object> map) {
		map.put("column", columnName);
		map.put("type", type);
		
		if(type.equals("2")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			Date remindStartDate = null;
			Date remindEndDate = null;
			if(map.get("remindstart")!=null) {
				Integer remindStart = Integer.parseInt(map.get("remindstart").toString());
				c.setTime(new Date()); 
				c.add(Calendar.DAY_OF_MONTH,remindStart);
				remindStartDate = c.getTime();
				map.put("remindstart", sdf.format(remindStartDate));
			}
			if(map.get("remindend")!=null) {
				Integer remindEnd = Integer.parseInt(map.get("remindend").toString());
				c.setTime(new Date());
				c.add(Calendar.DAY_OF_MONTH,remindEnd);
				remindEndDate = c.getTime();
				map.put("remindend", sdf.format(remindEndDate));
			}
		}
		
		Wrapper<JiazhengrenyuanEntity> wrapper = new EntityWrapper<JiazhengrenyuanEntity>();
		if(map.get("remindstart")!=null) {
			wrapper.ge(columnName, map.get("remindstart"));
		}
		if(map.get("remindend")!=null) {
			wrapper.le(columnName, map.get("remindend"));
		}


		int count = jiazhengrenyuanService.selectCount(wrapper);
		return R.ok().put("count", count);
	}
	
	/**
     * 前端智能排序
     */
	@IgnoreAuth
    @RequestMapping("/autoSort")
    public R autoSort(@RequestParam Map<String, Object> params,JiazhengrenyuanEntity jiazhengrenyuan, HttpServletRequest request,String pre){
        EntityWrapper<JiazhengrenyuanEntity> ew = new EntityWrapper<JiazhengrenyuanEntity>();
        Map<String, Object> newMap = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
		Iterator<Map.Entry<String, Object>> it = param.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> entry = it.next();
			String key = entry.getKey();
			String newKey = entry.getKey();
			if (pre.endsWith(".")) {
				newMap.put(pre + newKey, entry.getValue());
			} else if (StringUtils.isEmpty(pre)) {
				newMap.put(newKey, entry.getValue());
			} else {
				newMap.put(pre + "." + newKey, entry.getValue());
			}
		}
		params.put("sort", "clicknum");
        
        params.put("order", "desc");
		PageUtils page = jiazhengrenyuanService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, jiazhengrenyuan), params), params));
        return R.ok().put("data", page);
    }







}
