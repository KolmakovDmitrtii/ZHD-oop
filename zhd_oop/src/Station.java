class Station {
    private String name;
    private double x;
    private double y;

    public Station(String name, double x, double y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double distanceTo(Station other) {
        double dx = this.x - other.getX();
        double dy = this.y - other.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
}