package com.txdata.activiti.controller;

import com.txdata.activiti.service.ProcessService;
import com.txdata.activiti.vo.ProcessVO;
import com.txdata.common.controller.BaseController;
import com.txdata.common.utils.Query;
import com.txdata.common.utils.R;
import com.txdata.common.utils.StringUtils;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("act/process")
@RestController
public class ProcessController extends BaseController{

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ProcessService processService;

    @PostMapping("list")
    @ResponseBody
    @RequiresPermissions("act:process:edit")
    R list(@RequestParam Map<String, Object> param) {
    		Query query = new Query(param);
    		int offset = query.getOffset();
    		int limit = query.getLimit();
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                .listPage(offset, limit);
        int count = (int) repositoryService.createProcessDefinitionQuery().count();
        List<Object> list = new ArrayList<>();
        for(ProcessDefinition processDefinition: processDefinitions){
        		String deploymentId = processDefinition.getDeploymentId();
        		Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
            list.add(new ProcessVO(processDefinition, deployment));
        }
        // 存储结果
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rows", list);
        map.put("count", count);
        map.put("pageNo", query.getPageNo());
        map.put("pageSize", query.getPageSize());
        return R.success(map);
    }

    /**
     * 部署流程，保存流程XML文件
     * @param file
     * @return
     */
    @PostMapping("/deploy")
    @ResponseBody
    @Transactional(readOnly = false)
    @RequiresPermissions("act:process:edit")
    public R deploy(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (StringUtils.isBlank(fileName)){
        		return R.error("请选择要部署的流程文件");
        }else {
        		R r = processService.deploy(file);
        		return r;
        }
    }

    /**
     * 将部署的流程转换为模型
     *
     * @param id
     * @return
     * @throws UnsupportedEncodingException
     * @throws XMLStreamException
     */
    @PostMapping(value = "/convertToModel")
    @ResponseBody
    @RequiresPermissions("act:process:edit")
    public R convertToModel(String id) {
        org.activiti.engine.repository.Model modelData = null;
        try {
            modelData = processService.convertToModel(id);
            return R.success( "转换模型成功，模型ID=" + modelData.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return R.error( "转换模型失败");
        }
    }

    /**
     * 查看流程的xml文件或图片
     * @param resType
     * @param id
     * @param response
     */
    @RequestMapping(value = "/resource/read")
    @RequiresPermissions("act:process:edit")
    public void resourceRead( String resType, String id, HttpServletResponse response) {
    		OutputStream os = null;
//        InputStream resourceAsStream = processService.resourceRead(id,resType);
    		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult();
        String resourceName = "";
        if (resType.equals("image")) {
            resourceName = processDefinition.getDiagramResourceName();
        } else if (resType.equals("xml")) {
            resourceName = processDefinition.getResourceName();
        }
        InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);

        byte[] b = new byte[1024];
        int len = -1;
        try {
            while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
                response.getOutputStream().write(b, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        byte[] b = new byte[1024];
//        int len = -1;
//        try {
//	        	os = response.getOutputStream();
//	        	response.setContentType("application/octet-stream; charset=utf-8");
//	        	response.setHeader("Content-Disposition", "attachment; filename="
//	        			+ new String(resourceName.getBytes(), "iso-8859-1"));
//	        	while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
//	        		os.write(b, 0, len);
//	        	}
//	        	os.flush();
//	        	os.close();
//	        	resourceAsStream.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}finally {
//			if(os != null) {
//				try {
//					os.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//			if(resourceAsStream != null) {
//				try {
//					resourceAsStream.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
    }

    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("act:process:edit")
    public R remove(String id){
        repositoryService.deleteDeployment(id,true);
        return R.success();
    }
    
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("act:process:edit")
    public R batchRemove(@RequestParam("ids[]") String[] ids) {
        for (String id : ids) {
            repositoryService.deleteDeployment(id,true);
        }
        return R.success();
    }
    
    /**
     * 流程挂起
     * @param id 流程ID
     * @return
     */
	@RequestMapping(value = "update/suspend")
	@ResponseBody
    @RequiresPermissions("act:process:edit")
	public R updateSuspend(String id) {
		String message = processService.updateState("suspend", id);
		return R.success(message);
	}
	
	/**
	 * 流程激活
	 * @param id 流程ID
	 * @return
	 */
	@RequestMapping(value = "update/active")
	@ResponseBody
    @RequiresPermissions("act:process:edit")
	public R updateActive(String id) {
		String message = processService.updateState("active", id);
		return R.success(message);
	}
}
