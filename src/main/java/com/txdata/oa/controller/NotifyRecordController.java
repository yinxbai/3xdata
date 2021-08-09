package com.txdata.oa.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txdata.common.controller.BaseController;
import com.txdata.oa.domain.NotifyRecordDO;
import com.txdata.oa.service.NotifyRecordService;
import com.txdata.common.utils.Query;
import com.txdata.common.utils.R;
import com.alibaba.fastjson.JSONObject;

/**
 * 通知通告发送记录
 * 
 * @author 3xdata
 * @email 3xdata@3xdata.cn
 * @date 2019-07-12 11:01:06
 */
@RestController
@RequestMapping("/oa/notifyRecord")
public class NotifyRecordController extends BaseController {
	@Autowired
	private NotifyRecordService notifyRecordService;
	
	@PostMapping("/list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
        Page<NotifyRecordDO> page = new Page<NotifyRecordDO>(query.getPageNo(), query.getPageSize());
		page = notifyRecordService.page(page, query);
		// 封装分页数据
		JSONObject jsonMap = new JSONObject();
        jsonMap.put("rows", page.getRecords());
        jsonMap.put("pageSize", page.getSize());
        jsonMap.put("pageNo", page.getCurrent());
        jsonMap.put("count", page.getTotal());
        return R.success(jsonMap);
	}
	
    @PostMapping("/form")
    public R form(@RequestParam(required = true)String id){
        NotifyRecordDO notifyRecord = notifyRecordService.get(id);
        JSONObject jsonMap = new JSONObject();
        jsonMap.put("formObject", notifyRecord);
        return R.success(jsonMap);
    }
	
	@PostMapping("/save")
	public R save(NotifyRecordDO notifyRecord){
		if (notifyRecordService.save(notifyRecord) > 0){
			return R.success();
		}
		return R.error();
	}
	
	@PostMapping("/remove")
	public R remove(String id){
		if (notifyRecordService.remove(id) > 0){ //逻辑删除
		    return R.success();
		}
		return R.error();
	}
	
	@PostMapping("/batchRemove")
	public R batchRemove(@RequestParam("ids[]") String[] ids){
		notifyRecordService.batchRemove(ids);//批量逻辑删除
		return R.success();
	}
	
	@PostMapping("/delete")
	public R delete(String id){
		if (notifyRecordService.delete(id) > 0){//物理删除
		    return R.success();
		}
		return R.error();
	}
	
	@PostMapping("/batchDelete")
	public R batchDelete(@RequestParam("ids[]") String[] ids){
		notifyRecordService.batchDelete(ids);//批量物理删除
		return R.success();
	}
	
	@PostMapping("/copy")
	public R copy(String id){
		if (notifyRecordService.copy(id) > 0){
			return R.success();
		}
		return R.error();
	}
}
