package com.txdata.oa.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txdata.common.controller.BaseController;
import com.txdata.common.utils.Query;
import com.txdata.common.utils.R;
import com.txdata.common.utils.ShiroUtils;
import com.txdata.oa.domain.NotifyDO;
import com.txdata.oa.service.NotifyService;

import cn.hutool.core.util.StrUtil;

/**
 * 通知通告
 * 
 * @author 3xdata
 * @email 3xdata@3xdata.cn
 * @date 2019-07-11 14:57:30
 */
@RestController
@RequestMapping(value = "/oa/oaNotify")
public class NotifyController extends BaseController {
	@Autowired
	private NotifyService notifyService;
	
	@PostMapping("/list")
	@RequiresPermissions("oa:oaNotify:view")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
		if(null != params.get("self") && params.get("self").equals("true")) {
			params.put("userId", ShiroUtils.getUserId());
		}
        Query query = new Query(params);
        Page<NotifyDO> page = new Page<NotifyDO>(query.getPageNo(), query.getPageSize());
		page = notifyService.page(page, query);
		// 封装分页数据
		JSONObject jsonMap = new JSONObject();
        jsonMap.put("rows", page.getRecords());
        jsonMap.put("pageSize", page.getSize());
        jsonMap.put("pageNo", page.getCurrent());
        jsonMap.put("count", page.getTotal());
        return R.success(jsonMap);
	}
	
    @PostMapping(value = {"/form", "/view"})
	@RequiresPermissions("oa:oaNotify:view")
    public R form(@RequestParam(required = true)String id, String pageNo, String pageSize){
		Map<String, Object> params = new HashMap<>();
		params.put("pageNo", pageNo);
		params.put("pageSize", pageSize);
        NotifyDO notify = notifyService.get(id, params);
        notifyService.read(id);
        JSONObject jsonMap = new JSONObject();
        jsonMap.put("formObject", notify);
        return R.success(jsonMap);
    }
	
	@PostMapping("/save")
	@RequiresPermissions("oa:oaNotify:edit")
	public R save(NotifyDO notify){
		if (notifyService.save(notify) > 0){
			return R.success();
		}
		return R.error();
	}
	
	@PostMapping("/remove")
	@RequiresPermissions("oa:oaNotify:edit")
	public R remove(String id){
		if (notifyService.remove(id) > 0){ //逻辑删除
		    return R.success();
		}
		return R.error();
	}
	
	@PostMapping("/batchRemove")
	@RequiresPermissions("oa:oaNotify:edit")
	public R batchRemove(@RequestParam("ids[]") String[] ids){
		notifyService.batchRemove(ids);//批量逻辑删除
		return R.success();
	}
	
	@PostMapping("/delete")
	@RequiresPermissions("oa:oaNotify:edit")
	public R delete(String id){
		if (notifyService.delete(id) > 0){//物理删除
		    return R.success();
		}
		return R.error();
	}
	
	@PostMapping("/batchDelete")
	@RequiresPermissions("oa:oaNotify:edit")
	public R batchDelete(@RequestParam("ids[]") String[] ids){
		notifyService.batchDelete(ids);//批量物理删除
		return R.success();
	}
	
	@PostMapping("/copy")
	@RequiresPermissions("oa:oaNotify:edit")
	public R copy(String id){
		if (notifyService.copy(id) > 0){
			return R.success();
		}
		return R.error();
	}
	
	@PostMapping("/self/count")
    public R count(){
		Map<String, Object> map = new HashMap<>();
		map.put("isRead", "0");
		map.put("userId", ShiroUtils.getUserId());
		int count = notifyService.count(map);
        JSONObject jsonMap = new JSONObject();
        jsonMap.put("count", count);
        return R.success(jsonMap);
    }
	
    /**
     * 批量置为已读
     */
    @ResponseBody
    @PostMapping("self/batchRead")
    public R batchRead(String ids) {
    	if(StrUtil.isNotBlank(ids)){
    		String[] idArray = ids.split(",");
        	for(String id : idArray){
        		notifyService.read(id);
        	}
        	return R.success();
    	}else{
    		return R.error();
    	}
    }
}
