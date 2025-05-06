package myapp.erpnewapp.model;

import lombok.Data;

 @Data
 public  class PaymentReference {
     private String referenceDoctype;
     private String referenceName;
     private Double totalAmount;
     private Double outstandingAmount;
     private Double allocatedAmount;

     public PaymentReference() {}

     // Getters et Setters
     public String getReferenceDoctype() {
         return referenceDoctype;
     }

     public void setReferenceDoctype(String referenceDoctype) {
         this.referenceDoctype = referenceDoctype;
     }

     public String getReferenceName() {
         return referenceName;
     }

     public void setReferenceName(String referenceName) {
         this.referenceName = referenceName;
     }

     public Double getTotalAmount() {
         return totalAmount;
     }

     public void setTotalAmount(Double totalAmount) {
         this.totalAmount = totalAmount;
     }

     public Double getOutstandingAmount() {
         return outstandingAmount;
     }

     public void setOutstandingAmount(Double outstandingAmount) {
         this.outstandingAmount = outstandingAmount;
     }

     public Double getAllocatedAmount() {
         return allocatedAmount;
     }

     public void setAllocatedAmount(Double allocatedAmount) {
         this.allocatedAmount = allocatedAmount;
     }
 }
