package Hongik_SafeMap_Server.vo;

public enum DisasterReportEvaluationType {
    HELPFUL("도움됨"),
    NOT_HELPFUL("도움 안됨"),
    ACCURATE("정확함"),
    FALSE_REPORT("허위제보");

    private final String description;

    DisasterReportEvaluationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}