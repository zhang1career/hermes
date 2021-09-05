package lab.zhang.hermes.repo;

import lab.zhang.hermes.dao.IndicatorDao;
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

    public List<IndicatorEntity> getList() {
        return indicatorDao.findAll();
    }

    public IndicatorEntity getItem(long id) {
        return indicatorDao.findOne(id);
    }

    public Long create(IndicatorEntity indicatorEntity) {
        return indicatorDao.insert(indicatorEntity);
    }
}
