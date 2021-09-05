package lab.zhang.hermes.controller;

import lab.zhang.hermes.entity.indicator.IndicatorEntity;
import lab.zhang.hermes.repo.IndicatorRepo;
import lab.zhang.hermes.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangrj
 */
@RestController
public class IndicatorController {

    @Autowired
    private IndicatorRepo indicatorRepo;

//    @GetMapping("/indicators")
//    List<IndicatorVo> all() {
//        List<IndicatorEntity> indicatorEntityList = indicatorRepo.findAll();
//        return IndicatorMapper.INSTANCE.voListFrom(indicatorEntityList);
//    }
//
//    @GetMapping("/indicators/{id}")
//    IndicatorVo one(@PathVariable Long id) {
//        IndicatorEntity indicatorEntity = indicatorRepo.findOne(id);
//        return IndicatorMapper.INSTANCE.voFrom(indicatorEntity);
//    }

    @PostMapping("/indcators")
    ResponseVo<Long> newIndicator(@RequestBody IndicatorEntity indicatorEntity) {
        Long id = indicatorRepo.create(indicatorEntity);
        return new ResponseVo<>(id);
    }
}
