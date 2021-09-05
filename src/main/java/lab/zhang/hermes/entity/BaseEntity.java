package lab.zhang.hermes.entity;

/**
 * @author zhangrj
 */
abstract public class BaseEntity implements Identical {
    protected Long id;

    @Override
    public Long getId() {
        return id;
    }
}
