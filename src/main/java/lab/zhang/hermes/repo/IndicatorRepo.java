package lab.zhang.hermes.repo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lab.zhang.apollo.pojo.ApolloType;
import lab.zhang.apollo.pojo.Token;
import lab.zhang.apollo.service.LexerService;
import lab.zhang.apollo.service.lexer.BasicLexerService;
import lab.zhang.hermes.dao.IndicatorDao;
import lab.zhang.hermes.dao.IndicatorIndicatorDao;
import lab.zhang.hermes.dao.OperatorDao;
import lab.zhang.hermes.dao.OriginalExpressionDao;
import lab.zhang.hermes.entity.expression.OriginalExpressionEntity;
import lab.zhang.hermes.entity.indicator.IndicatorEntity;
import lab.zhang.hermes.exception.SqlException;
import lab.zhang.hermes.util.ArrayUtil;
import lab.zhang.hermes.util.ListUtil;
import lab.zhang.hermes.util.StrUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @author zhangrj
 */
@Repository
public class IndicatorRepo extends BaseRepo {

    @Autowired
    private IndicatorDao indicatorDao;

    @Autowired
    private IndicatorIndicatorDao indicatorIndicatorDao;

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
            // children
            List<Token> operandTokenList = lexerService.tokenListOf(operands);
            List<Long> targetChildrenIdList = getChildrenIdList(operandTokenList);
            // nested children
            Long[] addingChildrenIds = null;
            if (targetChildrenIdList != null && !targetChildrenIdList.isEmpty()) {
                List<Long> existedChildrenIdList = getChildrenIdListForUpdate(indicatorEntity);
                addingChildrenIds = calcAddingChildrenIds(existedChildrenIdList, targetChildrenIdList); // for update
                Map<Long, AtomicLong> nestedChildrenIdsMap = calcNestedChildrenIds(indicatorEntity, addingChildrenIds, null);
                indicatorEntity.setNestedChildrenIds(JSON.toJSONString(nestedChildrenIdsMap));
            }

            // create indicator
            if (indicatorDao.insert(indicatorEntity) < 1) {
                throw new SqlException("origin indicator insert failed");
            }
            long indicatorEntityId = indicatorEntity.getId();
            // create indicator relation
            if (addingChildrenIds != null) {
                indicatorIndicatorDao.insertChildren(indicatorEntity.getId(), addingChildrenIds);
            }
            // create indicator expression
            Token token = new Token(name, ApolloType.EXTERNAL_OPERATOR, operatorId, operandTokenList);
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

    public IndicatorEntity update(long id, String name, long operatorId, String operands) {
        IndicatorEntity indicatorEntity;

        // transaction
        TransactionStatus txStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            // indicator
            indicatorEntity = indicatorDao.selectOneForUpdate(id);
            if (indicatorEntity == null) {
                throw new SqlException("the indicator is not found, id: " + id);
            }
            indicatorEntity.setName(name);
            indicatorEntity.setOperatorId(operatorId);
            indicatorEntity.setOperands(operands);
            // children
            List<Token> operandTokenList = lexerService.tokenListOf(operands);
            List<Long> targetChildrenIdList = getChildrenIdList(operandTokenList);
            // nested children
            Long[] addingChildrenIds = null;
            Long[] deletingChildrenIds = null;
            if (targetChildrenIdList != null) {
                List<Long> existedChildrenIdList = getChildrenIdListForUpdate(indicatorEntity);
                addingChildrenIds = calcAddingChildrenIds(existedChildrenIdList, targetChildrenIdList);
                deletingChildrenIds = calcDeletingChildrenIds(existedChildrenIdList, targetChildrenIdList);
                Map<Long, AtomicLong> nestedChildrenIdsMap = calcNestedChildrenIds(indicatorEntity, addingChildrenIds, deletingChildrenIds);
                indicatorEntity.setNestedChildrenIds(JSON.toJSONString(nestedChildrenIdsMap));
            }

            // update indicator
            indicatorDao.update(indicatorEntity);
            // update indicator relation
            if (!ArrayUtil.isNill(addingChildrenIds)) {
                indicatorIndicatorDao.insertChildren(indicatorEntity.getId(), addingChildrenIds);
            }
            if (!ArrayUtil.isNill(deletingChildrenIds)) {
                indicatorIndicatorDao.deleteChildren(indicatorEntity.getId(), deletingChildrenIds);
            }
            // update indicator expression
            Token token = new Token(name, ApolloType.EXTERNAL_OPERATOR, operatorId, operandTokenList);
            OriginalExpressionEntity originalExpressionEntity = originalExpressionDao.findByIndicatorId(indicatorEntity.getId());
            originalExpressionEntity.setExpression(lexerService.jsonOf(token));
            originalExpressionDao.update(originalExpressionEntity);

            // recursively update parents' childrenIds
            updateParentsChildrenIds(indicatorEntity, addingChildrenIds, deletingChildrenIds);
        } catch (Exception e) {
            transactionManager.rollback(txStatus);
            throw e;
        }
        transactionManager.commit(txStatus);

        return indicatorEntity;
    }

    @Nullable
    private List<Long> getChildrenIdList(List<Token> operandTokenList) {
        List<Long> childrenIdList = null;
        if (operandTokenList != null && !operandTokenList.isEmpty()) {
            childrenIdList = operandTokenList.stream()
                    .filter(e -> e.getType() == ApolloType.ORIGINAL_OPERATION)
                    .map(Token::getId)
                    .collect(Collectors.toList());
        }
        return childrenIdList;
    }

    private void updateParentsChildrenIds(@NotNull IndicatorEntity indicatorEntity, Long[] addIds, Long[] delIds) {
        List<IndicatorEntity> parentIndicatorEntityList = getParents(indicatorEntity);

        // check
        if (ListUtil.isNill(parentIndicatorEntityList)) {
            return;
        }

        // update
        Map<Long, AtomicLong> childrenIdsMap = calcNestedChildrenIds(indicatorEntity, addIds, delIds);
        indicatorEntity.setNestedChildrenIds(JSON.toJSONString(childrenIdsMap));
        indicatorDao.update(indicatorEntity);

        // recursively route up
        for (IndicatorEntity parentIndicatorEntity : parentIndicatorEntityList) {
            updateParentsChildrenIds(parentIndicatorEntity, addIds, delIds);
        }
    }

    @NotNull
    private List<IndicatorEntity> getParents(@NotNull IndicatorEntity indicatorEntity) {
        return indicatorIndicatorDao.selectParents(indicatorEntity.getId());
    }

    private List<Long> getChildrenIdListForUpdate(@NotNull IndicatorEntity indicatorEntity) {
        List<IndicatorEntity> childrenList = indicatorIndicatorDao.selectChildrenForUpdate(indicatorEntity.getId());
        if (ListUtil.isNill(childrenList)) {
            return null;
        }
        return columnOf(childrenList, IndicatorEntity::getId);
    }

    @NotNull
    private Long[] calcAddingChildrenIds(List<Long> childrenIdList, @NotNull List<Long> targetChildrenIdList) {
        if (childrenIdList == null || childrenIdList.isEmpty()) {
            return targetChildrenIdList.toArray(new Long[0]);
        }

        return targetChildrenIdList.stream()
                .filter(e -> !childrenIdList.contains(e)).toArray(Long[]::new);
    }

    @Nullable
    private Long[] calcDeletingChildrenIds(@NotNull List<Long> childrenIdList, List<Long> targetChildrenIdList) {
        return childrenIdList.stream()
                .filter(e -> !targetChildrenIdList.contains(e)).toArray(Long[]::new);
    }

    @Nullable
    private Map<Long, AtomicLong> calcNestedChildrenIds(@NotNull IndicatorEntity indicatorEntity, Long[] addIds, Long[] delIds) {
        if (ArrayUtil.isNill(addIds) && ArrayUtil.isNill(delIds)) {
            return null;
        }

        ConcurrentMap<Long, AtomicLong> nestedChildrenIdsMap = getNestedChildrenIdConcurrentMap(indicatorEntity);
        if (nestedChildrenIdsMap == null) {
            nestedChildrenIdsMap = new ConcurrentHashMap<>(0);
        }
        if (!ArrayUtil.isNill(addIds)) {
            for (Long childId : addIds) {
                if (!nestedChildrenIdsMap.containsKey(childId)) {
                    nestedChildrenIdsMap.put(childId, new AtomicLong(1));
                    continue;
                }
                nestedChildrenIdsMap.get(childId).incrementAndGet();
            }
        }
        if (!ArrayUtil.isNill(delIds)) {
            for (Long childId : delIds) {
                if (!nestedChildrenIdsMap.containsKey(childId)) {
                    continue;
                }
                if (nestedChildrenIdsMap.get(childId).decrementAndGet() <= 0) {
                    nestedChildrenIdsMap.remove(childId);
                }
            }
        }

        return nestedChildrenIdsMap;
    }

    private Map<Long, Long> getNestedChildrenIdMap(@NotNull IndicatorEntity indicatorEntity) {
        String childrenIdsStr = indicatorEntity.getNestedChildrenIds();
        if (StrUtil.isNill(childrenIdsStr)) {
            childrenIdsStr = "";
        }

        return JSON.parseObject(childrenIdsStr, new TypeReference<Map<Long, Long>>() {
        });
    }

    @Nullable
    private ConcurrentMap<Long, AtomicLong> getNestedChildrenIdConcurrentMap(@NotNull IndicatorEntity indicatorEntity) {
        String nestedChildrenIdsStr = indicatorEntity.getNestedChildrenIds();
        if (StrUtil.isNill(nestedChildrenIdsStr)) {
            return null;
        }

        return JSON.parseObject(nestedChildrenIdsStr, new TypeReference<ConcurrentMap<Long, AtomicLong>>() {
        });
    }
}
