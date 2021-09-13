package lab.zhang.hermes.repo;

import lab.zhang.apollo.pojo.Token;
import lab.zhang.apollo.repo.StorableExpression;
import lab.zhang.apollo.service.LexerService;
import lab.zhang.apollo.service.lexer.BasicLexerService;
import lab.zhang.hermes.dao.PlannedExpressionDao;
import lab.zhang.hermes.entity.expression.PlannedExpressionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author zhangrj
 */
@Repository
public class PlannedExpressionRepo extends BaseRepo implements StorableExpression {

    @Autowired
    private PlannedExpressionDao plannedExpressionDao;

    private final LexerService lexerService = new BasicLexerService();


    public PlannedExpressionEntity create(long operationId, Token node) {
        PlannedExpressionEntity plannedExpressionEntity = new PlannedExpressionEntity(operationId, lexerService.jsonOf(node));
        int count = plannedExpressionDao.insert(plannedExpressionEntity);
        if (count < 1) {
            return null;
        }
        return plannedExpressionEntity;
    }

    @Override
    public String getExpression(long indicatorId) {
        PlannedExpressionEntity plannedExpressionEntity = plannedExpressionDao.findByIndicatorId(indicatorId);
        if (plannedExpressionEntity == null) {
            return null;
        }
        return plannedExpressionEntity.getExpression();
    }
}
