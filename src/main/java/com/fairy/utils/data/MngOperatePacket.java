package com.fairy.utils.data;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class MngOperatePacket {

	public String username;

	public String viewName;
	
	public String methodName;

	public HashMap<String, Object> condition;

	public HashMap<String, Object> formData;
	
	public String roleName;

	public int pageNumber;
	
	public int pageSize;
	
	public MultipartFile file;

	public HttpServletResponse response;

	//不需要增加where字段
	public String customWhereSql;
	
	//排序类型
	public Map<String,Object> sortType;

}
