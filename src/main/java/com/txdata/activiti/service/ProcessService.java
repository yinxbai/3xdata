package com.txdata.activiti.service;

import com.txdata.activiti.service.ProcessService;
import com.txdata.common.utils.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * 工作流 流程相关Service
 * @author mark
 *
 */
@Service
public class ProcessService {
	
    @Autowired
    RepositoryService repositoryService;
    
    @Autowired
    RuntimeService runtimeService;

    /**
     * 将流程转换成模型
     */
    public Model convertToModel(String procDefId) throws Exception {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(procDefId).singleResult();
        InputStream bpmnStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
                processDefinition.getResourceName());
        XMLInputFactory xif = XMLInputFactory.newInstance();
        InputStreamReader in = new InputStreamReader(bpmnStream, "UTF-8");
        XMLStreamReader xtr = xif.createXMLStreamReader(in);
        BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);
        BpmnJsonConverter converter = new BpmnJsonConverter();
        ObjectNode modelNode = converter.convertToJson(bpmnModel);
        
        Model modelData = repositoryService.newModel();
        modelData.setKey(processDefinition.getKey());
        modelData.setName(processDefinition.getResourceName());
        modelData.setCategory(processDefinition.getCategory());//.getDeploymentId());
        modelData.setDeploymentId(processDefinition.getDeploymentId());
        modelData.setVersion(Integer.parseInt(String.valueOf(repositoryService.createModelQuery().modelKey(modelData.getKey()).count() + 1)));
        
        ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
        modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, processDefinition.getName());
        modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, modelData.getVersion());
        modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, processDefinition.getDescription());
        modelData.setMetaInfo(modelObjectNode.toString());
        
        repositoryService.saveModel(modelData);
        repositoryService.addModelEditorSource(modelData.getId(), modelNode.toString().getBytes("utf-8"));
        return modelData;
    }

    /**
     * 获取流程文件（XML、图片）
     */
    public InputStream resourceRead(String id, String resType) throws Exception {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult();
        String resourceName = "";
        if (resType.equals("image")) {
            resourceName = processDefinition.getDiagramResourceName();
        } else if (resType.equals("xml")) {
            resourceName = processDefinition.getResourceName();
        }
        InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
        return resourceAsStream;
    }
    
    /**
	 * 挂起、激活流程实例
	 */
	@Transactional(readOnly = false)
	public String updateState(String state, String procDefId) {
		if (state.equals("active")) {
			repositoryService.activateProcessDefinitionById(procDefId, true, null);
			return "已激活ID为[" + procDefId + "]的流程定义。";
		} else if (state.equals("suspend")) {
			repositoryService.suspendProcessDefinitionById(procDefId, true, null);
			return "已挂起ID为[" + procDefId + "]的流程定义。";
		}
		return "无操作";
	}
    
	/**
	 * 部署流程 - 保存
	 * @param file
	 * @return
	 */
	@Transactional(readOnly = false)
	public R deploy(MultipartFile file) {
		String message = "";
		String fileName = file.getOriginalFilename();
		try {
			InputStream fileInputStream = file.getInputStream();
			Deployment deployment = null;
			String extension = FilenameUtils.getExtension(fileName);
			if (extension.equals("zip") || extension.equals("bar")) {
				ZipInputStream zip = new ZipInputStream(fileInputStream);
				deployment = repositoryService.createDeployment().addZipInputStream(zip).deploy();
			} else if (extension.equals("png")) {
				deployment = repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
			} else if (fileName.indexOf("bpmn20.xml") != -1) {
				deployment = repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
			} else if (extension.equals("bpmn")) { // bpmn扩展名特殊处理，转换为bpmn20.xml
				String baseName = FilenameUtils.getBaseName(fileName); 
				deployment = repositoryService.createDeployment().addInputStream(baseName + ".bpmn20.xml", fileInputStream).deploy();
			} else {
				message = "不支持的文件类型：" + extension;
				return R.error(message);
			}
			List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();
			// 设置流程分类
			for (ProcessDefinition processDefinition : list) {
				repositoryService.setProcessDefinitionCategory(processDefinition.getId(), "1");
				message += "部署成功，流程ID=" + processDefinition.getId();
			}
			if (list.size() == 0){
				return R.error( "部署失败，没有流程。");
			}else {
				return R.success(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("部署失败！");
		}
	}
}
