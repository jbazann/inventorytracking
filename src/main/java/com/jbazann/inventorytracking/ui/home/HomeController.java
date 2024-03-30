package com.jbazann.inventorytracking.ui.home;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jbazann.inventorytracking.ui.BuildManifest;
import com.jbazann.inventorytracking.ui.inventoryview.InventoryViewItemDTO;
import com.jbazann.inventorytracking.ui.inventoryview.InventoryViewService;

@Controller
public class HomeController {

    @Autowired
    private BuildManifest manifest;
    @Autowired
    private InventoryViewService inventoryViewService;

    private record Filter(String id, String displayText) {}

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("script", manifest.get("index.js"));
        model.addAttribute("stylesheet", manifest.getAt("styles.css","dist/css/"));

        model.addAttribute("filters", List.of(
            new Filter("sample-filter-one","Sample Filter 1"),
            new Filter("sample-filter-two", "Sample Filter 2")
        ));

        return "index";
    }

    @GetMapping("/inventory/issues") 
    @ResponseBody
    public List<InventoryViewItemDTO> getIssues() {
        return inventoryViewService.getIssues(0, 20);//TODO defaults
    }

}
