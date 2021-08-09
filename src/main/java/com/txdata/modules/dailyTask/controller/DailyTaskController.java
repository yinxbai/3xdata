package com.txdata.modules.dailyTask.controller;

import com.txdata.common.utils.R;
import com.txdata.modules.dailyTask.service.DailyTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/dailyTask")
public class DailyTaskController {
    @Autowired
    private DailyTaskService dailyTaskService;

    @PostMapping( "/delete")
    @ResponseBody
    public R remove(String id){
        if(dailyTaskService.remove(id)>0){
            return R.success();
        }
        return R.error();
    }
}
