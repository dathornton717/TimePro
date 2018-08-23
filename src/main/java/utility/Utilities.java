package utility;

import event.Event;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Utilities {

    public static Event stringToEvent(String s) {
        switch (s) {
            case "50Free":
                return Event.FIFTY_FREE;
            case "100Free":
                return Event.HUNDRED_FREE;
            case "200Free":
                return Event.TWO_HUNDRED_FREE;
            case "500Free":
                return Event.FIVE_HUNDRED_FREE;
            case "1000Free":
                return Event.THOUSAND_FREE;
            case "1650Free":
                return Event.SIXTEEN_FIFTY_FREE;
            case "50Back":
                return Event.FIFTY_BACK;
            case "100Back":
                return Event.HUNDRED_BACK;
            case "200Back":
                return Event.TWO_HUNDRED_BACK;
            case "50Breast":
                return Event.FIFTY_BREAST;
            case "100Breast":
                return Event.HUNDRED_BREAST;
            case "200Breast":
                return Event.TWO_HUNDRED_BREAST;
            case "50Fly":
                return Event.FIFTY_FLY;
            case "100Fly":
                return Event.HUNDRED_FLY;
            case "200Fly":
                return Event.TWO_HUNDRED_FLY;
            case "100IM":
                return Event.HUNDRED_IM;
            case "200IM":
                return Event.TWO_HUNDRED_IM;
            case "400IM":
                return Event.FOUR_HUNDRED_IM;
            default:
                throw new IllegalArgumentException("Unexpected event");
        }
    }

    public static String stackTraceToString(Exception e) {
        StringWriter result = new StringWriter();
        e.printStackTrace(new PrintWriter(result));
        return result.toString();
    }
}
