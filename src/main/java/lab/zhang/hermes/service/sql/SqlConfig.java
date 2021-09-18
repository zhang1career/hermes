package lab.zhang.hermes.service.sql;

import lab.zhang.hermes.exception.InvalidParameterException;
import lab.zhang.hermes.util.CastUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

/**
 * @author zhangrj
 */
@Data
@AllArgsConstructor
public class SqlConfig {
    static private final String HOST = "host";
    static private final String PORT = "port";
    static private final String USER = "user";
    static private final String PASS = "pass";
    static private final String DB = "db";

    private String host;
    private int port;
    private String user;
    private String pass;
    private String db;

    @NotNull
    @Contract("_ -> new")
    public static SqlConfig of(@NotNull Map<String, Object> configMap) {
        if (!configMap.containsKey(HOST)) {
            throw new InvalidParameterException("A field of sql config is not found, name: " + HOST);
        }
        String host = CastUtil.from(configMap.get(HOST));

        if (!configMap.containsKey(PORT)) {
            throw new InvalidParameterException("A field of sql config is not found, name: " + PORT);
        }
        int port = CastUtil.from(configMap.get(PORT));

        if (!configMap.containsKey(USER)) {
            throw new InvalidParameterException("A field of sql config is not found, name: " + USER);
        }
        String user = CastUtil.from(configMap.get(USER));

        if (!configMap.containsKey(PASS)) {
            throw new InvalidParameterException("A field of sql config is not found, name: " + PASS);
        }
        String pass = CastUtil.from(configMap.get(PASS));

        if (!configMap.containsKey(DB)) {
            throw new InvalidParameterException("A field of sql config is not found, name: " + DB);
        }
        String db = CastUtil.from(configMap.get(DB));

        return new SqlConfig(host, port, user, pass, db);
    }

    public int hash() {
        return Objects.hash(host, port, db, user);
    }
}
