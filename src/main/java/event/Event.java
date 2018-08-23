package event;

import factory.DataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum Event {
    FIFTY_FREE, HUNDRED_FREE, TWO_HUNDRED_FREE, FIVE_HUNDRED_FREE, THOUSAND_FREE, SIXTEEN_FIFTY_FREE,
    FIFTY_BACK, HUNDRED_BACK, TWO_HUNDRED_BACK,
    FIFTY_BREAST, HUNDRED_BREAST, TWO_HUNDRED_BREAST,
    FIFTY_FLY, HUNDRED_FLY, TWO_HUNDRED_FLY,
    HUNDRED_IM, TWO_HUNDRED_IM, FOUR_HUNDRED_IM;

    public String eventToTable() {
        switch (this) {
            case FIFTY_FREE:
                return "free_50";
            case HUNDRED_FREE:
                return "free_100";
            case TWO_HUNDRED_FREE:
                return "free_200";
            case FIVE_HUNDRED_FREE:
                return "free_500";
            case THOUSAND_FREE:
                return "free_1000";
            case SIXTEEN_FIFTY_FREE:
                return "free_1650";
            case FIFTY_BACK:
                return "back_50";
            case HUNDRED_BACK:
                return "back_100";
            case TWO_HUNDRED_BACK:
                return "back_200";
            case FIFTY_BREAST:
                return "breast_50";
            case HUNDRED_BREAST:
                return "breast_100";
            case TWO_HUNDRED_BREAST:
                return "breast_200";
            case FIFTY_FLY:
                return "fly_50";
            case HUNDRED_FLY:
                return "fly_100";
            case TWO_HUNDRED_FLY:
                return "fly_200";
            case HUNDRED_IM:
                return "im_100";
            case TWO_HUNDRED_IM:
                return "im_200";
            case FOUR_HUNDRED_IM:
                return "im_400";
            default:
                throw new IllegalArgumentException("Unexpected event");
        }
    }
}
