package warehouse.domain.repository;

import warehouse.domain.Item;

import java.util.List;

public interface ItemRepository {
    List<Item> getAllItems();
}
