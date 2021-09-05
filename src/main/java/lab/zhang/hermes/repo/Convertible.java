package lab.zhang.hermes.repo;

/**
 * @author zhangrj
 */
public interface Convertible<P, R> {
    R covertFrom(P param);
}
