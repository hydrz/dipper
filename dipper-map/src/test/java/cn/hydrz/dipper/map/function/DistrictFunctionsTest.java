package cn.hydrz.dipper.map.function;

import cn.hydrz.dipper.map.model.District;
import cn.hydrz.dipper.map.model.DistrictLevel;
import org.junit.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

/**
 * @author hydrz
 * @email n.haoyuan@gmail.com
 * @date 2019/11/1 下午2:36
 */
public class DistrictFunctionsTest {
    private static final WKTReader wkt = new WKTReader(new GeometryFactory());

    @Test
    public void initData() {
        DistrictFunctions.initData();
    }

    @Test
    public void getDistrictByPoint() throws ParseException {
        Point point = (Point) wkt.read("POINT(119.319972 26.060499)");
        District districtByPoint = DistrictFunctions.getDistrictByPoint(point, DistrictLevel.DISTRICT);
        System.out.println(districtByPoint);
    }
}