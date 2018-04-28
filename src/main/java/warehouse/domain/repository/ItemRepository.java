package warehouse.domain.repository;

import warehouse.domain.Item;

import java.util.List;

public interface ItemRepository {
    List<Item> getAllItems();

    Item getItemById(String itemId);

    Item getItemByName(String name);

    List<Item> getItemsByCategory(String category);

    List<Item> getItemsByColor(String color);

    List<Item> getItemsBySize(String size);

    List<Item> getItemsByArchived(boolean archived);

    void addItem(Item newItem);

    void updateItem(Item item, String itemId);

    void delete(String itemId);
}
