package lab.zhang.hermes.repo;

import lab.zhang.apollo.repo.StorableOperator;
import lab.zhang.hermes.dao.OperatorDao;
import lab.zhang.hermes.entity.operator.OperatorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhangrj
 */
@Repository
public class OperatorRepo extends BaseRepo implements StorableOperator {

    @Autowired
    private OperatorDao operatorDao;


    public List<OperatorEntity> getList() {
        return operatorDao.findAll();
    }

    public OperatorEntity getItem(long id) {
        return operatorDao.findOne(id);
    }

    public OperatorEntity create(OperatorEntity operatorEntity) {
        int count = operatorDao.insert(operatorEntity);
        if (count < 1) {
            return null;
        }
        return operatorEntity;
    }

    @Override
    public String getClazz(long id) {
        OperatorEntity operatorEntity = getItem(id);
        if (operatorEntity == null) {
            return null;
        }
        return operatorEntity.getClazz();
    }
}
