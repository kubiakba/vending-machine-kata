package pl.bk.kata;

enum ProductLocation {
    FIRST_FLOOR_FIRST_PLACE(1,11),
    FIRST_FLOOR_SECOND_PLACE(1,12),
    FIRST_FLOOR_THIRD_PLACE(1,13),
    FIRST_FLOOR_FOURTH_PLACE(1,14),
    SECOND_FLOOR_FIRST_PLACE(2,21),
    SECOND_FLOOR_SECOND_PLACE(2,22),
    SECOND_FLOOR_THIRD_PLACE(2,23),
    SECOND_FLOOR_FOURTH_PLACE(2,24);

    private final int floor;
    private final int place;

    ProductLocation(int floor, int place) {
        this.floor = floor;
        this.place = place;
    }

    public int getFloor() {
        return floor;
    }

    public int getPlace() {
        return place;
    }
}
