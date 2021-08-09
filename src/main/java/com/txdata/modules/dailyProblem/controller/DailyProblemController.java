package com.txdata.modules.dailyProblem.controller;

import com.txdata.common.utils.R;
import com.txdata.modules.dailyProblem.service.DailyProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/dailyProblem")
public class DailyProblemController {
    @Autowired
    private DailyProblemService dailyProblemService;

    @PostMapping( "/delete")
    @ResponseBody
    public R remove(String id){
        if(dailyProblemService.remove(id)>0){
            return R.success();
        }
        return R.error();
    }
}
