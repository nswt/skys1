package com.sky.toolkit.coder.helper;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.sky.core.constant.Global;
import com.sky.core.util.Configure;
import com.sky.core.util.ReflectHelper;
import com.sky.toolkit.coder.model.Coder;
import com.sky.toolkit.coder.model.Column;
import com.sky.toolkit.util.Freemarker;

/**
 * 代码生成工具类
 * 
 * @author Simon
 * @date 2017-10-23
 */
public class CoderGenerator {
	public static void generate(Coder coder) throws Exception {
		CoderGenerator.processProperties(coder);
		String xml = Freemarker.printFreemarkerString("/templates/coder/config.xml", CoderGenerator.toMap(coder),
				"UTF-8");
		Document document = null;
		SAXReader reader = null;
		reader = new SAXReader();
		document = reader.read(new ByteArrayInputStream(xml.getBytes("UTF-8")));

		Element root = document.getRootElement();
		@SuppressWarnings("unchecked")
		Iterator<Element> iterator = root.elementIterator();
		while (iterator.hasNext()) {
			Element element = (Element) iterator.next();
			String packageName = element.element("packageName").getText();
			String className = element.element("className").getText();
			String templateFileName = element.element("template").getText();
			String encoding = element.elementText("encoding");
			if (StringUtils.isEmpty(encoding)) {
				encoding = root.attributeValue("encoding");
			}
			String outFileName = "";
			if ("view".equalsIgnoreCase(packageName)) {
				outFileName = Configure.getProperty("toolkit.coder.path.view", "d:/coder/view") + "/" + coder.getModelName()
						+ "/" + coder.getFunctionName() + "/" + className;
			} else {
				outFileName = Configure.getProperty("toolkit.coder.path.java", "d:/coder/java") + "/"
						+ coder.getPackageName().replaceAll("\\.", "/") + "/" + packageName.replaceAll("\\.", "/") + "/"
						+ className;
			}
			if (templateFileName != null && templateFileName.toLowerCase().endsWith(".ftl")) {
				Freemarker.printFreemarkerFile("/templates/coder/" + templateFileName, outFileName,
						CoderGenerator.toMap(coder), encoding);
			}
			if (templateFileName != null && templateFileName.toLowerCase().endsWith(".vm")) {
				Freemarker.printVelocityFile("/templates/coder/" + templateFileName, outFileName,
						CoderGenerator.toMap(coder));
			}
		}
	}

	/**
	 * 准备map对象
	 * 
	 * @param coder
	 * @return
	 */
	public static Map<String, Object> toMap(Coder coder) {
		Map<String, Object> map = new HashMap<String, Object>(50);
		map.put("coder", coder);
		map.put("fields", coder.getFields());
		return map;
	}

	private static Map<String, String> map;
	static {
		map = new HashMap<String, String>();
		map.put("varchar2", "String");
		map.put("varchar", "String");
		map.put("int", "Integer");
		map.put("datetime", "Date");
		map.put("date", "Date");
		map.put("timestamp", "Date");
		map.put("nvarchar", "String");
		map.put("char", "String");
		map.put("uniqueidentifier", "String");
		map.put("number", "BigDecimal");
		map.put("decimal", "BigDecimal");
		map.put("bigint", "Long");
		map.put("tinyint", "Boolean");
		map.put("blob", "Blob");
		map.put("clob", "String");
	}

	public static String getPropertyType(String dataType) {
		String tmp = dataType.toLowerCase();
		StringTokenizer st = new StringTokenizer(tmp);

		return (String) map.get(st.nextToken());
	}

	private static Map<String, String> modelMapping;
	static {
		modelMapping = new HashMap<String, String>();
		// 系统管理模块
		modelMapping.put("sys", "system");
		// 配置管理模块
		modelMapping.put("cfg", "config");
		// 指标管理模块
		modelMapping.put("ie", "index");
		// 关系管理
		modelMapping.put("prm", "relation");
	}

	public static String getModel(String model) {
		String model1 = modelMapping.get(model);
		if (model1 == null) {
			model1 = model;
		}
		return model1;
	}

	/**
	 * 处理部分关键字段
	 * 
	 * @param coder
	 */
	public static void processProperties(Coder coder) {
		CoderGenerator.processClassName(coder);
		CoderGenerator.processModelName(coder);
		CoderGenerator.processFunctionName(coder);
		CoderGenerator.processPackageName(coder);
		CoderGenerator.processFields(coder);
		CoderGenerator.processTitle(coder);
		if (coder.getCrtDate() == null) {
			coder.setCrtDate(new Date());
		}
		if (coder.getSystemUser() == null) {
			coder.setSystemUser(System.getProperty("user.name"));
		}
	}

	/**
	 * 当类名为空时根据表名设置类名
	 * 
	 * @param className
	 * @return
	 */
	public static void processClassName(Coder coder) {
		String tableName = coder.getTableName();
		if (StringUtils.isEmpty(coder.getClassName())) {
			coder.setClassName(ReflectHelper
					.getPropertyName(tableName.substring(tableName.indexOf(Global.CHARACTER_UNDERLINE) + 1), false));
			coder.setClassDeclare(ReflectHelper
					.getPropertyName(tableName.substring(tableName.indexOf(Global.CHARACTER_UNDERLINE) + 1)));
		}
	}

	/**
	 * 当模块名为空时根据表名设置模块名
	 * 
	 * @param coder
	 */
	public static void processModelName(Coder coder) {
		String tableName = coder.getTableName();
		if (StringUtils.isEmpty(coder.getModelName())) {
			coder.setModelName(ReflectHelper
					.getPropertyName(getModel(tableName.substring(0, tableName.indexOf("_")).toLowerCase())));
		}
		String tableComment = coder.getTableComment();
		if (tableComment == null) {
			tableComment = tableName;
		}
		if (StringUtils.isEmpty(coder.getModelNameCn())) {
			String modelNameCn = tableComment;
			if (tableComment.indexOf(Global.CHARACTER_STICK) > 0) {
				modelNameCn = tableComment.substring(0, tableComment.indexOf(Global.CHARACTER_STICK));
			}
			coder.setModelNameCn(modelNameCn);
		}
	}

	/**
	 * 当功能名为空时根据表名设置功能名
	 * 
	 * @param coder
	 */
	public static void processFunctionName(Coder coder) {
		String tableName = coder.getTableName();
		if (StringUtils.isEmpty(coder.getFunctionName())) {
			coder.setFunctionName(ReflectHelper.getPropertyName(tableName.substring(tableName.indexOf("_") + 1)));
		}
	}

	/**
	 * 当标题为空时根据表名设置标题
	 * 
	 * @param coder
	 */
	public static void processTitle(Coder coder) {
		if (StringUtils.isEmpty(coder.getTitle())) {
			coder.setTitle(coder.getModelNameCn() + "->" + coder.getTableComment());
		}
	}

	/**
	 * 当包名为空时根据表名设置包名
	 * 
	 * @param coder
	 */
	public static void processPackageName(Coder coder) {
		if (StringUtils.isEmpty(coder.getPackageName())) {
			coder.setPackageName(Configure.getProperty("coder.package.prefix", "com.sky") + "." + coder.getModelName()
					+ "." + coder.getFunctionName().toLowerCase());
		}
	}

	/**
	 * 处理字段名称首字母大写
	 * 
	 * @param coder
	 */
	public static void processFields(Coder coder) {
		for (Column column : coder.getFields()) {
			column.setPropFuncName(ReflectHelper.capitalName(column.getPropertyName()));
			if ("1".equals(column.getPrimaryKeyFlag())) {
				coder.setIdName(column.getPropertyName());
			}
			// System.out.println(column.getPropertyName() + "=====>>>>>" +
			// column.getRequiredFlag());
		}
	}
}
