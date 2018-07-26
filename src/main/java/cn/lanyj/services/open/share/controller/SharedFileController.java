package cn.lanyj.services.open.share.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.lanyj.services.models.SharedFile;
import cn.lanyj.services.service.SharedFileService;

@Controller
@RequestMapping("/open/share/file")
public class SharedFileController {
	
	@Autowired
	SharedFileService service;
	
	@ResponseBody
	@RequestMapping(path="/save")
	public String save(@RequestParam(name="expire", required=false, defaultValue="3") int expire, HttpServletRequest request, HttpServletResponse rep, @RequestParam(name="file", required=true) MultipartFile file) {
		if(file.getSize() == 0) {
			rep.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return "";
		}
		SharedFile f = service.createAndSave(file, request.getCharacterEncoding(), expire);
		String baseUrl = String.format("%s://%s:%d/open/share/file/url/" + f.getId(),request.getScheme(),  request.getServerName(), request.getServerPort());
		return baseUrl;
	}
	
	@RequestMapping("/get/{id}")
	public void get(HttpServletRequest request, HttpServletResponse response, @PathVariable(name="id") String id) {
		if(!StringUtils.hasLength(id)) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		SharedFile file = service.get(id);
		if(request.getParameter("reload") != null) {
			file = service.sync(id);
		}
		if(file == null || !(new File(file.getPath()).exists())) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			service.sync(id);
			return;
		}
		
		try {
			response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(file.getOriginalName(), "utf-8"));
		} catch (UnsupportedEncodingException e1) {
			response.setHeader("Content-Disposition", "attachment; filename=" + file.getOriginalName());
		}
		response.setContentType("Content-Type: application/octet-stream");
		
		try {
			service.read(file, response.getOutputStream());
		} catch (IOException e) {
			response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
		}
	}
	
}
