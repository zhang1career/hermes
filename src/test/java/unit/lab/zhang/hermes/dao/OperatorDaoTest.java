package unit.lab.zhang.hermes.dao;

import lab.zhang.hermes.Application;
import lab.zhang.hermes.dao.OperatorDao;
import lab.zhang.hermes.entity.operator.OperatorEntity;
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
public class OperatorDaoTest {

    @Autowired
    OperatorDao target;

    @Before
    public void setUp() {
    }

    @Test
    public void test_findAll() {
        List<OperatorEntity> list = target.findAll();
        assertNotNull(list);
        System.out.println(list);
    }

    @Test
    public void test_findOne() {
        OperatorEntity item1 = target.findOne(1);
        assertNotNull(item1);
        System.out.println(item1);

        OperatorEntity item2 = target.findOne(2);
        assertNotNull(item2);
        System.out.println(item2);
    }

    @Test
    public void test_insert() {
        OperatorEntity item = new OperatorEntity("运算符", "lab.zhang.hermes.pojo.operators.externals.ExternalOperatorFourior");
        int count = target.insert(item);
        assertTrue(count > 0);
        System.out.println(count);
    }

    @Test
    public void test_update() {
        OperatorEntity item1 = target.findOne(4);
        assertNotNull(item1);
        item1.setClazz("测试式4改");
        target.update(item1);
        System.out.println(item1);

        OperatorEntity item2 = target.findOne(4);
        assertNotNull(item2);
        assertEquals("测试式4改", item2.getClazz());
    }

    @Test
    public void test_delete() {
        target.delete(4);
        OperatorEntity item1 = target.findOne(4);
        assertNull(item1);
    }
}
