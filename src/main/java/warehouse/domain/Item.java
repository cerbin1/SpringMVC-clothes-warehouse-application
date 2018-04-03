package warehouse.domain;

public class Item {
    private static int nextId = 0;

    private int id;
    private String name;
    private int quantity;

    public Item(String name, int quantity) {
        this.id = getNextId();
        this.name = name;
        this.quantity = quantity;
    }

    public Item(int id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public static int getNextId() {
        return nextId++;
    }
}
