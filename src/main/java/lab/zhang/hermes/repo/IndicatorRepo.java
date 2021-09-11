package lab.zhang.hermes.repo;

import lab.zhang.hermes.dao.IndicatorDao;
import lab.zhang.hermes.dao.IndicatorIndicatorRelationDao;
import lab.zhang.hermes.entity.indicator.IndicatorEntity;
import lab.zhang.hermes.exception.SqlException;
import lab.zhang.hermes.util.ListUtil;
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
    private IndicatorIndicatorRelationDao indicatorIndicatorRelationDao;

    @Autowired
    private PlatformTransactionManager transactionManager;


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
        IndicatorEntity indicatorEntity = indicatorDao.findOne(id);
        if (indicatorEntity == null) {
            return null;
        }

        List<IndicatorEntity> children = indicatorIndicatorRelationDao.findChildren(id);
        indicatorEntity.setChildren(children);

        return indicatorEntity;
    }


    public long create(String name, long operatorId, String expression, List<Long> childrenIdList) {
        long indicatorEntityId = 0;

        // prepare
        IndicatorEntity indicatorEntity = new IndicatorEntity(name, operatorId, expression);
        List<IndicatorEntity> indicatorEntityList = getList(childrenIdList);
        indicatorEntity.setChildren(indicatorEntityList);

        // transaction
        TransactionStatus txStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            // indicator table
            if (indicatorDao.insert(indicatorEntity) < 1) {
                throw new SqlException("indicator insert failed");
            }
            indicatorEntityId = indicatorEntity.getId();
            // indicator_indicator table
            indicatorIndicatorRelationDao.insertChildren(indicatorEntityId, childrenIdList);
        } catch (Exception e) {
            transactionManager.rollback(txStatus);
            throw e;
        }
        transactionManager.commit(txStatus);

        return indicatorEntityId;
    }

//    public long create(String name, long operatorId, String expression, List<Long> childrenIdList) {
//        long indicatorEntityId = 0;
//
//        // prepare
//        IndicatorEntity indicatorEntity = new IndicatorEntity(name, operatorId, expression);
//        List<IndicatorEntity> indicatorEntityList = getList(childrenIdList);
//        indicatorEntity.setChildren(indicatorEntityList);
//
//        // transaction
//        TransactionStatus txStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
//        try {
//            // indicator table
//            if (indicatorDao.insert(indicatorEntity) < 1) {
//                throw new SqlException("indicator insert failed");
//            }
//            indicatorEntityId = indicatorEntity.getId();
//            // indicator_indicator table
//            List<Long> insertingChildrenIdList = null;
//            List<IndicatorEntity> existedChildrenEntityList = indicatorIndicatorRelationDao.findChildren(indicatorEntityId);
//            if (!ListUtil.isEmpty(existedChildrenEntityList)) {
//                List<Long> existedChildrenIdList = BaseRepo.columnOf(existedChildrenEntityList, IndicatorEntity::getId);
//                insertingChildrenIdList = ListUtil.diff(childrenIdList, existedChildrenIdList);
//            }
//            if (insertingChildrenIdList != null && insertingChildrenIdList.size() > 0) {
//                indicatorIndicatorRelationDao.insertChildren(indicatorEntityId, insertingChildrenIdList);
//            }
//        } catch (Exception e) {
//            transactionManager.rollback(txStatus);
//            throw e;
//        }
//        transactionManager.commit(txStatus);
//
//        return indicatorEntityId;
//    }
}
