package warehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import warehouse.domain.Item;
import warehouse.service.ItemService;

import java.util.List;

@RequestMapping("warehouse")
@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;

    @RequestMapping
    public @ResponseBody
    List<Item> getAllItems() {
        return itemService.getAllItems();
    }
}
