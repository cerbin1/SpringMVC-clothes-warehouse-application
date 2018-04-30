package warehouse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.StringUtils;
import warehouse.domain.Item;
import warehouse.domain.repository.ItemRepository;
import warehouse.exception.ArchivedNotFoundException;
import warehouse.exception.ItemNotFoundException;
import warehouse.exception.ItemWithIdExistException;

import java.util.List;

@Service
public class ItemServiceImplementation implements ItemService {
    private final ItemRepository itemRepository;

    @Autowired
    public ItemServiceImplementation(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public List<Item> getAllItems() {
        return itemRepository.getAllItems();
    }

    @Override
    public Item getItemById(String itemId) {
        return itemRepository.getItemById(itemId);
    }

    @Override
    public Item getItemByName(String name) {
        return itemRepository.getItemByName(name);
    }

    @Override
    public List<Item> getItemsByCategory(String category) {
        return itemRepository.getItemsByCategory(category);
    }

    @Override
    public List<Item> getItemsByColor(String color) {
        return itemRepository.getItemsByColor(color);
    }

    @Override
    public List<Item> getItemsBySize(String size) {
        return itemRepository.getItemsBySize(size);
    }

    @Override
    public List<Item> getItemsByArchived(String archived) {
        if (StringUtils.isBoolean(archived)) {
            return itemRepository.getItemsByArchived(Boolean.parseBoolean(archived));
        }
        throw new ArchivedNotFoundException();
    }

    @Override
    public void addItem(Item newItem) {
        if (itemWithIdExist(newItem.getItemId())) {
            throw new ItemWithIdExistException();
        }
        itemRepository.addItem(newItem);
    }

    @Override
    public void updateItem(Item item, String itemId) {
        if (itemWithIdExist(itemId)) {
            itemRepository.updateItem(item, itemId);
        } else {
            throw new ItemNotFoundException();
        }
    }

    @Override
    public void delete(String itemId) {
        if (itemWithIdExist(itemId)) {
            itemRepository.delete(itemId);
        } else {
            throw new ItemNotFoundException();
        }
    }

    private boolean itemWithIdExist(String itemId) {
        return getAllItems().stream().filter(item -> item.getItemId().equals(itemId)).findAny().orElse(null) != null;
    }
}
