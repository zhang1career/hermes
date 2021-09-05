package lab.zhang.hermes.service;

import lab.zhang.apollo.pojo.Token;
import lab.zhang.apollo.service.LexerService;
import lab.zhang.apollo.service.PlanService;
import lab.zhang.apollo.service.lexer.BasicLexerService;
import lab.zhang.hermes.dao.IndicatorDao;
import lab.zhang.hermes.entity.indicator.IndicatorEntity;
import lab.zhang.hermes.repo.PlannedIndicatorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhangrj
 */
@Service
public class IndicatorPlanService {
    static protected LexerService lexerService = new BasicLexerService();
    static protected PlanService planService = new PlanService(new PlannedIndicatorRepo());

    @Autowired
    private IndicatorDao indicatorDao;

    @Autowired
    private PlannedIndicatorRepo plannedIndicatorRepo;

    public Long plan(Long id) {
         IndicatorEntity indicatorEntity = indicatorDao.findOne(id);
        if (indicatorEntity == null) {
            return null;
        }

        String freshExpression = indicatorEntity.getExpression();
        if (freshExpression == null) {
            return null;
        }

        Token freshToken = lexerService.tokenOf(freshExpression);
        if (freshToken == null) {
            return null;
        }

        Token plannedToken = planService.plan(freshToken);
        if (plannedToken == null) {
            return null;
        }

        return plannedIndicatorRepo.create(indicatorEntity.getId(), plannedToken);
    }
}
