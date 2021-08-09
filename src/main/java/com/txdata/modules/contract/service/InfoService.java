package com.txdata.modules.contract.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txdata.common.utils.DateUtils;
import com.txdata.common.utils.IdGen;
import com.txdata.modules.contract.dao.ContractProjectDao;
import com.txdata.modules.contract.dao.PaymentDao;
import com.txdata.modules.contract.dao.PurchaseDao;
import com.txdata.modules.contract.domain.*;
import com.txdata.modules.contract.dao.InfoDao;
import com.txdata.common.persistence.CrudService;

import java.util.*;

import com.txdata.modules.daily.dao.MyUserDao;
import com.txdata.modules.daily.domain.DailyDO;
import org.activiti.engine.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

/**
 * @author InRoota
 * @2021-08-02 09:24:19
 */
 @Service
public class InfoService extends CrudService<InfoDao, InfoDO> {

    @Autowired
    private InfoDao infoDao;
    @Autowired
    private PaymentDao paymentDao;
    @Autowired
    private PurchaseDao purchaseDao;
    @Autowired
    private ContractProjectDao contractProjectDao;
    @Autowired
    private MyUserDao myUserDao; // 获取用户类
    @Autowired
    TaskService taskService;
    @Autowired
    HistoryService historyService;
    // 引擎配置
    ProcessEngineConfiguration pec = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml") // 读取配置文件
            .setActivityFontName("宋体").setLabelFontName("宋体").setAnnotationFontName("宋体"); // 设置字体格式

    // 获取流程引擎对象
    ProcessEngine processEngine=pec.buildProcessEngine();
    // 获取查询集合
    List<DailyDO> resultList = new ArrayList<>();
    
    /**
	 * 通过id查找数据
	 */
    public InfoDO get(String id){
        return infoDao.get(id);
    }
    
    /**
	 * 分页查询列表
	 */
    public Page<InfoDO> page(Page<InfoDO> page, @Param("entity") Map<String, Object> map){
        return infoDao.list(page, map);
    }
    
    /**
	 * 查询列表
	 */
    public List<InfoDO> list(Map<String, Object> map){
        return infoDao.list(map);
    }
    
    /**
	 * 保存/修改
	 */
    @Transactional(readOnly=false)
    public int save(InfoDO info){

        //判断是否存在主键ID
       if (info.getId()==null||info.getId()=="") {

           //合同Id
           String id = IdGen.uuid();
           info.setId(id);
           //符合校验
           if (infoDao.check(info.getId(), info.getName()) == 0  //判断是否唯一
                   && info.getPayments().size() >= 1 //支付方式必须大于等于1
           ) {
               /*processEngine.getRepositoryService() // 生成本地库服务
                       .createDeployment() // 创建部署类
                       .addClasspathResource("bpmn/contract.bpmn") //加载流程文件
                       .addClasspathResource("bpmn/contract.png") // 加载流程图片
                       .name("合同审查") // 设置名称
                       .deploy(); // 部署
               //获取到RuntimeService对象
               RuntimeService runtimeService = processEngine.getRuntimeService();
               //封装数据
               ContractInfoDO infoDO = new ContractInfoDO();
               //储存职位
               infoDO.setJob(info.getUnitName());
               //储存用户名
               infoDO.setUserId(info.getPartyA());
               //Map集合生成
               Map<String,Object> map = new HashMap<>();
               map.put("infoDO",infoDO);
               //流程实例创建
               ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("contract",map);
               //获得流程实例id
               String procId = processInstance.getId();
               info.setProcId(procId);
               infoDO.setProcId(procId);*/

               //合同编号
               String code = "H3XDATA" + DateUtils.getDate("yyyyMMddHH");
               info.setCode(code);
               //是否归档
               info.setIsArchive("0");
               //归档日期
               String ArchiveDate = DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
               info.setArchiveDate(ArchiveDate);
               //合同存放位置
               String filepath = "/upload/" + info.getFinishDate().toString();
               info.setFilePath(filepath);
               //保存任务列表
               int num = infoDao.save(info);

               //讲对象转化为子对象保存
               List<PaymentDO> paymentDOS = info.getPayments();
               List<PurchaseDO> purchaseDOS = info.getPurchases();
               List<ContractProjectDO> projectDOS = info.getContractProjects();

               for (PaymentDO paymentDO : paymentDOS) {
                   if (paymentDO != null) {
                       paymentDO.setId(IdGen.uuid());
                       paymentDO.setContractId(id);
                       paymentDao.save(paymentDO);
                   }
               }
                if(info.getPurchases() !=null){
                   for (PurchaseDO purchaseDO : purchaseDOS) {
                       if (purchaseDO != null) {
                           purchaseDO.setId(IdGen.uuid());
                           purchaseDO.setContractId(id);
                           purchaseDao.save(purchaseDO);
                       }
                   }
                }

               if (info.getContractProjects() != null) {
                   for (ContractProjectDO projectDO : projectDOS) {
                       if (projectDO != null) {
                           projectDO.setId(IdGen.uuid());
                           projectDO.setContractId(id);
                           contractProjectDao.save(projectDO);
                       }
                   }
               } else {
                   return 0;
               }

               /*// 根据流程实例id查询一个任务
               Task task = taskService.createTaskQuery().processInstanceId(procId).singleResult();
               // 将任务id存入活动对象中
               infoDO.setTaskId(task.getId());
               // 认领任务
               taskService.claim(task.getId(),infoDO.getUserId());
               // 提交任务
               taskService.complete(task.getId(),map);
               // 返回保存的结果*/
               return num;
           } else {
               return 0;
           }
       } else {
           //主键更新则需做出修改操作
           List<PaymentDO> paymentDOS = info.getPayments();
           List<PurchaseDO> purchaseDOS = info.getPurchases();
           List<ContractProjectDO> projectDOS = info.getContractProjects();

           for (PaymentDO paymentDO : paymentDOS) {
               if (paymentDO != null) {
                   paymentDao.update(paymentDO);
               }
           }

           for (ContractProjectDO projectDO : projectDOS) {
               if (projectDO != null) {
                   contractProjectDao.update(projectDO);
               }
           }

           for (PurchaseDO purchaseDO : purchaseDOS) {
               if (purchaseDO != null) {
                   purchaseDao.update(purchaseDO);
               }
           }
           return infoDao.update(info);
       }
    }
    
    /**
	 * 通过id逻辑删除
	 */
    @Transactional(readOnly=false)
    public int remove(String id){
        return infoDao.remove(id);
    }

    
    /**
	 * 通过id物理删除
	 */
    @Transactional(readOnly=false)
    public int delete(String id){
        contractProjectDao.delete(id);
        paymentDao.delete(id);
        purchaseDao.delete(id);
        return infoDao.delete(id);
    }

    /**
     * 删除付款明细
     * @param id
     * @return
     */
    @Transactional(readOnly = false)
    public int deletePayment(String id){
        return infoDao.deletePayment(id);
    }

    /**
     * 修改商务成本
     * @param id
     * @param businessCost
     * @return
     */
    @Transactional(readOnly = false)
    public int editBusinessCost(String id,String businessCost){
        return infoDao.editBusinessCost(id,businessCost);
    }

    public String  getContractCode(String id){
        return infoDao.getContractCode(id);
    }

    /**
     * 归档
     * @param id
     * @return
     */
    @Transactional(readOnly = false)
    public int archive(String id){
        return infoDao.archive(id);
    }

    @Transactional(readOnly = false)
    public int revokeArchive(String id){
        return infoDao.revokeArchive(id);
    }

    public String isArchive(String id){
        return infoDao.isArchive(id);
    }

    public int count(String id){
        return infoDao.count(id);
    }

    public List<PurchaseDO> purchaseDetailList(String id){
        return infoDao.purchaseDetailList(id);
    }

    /**
     * 项目名唯一性
     * @param id
     * @param name
     * @return
     */
    public int check(String id,String name){
        int flag = infoDao.check(id,name);
        if (flag > 0){
            System.out.println("该名称被占用，请更换合同名称");
        }
        return flag;
    }

    public List<ContractProjectDO> productNameList(@Param("entity")Map<String,Object> map){
        return infoDao.productNameList(map);
    }

}
