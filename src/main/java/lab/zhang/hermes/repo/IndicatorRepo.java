package lab.zhang.hermes.repo;

import lab.zhang.hermes.dao.IndicatorDao;
import lab.zhang.hermes.dao.IndicatorIndicatorRelationDao;
import lab.zhang.hermes.entity.BaseEntity;
import lab.zhang.hermes.entity.indicator.IndicatorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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


    public List<IndicatorEntity> getList() {
        return indicatorDao.findAll();
    }


    private List<IndicatorEntity> getList(List<Long> idList) {
        Map<String, Object> condition = new HashMap<>(0);
        condition.put("ids", idList);
        return indicatorDao.findByCondition(condition);
    }


    public Map<Long, IndicatorEntity> getListIndexById(List<Long> idList) {
        List<IndicatorEntity> indicatorEntityList = getList(idList);
        if (indicatorEntityList == null) {
            return null;
        }
        return indexById(indicatorEntityList);
    }


    public List<Long> getIdList(List<Long> idList) {
        List<IndicatorEntity> indicatorEntityList = getList(idList);
        if (indicatorEntityList == null) {
            return null;
        }
        return columnOf(indicatorEntityList, BaseEntity::getId);
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


    public Long create(String name, long operatorId, List<Long> childrenIdList) {
        IndicatorEntity indicatorEntity = new IndicatorEntity(name, operatorId, "");
        List<IndicatorEntity> children = getList(childrenIdList);
        indicatorEntity.setChildren(children);

        int count = indicatorDao.insert(indicatorEntity);
        if (count < 1) {
            return null;
        }
        return indicatorEntity.getId();
    }
}
