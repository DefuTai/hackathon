package com.dfire.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class FileUtil {

    static Logger logger = Logger.getLogger(FileUtil.class);

    /**
     * 把content内容，以追加方式写入目标文件filePath
     *
     * @param filePath 写入文件地址
     * @param content  待写入内容
     */
    public static void write(String filePath, String content) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(filePath, true);
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 清洗原日志文件
     *
     * @param filePath 目标文件路径
     * @return 结果文件路径
     */
    public static String readFile(String filePath) {
        String formatFilePath = System.getProperty("user.dir") + "/src/main/resources/format/DataFormat_" + DataTimeUtil.date2String() + ".txt";
        BufferedReader bufferedReader = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            bufferedReader = new BufferedReader(inputStreamReader);
            //接收日志文件单行内容
            String var;
            //保存调用的服务
            String serviceName = "";
            //保存调用方法
            String methodName = "";
            //保存方法参数信息

            String[] strList;
            Map<String, String> map = new LinkedHashMap<>();
            while (null != (var = bufferedReader.readLine())) {
                StringBuffer sb = new StringBuffer();
                StringBuilder parameterName = new StringBuilder();
                strList = var.split(" ");
                for (int j = 0; j < strList.length; j++) {
                    if (j == 9) {
                        String[] s = strList[j].split(":");
                        serviceName = s[0];
                    }
                    if (j == 10) {
                        String[] s = strList[j].split("\\(");
                        methodName = s[0];
                    }
                    //由于用户输入参数内可能包含空格" "，所以在处理最后一个参数时直接去掉数组中最后两个下标对应的值即可
                    if (j >= 11 && j < strList.length - 2) {
                        parameterName = parameterName.append(strList[j]);
                    }
                }
                map.put("service", serviceName);
                map.put("method", methodName);
                map.put("params", parameterName.toString());
                //
                sb.append(JSONObject.toJSON(map) + "\n");
                //逐行写入
                write(formatFilePath, sb.toString());
            }
        } catch (IOException e) {
            logger.error("获取文件出错：" + e);
            e.printStackTrace();
        } finally {
            try {
                if (null != bufferedReader) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return formatFilePath;
    }

    /**
     * 解析aliyunLog导出的日志文件
     *
     * @return
     */
    public static String analysisAliyunLog(String filePath) {
        String formatFilePath = System.getProperty("user.dir") + "/src/main/resources/format/UploadOrderData_" + DataTimeUtil.date2String() + ".txt";
        BufferedReader bufferedReader = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            bufferedReader = new BufferedReader(inputStreamReader);
            //接收日志文件单行内容
            String var;
            while (null != (var = bufferedReader.readLine())) {
                StringBuilder sb = new StringBuilder();
                JSONObject jsonObject = JSONObject.parseObject(var);

                JSONObject totalPayInfo = jsonObject.getJSONObject("dataJson");
                sb.append(totalPayInfo);
                sb.append("∞");

                String entityId = totalPayInfo.getString("orderId").substring(0, 8);
                sb.append(entityId + "\n");
                //逐行写入
                write(formatFilePath, sb.toString());
            }
        } catch (IOException e) {
            logger.error("获取文件出错：" + e);
            e.printStackTrace();
        } finally {
            try {
                if (null != bufferedReader) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return formatFilePath;
    }

    public static void main(String[] args) {
//        String source = System.getProperty("user.dir") + "/src/main/resources/datafile/2019_0706-0708.txt";
        String source = "/Users/defu/Desktop/20200106-chef-soa/0106.txt";
        System.out.println(analysisAliyunLog(source));
    }

}
