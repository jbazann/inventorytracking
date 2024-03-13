package com.jbazann.inventorytracking.ui.home;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jbazann.inventorytracking.ui.BuildManifest;

@Controller
public class HomeController {

    @Autowired
    private BuildManifest manifest;

    private record Filter(String id, String displayText) {}

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("script", manifest.get("index.js"));
        model.addAttribute("stylesheet", manifest.getAt("styles.css","dist/css/"));

        model.addAttribute("dlp_filters", List.of(
            new Filter("dlp-sample-filter-one","Sample Filter 1"),
            new Filter("dlp-sample-filter-two", "Sample Filter 2")
        ));

        return "index";
    }

}
