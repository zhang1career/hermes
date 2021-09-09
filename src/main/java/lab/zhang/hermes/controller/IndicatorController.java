package lab.zhang.hermes.controller;

import lab.zhang.hermes.entity.indicator.IndicatorEntity;
import lab.zhang.hermes.entity.operator.OperatorEntity;
import lab.zhang.hermes.exception.IllegalParameterException;
import lab.zhang.hermes.repo.IndicatorRepo;
import lab.zhang.hermes.repo.OperatorRepo;
import lab.zhang.hermes.util.ListUtil;
import lab.zhang.hermes.util.StrUtil;
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
    private OperatorRepo operatorRepo;
    private OperatorRepo operatorRepo1;

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
        if (StrUtil.isNill(name)) {
            throw new IllegalParameterException();
        }

        OperatorEntity item = operatorRepo.getItem(operatorId);
        if (item == null) {
            return new ResponseVo<>(-1L);
        }

        if (StrUtil.isNill(indicatorIds)) {
            throw new IllegalParameterException();
        }
        List<Long> requestedIndicatorIdList = StrUtil.explode(indicatorIds);
        List<Long> existedIndicatorIdList = indicatorRepo.getIdList(requestedIndicatorIdList);
        if (!ListUtil.isEqual(requestedIndicatorIdList, existedIndicatorIdList)) {
            throw new IllegalParameterException();
        }

        Long id = indicatorRepo.create(name, operatorId, existedIndicatorIdList);
        return new ResponseVo<>(id);
    }
}
