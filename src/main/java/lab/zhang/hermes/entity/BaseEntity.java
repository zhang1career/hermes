package lab.zhang.hermes.entity;

/**
 * @author zhangrj
 */
abstract public class BaseEntity implements Identical {
    protected long id;

    @Override
    public long getId() {
        return id;
    }
}
