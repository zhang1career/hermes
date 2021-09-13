package lab.zhang.hermes.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lab.zhang.hermes.action.calculator.CalcAction;
import lab.zhang.hermes.util.CastUtil;
import lab.zhang.hermes.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
    ResponseVo<Long> create(@RequestParam("event_id") int eventId,
                            @RequestParam("user_id") long userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userId);

        try {
            Long id = (Long) calcAction.act(eventId, params);
            return new ResponseVo<>(id);
        } catch (Exception e) {
            return new ResponseVo<>(e);
        }
    }
}
