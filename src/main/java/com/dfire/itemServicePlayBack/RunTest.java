package com.dfire.itemServicePlayBack;

import com.dfire.soa.item.bo.KindMenu;
import com.dfire.soa.item.service.IGetMenuService;
import com.dfire.soa.item.service.IMultiMenuReadService;
import com.dfire.utils.DubboInit;
import com.twodfire.share.result.Result;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class RunTest extends AbstractJavaSamplerClient {


    private IMultiMenuReadService multiMenuReadService;
    private  IGetMenuService getMenuService;

    private long star = 0;
    private long end = 0;
    private String p1="";
    private String p2="";
    private String p3="";
    private String p4="";
    private String p5="";
    private String p6="";
    private String p7="";
    private String p8="";
    private String service = "";

    @Before
    public void setupTest(JavaSamplerContext context) {
        DubboInit.initApplicationContext();
        DubboInit init = DubboInit.getInstance();
        getMenuService = (IGetMenuService) init.getBean("getMenuService");
        multiMenuReadService = (IMultiMenuReadService) init.getBean("multiMenuReadService");

    }

    @Test
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {

        star = System.currentTimeMillis();
        SampleResult sr = new SampleResult();
        getJmeterParm(javaSamplerContext);
        sr.sampleStart();

        switch (service){
            case "getKindMenus":
                try {
                    Result<List<KindMenu>> rlt = getMenuService.getKindMenus(p1);
                    if (!rlt.equals(null)) {
                        sr.setSuccessful(true);
                        sr.setResponseMessage(service + " getKindMenus 方法 执行 成功！");
                        sr.setResponseCode("200");
                        System.out.println("成功1");
                    } else {
                        sr.setSuccessful(false);
                        sr.setResponseMessage(service + " getKindMenus 方法 执行 失败！");
                        sr.setResponseCode("404");
                        System.out.println("失败1");
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    sr.setSuccessful(false);
                }finally {
                    sr.sampleEnd();
                }
                break;
            case "getMultiMenuIdForEatIn":
                try {
                    Result<String> rlt = multiMenuReadService.getMultiMenuIdForEatIn(p1,p2);
                    if (rlt.isSuccess()) {
                        sr.setSuccessful(true);
                        sr.setResponseMessage(service + " getMultiMenuIdForEatIn 方法 执行 成功！");
                        sr.setResponseCode("200");
                        System.out.println("成功2");
                    } else {
                        sr.setSuccessful(false);
                        sr.setResponseMessage(service + " getMultiMenuIdForEatIn 方法 执行 失败！");
                        sr.setResponseCode("404");
                        System.out.println("失败2");
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    sr.setSuccessful(false);
                }finally {
                    sr.sampleEnd();
                }
                break;
             default:
                 System.out.println("过滤掉的service");
                 sr.setSuccessful(true);
                 sr.setResponseCode("200");
                 break;
        }

        return sr;
    }

    @Override
    public void teardownTest(JavaSamplerContext context) {
        end = System.currentTimeMillis();
        System.out.println("共耗时"+(end-star)+"ms");
    }

    /**
     * 设置传输ServiceName、ParameterList默认值
     *
     * @return
     */
    public Arguments getDefaultParameters() {

        Arguments params = new Arguments();
        params.addArgument("DubboService", service);
        params.addArgument("Par1", p1);
        params.addArgument("Par2", p2);
        params.addArgument("Par3", p3);
        params.addArgument("Par4", p4);
        params.addArgument("Par5", p5);
        params.addArgument("Par6", p6);
        params.addArgument("Par7", p7);
        params.addArgument("Par8", p8);

        return params;
    }

    /**
     * 获取jmeter输入的参数
     * @param arg0
     */
    public void getJmeterParm(JavaSamplerContext arg0) {
        service = arg0.getParameter("DubboService");
        p1 = arg0.getParameter("Par1");
        p2 = arg0.getParameter("Par2");
        p3 = arg0.getParameter("Par3");
        p4 = arg0.getParameter("Par4");
        p5 = arg0.getParameter("Par5");
        p6 = arg0.getParameter("Par6");
        p7 = arg0.getParameter("Par7");
        p8 = arg0.getParameter("Par8");

    }


}
