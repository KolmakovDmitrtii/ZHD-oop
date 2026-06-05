class RejectedRequest {
    private TicketRequest request;
    private String reason;
    private Station station;

    public RejectedRequest(TicketRequest request, String reason, Station station) {
        this.request = request;
        this.reason = reason;
        this.station = station;
    }

    public TicketRequest getRequest() {
        return request;
    }

    public String getReason() {
        return reason;
    }

    public Station getStation() {
        return station;
    }
}