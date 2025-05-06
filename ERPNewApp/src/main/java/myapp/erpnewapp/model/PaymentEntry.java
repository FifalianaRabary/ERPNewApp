package myapp.erpnewapp.model;

import lombok.Data;
import java.util.List;

@Data
public class PaymentEntry {
    private String paymentType = "Pay";
    private String partyType = "Supplier";
    private String party;
    private String company; // Champ critique pour la validation
    private double paidAmount;
    private double receivedAmount;
    private String paidFrom = "Cash - FID";
    private String paidTo = "Creditors - FID";
    private String modeOfPayment = "Cash";
    private String referenceNo;
    private String referenceDate;
    private List<PaymentReference> references;

    // Getters et setters dans l'ordre de priorité ERPNext
    public String getPartyType() {
        return partyType;
    }

    public void setPartyType(String partyType) {
        this.partyType = partyType;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaidFrom() {
        return paidFrom;
    }

    public void setPaidFrom(String paidFrom) {
        this.paidFrom = paidFrom;
    }

    public String getPaidTo() {
        return paidTo;
    }

    public void setPaidTo(String paidTo) {
        this.paidTo = paidTo;
    }

    public String getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public double getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(double receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getReferenceDate() {
        return referenceDate;
    }

    public void setReferenceDate(String referenceDate) {
        this.referenceDate = referenceDate;
    }

    public List<PaymentReference> getReferences() {
        return references;
    }

    public void setReferences(List<PaymentReference> references) {
        this.references = references;
    }

    public PaymentEntry() {
    }
}