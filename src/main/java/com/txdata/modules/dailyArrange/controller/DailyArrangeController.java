package com.txdata.modules.dailyArrange.controller;

import com.txdata.common.utils.R;
import com.txdata.modules.dailyArrange.service.DailyArrangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/dailyArrange")
public class DailyArrangeController {
    @Autowired
    private DailyArrangeService dailyArrangeService;

    @PostMapping( "/delete")
    @ResponseBody
    public R remove(String id){
        if(dailyArrangeService.remove(id)>0){
            return R.success();
        }
        return R.error();
    }
}
