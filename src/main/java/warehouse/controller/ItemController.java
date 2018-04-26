package warehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import util.StringUtils;
import warehouse.domain.Item;
import warehouse.exception.ArchivedNotFoundException;
import warehouse.exception.ItemWithIdExistException;
import warehouse.service.ItemService;

import java.util.List;

@RestController
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("items")
    public List<Item> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping("items/{itemId:[0-9]+}")
    public Item getItemById(@PathVariable String itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping("items/{name:[a-zA-Z]+}")
    public Item getItemsByName(@PathVariable String name) {
        return itemService.getItemByName(name);
    }

    @GetMapping("items/category/{category}")
    public List<Item> getItemByCategory(@PathVariable String category) {
        return itemService.getItemsByCategory(category);
    }

    @GetMapping("items/color/{color}")
    public List<Item> getItemsByColor(@PathVariable String color) {
        return itemService.getItemsByColor(color);
    }

    @GetMapping("items/size/{size}")
    public List<Item> getItemsBySize(@PathVariable String size) {
        return itemService.getItemsBySize(size);
    }

    @GetMapping("items/archived/{archived}")
    public List<Item> getItemsByArchived(@PathVariable String archived) {
        if (StringUtils.isBoolean(archived)) {
            return itemService.getItemsByArchived(Boolean.parseBoolean(archived));
        } else {
            throw new ArchivedNotFoundException();
        }
    }

    @PostMapping("items/add")
    public ResponseEntity createItem(@RequestBody Item newItem) {
        try {
            itemService.addItem(newItem);
        } catch (ItemWithIdExistException exception) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
