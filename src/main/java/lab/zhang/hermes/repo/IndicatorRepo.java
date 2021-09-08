package lab.zhang.hermes.repo;

import lab.zhang.hermes.dao.IndicatorDao;
import lab.zhang.hermes.dao.IndicatorRelationDao;
import lab.zhang.hermes.entity.indicator.IndicatorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhangrj
 */
@Repository
public class IndicatorRepo {

    @Autowired
    private IndicatorDao indicatorDao;

    @Autowired
    private IndicatorRelationDao indicatorRelationDao;


    public List<IndicatorEntity> getList() {
        return indicatorDao.findAll();
    }

    public IndicatorEntity getItem(long id) {
        IndicatorEntity indicatorEntity = indicatorDao.findOne(id);
        List<IndicatorEntity> children = indicatorRelationDao.findChildren(id);
        indicatorEntity.setChildren(children);
        return  indicatorEntity;
    }

    public Long create(IndicatorEntity indicatorEntity) {
        int count = indicatorDao.insert(indicatorEntity);
        if (count < 1) {
            return null;
        }
        return indicatorEntity.getId();
    }
}
