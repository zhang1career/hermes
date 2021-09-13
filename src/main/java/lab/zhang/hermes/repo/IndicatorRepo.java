package lab.zhang.hermes.repo;

import lab.zhang.apollo.pojo.ApolloType;
import lab.zhang.apollo.pojo.Token;
import lab.zhang.apollo.service.LexerService;
import lab.zhang.apollo.service.lexer.BasicLexerService;
import lab.zhang.hermes.dao.IndicatorDao;
import lab.zhang.hermes.dao.OperatorDao;
import lab.zhang.hermes.dao.OriginalExpressionDao;
import lab.zhang.hermes.entity.expression.OriginalExpressionEntity;
import lab.zhang.hermes.entity.indicator.IndicatorEntity;
import lab.zhang.hermes.entity.operator.OperatorEntity;
import lab.zhang.hermes.exception.SqlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangrj
 */
@Repository
public class IndicatorRepo extends BaseRepo {

    @Autowired
    private IndicatorDao indicatorDao;

    @Autowired
    private OperatorDao operatorDao;

    @Autowired
    private OriginalExpressionDao originalExpressionDao;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private final LexerService lexerService = new BasicLexerService();


    public List<IndicatorEntity> getList() {
        return indicatorDao.findAll();
    }


    public List<IndicatorEntity> getList(List<Long> idList) {
        Map<String, Object> condition = new HashMap<>(0);
        condition.put("ids", idList);
        return indicatorDao.findByCondition(condition);
    }


    public List<Long> getIdList(List<Long> idList) {
        List<IndicatorEntity> indicatorEntityList = getList(idList);
        if (indicatorEntityList == null) {
            return null;
        }
        return columnOf(indicatorEntityList, IndicatorEntity::getId);
    }


    public IndicatorEntity getItem(long id) {
        return indicatorDao.findOne(id);
    }


    public IndicatorEntity create(String name, long operatorId, String operands) {
        IndicatorEntity indicatorEntity;

        // transaction
        TransactionStatus txStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            // indicator
            indicatorEntity = new IndicatorEntity(name, operatorId, operands);
            if (indicatorDao.insert(indicatorEntity) < 1) {
                throw new SqlException("origin indicator insert failed");
            }
            long indicatorEntityId = indicatorEntity.getId();
            // expression
            List<Token> operandTokenList = lexerService.tokenListOf(operands);
            Token token = new Token(name, ApolloType.EXTERNAL_OPERATOR, indicatorEntityId, operandTokenList);
            OriginalExpressionEntity originalExpressionEntity = new OriginalExpressionEntity(indicatorEntityId, lexerService.jsonOf(token));
            if (originalExpressionDao.insert(originalExpressionEntity) < 1) {
                throw new SqlException("origin expression insert failed");
            }
        } catch (Exception e) {
            transactionManager.rollback(txStatus);
            throw e;
        }
        transactionManager.commit(txStatus);

        return indicatorEntity;
    }
}
