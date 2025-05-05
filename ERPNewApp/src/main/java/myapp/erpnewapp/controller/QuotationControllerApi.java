package myapp.erpnewapp.controller;

import myapp.erpnewapp.model.Quotation;
import myapp.erpnewapp.service.ErpNextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/quotations")
public class QuotationControllerApi {

    private final ErpNextService erpNextService;

    @Autowired
    public QuotationControllerApi(ErpNextService erpNextService) {
        this.erpNextService = erpNextService;
    }

    @GetMapping
    public ResponseEntity<List<Quotation>> getAllQuotations(
            @RequestParam(required = false) String supplier) {

        try {
            List<Quotation> quotations = erpNextService.getQuotations(supplier);
            return ResponseEntity.ok(quotations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}