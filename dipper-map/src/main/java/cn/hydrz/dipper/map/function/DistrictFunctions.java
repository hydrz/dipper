package cn.hydrz.dipper.map.function;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hydrz.dipper.map.constant.AmapConst;
import cn.hydrz.dipper.map.model.District;
import cn.hydrz.dipper.map.model.DistrictLevel;
import cn.hydrz.dipper.map.util.AmapUtil;
import cn.hydrz.dipper.map.util.H2Util;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author hydrz
 */
public class DistrictFunctions {
    private static final Logger log = Logger.getLogger(DistrictFunctions.class.getName());
    private static final WKTReader wkt = new WKTReader(new GeometryFactory());
    private static Integer INIT_ID = 10000;

    /**
     * 获取一个点所在的行政区域
     *
     * @param point 点
     * @param level 行政区域级别
     * @return 行政区域
     */
    public static District getDistrictByPoint(Point point, DistrictLevel level) {
        String sql = "SELECT * FROM district WHERE ST_Within(?, polyline) AND level = ?";

        try {
            List<Entity> list = Db.use(H2Util.getDs()).query(sql, point.toString(), level.getCode());

            List<District> collect = list.stream().map(i -> {
                try {
                    i.set("center", wkt.read(i.getStr("center")));
                    i.set("polyline", wkt.read(i.getStr("polyline")));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                District district = new District();
                BeanUtil.copyProperties(i, district);
                return district;
            }).sorted((i1, i2) ->
                    NumberUtil.compare(point.distance(i1.getCenter()), point.distance(i2.getCenter()))
            ).collect(Collectors.toList());

            return collect.isEmpty() ? null : collect.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 初始化数据
     */
    public static void initData() {
        Map<String, Object> all = getDistrictsList();
        List<Map<String, Object>> list = (List<Map<String, Object>>) all.get("districts");
        ArrayList<District> districts = new ArrayList<>();
        apply(list, 0, districts);

        H2Util.init();

        for (District district : districts) {
            setPolyline(district);
            save(district);
        }
    }

    /**
     * 写入数据库
     *
     * @param district 行政区域
     */
    private static void save(District district) {
        try {
            Entity entity = Entity.create("district");
            BeanUtil.copyProperties(district, entity);
            entity.set("center", Convert.toStr(entity.get("center")));
            entity.set("polyline", Convert.toStr(entity.get("polyline")));
            log.info(entity.toString());
            Db.use(H2Util.getDs()).insert(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 所有行政区域列表
     *
     * @return 列表
     */
    private static Map<String, Object> getDistrictsList() {
        String url = String.format(AmapConst.DISTRICT_ALL, AmapUtil.getWebKey());
        return AmapUtil.get(url);
    }

    /**
     * 数据格式转换
     *
     * @param nodeTree  行政区域树
     * @param parentId  父级ID
     * @param districts 最后得到的列表
     */
    private static void apply(List<Map<String, Object>> nodeTree, Integer parentId, List<District> districts) {
        nodeTree.stream().forEach(i -> {
            try {
                District district = new District();
                String centerStr = MapUtil.get(i, "center", String.class);
                Point center = (Point) wkt.read(String.format("POINT(%s)", centerStr.replace(",", " ")));
                district.setCityCode(ObjectUtil.hasEmpty(i.get("citycode")) ? 0 : MapUtil.get(i, "citycode", Integer.class));
                district.setAdCode(MapUtil.get(i, "adcode", Integer.class));
                district.setName(MapUtil.get(i, "name", String.class));
                district.setLevel(DistrictLevel.getByName(MapUtil.get(i, "level", String.class)).getCode());
                Integer id = getId(district);
                district.setId(id);
                district.setParentId(parentId);
                district.setCenter(center);
                districts.add(district);

                List<Map<String, Object>> children = (List<Map<String, Object>>) i.get("districts");

                if (!children.isEmpty()) {
                    apply(children, id, districts);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 给行政区域设置ID
     *
     * @param district 行政区域
     * @return ID
     */
    private static Integer getId(District district) {
        Integer id = ++INIT_ID;
        return id;
    }

    /**
     * 设置行政区域的polyline
     *
     * @param district 行政区域
     */
    private static void setPolyline(District district) {
        String url = String.format(AmapConst.DISTRICT_POLYLINE, district.getLevel(), district.getName(), AmapUtil.getWebKey());
        Map<String, Object> result = AmapUtil.get(url);

        List<Map<String, Object>> districts = (List<Map<String, Object>>) result.get("districts");

        Map<String, Object> item = districts.get(0);

        if (!ObjectUtil.hasEmpty(item)) {
            try {
                String polylineStr = (String) item.get("polyline");
                polylineStr = ObjectUtil.hasEmpty(polylineStr) ? "" : transformerPolylineToWKT(polylineStr).toString();
                MultiPolygon polyline = (MultiPolygon) wkt.read(polylineStr);
                district.setPolyline(polyline);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 高德格式转wkt格式
     *
     * @param polylineStr 高德返回的polyline
     * @return
     */
    private static StringBuilder transformerPolylineToWKT(String polylineStr) {
        StringBuilder stringBuilder = new StringBuilder(polylineStr);
        stringBuilder.insert(0, "MULTIPOLYGON(((");
        stringBuilder.append(")))");

        int pos = 0;
        int length = stringBuilder.length();

        while (pos < length) {
            char c = stringBuilder.charAt(pos);

            if (CharUtil.equals(c, ',', true)) {
                stringBuilder.replace(pos, pos + 1, " ");
            }

            if (CharUtil.equals(c, ';', true)) {
                stringBuilder.replace(pos, pos + 1, ",");
            }

            if (CharUtil.equals(c, '|', true)) {
                stringBuilder.replace(pos, pos + 1, ")),((");
                pos += 4;
                length += 4;
            }

            pos++;
        }
        return stringBuilder;
    }


}
