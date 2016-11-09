package web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import model.Cell;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;
import service.DatabaseService;
import service.SQLQueryBuilder;

@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/v1")
@Api(value = "/v1")
public class HeatmapController {

    @RequestMapping(value = "/servicetest", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(
            value = "Simple test if service works.",
            response = String.class,
            produces = "plain/text")
    String serviceTest() {
        return "Service works.";
    }

    @RequestMapping(value = "/dbtest", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(
            value = "Provides one entry out of the database.",
            response = Cell.class,
            produces = "application/json")
    Cell[] databaseTest() {
        return new DatabaseService().startRequest();
    }

    @CrossOrigin
    @RequestMapping(value = "/{year}/{month}/{day}/{hour}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(
            value = "Provides a grid by given parameters.",
            response = Cell.class,
            produces = "application/json")
    Cell[] getGrid(@ApiParam(name="year", value="Year", defaultValue="2014")
                   @PathVariable int year,
                   @ApiParam(name="month", value="Month", defaultValue="12")
                   @PathVariable int month,
                   @ApiParam(name="day", value="Day", defaultValue="24")
                   @PathVariable int day,
                   @ApiParam(name="hour", value="Hour xx:00 - xx:59", defaultValue="18")
                   @PathVariable int hour) {
        // Zusammensetzen der SQL Anfrage
        String sqlQuery = SQLQueryBuilder.getGridQuery(year, month, day, hour);
        // Anfrage absenden und an Client zurückliefern
        System.out.println(sqlQuery);
        return new DatabaseService(sqlQuery).startRequest();
    }

    @CrossOrigin
    @RequestMapping(value = "/{year}/{month}/{day}/{hour}/{resolution}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(
            value = "Provides a grid by given parameters.",
            response = Cell.class,
            produces = "application/json")
    Cell[] getGrid(@ApiParam(name="year", value="Year", defaultValue="2014")
                   @PathVariable int year,
                   @ApiParam(name="month", value="Month", defaultValue="12")
                   @PathVariable int month,
                   @ApiParam(name="day", value="Day", defaultValue="24")
                   @PathVariable int day,
                   @ApiParam(name="hour", value="Hour xx:00 - xx:59", defaultValue="18")
                   @PathVariable int hour,
                   @ApiParam(name="resolution", value="10, 20, 100, 200 or 1000", defaultValue="100")
                   @PathVariable int resolution) {
        // Zusammensetzen der SQL Anfrage
        String sqlQuery = SQLQueryBuilder.getGridQuery(year, month, day, hour, resolution);
        System.out.println(sqlQuery);
        // Anfrage absenden und an Client zurückliefern
        return new DatabaseService(sqlQuery).startRequest();
    }
}
