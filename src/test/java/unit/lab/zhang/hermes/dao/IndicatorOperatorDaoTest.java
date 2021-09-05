package unit.lab.zhang.hermes.dao;

import lab.zhang.hermes.Application;
import lab.zhang.hermes.dao.IndicatorOperatorDao;
import lab.zhang.hermes.entity.operator.IndicatorOperatorEntity;
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
public class IndicatorOperatorDaoTest {

    @Autowired
    IndicatorOperatorDao target;

    @Before
    public void setUp() {
    }

    @Test
    public void test_findAll() {
        List<IndicatorOperatorEntity> list = target.findAll();
        assertNotNull(list);
        System.out.println(list);
    }

    @Test
    public void test_findOne() {
        IndicatorOperatorEntity item1 = target.findOne(1);
        assertNotNull(item1);
        System.out.println(item1);

        IndicatorOperatorEntity item2 = target.findOne(2);
        assertNotNull(item2);
        System.out.println(item2);
    }

    @Test
    public void test_insert() {
        IndicatorOperatorEntity item = new IndicatorOperatorEntity("lab.zhang.hermes.pojo.operators.externals.ExternalOperatorFourior");
        Long id = target.insert(item);
        assertNotNull(id);
        assertTrue(id > 0);
        System.out.println(id);
    }

    @Test
    public void test_update() {
        IndicatorOperatorEntity item1 = target.findOne(4);
        assertNotNull(item1);
        item1.setClazz("测试式4改");
        target.update(item1);
        System.out.println(item1);

        IndicatorOperatorEntity item2 = target.findOne(4);
        assertNotNull(item2);
        assertEquals("测试式4改", item2.getClazz());
    }

    @Test
    public void test_delete() {
        target.delete(4);
        IndicatorOperatorEntity item1 = target.findOne(4);
        assertNull(item1);
    }
}
