package myapp.erpnewapp.controller;

import myapp.erpnewapp.model.Quotation;
import myapp.erpnewapp.model.Supplier;
import myapp.erpnewapp.service.ErpNextService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/quotations")
public class QuotationController {

    private final ErpNextService erpNextService;

    public QuotationController(ErpNextService erpNextService) {
        this.erpNextService = erpNextService;
    }

    @GetMapping
    public String showQuotations(
            @RequestParam(required = false) String supplier,
            Model model) {

        List<Supplier> suppliers = erpNextService.getAllSuppliers();
        List<Quotation> quotations = erpNextService.getQuotations(supplier);

        model.addAttribute("suppliers", suppliers);
        model.addAttribute("quotations", quotations);
        model.addAttribute("selectedSupplier", supplier);

        return "quotations";
    }
}