class Wagon {
    private int number;
    private String type;
    private boolean tv;
    private boolean phone;

    public Wagon(int number, String type, boolean tv, boolean phone) {
        this.number = number;
        this.type = type;
        this.tv = tv;
        this.phone = phone;
    }

    public int getNumber() {
        return number;
    }

    public String getType() {
        return type;
    }

    public boolean isTv() {
        return tv;
    }

    public boolean isPhone() {
        return phone;
    }

    public String info() {
        String s = "Вагон №" + number + " [" + type + "]";
        if (tv) {
            s += " TV";
        }
        if (phone) {
            s += " Телефон";
        }
        return s;
    }
}