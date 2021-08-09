package com.txdata.activiti.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.txdata.common.config.Constant;
import com.txdata.common.controller.BaseController;
import com.txdata.common.utils.Query;
import com.txdata.common.utils.R;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.rest.editor.model.ModelEditorJsonRestResource;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.activiti.editor.constants.ModelDataJsonConstants.MODEL_DESCRIPTION;
import static org.activiti.editor.constants.ModelDataJsonConstants.MODEL_ID;
import static org.activiti.editor.constants.ModelDataJsonConstants.MODEL_NAME;

/**
 * @author bootdo 1992lcg@163.com
 */
@RequestMapping("/act")
@RestController
public class ModelController extends BaseController{
    protected static final Logger LOGGER = LoggerFactory.getLogger(ModelEditorJsonRestResource.class);

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/model/list")
    @RequiresPermissions("act:model:edit")
    R list(@RequestParam Map<String, Object> param) {
    		Query query = new Query(param);
		int offset = query.getOffset();
		int limit = query.getLimit();
        List<Model> list = repositoryService.createModelQuery().listPage(offset
                , limit);
        int total = (int) repositoryService.createModelQuery().count();
        // 存储结果
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rows", list);
        map.put("count", total);
        map.put("pageNo", query.getPageNo());
        map.put("pageSize", query.getPageSize());
        return R.success(map);
    }

    @PostMapping("/model/remove")
    @RequiresPermissions("act:model:edit")
    public R remove(String id) {
        repositoryService.deleteModel(id);
        return R.success();
    }

    /**
     * 发布流程
     * @param id
     * @return
     * @throws Exception
     */
    @PostMapping("/model/deploy")
    @RequiresPermissions("act:model:edit")
    public R deploy(String id) {
        //获取模型
        Model modelData = repositoryService.getModel(id);
        byte[] bytes = repositoryService.getModelEditorSource(modelData.getId());
        if (bytes == null) {
            return R.error("模型数据为空，请先设计流程并成功保存，再进行发布。");
        }
        JsonNode modelNode = null;
		try {
			modelNode = new ObjectMapper().readTree(bytes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return R.error();
		}
        BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
        if (model.getProcesses().size() == 0) {
            return R.error("数据模型不符要求，请至少设计一条主线流程。");
        }
        byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(model);
        //发布流程
        String processName = modelData.getName() + ".bpmn20.xml";
        Deployment deployment = null;
        try {
	        	deployment = repositoryService.createDeployment()
	        			.name(modelData.getName())
	        			.addString(processName, new String(bpmnBytes, "UTF-8"))
	        			.deploy();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return R.error();
		}
        modelData.setDeploymentId(deployment.getId());
        repositoryService.saveModel(modelData);
        return R.success();
    }

    @PostMapping("/model/batchRemove")
    @RequiresPermissions("act:model:edit")
    public R batchRemove(@RequestParam("ids[]") String[] ids) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return R.error("1", "演示系统不允许修改,完整体验请部署程序");
        }
        for (String id : ids) {
            repositoryService.deleteModel(id);
        }
        return R.ok();
    }

    /**
     * 将模型导出为流程XML文件
     * @param id
     * @param response
     */
    @GetMapping("/model/export")
    @RequiresPermissions("act:model:edit")
    public void exportToXml(String id, HttpServletResponse response) {
    		ByteArrayInputStream in = null;
    		OutputStream os = null;
        try {
            org.activiti.engine.repository.Model modelData = repositoryService.getModel(id);
            BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
            JsonNode editorNode = new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
            BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
            BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
            byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);
            in = new ByteArrayInputStream(bpmnBytes);
            String filename = bpmnModel.getMainProcess().getId() + ".bpmn20.xml";
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            byte[] b = new byte[1024];
			int len = 0;
			os = response.getOutputStream();
			while ((len = in.read(b)) != -1) {
				os.write(b, 0, len);
			}
			os.flush();
			os.close();
			in.close();
        } catch (Exception e) {
            throw new ActivitiException("导出model的xml文件失败，模型ID=" + id, e);
        } finally {
			if(os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    }

    @PostMapping("/model/add")
    @RequiresPermissions("act:model:edit")
    public R newModel(HttpServletResponse response) throws UnsupportedEncodingException {

        //初始化一个空模型
        Model model = repositoryService.newModel();
        //设置一些默认信息
        String name = "new-process";
        String description = "";
        int revision = 1;
        String key = "process";

        ObjectNode modelNode = objectMapper.createObjectNode();
        modelNode.put(ModelDataJsonConstants.MODEL_NAME, name);
        modelNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
        modelNode.put(ModelDataJsonConstants.MODEL_REVISION, revision);

        model.setName(name);
        model.setKey(key);
        model.setMetaInfo(modelNode.toString());

        repositoryService.saveModel(model);
        String id = model.getId();

        //完善ModelEditorSource
        ObjectNode editorNode = objectMapper.createObjectNode();
        editorNode.put("id", "canvas");
        editorNode.put("resourceId", "canvas");
        ObjectNode stencilSetNode = objectMapper.createObjectNode();
        stencilSetNode.put("namespace",
                "http://b3mn.org/stencilset/bpmn2.0#");
        editorNode.put("stencilset", stencilSetNode);
        repositoryService.addModelEditorSource(id, editorNode.toString().getBytes("utf-8"));
//        try {
//            response.sendRedirect("/modeler.html?modelId=" + id);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        return R.success(map);
    }

    @RequestMapping(value = "/editor/stencilset", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public String getStencilset() {
        InputStream stencilsetStream = this.getClass().getClassLoader().getResourceAsStream("stencilset.json");
        try {
            return IOUtils.toString(stencilsetStream, "utf-8");
        } catch (Exception e) {
            throw new ActivitiException("Error while loading stencil set", e);
        }
    }

    @GetMapping(value = "/model/{modelId}/json")
    public ObjectNode getEditorJson(@PathVariable String modelId) {
        ObjectNode modelNode = null;
        Model model = repositoryService.getModel(modelId);
        if (model != null) {
            try {
                if (StringUtils.isNotEmpty(model.getMetaInfo())) {
                    modelNode = (ObjectNode) objectMapper.readTree(model.getMetaInfo());
                } else {
                    modelNode = objectMapper.createObjectNode();
                    modelNode.put(MODEL_NAME, model.getName());
                }
                modelNode.put(MODEL_ID, model.getId());
                ObjectNode editorJsonNode = (ObjectNode) objectMapper.readTree(
                        new String(repositoryService.getModelEditorSource(model.getId()), "utf-8"));
                modelNode.put("model", editorJsonNode);

            } catch (Exception e) {
                LOGGER.error("Error creating model JSON", e);
                throw new ActivitiException("Error creating model JSON", e);
            }
        }
        return modelNode;
    }

    @RequestMapping(value = "/model/{modelId}/save", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @RequiresPermissions("act:model:edit")
    public void saveModel(@PathVariable String modelId
            , String name, String description
            , String json_xml, String svg_xml) {
        try {

            Model model = repositoryService.getModel(modelId);

            ObjectNode modelJson = (ObjectNode) objectMapper.readTree(model.getMetaInfo());

            modelJson.put(MODEL_NAME, name);
            modelJson.put(MODEL_DESCRIPTION, description);
            model.setMetaInfo(modelJson.toString());
            model.setName(name);

            repositoryService.saveModel(model);

            repositoryService.addModelEditorSource(model.getId(), json_xml.getBytes("utf-8"));

            InputStream svgStream = new ByteArrayInputStream(svg_xml.getBytes("utf-8"));
            TranscoderInput input = new TranscoderInput(svgStream);

            PNGTranscoder transcoder = new PNGTranscoder();
            // Setup output
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            TranscoderOutput output = new TranscoderOutput(outStream);

            // Do the transformation
            transcoder.transcode(input, output);
            final byte[] result = outStream.toByteArray();
            repositoryService.addModelEditorSourceExtra(model.getId(), result);
            outStream.close();

        } catch (Exception e) {
            LOGGER.error("Error saving model", e);
            throw new ActivitiException("Error saving model", e);
        }
    }
}
