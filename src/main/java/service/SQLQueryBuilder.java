package service;

import org.springframework.stereotype.Component;

/**
 * Created by Sebastian Urbanek on 26.10.16.
 */
@Component
public class SQLQueryBuilder {

    public static String getGridQuery(int year, int month, int day, int hour) {
        String databaseName = getDatabaseName(0);
        String s_day = checkDateComponent(day);
        String s_month = checkDateComponent(month);
        String s_hour = checkDateComponent(hour);
        return "SELECT * FROM " + databaseName + " WHERE time='" +
                year + "-" + s_month + "-" + s_day + " " + s_hour +
                ":00:00';";
    }

    public static String getGridQuery(int year, int month, int day, int hour, int resolution) {
        String databaseName = getDatabaseName(resolution);
        String s_day = checkDateComponent(day);
        String s_month = checkDateComponent(month);
        String s_hour = checkDateComponent(hour);
        return "SELECT * FROM " + databaseName + " WHERE time='" +
                year + "-" + s_month + "-" + s_day + " " + s_hour +
                ":00:00';";
    }

    private static String checkDateComponent(int component) {
        if (component < 10) {
            return "0" + component;
        } else {
            return "" + component;
        }
    }

    private static String getDatabaseName(int resolution) {
        String databaseName;
        switch (resolution) {
            case 10:   databaseName = "heatmap_data_10";   break;
            case 100:  databaseName = "heatmap_data_100";  break;
            case 1000: databaseName = "heatmap_data_1000"; break;
            case 20:   databaseName = "heatmap_data_20";   break;
            case 200:  databaseName = "heatmap_data_200";  break;
            default:   databaseName = "heatmap_data_100";  break;
        }
        return databaseName;
    }
}
