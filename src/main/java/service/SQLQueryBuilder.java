package service;

import org.springframework.stereotype.Component;

/**
 * Created by Sebastian Urbanek on 26.10.16.
 */
@Component
public class SQLQueryBuilder {

    public static String getGridQuery(int year, int month, int day, int hour) {
        return "SELECT * FROM heatmap_data WHERE time='" +
                year + "-" + month + "-" + day + " " + hour +
                ":00:00';";
    }
}
