package lab.zhang.hermes.controller;

import lab.zhang.hermes.action.indicator.CreateAction;
import lab.zhang.hermes.entity.indicator.IndicatorEntity;
import lab.zhang.hermes.repo.IndicatorRepo;
import lab.zhang.hermes.vo.ResponseVo;
import lab.zhang.hermes.vo.indicator.IndicatorVo;
import lab.zhang.hermes.vo.indicator.IndicatorVoLite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhangrj
 */
@CrossOrigin
@RestController
public class IndicatorController {

    @Autowired
    private IndicatorRepo indicatorRepo;

    @Autowired
    private CreateAction indicatorCreateAction;

    @GetMapping("/api/indicators")
    List<IndicatorVoLite> getList() {
        List<IndicatorEntity> indicatorEntityList = indicatorRepo.getList();
        return IndicatorVoLite.listOf(indicatorEntityList);
    }

    @GetMapping("/api/indicators/{id}")
    ResponseVo<IndicatorVo> getItem(@PathVariable Long id) {
        IndicatorEntity indicatorEntity = indicatorRepo.getItem(id);
        if (indicatorEntity == null) {
            return new ResponseVo<>(null);
        }
        return new ResponseVo<>(IndicatorVo.of(indicatorEntity));
    }

    @PostMapping("/api/indicators")
    ResponseVo<Long> create(@RequestParam("name") String name,
                            @RequestParam("operator_id") long operatorId,
                            @RequestParam("indicator_ids") String indicatorIds
    ) {
        try {
            Long id = indicatorCreateAction.act(name, operatorId, indicatorIds);
            return new ResponseVo<>(id);
        } catch (Exception e) {
            return new ResponseVo<>(e);
        }
    }
}
