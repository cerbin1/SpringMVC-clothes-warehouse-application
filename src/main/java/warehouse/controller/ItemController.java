package warehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import util.StringUtils;
import warehouse.domain.Item;
import warehouse.exception.ArchivedNotFoundException;
import warehouse.service.ItemService;

import java.util.List;

@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;

    @RequestMapping("items")
    public @ResponseBody
    List<Item> getAllItems() {
        return itemService.getAllItems();
    }

    @RequestMapping("items/{itemId:[0-9]+}")
    public @ResponseBody
    Item getItemById(@PathVariable String itemId) {
        return itemService.getItemById(itemId);
    }

    @RequestMapping("items/{name:[a-zA-Z]+}")
    @ResponseBody
    public Item getItemsByName(@PathVariable String name) {
        return itemService.getItemByName(name);
    }

    @RequestMapping("items/category/{category}")
    @ResponseBody
    public List<Item> getItemByCategory(@PathVariable String category) {
        return itemService.getItemsByCategory(category);
    }

    @RequestMapping("items/color/{color}")
    @ResponseBody
    public List<Item> getItemsByColor(@PathVariable String color) {
        return itemService.getItemsByColor(color);
    }

    @RequestMapping("items/size/{size}")
    @ResponseBody
    public List<Item> getItemsBySize(@PathVariable String size) {
        return itemService.getItemsBySize(size);
    }

    @RequestMapping("items/archived/{archived}")
    @ResponseBody
    public List<Item> getItemsByArchived(@PathVariable String archived) {
        if (StringUtils.isBoolean(archived)) {
            return itemService.getItemsByArchived(Boolean.parseBoolean(archived));
        } else {
            throw new ArchivedNotFoundException();
        }
    }
}
