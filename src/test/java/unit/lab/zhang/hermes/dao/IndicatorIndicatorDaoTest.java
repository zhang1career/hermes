package unit.lab.zhang.hermes.dao;

import lab.zhang.hermes.Application;
import lab.zhang.hermes.dao.IndicatorIndicatorDao;
import lab.zhang.hermes.entity.indicator.IndicatorEntity;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class IndicatorIndicatorDaoTest {

    @Autowired
    IndicatorIndicatorDao target;

    @Before
    public void setUp() {
    }

    @Test
    public void test_getParents() {
        List<IndicatorEntity> parents = target.selectParents(2);
        System.out.println(parents);
    }

    @Test
    public void test_getChildren() {
        List<IndicatorEntity> children = target.selectChildren(7);
        System.out.println(children);
    }

    @Test
    public void test_insertChild() {
        int i = target.insertChild(11L, 12L);
        System.out.println(i);
    }

    @Test
    public void test_insertChildren() {
        int i = target.insertChildren(12L, new Long[]{13L, 14L, 15L});
        System.out.println(i);
    }

    @Test
    public void test_delChild() {
        int i = target.deleteChild(12L, 13L);
        System.out.println(i);
    }

    @Test
    public void test_delChildren() {
        int i = target.deleteChildren(12L, new Long[]{13L, 14L, 15L});
        System.out.println(i);
    }
}
