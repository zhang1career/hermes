package lab.zhang.hermes.service;

import lab.zhang.apollo.pojo.Token;
import lab.zhang.apollo.service.LexerService;
import lab.zhang.apollo.service.PlanService;
import lab.zhang.apollo.service.lexer.BasicLexerService;
import lab.zhang.hermes.entity.expression.PlannedExpressionEntity;
import lab.zhang.hermes.entity.indicator.IndicatorEntity;
import lab.zhang.hermes.repo.IndicatorRepo;
import lab.zhang.hermes.repo.OriginalExpressionRepo;
import lab.zhang.hermes.repo.PlannedExpressionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author zhangrj
 */
@Service
public class IndicatorService {

    @Autowired
    private IndicatorRepo indicatorRepo;

    @Autowired
    private OriginalExpressionRepo originalExpressionRepo;

    @Autowired
    private PlannedExpressionRepo plannedExpressionRepo;


    /**
     * @param name
     * @param operatorId
     * @param operands
     * @return IndicatorEntity
     */
    public IndicatorEntity createOriginalIndicator(String name, long operatorId, String operands) {
        return indicatorRepo.create(name, operatorId, operands);
    }

    public PlannedExpressionEntity createPlannedIndicator(IndicatorEntity indicatorEntity) {
        String originalExpression = originalExpressionRepo.getExpression(indicatorEntity.getId());
        if (originalExpression == null) {
            return null;
        }

        LexerService lexerService = new BasicLexerService();
        Token originalToken = lexerService.tokenOf(originalExpression);
        if (originalToken == null) {
            return null;
        }

        PlanService planService = new PlanService(plannedExpressionRepo);
        Token plannedToken = planService.plan(originalToken);
        if (plannedToken == null) {
            return null;
        }

        return plannedExpressionRepo.create(indicatorEntity.getId(), plannedToken);
    }

    public PlannedExpressionEntity createPlannedIndicator(long id) {
        IndicatorEntity indicatorEntity = indicatorRepo.getItem(id);
        if (indicatorEntity == null) {
            return null;
        }

        return createPlannedIndicator(indicatorEntity);
    }
}
