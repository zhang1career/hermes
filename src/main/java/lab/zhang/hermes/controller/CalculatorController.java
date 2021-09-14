package lab.zhang.hermes.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lab.zhang.hermes.action.calculator.CalcAction;
import lab.zhang.hermes.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author zhangrj
 */
@CrossOrigin
@RestController
public class CalculatorController {

    @Autowired
    private CalcAction calcAction;


    @PostMapping("/api/calc")
    ResponseVo<Object> create(@RequestParam("indicator_id") long indicatorId,
                            @RequestParam("params") String params) {
        Map<String, Object> paramMap = JSON.parseObject(params, new TypeReference<Map<String, Object>>() {});
        try {
            Object result = calcAction.act(indicatorId, paramMap);
            return new ResponseVo<>(result);
        } catch (Exception e) {
            return new ResponseVo<>(e);
        }
    }
}
