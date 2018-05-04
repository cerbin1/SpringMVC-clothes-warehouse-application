package warehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import warehouse.domain.Item;
import warehouse.exception.ArchivedNotFoundException;
import warehouse.exception.ItemNotFoundException;
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

    @GetMapping("/items")
    public List<Item> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping("/items/item/{itemId:[0-9]+}")
    public Item getItemById(@PathVariable String itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping("/items/item/{name:[a-zA-Z]+(?:\\s[a-zA-Z]+)*}")
    public Item getItemByName(@PathVariable String name) {
        return itemService.getItemByName(name);
    }

    @GetMapping("/items/category/{category}")
    public List<Item> getItemByCategory(@PathVariable String category) {
        return itemService.getItemsByCategory(category);
    }

    @GetMapping("/items/color/{color}")
    public List<Item> getItemsByColor(@PathVariable String color) {
        return itemService.getItemsByColor(color);
    }

    @GetMapping("/items/size/{size}")
    public List<Item> getItemsBySize(@PathVariable String size) {
        return itemService.getItemsBySize(size);
    }

    @GetMapping("/items/archived/{archived}")
    public ResponseEntity<List<Item>> getItemsByArchived(@PathVariable String archived) {
        try {
            return new ResponseEntity<>(itemService.getItemsByArchived(archived), HttpStatus.OK);
        } catch (ArchivedNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/items")
    public ResponseEntity create(@RequestBody Item item) {
        if (item == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        try {
            itemService.addItem(item);
        } catch (ItemWithIdExistException exception) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PutMapping("/items/item/{itemId:[0-9]+}")
    public ResponseEntity update(@RequestBody Item item,
                                 @PathVariable String itemId) {
        if (item == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        try {
            itemService.updateItem(item, itemId);
        } catch (ItemNotFoundException exception) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/items/item/{itemId:[0-9]+}")
    public ResponseEntity delete(@PathVariable String itemId) {
        try {
            itemService.delete(itemId);
        } catch (ItemNotFoundException exception) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
