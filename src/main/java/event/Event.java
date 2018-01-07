package main.java.event;

public enum Event {
    FIFTY_FREE, HUNDRED_FREE, TWO_HUNDRED_FREE, FIVE_HUNDRED_FREE, THOUSAND_FREE, SIXTEEN_FIFTY_FREE,
    FIFTY_BACK, HUNDRED_BACK, TWO_HUNDRED_BACK,
    FIFTY_BREAST, HUNDRED_BREAST, TWO_HUNDRED_BREAST,
    FIFTY_FLY, HUNDRED_FLY, TWO_HUNDRED_FLY,
    HUNDRED_IM, TWO_HUNDRED_IM, FOUR_HUNDRED_IM;

    public String eventToTable() {
        switch (this) {
            case FIFTY_FREE:
                return "50_free";
            case HUNDRED_FREE:
                return "100_free";
            case TWO_HUNDRED_FREE:
                return "200_free";
            case FIVE_HUNDRED_FREE:
                return "500_free";
            case THOUSAND_FREE:
                return "1000_free";
            case SIXTEEN_FIFTY_FREE:
                return "1650_free";
            case FIFTY_BACK:
                return "50_back";
            case HUNDRED_BACK:
                return "100_back";
            case TWO_HUNDRED_BACK:
                return "200_back";
            case FIFTY_BREAST:
                return "50_breast";
            case HUNDRED_BREAST:
                return "100_breast";
            case TWO_HUNDRED_BREAST:
                return "200_breast";
            case FIFTY_FLY:
                return "50_fly";
            case HUNDRED_FLY:
                return "100_fly";
            case TWO_HUNDRED_FLY:
                return "200_fly";
            case HUNDRED_IM:
                return "100_im";
            case TWO_HUNDRED_IM:
                return "200_im";
            case FOUR_HUNDRED_IM:
                return "400_im";
            default:
                throw new IllegalArgumentException("Unexpected event");
        }
    }
}
