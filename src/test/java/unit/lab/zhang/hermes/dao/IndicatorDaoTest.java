package unit.lab.zhang.hermes.dao;

import lab.zhang.hermes.Application;
import lab.zhang.hermes.dao.IndicatorDao;
import lab.zhang.hermes.entity.indicator.IndicatorEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class IndicatorDaoTest {

    @Autowired
    IndicatorDao target;

    @Before
    public void setUp() {
    }

    @Test
    public void test_findAll() {
        List<IndicatorEntity> list = target.findAll();
        assertNotNull(list);
        System.out.println(list);
    }

    @Test
    public void test_findOne() {
        IndicatorEntity item1 = target.findOne(1);
        assertNotNull(item1);
        System.out.println(item1);

        IndicatorEntity item2 = target.findOne(2);
        assertNotNull(item2);
        System.out.println(item2);
    }

    @Test
    public void test_insert() {
        IndicatorEntity item = new IndicatorEntity("测试式3", 0, "{\"name\":\"指标1\",\"type\":1,\"id\":0,\"value\":1}");
        int id = target.insert(item);
        assertNotNull(id);
        assertTrue(id > 0);
        System.out.println(id);
    }

    @Test
    public void test_update() {
        IndicatorEntity item1 = target.findOne(3);
        assertNotNull(item1);
        item1.setName("测试式3改");
        item1.setExpression("{\"name\":\"指标3\",\"type\":1,\"id\":0,\"value\":3}");
        target.update(item1);
        System.out.println(item1);

        IndicatorEntity item2 = target.findOne(3);
        assertNotNull(item2);
        assertEquals("测试式3改", item2.getName());
        assertEquals("{\"name\":\"指标3\",\"type\":1,\"id\":0,\"value\":3}", item2.getExpression());
    }

    @Test
    public void test_delete() {
        target.delete(3);
        IndicatorEntity item1 = target.findOne(3);
        assertNull(item1);
    }
}
