package warehouse.service;

import warehouse.domain.Item;

import java.util.List;

public interface ItemService {
    List<Item> getAllItems();

    Item getItemById(String itemId);

    Item getItemByName(String name);

    List<Item> getItemsByCategory(String category);
}
