package myapp.erpnewapp.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import myapp.erpnewapp.model.PaymentEntry;
import myapp.erpnewapp.model.PaymentReference;
import myapp.erpnewapp.model.PurchaseInvoice;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ErpNextInvoiceService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String erpnextUrl = "http://erpnext.localhost:8000";

    public List<PurchaseInvoice> getPurchaseInvoices(String sid) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String resource = "Purchase Invoice";
        String fieldsParam = "[\"*\"]";
        String url = erpnextUrl + "/api/resource/" + resource + "?fields=" + fieldsParam;

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        System.out.println("Body " + response.getBody());

        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode dataNode = root.get("data");

            List<PurchaseInvoice> invoices = new ArrayList<>();
            for (JsonNode node : dataNode) {
                PurchaseInvoice invoice = new PurchaseInvoice();
                invoice.setName(node.path("name").asText(null));
                invoice.setSupplier(node.path("supplier").asText(null));
                invoice.setPostingDate(node.path("posting_date").asText(null));
                invoice.setStatus(node.path("status").asText(null));
                invoice.setOutstandingAmount(node.path("outstanding_amount").asDouble(0));
                invoice.setTotal(node.path("total").asDouble(0));
                invoices.add(invoice);
            }

            return invoices;
        } else {
            throw new Exception("Échec de la récupération des factures d'achat : " + response.getStatusCode());
        }
    }

    // public boolean payInvoice(String sid, String invoiceName, double amountPaid) throws Exception {
    //     HttpHeaders headers = new HttpHeaders();
    //     headers.add("Cookie", "sid=" + sid);
    //     headers.setContentType(MediaType.APPLICATION_JSON);

    //     String url = erpnextUrl + "/api/resource/Purchase Invoice/" + invoiceName;

    //     // Préparer le corps de la requête avec les informations pertinentes
    //     Map<String, Object> body = new HashMap<>();
    //     body.put("status", "Paid");  // Mettre à jour le statut
    //     body.put("paid_amount", amountPaid);  // Ajouter le montant payé
    //     body.put("outstanding_amount", 0);  // Le montant restant doit être 0
    //     body.put("clearance_date", LocalDate.now().toString());  // Ajouter la date de paiement (facultatif)

    //     // Créer la requête
    //     HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(body), headers);

    //     // Effectuer la requête PUT
    //     ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);

    //     // Vérifier si la requête a réussi
    //     return response.getStatusCode() == HttpStatus.OK;
    // }



    // public boolean payInvoice(String sid, String invoiceName, double amountPaid) throws Exception {
    //     HttpHeaders headers = new HttpHeaders();
    //     headers.add("Cookie", "sid=" + sid);
    //     headers.setContentType(MediaType.APPLICATION_JSON);
    
    //     // 1. Créer une entrée de paiement
    //     String paymentUrl = erpnextUrl + "/api/resource/Payment Entry";
        
    //     Map<String, Object> paymentEntry = new HashMap<>();
    //     paymentEntry.put("payment_type", "Pay");
    //     paymentEntry.put("party_type", "Supplier");
    //     paymentEntry.put("party", getSupplierFromInvoice(sid, invoiceName)); // Vous devez implémenter cette méthode
    //     paymentEntry.put("paid_amount", amountPaid);
    //     paymentEntry.put("received_amount", amountPaid);
    //     paymentEntry.put("reference_no", "PAY-" + System.currentTimeMillis());
    //     paymentEntry.put("reference_date", LocalDate.now().toString());
    //     paymentEntry.put("company", "Fifaliana ITU (Demo)"); 
    //     List<Map<String, Object>> references = new ArrayList<>();
    //     Map<String, Object> reference = new HashMap<>();
    //     reference.put("reference_doctype", "Purchase Invoice");
    //     reference.put("reference_name", invoiceName);
    //     reference.put("total_amount", amountPaid);
    //     reference.put("outstanding_amount", amountPaid);
    //     reference.put("allocated_amount", amountPaid);
    //     references.add(reference);
        
    //     paymentEntry.put("references", references);
    
    //     HttpEntity<String> paymentRequest = new HttpEntity<>(objectMapper.writeValueAsString(paymentEntry), headers);
    //     ResponseEntity<String> paymentResponse = restTemplate.exchange(
    //         paymentUrl, HttpMethod.POST, paymentRequest, String.class);
    
    //     if (paymentResponse.getStatusCode() != HttpStatus.OK) {
    //         throw new Exception("Échec de la création du paiement : " + paymentResponse.getBody());
    //     }
    
    //     // 2. Soumettre le paiement
    //     JsonNode paymentData = objectMapper.readTree(paymentResponse.getBody()).get("data");
    //     String paymentName = paymentData.path("name").asText();
        
    //     String submitUrl = erpnextUrl + "/api/resource/Payment Entry/" + paymentName + "/submit";
    //     HttpEntity<String> submitRequest = new HttpEntity<>("{}", headers);
    //     ResponseEntity<String> submitResponse = restTemplate.exchange(
    //         submitUrl, HttpMethod.POST, submitRequest, String.class);
    
    //     return submitResponse.getStatusCode() == HttpStatus.OK;
    // }


    // public boolean payInvoice(String sid, String invoiceName, double amountPaid) throws Exception {
    //     HttpHeaders headers = new HttpHeaders();
    //     headers.add("Cookie", "sid=" + sid);
    //     headers.setContentType(MediaType.APPLICATION_JSON);

    //     // 1. Récupérer la facture
    //     PurchaseInvoice invoice = getPurchaseInvoiceByName(sid, invoiceName);
    //     if (invoice == null) {
    //         throw new Exception("Facture introuvable");
    //     }

    //     // 2. Configurer ObjectMapper pour garder l'ordre des champs
    //     ObjectMapper mapper = new ObjectMapper();
    //     mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, false);
    //     mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

    //     // 3. Préparer les données dans l'ordre requis par ERPNext
    //     Map<String, Object> paymentData = new LinkedHashMap<>();
    //     paymentData.put("doctype", "Payment Entry");
    //     paymentData.put("party_type", "Supplier"); // Doit être en premier
    //     paymentData.put("party", invoice.getSupplier());
    //     paymentData.put("company", invoice.getCompany());
    //     paymentData.put("payment_type", "Pay");
    //     paymentData.put("paid_from", "Cash - FID");
    //     paymentData.put("paid_to", "Creditors - FID");
    //     paymentData.put("paid_amount", amountPaid);
    //     paymentData.put("received_amount", amountPaid);
    //     paymentData.put("mode_of_payment", "Cash");
    //     paymentData.put("reference_no", "PAY-" + System.currentTimeMillis());
    //     paymentData.put("reference_date", LocalDate.now().toString());

    //     // Références
    //     List<Map<String, Object>> references = new ArrayList<>();
    //     Map<String, Object> reference = new LinkedHashMap<>();
    //     reference.put("reference_doctype", "Purchase Invoice");
    //     reference.put("reference_name", invoiceName);
    //     reference.put("total_amount", amountPaid);
    //     reference.put("outstanding_amount", amountPaid);
    //     reference.put("allocated_amount", amountPaid);
    //     references.add(reference);
        
    //     paymentData.put("references", references);

    //     // 4. Envoyer la requête
    //     String paymentUrl = erpnextUrl + "/api/resource/Payment Entry";
    //     Map<String, Object> requestBody = Map.of("data", paymentData);
        
    //     // Log pour débogage
    //     System.out.println("Requête envoyée: " + mapper.writeValueAsString(requestBody));
        
    //     HttpEntity<String> paymentRequest = new HttpEntity<>(
    //         mapper.writeValueAsString(requestBody), headers);
        
    //     ResponseEntity<String> paymentResponse = restTemplate.exchange(
    //         paymentUrl, HttpMethod.POST, paymentRequest, String.class);

    //     if (!paymentResponse.getStatusCode().is2xxSuccessful()) {
    //         throw new Exception("Échec création paiement: " + paymentResponse.getBody());
    //     }

    //     // 5. Soumettre le paiement
    //     JsonNode paymentNode = mapper.readTree(paymentResponse.getBody());
    //     String paymentName = paymentNode.get("data").path("name").asText();
        
    //     String submitUrl = erpnextUrl + "/api/resource/Payment Entry/" + paymentName + "/submit";
    //     HttpEntity<String> submitRequest = new HttpEntity<>("{}", headers);
    //     ResponseEntity<String> submitResponse = restTemplate.exchange(
    //         submitUrl, HttpMethod.POST, submitRequest, String.class);

    //     return submitResponse.getStatusCode().is2xxSuccessful();
    // }

    // public boolean payInvoice(String sid, String invoiceName, double amountPaid) throws Exception {
    //     HttpHeaders headers = new HttpHeaders();
    //     headers.add("Cookie", "sid=" + sid);
    //     headers.setContentType(MediaType.APPLICATION_JSON);
    
    //     // 1. Récupérer la facture
    //     PurchaseInvoice invoice = getPurchaseInvoiceByName(sid, invoiceName);
    //     if (invoice == null) {
    //         throw new Exception("Facture introuvable");
    //     }
    
    //     // 2. Configurer ObjectMapper
    //     ObjectMapper mapper = new ObjectMapper();
    //     mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, false);
    
    //     // 3. Préparer les données du paiement
    //     Map<String, Object> paymentData = new LinkedHashMap<>();
    //     paymentData.put("doctype", "Payment Entry");
    //     paymentData.put("party_type", "Supplier");
    //     paymentData.put("party", invoice.getSupplier());
    //     paymentData.put("company", invoice.getCompany());
    //     paymentData.put("payment_type", "Pay");
    //     paymentData.put("paid_from", "Cash - FID");
    //     paymentData.put("paid_to", "Creditors - FID");
    //     paymentData.put("paid_amount", amountPaid);
    //     paymentData.put("received_amount", amountPaid);
    //     paymentData.put("mode_of_payment", "Cash");
    //     paymentData.put("reference_no", "PAY-" + System.currentTimeMillis());
    //     paymentData.put("reference_date", LocalDate.now().toString());
    
    //     // Préparer les références
    //     List<Map<String, Object>> references = new ArrayList<>();
    //     Map<String, Object> reference = new LinkedHashMap<>();
    //     reference.put("reference_doctype", "Purchase Invoice");
    //     reference.put("reference_name", invoiceName);
    //     reference.put("total_amount", amountPaid);
    //     reference.put("outstanding_amount", amountPaid);
    //     reference.put("allocated_amount", amountPaid);
    //     references.add(reference);
    //     paymentData.put("references", references);
    
    //     // 4. Créer le Payment Entry
    //     String paymentUrl = erpnextUrl + "/api/resource/Payment Entry";
    //     HttpEntity<String> paymentRequest = new HttpEntity<>(
    //         mapper.writeValueAsString(Map.of("data", paymentData)), headers);
        
    //     ResponseEntity<String> paymentResponse = restTemplate.exchange(
    //         paymentUrl, HttpMethod.POST, paymentRequest, String.class);
    
    //     if (!paymentResponse.getStatusCode().is2xxSuccessful()) {
    //         throw new Exception("Échec création paiement: " + paymentResponse.getBody());
    //     }
    
    //     // 5. Soumettre le Payment Entry (méthode corrigée)
    //     JsonNode paymentNode = mapper.readTree(paymentResponse.getBody());
    //     String paymentName = paymentNode.get("data").path("name").asText();
        
    //     String submitUrl = erpnextUrl + "/api/method/frappe.client.submit";
    //     Map<String, String> submitData = Map.of(
    //         "doc", mapper.writeValueAsString(Map.of(
    //             "doctype", "Payment Entry",
    //             "name", paymentName
    //         ))
    //     );
        
    //     HttpEntity<String> submitRequest = new HttpEntity<>(
    //         mapper.writeValueAsString(submitData), headers);
        
    //     ResponseEntity<String> submitResponse = restTemplate.exchange(
    //         submitUrl, HttpMethod.POST, submitRequest, String.class);
    
    //     return submitResponse.getStatusCode().is2xxSuccessful();
    // }

    
    
    private String getSupplierFromInvoice(String sid, String invoiceName) throws Exception {
        PurchaseInvoice invoice = getPurchaseInvoiceByName(sid, invoiceName);
        return invoice != null ? invoice.getSupplier() : null;
    }

    public PurchaseInvoice getPurchaseInvoiceByName(String sid, String invoiceName) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);
    
        String url = erpnextUrl + "/api/resource/Purchase Invoice/" + invoiceName;
    
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
    
        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode data = root.get("data");
    
            PurchaseInvoice invoice = new PurchaseInvoice();
            invoice.setName(data.path("name").asText(null));
            invoice.setSupplier(data.path("supplier").asText(null));
            invoice.setPostingDate(data.path("posting_date").asText(null));
            invoice.setStatus(data.path("status").asText(null));
            invoice.setOutstandingAmount(data.path("outstanding_amount").asDouble(0));
            invoice.setTotal(data.path("total").asDouble(0));
            invoice.setCompany(data.path("company").asText(null));
            // invoice.setPaid(data.path("is_paid").asInt(0) == 1);
            
            return invoice;
        } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            return null;
        } else {
            throw new Exception("Échec de la récupération de la facture : " + response.getStatusCode());
        }
    }



        public void payInvoice(String sid, String nomFacture) throws Exception {
            // Étape 1 : Récupérer les informations de la facture
            String urlFacture = erpnextUrl + "/api/resource/Purchase Invoice/" + nomFacture;

            HttpHeaders headers = new HttpHeaders();
            headers.add("Cookie", "sid=" + sid);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestFacture = new HttpEntity<>(headers);
            ResponseEntity<String> reponseFacture = restTemplate.exchange(urlFacture, HttpMethod.GET, requestFacture, String.class);

            if (reponseFacture.getStatusCode() != HttpStatus.OK) {
                throw new Exception("Erreur lors de la récupération de la facture : " + reponseFacture.getStatusCode());
            }

            JsonNode factureData = objectMapper.readTree(reponseFacture.getBody()).get("data");
            String fournisseur = factureData.get("supplier").asText();
            double montantTotal = factureData.get("grand_total").asDouble();
            double montantRestant = factureData.get("outstanding_amount").asDouble();
            String dateDuJour = LocalDate.now().toString();

            // Étape 2 : Créer le JSON du paiement avec les bons comptes
            ObjectNode paiement = objectMapper.createObjectNode();
            paiement.put("doctype", "Payment Entry");
            paiement.put("payment_type", "Pay");
            paiement.put("party_type", "Supplier");
            paiement.put("party", fournisseur);
            paiement.put("posting_date", dateDuJour);
            paiement.put("mode_of_payment", "Cash");
            paiement.put("paid_from", "Cash - FID");  
            paiement.put("paid_to", "Creditors - FID");       
            paiement.put("paid_amount", montantTotal);
            paiement.put("received_amount", montantTotal);
            paiement.put("reference_no", "AUTO-" + nomFacture);
            paiement.put("reference_date", dateDuJour);
            paiement.put("remarks", "Paiement automatique de la facture " + nomFacture);

            ArrayNode references = objectMapper.createArrayNode();
            ObjectNode reference = objectMapper.createObjectNode();
            reference.put("reference_doctype", "Purchase Invoice");
            reference.put("reference_name", nomFacture);
            reference.put("total_amount", montantTotal);
            reference.put("outstanding_amount", montantRestant);
            reference.put("allocated_amount", montantRestant);
            references.add(reference);
            paiement.set("references", references);

            // Étape 3 : Créer la Payment Entry
            HttpEntity<String> createRequest = new HttpEntity<>(paiement.toString(), headers);
            ResponseEntity<String> creationPaiement = restTemplate.postForEntity(
                    erpnextUrl + "/api/resource/Payment Entry",
                    createRequest,
                    String.class
            );

            if (creationPaiement.getStatusCode() != HttpStatus.OK && creationPaiement.getStatusCode() != HttpStatus.CREATED) {
                throw new Exception("Erreur lors de la création du paiement : " + creationPaiement.getStatusCode()
                        + "\n" + creationPaiement.getBody());
            }

            String nomPaiement = objectMapper.readTree(creationPaiement.getBody()).get("data").get("name").asText();

            // Étape 4 : Récupérer le document complet de paiement pour soumission
            String urlPaiement = erpnextUrl + "/api/resource/Payment Entry/" + nomPaiement;
            ResponseEntity<String> reponsePaiement = restTemplate.exchange(urlPaiement, HttpMethod.GET, requestFacture, String.class);
            JsonNode docComplet = objectMapper.readTree(reponsePaiement.getBody()).get("data");

            // Étape 5 : Soumettre le paiement via frappe.client.submit
            String urlSoumission = erpnextUrl + "/api/method/frappe.client.submit";
            ObjectNode soumissionJson = objectMapper.createObjectNode();
            soumissionJson.put("doc", objectMapper.writeValueAsString(docComplet));

            HttpEntity<String> submitRequest = new HttpEntity<>(soumissionJson.toString(), headers);
            ResponseEntity<String> reponseSoumission = restTemplate.postForEntity(urlSoumission, submitRequest, String.class);

            if (reponseSoumission.getStatusCode() != HttpStatus.OK) {
                throw new Exception("Erreur lors de la soumission du paiement : " + reponseSoumission.getStatusCode()
                        + "\n" + reponseSoumission.getBody());
            }

            System.out.println("✅ Paiement soumis avec succès pour la facture : " + nomFacture);
        }
    


}
