package com.txdata.modules.daily.activiti;

import com.txdata.system.domain.UserDO;
import com.txdata.system.utils.UserUtils;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class Task implements TaskListener, Serializable {

    @Override
    public void notify(DelegateTask delegateTask) {
        UserDO userDO = UserUtils.getUser();
        //监听器分配任务处理人
        delegateTask.setAssignee(userDO.getUsername());
    }
}