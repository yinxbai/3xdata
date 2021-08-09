package com.txdata.oa.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.util.StrUtil;
import com.txdata.common.utils.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txdata.common.persistence.CrudService;
import com.txdata.common.utils.ShiroUtils;
import com.txdata.oa.dao.NotifyDao;
import com.txdata.oa.domain.NotifyDO;
import com.txdata.oa.domain.NotifyRecordDO;

/**
 * 通知通告
 * 
 * @author 3xdata
 * @email 3xdata@3xdata.cn
 * @date 2019-07-11 14:57:30
 */
 @Service
public class NotifyService extends CrudService<NotifyDao, NotifyDO> {

    @Autowired
    private NotifyDao notifyDao;
    @Autowired
    private NotifyRecordService notifyRecordService;
    
    /**
	 * 通过id查找数据
	 */
    @Transactional(readOnly = false)
    public NotifyDO get(String id, Map<String, Object> params){
    	NotifyDO notify = notifyDao.get(id);
    	Map<String, Object> whereMap = new HashMap<>();
    	whereMap.put("notifyId", id);//消息id
		Query query = new Query(params);
		query.putAll(whereMap);
		Page<NotifyRecordDO> page = new Page<NotifyRecordDO>(query.getPageNo(), query.getPageSize());
    	notify.setOaNotifyRecordList(notifyRecordService.page(page, query).getRecords());//将关联数据塞入notify中
        return notify;
    }
    
    /**
	 * 分页查询列表
	 */
    public Page<NotifyDO> page(Page<NotifyDO> page, Map<String, Object> map){
        return notifyDao.list(page, map);
    }
    
    /**
	 * 查询列表
	 */
    public List<NotifyDO> list(Map<String, Object> map){
        return notifyDao.list(map);
    }
    
    /**
	 * 保存/修改
	 */
    @Transactional(readOnly=false)
    public int save(NotifyDO notify){
    	int result = 0;
    	if (notify.getIsNewRecord()) {
    		notify.preInsert();
    		result = notifyDao.insert(notify);
		} else {
			notify.preUpdate();
			result = notifyDao.update(notify);
		}
    	Map<String, Object> whereMap = new HashMap<>();
    	whereMap.put("notifyId", notify.getId());//消息id
    	notifyRecordService.deleteByWhere(whereMap);//物理删除掉记录表中关联的信息
    	whereMap.clear();
    	//将新的人员信息保存至记录表中
    	String[] userIds = notify.getOaNotifyRecordIds().trim().split(",");
		 List<NotifyRecordDO> notifyRecords = new ArrayList<>();
		 for (String userId : userIds) {
			 if (StrUtil.isBlank(userId)){
				 continue;
			 }
			 NotifyRecordDO notifyRecord = new NotifyRecordDO();
			 notifyRecord.preInsert();
			 notifyRecord.setNotifyId(notify.getId());//通知id
			 notifyRecord.setUserId(userId);//接收人
			 notifyRecord.setIsRead(0);//阅读标记
			 notifyRecords.add(notifyRecord);
		}
		if(!notifyRecords.isEmpty()) {//判断notifyRecords不为空时，进行批量插入
			result = notifyRecordService.batchInsert(notifyRecords);//批量插入
		}
        return result;
    }
    
    /**
	 * 通过id逻辑删除
	 */
    @Transactional(readOnly=false)
    public int remove(String id){
        return notifyDao.remove(id);
    }
    
    /**
	 * 通过ids批量逻辑删除
	 */
    @Transactional(readOnly=false)
    public int batchRemove(String[] ids){
        return notifyDao.batchRemove(ids);
    }
    
    /**
	 * 通过id物理删除
     * @return 
	 */
    @Transactional(readOnly=false)
    public int delete(String id){
        return notifyDao.delete(id);
    }
    
    /**
	 * 通过ids物理删除
	 */
    @Transactional(readOnly=false)
    public int batchDelete(String[] ids){
        return notifyDao.batchDelete(ids);
    }
    
    /**
	 * 批量插入
	 */
    @Transactional(readOnly=false)
    public int batchInsert(List<NotifyDO> notifys){
    	return notifyDao.batchInsert(notifys);
    }
    
    /**
	 * 批量修改
	 */
	@Transactional(readOnly=false)
	public int batchUpdate(List<NotifyDO> notifys){
		return notifyDao.batchUpdate(notifys);
	}
    
    /**
	 * 通过id复制一条相同的数据
	 */
    @Transactional(readOnly=false)
    public int copy(String id){
    	int result = 0;
    	NotifyDO notify = notifyDao.get(id);
    	if (notify != null){
    		notify.setId(null);
    		notify.preInsert();
    		//表结构中name字段不一定存在，故此自动生成代码时注释下列代码，存在时取消注释即可
//    		if (StrUtil.isNotBlank(notify.getName())){ 
//    			notify.setName(notify.getName() + "-复制");
//    		}
    		result = notifyDao.insert(notify);
    	}
        return result;
    }
    
    /**
     * 
     * @Description: 未读消息数
     * @param: 参数描述
     * @return: 返回结果描述
     * @throws: 异常描述
     * @author: 吁新鹏
     * @date: 2019年7月12日 下午4:40:46
     */
    public int count(Map<String, Object> map) {
    	return notifyDao.count(map);
    }
    
    /**
     * 阅读接口
     * @param: id 通知公告id
     * @return: 返回结果描述
     * @author: wanjm
     * @date: 2019年8月8日 上午11:01:34
     */
    @Transactional(readOnly = false)
    public void read(String id){
    	Map<String, Object> whereMap = new HashMap<>();
    	whereMap.put("notifyId", id);//消息id
    	whereMap.put("userId", ShiroUtils.getUserId());//用户id
    	//点击查看后，对通告发送记录表表中的阅读标志进行置标
    	NotifyRecordDO notifyRecord = new NotifyRecordDO();//要被修改的值
    	notifyRecord.setIsRead(1);//将阅读标志 置为1，标识已阅读
    	notifyRecord.setReadDate(new Date());//阅读时间
    	notifyRecordService.updateByWhere(notifyRecord, whereMap);//修改通告发送记录表中对应的数据，whereMap中存在消息id及用户id
    }
}
