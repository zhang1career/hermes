package unit.lab.zhang.hermes.service;

import lab.zhang.hermes.Application;
import lab.zhang.hermes.service.IndicatorPlanService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class IndicatorPlanServiceTest {
    @Autowired
    private IndicatorPlanService target;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setUp() {
    }

    @Test
    public void test_plan() {
        Long plannedId = target.plan(1L);
        assertNotNull(plannedId);
        System.out.println("id=" + plannedId);
    }
}
