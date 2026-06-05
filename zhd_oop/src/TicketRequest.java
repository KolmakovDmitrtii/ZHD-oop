class TicketRequest {
    private Passenger passenger;
    private int trainNumber;
    private int date;
    private Station from;
    private Station destination;
    private String wagonType;
    private boolean needRestaurant;
    private boolean needBuffet;
    private boolean needTv;
    private boolean needPhone;
    private boolean needLiePlace;

    public TicketRequest(Passenger passenger, int trainNumber, int date,
                         Station from, Station destination, String wagonType,
                         boolean needRestaurant, boolean needBuffet,
                         boolean needTv, boolean needPhone, boolean needLiePlace) {
        this.passenger = passenger;
        this.trainNumber = trainNumber;
        this.date = date;
        this.from = from;
        this.destination = destination;
        this.wagonType = wagonType;
        this.needRestaurant = needRestaurant;
        this.needBuffet = needBuffet;
        this.needTv = needTv;
        this.needPhone = needPhone;
        this.needLiePlace = needLiePlace;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public int getTrainNumber() {
        return trainNumber;
    }

    public int getDate() {
        return date;
    }

    public Station getFrom() {
        return from;
    }

    public Station getDestination() {
        return destination;
    }

    public String getWagonType() {
        return wagonType;
    }

    public boolean isNeedRestaurant() {
        return needRestaurant;
    }

    public boolean isNeedBuffet() {
        return needBuffet;
    }

    public boolean isNeedTv() {
        return needTv;
    }

    public boolean isNeedPhone() {
        return needPhone;
    }

    public boolean isNeedLiePlace() {
        return needLiePlace;
    }
}