package Hongik_SafeMap_Server.util;

public class DistanceUtil {

    private static final double EARTH_RADIUS_M = 6371000;

    // Haversine 공식을 사용하여 두 지점 간의 거리를 미터 단위로 계산
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double deltaLat = Math.toRadians(lat2 - lat1);
        double deltaLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_M * c;
    }

    // 주어진 반경 내에 위치가 있는지 확인
    public static boolean isWithinRadius(double lat1, double lon1, double lat2, double lon2, double radiusMeters) {
        return calculateDistance(lat1, lon1, lat2, lon2) <= radiusMeters;
    }
}