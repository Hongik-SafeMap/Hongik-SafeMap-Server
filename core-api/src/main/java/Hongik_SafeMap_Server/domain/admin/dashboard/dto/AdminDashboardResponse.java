package Hongik_SafeMap_Server.domain.admin.dashboard.dto;

public record AdminDashboardResponse(
        long totalReports,
        long credibleUsers,
        long totalUsers
) {
    public static AdminDashboardResponse of(long totalReports, long credibleUsers, long totalUsers) {
        return new AdminDashboardResponse(totalReports, credibleUsers, totalUsers);
    }
}
