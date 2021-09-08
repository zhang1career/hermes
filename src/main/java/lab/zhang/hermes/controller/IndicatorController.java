package lab.zhang.hermes.controller;

import lab.zhang.hermes.entity.indicator.IndicatorEntity;
import lab.zhang.hermes.repo.IndicatorRepo;
import lab.zhang.hermes.vo.indicator.IndicatorVo;
import lab.zhang.hermes.vo.ResponseVo;
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

    @GetMapping("/api/indicators")
    List<IndicatorVoLite> all() {
        List<IndicatorEntity> indicatorEntityList = indicatorRepo.getList();
        return IndicatorVoLite.listOf(indicatorEntityList);
    }

    @GetMapping("/api/indicators/{id}")
    IndicatorVo one(@PathVariable Long id) {
        IndicatorEntity indicatorEntity = indicatorRepo.getItem(id);
        return IndicatorVo.of(indicatorEntity);
    }

    @PostMapping("/api/indcators")
    ResponseVo<Long> newIndicator(@RequestBody IndicatorEntity indicatorEntity) {
        Long id = indicatorRepo.create(indicatorEntity);
        return new ResponseVo<>(id);
    }
}
