package lab.zhang.hermes.action.calculator;

import lab.zhang.hermes.service.CalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author zhangrj
 */
@Service
public class CalcAction {

    @Autowired
    private CalculatorService calculatorService;

    public Object act(int eventId, Map<String, Object> params) {
        return calculatorService.calc(eventId, params);
    }
}
