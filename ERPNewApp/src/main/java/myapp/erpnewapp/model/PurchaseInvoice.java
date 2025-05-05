package myapp.erpnewapp.model;

import lombok.Data;

@Data
public class PurchaseInvoice {
    private String name;
    private String status; 
    private String purchase_order;
}
