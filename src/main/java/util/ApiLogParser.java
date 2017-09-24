package util;

import model.ApiLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 20/09/2017.
 */
public class ApiLogParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiLogParser.class);

    // API log line example:
    // ====================
    // 9d311564aec493f23347e848439584e0 :: 08/04/2017 13:47:23 (081),account-id=ABC,gateway-id=6d5d6f2d96c9dc844c839b83925914de,country=GB,status=submitted,price={message-price=0.2759533},cost={transit-cost=0.08, route-cost=0.12}

    // TODO: the pattern and the schema should be parameters to be able to analyze any log line
    //                                  1:messageId 2:date 3:hour   4:var001        5:accountId   6:gatewayId   7:country     8:status                9:messagePrice                 10:transistCost      11:routeCost
    private static String LOG_PATTERN = "^(\\S+) :: (\\S+) (\\S+) \\((\\S+)\\)[^=]+=([^,]+)?[^=]+=([^,]+)?[^=]+=([^,]+)?[^=]+=([^,]+)?[^=]+[={]+[^=]+=(\\d+\\.?\\d+)?[^=]+[={]+[^=]+=(\\d+\\.?\\d+)?[^=]+=(\\d+\\.?\\d+)?}";
    private static final Pattern PATTERN = Pattern.compile(LOG_PATTERN);

    // ApiLog CSV schema
    public static final String LOG_FIELDS_SCHEMA = "messageId|date|hour|var001|accountId|gatewayId|country|status|messagePrice|transistCost|routeCost";

    /**
     * Parse a log line using a regular expression
     * @param logline the log line
     * @return the {@link ApiLog} object that encapsulates one line log
     */
    public static ApiLog parseFromLogLine(String logline) {
        Matcher m = PATTERN.matcher(logline);
        if ( !m.find() ) {
            LOGGER.warn("PARSING-ERROR: Unable to parse log line: " + logline);
            // TODO: do something with the wrong lines (i.e. send to Kafka topic)
            // throw new RuntimeException("Error parsing log line");
        }

        return new ApiLog(m.group(1), m.group(2), m.group(3), m.group(4), m.group(5), m.group(6),
                          m.group(7), m.group(8), m.group(9), m.group(10), m.group(11)
        );
    }

}
