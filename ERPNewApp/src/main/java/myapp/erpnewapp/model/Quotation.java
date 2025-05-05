package myapp.erpnewapp.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class Quotation {
    private String name;
    private String supplier;         // ID du fournisseur
    private String supplierName;     // Nom complet du fournisseur
    private LocalDate transactionDate;
    private String status;
}