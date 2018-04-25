package warehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.StringUtils;
import warehouse.domain.Item;
import warehouse.exception.ArchivedNotFoundException;
import warehouse.service.ItemService;

import java.util.List;

@RestController
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @RequestMapping("items")
    public List<Item> getAllItems() {
        return itemService.getAllItems();
    }

    @RequestMapping("items/{itemId:[0-9]+}")
    public Item getItemById(@PathVariable String itemId) {
        return itemService.getItemById(itemId);
    }

    @RequestMapping("items/{name:[a-zA-Z]+}")
    public Item getItemsByName(@PathVariable String name) {
        return itemService.getItemByName(name);
    }

    @RequestMapping("items/category/{category}")
    public List<Item> getItemByCategory(@PathVariable String category) {
        return itemService.getItemsByCategory(category);
    }

    @RequestMapping("items/color/{color}")
    public List<Item> getItemsByColor(@PathVariable String color) {
        return itemService.getItemsByColor(color);
    }

    @RequestMapping("items/size/{size}")
    public List<Item> getItemsBySize(@PathVariable String size) {
        return itemService.getItemsBySize(size);
    }

    @RequestMapping("items/archived/{archived}")
    public List<Item> getItemsByArchived(@PathVariable String archived) {
        if (StringUtils.isBoolean(archived)) {
            return itemService.getItemsByArchived(Boolean.parseBoolean(archived));
        } else {
            throw new ArchivedNotFoundException();
        }
    }
}
