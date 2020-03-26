package com.dfire.replay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dfire.utils.DataTimeUtil;
import com.dfire.utils.FileUtil;
import org.apache.log4j.Logger;

import java.io.*;

/**
 * Created by xuweidong on 2018/10/23.
 */
public class TransData {

    public static Logger logger = Logger.getLogger(TransData.class);

    /**
     * @param source 源文件路径
     */
    public static String transFromLogs(String source) {
        String dest = "./src/main/resources/format/Result_" + DataTimeUtil.date2String() + ".csv";
        String filePath = FileUtil.readFile(source);
        String method = "";
        String params = "";
        File file = new File(filePath);
        String temp = null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            FileOutputStream fileWriter = new FileOutputStream(dest);
            OutputStreamWriter writer = new OutputStreamWriter(fileWriter, "UTF-8");
            while ((temp = reader.readLine()) != null) {
                JSONObject obj = JSON.parseObject(temp);
                method = obj.get("method").toString();
                switch (method) {
                    case "calculateMenuPrice":
                    case "getKindMenus":
                    case "getMenuDetailDataByMenuIdForMultipleMenu":
                    case "findMenus":
                    case "queryUseLessMenuInfoList":
                    case "findMenuAndProp":
                    case "findMenuAndPropForMultipleMenu":
                    case "queryList":
                    case "queryMenuNonSkuValid":
                    case "getMultiMenuIdForEatIn":
                        JSONArray array = JSONArray.parseArray(obj.get("params").toString());
                        for (int i = 0; i < array.size(); i++) {
                            params += "∞" + array.get(i);
                        }
                        writer.write(method + params);
                        writer.write("\n");
                        break;
                    default:
                        logger.info("this method is not allowed in this class, methodName: " + method);
                        break;
                }
                params = "";
            }
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return dest;
    }

    public static void main(String[] args) {
        String source = System.getProperty("user.dir") + "/src/main/resources/datafile/long_text_2018-10-22-20-15-24.txt";
        transFromLogs(source);
    }

}
