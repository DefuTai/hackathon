package com.dfire.kds.logic;

import com.dfire.soa.flame.FlameFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;

/**
 * 描述：
 *
 * @Author 鲢鱼
 * @Data 2020-01-09 11:35
 * @Version 1.0
 **/
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class SpliceSql extends AbstractTestNGSpringContextTests {

    @Resource
    private FlameFactory flameFactory;

    @Test
    public void test() {
    }

    public String initKdsPlan(Long kdsPlanId, String entityId, int type) {
        String kdsPlanVar = "INSERT INTO kds.kds_plan (kds_plan_id, entity_id, type, name, is_all_area, user_id, menu_count, precondition_kds_type, is_valid, last_ver, create_time, update_time) VALUES (" + kdsPlanId + ", '" + entityId + "', 1, '凉菜', 1, '002324896dde40f5016e0d5c29604e55', 20, 0, 1, 41, 1515042888199, 1573962721591);";
        return null;
    }

}
