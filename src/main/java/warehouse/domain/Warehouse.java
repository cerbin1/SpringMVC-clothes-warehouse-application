package warehouse.domain;

public class Warehouse {
    private String name;

    public Warehouse() {

    }

    public Warehouse(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
