//package com.dfire.itemServicePlayBack;
//
//import com.dfire.soa.item.bo.Menu;
//import com.dfire.soa.item.service.IGetMenuService;
//import com.dfire.soa.item.service.IMultiMenuReadService;
//import com.dfire.utils.DubboInit;
//import com.twodfire.share.result.Result;
//import org.apache.jmeter.config.Arguments;
//import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
//import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
//import org.apache.jmeter.samplers.SampleResult;
//import org.apache.log4j.Logger;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import javax.annotation.Resource;
//import javax.measure.unit.SystemOfUnits;
//
////@RunWith(SpringJUnit4ClassRunner.class)
////@ContextConfiguration(locations = { "classpath:applicationContext.xml"})
//public class YBTest extends AbstractJavaSamplerClient {
//
//    private static final Logger logger = Logger.getLogger(RunTest.class);
//
//    // @Resource
//    // private IMultiMenuReadService multiMenuReadServiceImpl;
//    // @Resource
//    private  IGetMenuService getMenuService;
//
//    private long star = 0;
//    private long end = 0;
//    private String p1="";
//    private String p2="";
//    private String service = "";
//
//    @Before
//    public void setupTest() {
//        DubboInit init = DubboInit.getInstance();
//        System.out.println("2~~~~~~~~~~~~~~~~");
//        getMenuService = (IGetMenuService) init.getBean("getMenuService");
//
//    }
//
//    @Test
//    public void yibei_test(){
//        System.out.println("!!!!!!!!service:"+getMenuService.findMenu(p1, p2));
//    }
//
//    //@Test
//    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
//
//        star = System.currentTimeMillis();
//        SampleResult sr = new SampleResult();
//        sr.sampleStart();
//        getJmeterParm(javaSamplerContext);
//        switch (service){
//            case "IGetMenuService":
//
//                try {
//                    Result<Menu> rlt = getMenuService.findMenu(p1, p2);
//                    if (rlt != null) {
//                        rlt.setSuccess(true);
//                        rlt.setMessage(service + " findMenu 方法 执行 成功！");
//                        rlt.setResultCode("200");
//                    } else {
//                        rlt.setSuccess(false);
//                        rlt.setMessage(service + " findMenu 方法 执行 失败！");
//                        rlt.setResultCode("0");
//                    }
//                }catch(Exception e){
//                    e.printStackTrace();
//                    sr.setSuccessful(false);
//                }finally {
//                    sr.sampleEnd();
//                }
//
//                break;
//            default:
//                logger.info("过滤掉的service");
//                break;
//        }
//
//        return sr;
//    }
//
//
//    @Override
//    public void teardownTest(JavaSamplerContext context) {
//        end = System.currentTimeMillis();
//        logger.info("共耗时"+(end-star)/100+"ms");
//    }
//
//
//*
//     * 获取jmeter输入的参数
//     * @param arg0
//
//
//    public void getJmeterParm(JavaSamplerContext arg0) {
//        service = arg0.getParameter("DubboService");
//        p1 = arg0.getParameter("Par1");
//        p2 = arg0.getParameter("Par2");
//
//    }
//
//*
//     * 设置传输ServiceName、ParameterList默认值
//     *
//     * @return
//
//
// public Arguments getDefaultParameters() {
//        System.out.println("设置jmeter参数");
//        Arguments params = new Arguments();
//        params.addArgument("DubboService", service);
//        params.addArgument("Par1", p1);
//        params.addArgument("Par2", p2);
//
//        return params;
//    }
//
//
//}
