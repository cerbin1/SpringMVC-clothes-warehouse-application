package warehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import warehouse.domain.Item;
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
    public List<Item> getItemsByName(@PathVariable String name) {
        return itemService.getItemsByName(name);
    }

    @RequestMapping("items/category/{category}")
    @ResponseBody
    public List<Item> getItemByCategory(@PathVariable String category) {
        return itemService.getItemsByCategory(category);
    }
}
