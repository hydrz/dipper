package cn.hydrz.dipper.map.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.db.Db;
import cn.hutool.db.ds.simple.SimpleDataSource;
import lombok.Getter;

import java.sql.SQLException;

/**
 * @author hydrz
 */
public class H2Util {
    private static final String DB_PATH = H2Util.class.getClassLoader().getResource("/database/h2.mv.db").toString();
    private static final String SCHEMA_PATH = H2Util.class.getClassLoader().getResource("/database/schema.sql").toString();

    @Getter
    private static final SimpleDataSource ds = new SimpleDataSource("jdbc:h2:" + DB_PATH.replace(".mv.db", "") + ";DATABASE_TO_UPPER=false", null, null);

    /**
     * 初始化H2数据库
     */
    public static void init() {
        String create = FileUtil.readString(SCHEMA_PATH, CharsetUtil.CHARSET_UTF_8);
        try {
            Db.use(getDs()).execute(create);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
