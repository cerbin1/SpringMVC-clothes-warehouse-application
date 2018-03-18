package warehouse;

import java.util.List;

public class Warehouse {
    private String name;
    private List<Item> items;

    public Warehouse(String name, List<Item> items) {
        this.name = name;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Item getItemWithId(int id) {
        return items
                .stream()
                .filter(item -> item.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
