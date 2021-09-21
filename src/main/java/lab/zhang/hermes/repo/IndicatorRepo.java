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
import lab.zhang.hermes.exception.ServiceException;
import lab.zhang.hermes.exception.SqlException;
import lab.zhang.hermes.util.ArrayUtil;
import lab.zhang.hermes.util.ListUtil;
import lab.zhang.hermes.util.MapUtil;
import lab.zhang.hermes.util.StrUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.ArrayList;
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

            // direct children
            List<Token> operandTokenList = lexerService.tokenListOf(operands);
            List<Long> targetChildrenIdList = childrenIdListFrom(operandTokenList);

            // nested children
            Map<Long, Long> targetNestedChildrenIdMap = null;
            if (targetChildrenIdList != null) {
                targetNestedChildrenIdMap = sumNestedChildrenIdMapFrom(targetChildrenIdList);
            }
            if (targetNestedChildrenIdMap != null) {
                indicatorEntity.setNestedChildrenIds(JSON.toJSONString(targetNestedChildrenIdMap));
            }

            // create indicator
            if (indicatorDao.insert(indicatorEntity) < 1) {
                throw new SqlException("origin indicator insert failed");
            }
            long indicatorEntityId = indicatorEntity.getId();
            // create indicator relation
            if (targetChildrenIdList != null) {
                indicatorIndicatorDao.insertChildren(indicatorEntity.getId(), targetChildrenIdList.toArray(new Long[0]));
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

            // direct children
            List<IndicatorEntity> existingChildrenList = getChildren(indicatorEntity);
            List<Long> existingChildrenIdList = null;
            if (!ListUtil.isNill(existingChildrenList)) {
                existingChildrenIdList = columnOf(existingChildrenList, IndicatorEntity::getId);
            }
            List<Token> operandTokenList = lexerService.tokenListOf(operands);
            List<Long> targetChildrenIdList = childrenIdListFrom(operandTokenList);
            // adding/deleting
            Long[] addingChildrenIds = null;
            Long[] deletingChildrenIds = null;
            if (targetChildrenIdList != null) {
                addingChildrenIds = calcAddingChildrenIds(existingChildrenIdList, targetChildrenIdList);
                deletingChildrenIds = calcDeletingChildrenIds(existingChildrenIdList, targetChildrenIdList);
            }

            // nested children
            Map<Long, Long> targetNestedChildrenIdMap = null;
            if (targetChildrenIdList != null) {
                targetNestedChildrenIdMap = sumNestedChildrenIdMapFrom(targetChildrenIdList);
            }
            // loop check
            List<Long> targetNestedChildrenIdList = null;
            if (targetNestedChildrenIdMap != null) {
                targetNestedChildrenIdList = new ArrayList<>(targetNestedChildrenIdMap.keySet());
            }
            if (targetNestedChildrenIdList != null && isNestedChildrenLoop(indicatorEntity, targetNestedChildrenIdList)) {
                throw new ServiceException("loop detected in the nested children");
            }
            // adding/deleting
            Map<Long, Long> addingNestedChildrenMap = null;
            Map<Long, Long> deletingNestedChildrenMap = null;
            if (targetNestedChildrenIdMap != null) {
                Map<Long, Long> existedChildrenIdMap = sumNestedChildrenIdMapFrom(indicatorEntity);
                addingNestedChildrenMap = calcAddingChildrenIdMap(existedChildrenIdMap, targetNestedChildrenIdMap);
                deletingNestedChildrenMap = calcDeletingChildrenIdMap(existedChildrenIdMap, targetNestedChildrenIdMap);
                indicatorEntity.setNestedChildrenIds(JSON.toJSONString(targetNestedChildrenIdMap));
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
            updateParentsChildrenIds(indicatorEntity, addingNestedChildrenMap, deletingNestedChildrenMap);
        } catch (Exception e) {
            transactionManager.rollback(txStatus);
            throw e;
        }
        transactionManager.commit(txStatus);

        return indicatorEntity;
    }

    private boolean isNestedChildrenLoop(IndicatorEntity indicatorEntity, List<Long> nestedChildrenIdList) {
        List<IndicatorEntity> nestedChildrenIndicatorEntityList = getList(nestedChildrenIdList);
        for (IndicatorEntity childIndicatorEntity : nestedChildrenIndicatorEntityList) {
            Map<Long, Long> nestedChildrenIdMap = sumNestedChildrenIdMapFrom(childIndicatorEntity);
            if (nestedChildrenIdMap == null) {
                continue;
            }
            if (nestedChildrenIdMap.containsKey(indicatorEntity.getId())) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    private List<Long> childrenIdListFrom(List<Token> operandTokenList) {
        List<Long> childrenIdList = null;
        if (operandTokenList != null && !operandTokenList.isEmpty()) {
            childrenIdList = operandTokenList.stream()
                    .filter(e -> e.getType() == ApolloType.ORIGINAL_OPERATION)
                    .map(Token::getId)
                    .collect(Collectors.toList());
        }
        return childrenIdList;
    }

    private void updateParentsChildrenIds(@NotNull IndicatorEntity indicatorEntity, Map<Long, Long> addingIdMap, Map<Long, Long> deletingIdMap) {
        List<IndicatorEntity> parentIndicatorEntityList = getParents(indicatorEntity);
        // check
        if (ListUtil.isNill(parentIndicatorEntityList)) {
            return;
        }

        // recursively route up
        for (IndicatorEntity parentIndicatorEntity : parentIndicatorEntityList) {
            // update
            setNestedChildrenIds(parentIndicatorEntity, addingIdMap, deletingIdMap);
            indicatorDao.update(parentIndicatorEntity);
            updateParentsChildrenIds(parentIndicatorEntity, addingIdMap, deletingIdMap);
        }
    }

    @NotNull
    private List<IndicatorEntity> getParents(@NotNull IndicatorEntity indicatorEntity) {
        return indicatorIndicatorDao.selectParents(indicatorEntity.getId());
    }

    @NotNull
    private List<IndicatorEntity> getChildren(@NotNull IndicatorEntity indicatorEntity) {
        return indicatorIndicatorDao.selectChildren(indicatorEntity.getId());
    }

    @NotNull
    private Long[] calcAddingChildrenIds(List<Long> existingChildrenIdList, List<Long> targetChildrenIdList) {
        List<Long> adding = ListUtil.diff(targetChildrenIdList, existingChildrenIdList);
        return adding.toArray(new Long[0]);
    }

    @NotNull
    private Long[] calcDeletingChildrenIds(List<Long> existingChildrenIdList, List<Long> targetChildrenIdList) {
        List<Long> deleting = ListUtil.diff(existingChildrenIdList, targetChildrenIdList);
        return deleting.toArray(new Long[0]);
    }

    @NotNull
    private Map<Long, Long> calcAddingChildrenIdMap(Map<Long, Long> existingChildrenIdMap, @NotNull Map<Long, Long> targetChildrenIdMap) {
        if (existingChildrenIdMap == null || existingChildrenIdMap.isEmpty()) {
            return targetChildrenIdMap;
        }

        Map<Long, Long> addingIdMap = new HashMap<>();
        for (Map.Entry<Long, Long> entry : targetChildrenIdMap.entrySet()) {
            Long id = entry.getKey();
            Long targetCount = entry.getValue();

            if (!existingChildrenIdMap.containsKey(id)) {
                addingIdMap.put(id, targetCount);
                continue;
            }

            Long existingCount = existingChildrenIdMap.get(id);
            if (existingCount >= targetCount) {
                continue;
            }
            addingIdMap.put(id, targetCount - existingCount);
        }

        return addingIdMap;
    }

    @Nullable
    private Map<Long, Long> calcDeletingChildrenIdMap(Map<Long, Long> existingChildrenIdMap, Map<Long, Long> targetChildrenIdMap) {
        if (existingChildrenIdMap == null || existingChildrenIdMap.isEmpty()) {
            return null;
        }

        Map<Long, Long> deletingIdMap = new HashMap<>();
        for (Map.Entry<Long, Long> entry : existingChildrenIdMap.entrySet()) {
            Long id = entry.getKey();
            Long existingCount = entry.getValue();

            if (!targetChildrenIdMap.containsKey(id)) {
                deletingIdMap.put(id, existingCount);
                continue;
            }

            Long targetCount = targetChildrenIdMap.get(id);
            if (existingCount <= targetCount) {
                continue;
            }
            deletingIdMap.put(id, existingCount - targetCount);
        }

        return deletingIdMap;
    }

    private void setNestedChildrenIds(@NotNull IndicatorEntity indicatorEntity, Map<Long, Long> addingIdMap, Map<Long, Long> deletingIdMap) {
        if (MapUtil.isNill(addingIdMap) && MapUtil.isNill(deletingIdMap)) {
            return;
        }

        ConcurrentMap<Long, AtomicLong> nestedChildrenIdsMap = getNestedChildrenIdConcurrentMap(indicatorEntity);
        if (nestedChildrenIdsMap == null) {
            nestedChildrenIdsMap = new ConcurrentHashMap<>(0);
        }
        if (!MapUtil.isNill(addingIdMap)) {
            for (Map.Entry<Long, Long> entry : addingIdMap.entrySet()) {
                long id = entry.getKey();
                long count = entry.getValue();
                if (!nestedChildrenIdsMap.containsKey(id)) {
                    nestedChildrenIdsMap.put(id, new AtomicLong(count));
                    continue;
                }
                nestedChildrenIdsMap.get(id).addAndGet(count);
            }
        }
        if (!MapUtil.isNill(deletingIdMap)) {
            for (Map.Entry<Long, Long> entry : deletingIdMap.entrySet()) {
                long id = entry.getKey();
                long count = entry.getValue();
                if (!nestedChildrenIdsMap.containsKey(id)) {
                    continue;
                }
                if (nestedChildrenIdsMap.get(id).addAndGet(-count) <= 0) {
                    nestedChildrenIdsMap.remove(id);
                }
            }
        }

        indicatorEntity.setNestedChildrenIds(JSON.toJSONString(nestedChildrenIdsMap));
    }

    @Nullable
    private Map<Long, Long> sumNestedChildrenIdMapFrom(List<Long> childrenIdList) {
        if (childrenIdList == null) {
            return null;
        }

        Map<Long, Long> ret = new HashMap<>();
        for (Long id : childrenIdList) {
            ret.put(id, 1L);
        }

        Map<String, Object> condition = new HashMap<>(1);
        condition.put("ids", childrenIdList);
        List<IndicatorEntity> indicatorEntityList = indicatorDao.findByCondition(condition);
        if (indicatorEntityList == null || indicatorEntityList.isEmpty()) {
            return ret;
        }
        for (IndicatorEntity indicatorEntity : indicatorEntityList) {
            Map<Long, Long> nestedChildrenIdMap = sumNestedChildrenIdMapFrom(indicatorEntity);
            if (MapUtil.isNill(nestedChildrenIdMap)) {
                continue;
            }
            for (Map.Entry<Long, Long> entry : nestedChildrenIdMap.entrySet()) {
                long id = entry.getKey();
                long count = entry.getValue();
                if (!ret.containsKey(id)) {
                    ret.put(id, count);
                    continue;
                }
                ret.put(id, ret.get(id) + count);
            }
        }

        return ret;
    }

    @Nullable
    private Map<Long, Long> sumNestedChildrenIdMapFrom(@NotNull IndicatorEntity indicatorEntity) {
        String childrenIdsStr = indicatorEntity.getNestedChildrenIds();
        if (StrUtil.isNill(childrenIdsStr)) {
            return null;
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
