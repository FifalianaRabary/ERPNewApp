package myapp.erpnewapp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import myapp.erpnewapp.model.PaymentEntry;
import myapp.erpnewapp.model.PaymentReference;
import myapp.erpnewapp.model.PurchaseInvoice;

@Service
public class ErpNextPaymentEntryService {
    public static PaymentEntry fromPurchaseInvoice(PurchaseInvoice invoice, Double amount) {
        PaymentEntry entry = new PaymentEntry();
        entry.setPaymentType("Pay");
        entry.setPartyType("Supplier");
        entry.setParty(invoice.getSupplier());
        entry.setPaidAmount(amount);
        entry.setReceivedAmount(amount);
        entry.setReferenceNo("PAY-" + System.currentTimeMillis());
        entry.setReferenceDate(java.time.LocalDate.now().toString());
        entry.setCompany("Fifaliana ITU (Demo)"); 
        PaymentReference reference = new PaymentReference();
        reference.setReferenceDoctype("Purchase Invoice");
        reference.setReferenceName(invoice.getName());
        reference.setTotalAmount(amount);
        reference.setOutstandingAmount(amount);
        reference.setAllocatedAmount(amount);

        entry.setReferences(List.of(reference));
        
        return entry;
    }
}
