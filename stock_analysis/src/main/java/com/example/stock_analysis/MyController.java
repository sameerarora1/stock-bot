package com.example.stock_analysis;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MyController {

    @GetMapping("/")
    public String showOutput(Model model) {
        model.addAttribute("stockData", "");
        return "index";
    }

    @PostMapping("/getStock")
    public String getStockData(@RequestParam String stockSymbol, Model model) {
        Analyzer stockAnalysis = new Analyzer();
        String output = "";
        try {
            stockSymbol = stockSymbol.toUpperCase();
            stockAnalysis.analyze(stockSymbol);
        } catch (Exception e) {
            e.printStackTrace();
            output = "Not a valid stock ticker, please re-enter";
        }
        output = stockAnalysis.getOutput();
        model.addAttribute("stockData", output);
        return "index";
    }
}
