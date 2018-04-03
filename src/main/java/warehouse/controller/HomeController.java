package warehouse.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/")
@Controller
public class HomeController {
    @RequestMapping
    public @ResponseBody
    String defaultMessage() {
        return "Warehouse application";
    }
}
