package warehouse.service;

import warehouse.domain.Item;

import java.util.List;

public interface ItemService {
    List<Item> getAllItems();

    Item getItemById(String itemId);
}
