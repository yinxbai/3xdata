package com.txdata.modules.contract.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txdata.modules.contract.domain.ContractProjectDO;
import com.txdata.modules.contract.domain.InfoDO;
import com.txdata.common.persistence.proxy.CrudDao;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.txdata.modules.contract.domain.PurchaseDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author InRoota
 * @2021-08-02 09:21:59
 */
@Mapper
public interface InfoDao extends CrudDao<InfoDO> {

	InfoDO get(String id);

	/**
	 * 合同列表
	 * @param page
	 * @param map
	 * @return
	 */
	Page<InfoDO> list(Page<InfoDO> page, @Param("entity")Map<String,Object> map);
	
	List<InfoDO> list(@Param("entity")Map<String,Object> map);
	
	int update(InfoDO info);
	
	int remove(String id);
	
	int delete(String id);

	/**
	 * 新增合同
	 * @param infos
	 * @return
	 */
	int save(InfoDO infos);

	/**
	 * 删除合同付款明细
	 * @param id
	 * @return
	 */
	int deletePayment(String id);

	/**
	 * 商务成本修改
	 * @param id
	 * @param businessCost
	 * @return
	 */
	int editBusinessCost(String id, String businessCost);

	/**
	 * 获取合同编号
	 * @param id
	 * @return
	 */
	String getContractCode(String id);

	/**
	 * 归档
	 * @param id
	 * @return
	 */
	int archive(String id);

	/**
	 * 撤回归档
	 * @param id
	 * @return
	 */
	int revokeArchive(String id);

	/**
	 * 查询归档标识
	 * @param id
	 * @return
	 */
	String isArchive(String id);

	/**
	 * 查询记录条数
	 * @param id
	 * @return
	 */
	int count(String id);

	/**
	 * 获取关联合同的采购明细表
	 * @param id
	 * @return
	 */
	List<PurchaseDO> purchaseDetailList(String id);

	/**
	 * 合同名称唯一性校验
	 * @param id
	 * @param name
	 * @return
	 */
	int check(String id,String name);

	/**
	 * 获取关联销售合同产品名称
	 * @param map
	 * @return
	 */
	List<ContractProjectDO> productNameList(@Param("entity")Map<String,Object> map);

	/**
	 * 获取全部Code
	 * @return
	 */
	Map<String,Object>  getAllCode();
}
