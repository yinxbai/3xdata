package com.txdata.modules.contract.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.txdata.common.annotation.Log;
import com.txdata.common.utils.DateUtils;
import com.txdata.common.utils.StringUtils;
import com.txdata.modules.contract.dao.ContractProjectDao;
import com.txdata.modules.contract.domain.ContractProjectDO;
import com.txdata.modules.contract.domain.PaymentDO;
import com.txdata.modules.contract.domain.PurchaseDO;
import io.swagger.models.auth.In;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txdata.common.controller.BaseController;
import com.txdata.modules.contract.domain.InfoDO;
import com.txdata.modules.contract.service.InfoService;
import com.txdata.common.utils.Query;
import com.txdata.common.utils.R;
import com.alibaba.fastjson.JSONObject;

import javax.sound.sampled.Line;

/**
 *
 * @author InRoota
 * @2021-08-02 09:20:47
 */
@RestController
@RequestMapping("/contract")
public class InfoController extends BaseController {
	@Autowired
	private InfoService infoService;

	@ResponseBody
	@PostMapping("/list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
        Page<InfoDO> page = new Page<InfoDO>(query.getPageNo(), query.getPageSize());
		page = infoService.page(page, query);
		// 封装分页数据
		JSONObject jsonMap = new JSONObject();
        jsonMap.put("rows", page.getRecords());
        jsonMap.put("pageSize", page.getSize());
        jsonMap.put("pageNo", page.getCurrent());
        jsonMap.put("count", page.getTotal());
        return R.success(jsonMap);
	}
	
	@ResponseBody
    @PostMapping("/form")
    public R form(@RequestParam(required = true)String id){
        InfoDO info = infoService.get(id);
        Map<String,Object> map = new HashMap<String, Object>();
        if (info == null){
        	return R.error("查无此数据");
		}
       map.put("info",info);
        return R.success(map);
    }

	@ResponseBody
	@PostMapping("/save")
	public R save(InfoDO info){
		Map<String,Object> map = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(info.getPaymentDOS())){
			info.setPayments(JSON.parseArray(info.getPaymentDOS(), PaymentDO.class));
		}
		if (StringUtils.isNotBlank(info.getPurchaseDOS())){
			info.setPurchases(JSON.parseArray(info.getPurchaseDOS(),PurchaseDO.class));
		}
		if (StringUtils.isNotBlank(info.getContractProjectDOS())){
			info.setContractProjects(JSON.parseArray(info.getContractProjectDOS(), ContractProjectDO.class));
		}
		if (infoService.save(info) < 0){
			return R.error();
		}
		map.put("id",info.getId());
		return R.success(map);
	}
	
	@PostMapping("/remove")
	public R remove(String id){
		if (infoService.remove(id) > 0){
			//逻辑删除
		    return R.success();
		}
		return R.error();
	}

	
	@PostMapping("/delete")
	public R delete(String id){
	    Map<String,Object> map = new HashMap<String, Object>();
		if (infoService.delete(id) > 0){
			//物理删除
		    return R.success();
		}
		return R.error();
	}

	@PostMapping("/paymentDelete")
	public R paymentDelete(String id){
		if (infoService.deletePayment(id) > 0){
			return R.success("删除成功！");
		}
		return R.error();
	}

	@ResponseBody
	@PostMapping("/editBusinessCost")
	public R editBusinessCost(String id,String businessCost){
		int count = infoService.editBusinessCost(id,businessCost);
		InfoDO info = infoService.get(id);
		if (count >0){
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("info", info);
			return R.success(map);
		}
		return R.error("查无此数据");
	}

	@PostMapping("/getContractCode")
	public R getContractCode(String id){
		String code = infoService.getContractCode(id);
		if (code =="" && code == null){
			return R.error("查无数据");
		}
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("code", code);
		return R.success(map);
	}

	@PostMapping("/archive")
	public R archive(String id){
		InfoDO info = infoService.get(id);
		String archiveDate = DateUtils.getDateTime();
		info.setArchiveDate(archiveDate);
		int count = infoService.archive(id);
		if (count >0) {
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("count",count);
			return R.success(map);
		}
		return R.error();
	}

	@PostMapping("/revokeArchive")
	public R revokeArchive(String id){
		InfoDO info = infoService.get(id);
		String archiveDate = DateUtils.getDateTime();
		info.setArchiveDate(archiveDate);
		int count = infoService.revokeArchive(id);
		if (count >0){
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("count",count);
			return R.success(map);
		}
		return  R.error();
	}

	@ResponseBody
	@PostMapping("/purchaseDetailList")
	public R purchaseDetailList(String id){
		int count = infoService.count(id);
		if (count > 0){
			List<PurchaseDO> list = infoService.purchaseDetailList(id);
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("purchaseList",list);
			return R.success(map);
		}
		return R.error();
	}

	@ResponseBody
	@PostMapping("/productNameList")
	public R productNameList(@RequestParam Map<String, Object> params){
		List<ContractProjectDO> productNameList = infoService.productNameList(params);
		if (productNameList.isEmpty()){
			return R.error("查询无数据");
		}
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("productNameList", productNameList);
		return R.success(map);
	}
}
