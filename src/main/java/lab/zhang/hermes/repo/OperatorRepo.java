package lab.zhang.hermes.repo;

import lab.zhang.hermes.dao.OperatorDao;
import lab.zhang.hermes.entity.indicator.IndicatorEntity;
import lab.zhang.hermes.entity.operator.OperatorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhangrj
 */
@Repository
public class OperatorRepo {

    @Autowired
    private OperatorDao operatorDao;


    public List<OperatorEntity> getList() {
        return operatorDao.findAll();
    }

    public OperatorEntity getItem(long id) {
        return operatorDao.findOne(id);
    }

    public Long create(OperatorEntity operatorEntity) {
        int count = operatorDao.insert(operatorEntity);
        if (count < 1) {
            return null;
        }
        return operatorEntity.getId();
    }
}
