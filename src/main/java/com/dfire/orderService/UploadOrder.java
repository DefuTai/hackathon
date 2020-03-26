package com.dfire.orderService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dfire.soa.order.bo.*;
import com.dfire.soa.order.service.ICashierUploadOrderService;
import com.dfire.soa.order.util.UuidUtil;
import com.dfire.soa.order.vo.OrderInfoVo;
import com.dfire.soa.order.vo.TotalPayInfoVo;
import com.dfire.utils.DataTimeUtil;
import com.dfire.utils.DubboInit;
import com.google.gson.Gson;
import com.twodfire.exception.BizException;
import com.twodfire.share.result.Result;
import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 创建时间: 2019/7/3 下午5:05
 * 类描述:
 *
 * @author lianyu
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(value = {"classpath:applicationContext.xml"})
public class UploadOrder extends AbstractJavaSamplerClient {

    Logger logger = LoggerFactory.getLogger(UploadOrder.class);

    private String totalPayInfoVoString = "";
    private String entityId = "";

    private long star = 0;
    private long end = 0;

    private ICashierUploadOrderService cashierUploadOrderService;

    @Before
    public void setupTest(JavaSamplerContext context) {
        System.out.println("####:Before start...");

        DubboInit.initApplicationContext();
        DubboInit init = DubboInit.getInstance();
        cashierUploadOrderService = (ICashierUploadOrderService) init.getBean("cashierUploadOrderService");

        System.out.println("####:Before end " + cashierUploadOrderService);
    }

    @Test
    @Override
    public SampleResult runTest(JavaSamplerContext arg0) {
        SampleResult sampleResult = new SampleResult();

        totalPayInfoVoString = arg0.getParameter("totalPayInfoVoString");
        entityId = arg0.getParameter("entityId");

        star = System.currentTimeMillis();
        //订单上传
        try {
            String totalPayInfoVo = replaceTotalPayInfoVo(totalPayInfoVoString, entityId);

            sampleResult.sampleStart();
            Result result = cashierUploadOrderService.uploadOrder(totalPayInfoVo, entityId);

            System.out.println("####: 接口返回结果 " + JSONObject.toJSONString(result));

            sampleResult.sampleEnd();

            sampleResult.setSuccessful(result.isSuccess());
            sampleResult.setResponseData(JSONObject.toJSONString(result), "UTF-8");
            sampleResult.setResponseCode(result.getResultCode());
            sampleResult.setResponseMessage(result.getMessage());
        } catch (Exception e) {
            sampleResult.setResponseMessage("执行异常 ==> " + e);
            sampleResult.setSuccessful(false);
        }
        return sampleResult;
    }

    @Override
    public void teardownTest(JavaSamplerContext context) {
        end = System.currentTimeMillis();
        System.out.println("共耗时 " + (end - star) + "ms");
    }

    @Override
    public Arguments getDefaultParameters() {
        Arguments arg = new Arguments();
        arg.addArgument("totalPayInfoVoString", totalPayInfoVoString);
        arg.addArgument("entityId", entityId);
        return arg;
    }

    private boolean uploadOrder(String totalPayInfoVo, String entityId) {
        boolean flag = true;
        try {
            Result result = cashierUploadOrderService.uploadOrder(totalPayInfoVo, entityId);
            logger.info(result.getMessage());
            if (!result.isSuccess()) {
                throw new BizException(result.getMessage(), result.getResultCode());
            }
        } catch (Exception e) {
            flag = false;
            logger.error("uploadOrder error entityId:{}, totalPayInfo:{}, size:{}", entityId, totalPayInfoVo, totalPayInfoVo.length(), e);
        }
        return flag;
    }

    private String modifyTotalPayInfoVo(String totalPayInfoVoString, String entityId) {
        TotalPayInfoVo totalPayInfoVo = new Gson().fromJson(totalPayInfoVoString, TotalPayInfoVo.class);

        List<InstanceBill> instanceBills = totalPayInfoVo.getInstanceBills();
        for (InstanceBill instanceBill : instanceBills) {
            instanceBill.setId(UuidUtil.generate(entityId));
        }
        totalPayInfoVo.setInstanceBills(instanceBills);

        TotalPay totalPay = totalPayInfoVo.getTotalPay();
        totalPay.setLastVer(totalPay.getLastVer() + 1);
        totalPayInfoVo.setTotalPay(totalPay);

        List<OrderInfoVo> orderInfoVos = totalPayInfoVo.getOrderInfoVos();
        for (OrderInfoVo orderInfoVo : orderInfoVos) {
            List<Instance> instances = orderInfoVo.getInstances();
            for (Instance instance : instances) {
                instance.setLastVer(instance.getLastVer() + 1);
                instance.setOpTime(DataTimeUtil.currentTimeMillis());
            }
            orderInfoVo.setInstances(instances);

            Order order = orderInfoVo.getOrder();
            order.setLastVer(order.getLastVer() + 1);

            orderInfoVo.setOrder(order);
        }
        totalPayInfoVo.setOrderInfoVos(orderInfoVos);

        List<Pay> pays = totalPayInfoVo.getPays();
        for (Pay pay : pays) {
            pay.setLastVer(pay.getLastVer() + 1);
        }
        totalPayInfoVo.setPays(pays);

        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(totalPayInfoVo));
        jsonObject.put("useCashier", true);

        return jsonObject.toJSONString();
    }

    private String replaceTotalPayInfoVo(String totalPayInfoVoString, String entityId) {
        if (StringUtils.isEmpty(totalPayInfoVoString)) {
            return null;
        }
        JSONObject totalPayInfoVo = JSONObject.parseObject(totalPayInfoVoString);

        if (totalPayInfoVo != null) {
            JSONArray instanceBills = totalPayInfoVo.getJSONArray("instanceBills");
            if (instanceBills != null) {
                for (int i = 0; i < instanceBills.size(); i++) {
                    JSONObject billJson = instanceBills.getJSONObject(i);
                    if (billJson != null) {
                        billJson.put("id", UuidUtil.generate(entityId));
                        billJson.put("opTime", DataTimeUtil.currentTimeMillis());
                    }
                }
                totalPayInfoVo.put("instanceBills", instanceBills);
            }

            JSONArray orderInfoVos = totalPayInfoVo.getJSONArray("orderInfoVos");
            if (orderInfoVos != null) {
                int newLastVer = DataTimeUtil.interceptionTimestamp();
                for (int i = 0; i < orderInfoVos.size(); i++) {
                    JSONObject orderInfoJson = orderInfoVos.getJSONObject(i);
                    if (orderInfoJson != null) {
                        JSONArray instances = orderInfoJson.getJSONArray("instances");
                        if (instances != null) {
                            for (int j = 0; j < instances.size(); j++) {
                                JSONObject instanceJson = instances.getJSONObject(j);
                                if (instanceJson != null) {
                                    instanceJson.put("lastVer", newLastVer);
                                    instanceJson.put("modifyTime", DataTimeUtil.currentTimeMillis() / 1000);
                                }
                            }
                            orderInfoJson.put("instances", instances);
                        }

                        JSONObject order = orderInfoJson.getJSONObject("order");
                        if (order != null) {
                            order.put("lastVer", newLastVer);
                            order.put("modifyTime", DataTimeUtil.currentTimeMillis() / 1000);
                            orderInfoJson.put("order", order);
                        }
                    }
                }
                totalPayInfoVo.put("orderInfoVos", orderInfoVos);
            }
        }
        return totalPayInfoVo.toJSONString();
    }

}
