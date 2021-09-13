package lab.zhang.hermes.repo;

import lab.zhang.apollo.repo.StorableExpression;
import lab.zhang.hermes.dao.OriginalExpressionDao;
import lab.zhang.hermes.entity.expression.OriginalExpressionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author zhangrj
 */
@Repository
public class OriginalExpressionRepo extends BaseRepo implements StorableExpression {

    @Autowired
    private OriginalExpressionDao originalExpressionDao;

    @Override
    public String getExpression(long indicatorId) {
        OriginalExpressionEntity expression = originalExpressionDao.findByIndicatorId(indicatorId);
        if (expression == null) {
            return null;
        }
        return expression.getExpression();
    }
}
