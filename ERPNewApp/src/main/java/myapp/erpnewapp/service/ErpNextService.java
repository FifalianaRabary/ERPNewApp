package myapp.erpnewapp.service;

import myapp.erpnewapp.model.Quotation;
import myapp.erpnewapp.model.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class ErpNextService {
    private final String ERPNext_URL = "http://erpnext.localhost:8000";
    private final RestTemplate restTemplate;
    private final ErpNextAuthService authService;

    @Autowired
    public ErpNextService(RestTemplate restTemplate, ErpNextAuthService authService) {
        this.restTemplate = restTemplate;
        this.authService = authService;
    }

    public List<Supplier> getAllSuppliers() {
        HttpHeaders headers = createAuthHeaders();
        String url = ERPNext_URL + "/api/resource/Supplier?fields=[\"name\",\"supplier_name\",\"supplier_group\",\"country\"]";

        ResponseEntity<ErpListResponse<Supplier>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<ErpListResponse<Supplier>>() {}
        );

        return response.getBody().getData();
    }

    public List<Quotation> getQuotations(String supplierFilter) {
        HttpHeaders headers = createAuthHeaders();
        String url = ERPNext_URL + "/api/resource/Supplier%20Quotation?fields=[\"name\",\"supplier\",\"supplier_name\",\"transaction_date\",\"status\"]";

        if (supplierFilter != null && !supplierFilter.isEmpty()) {
            url += "&filters=" + URLEncoder.encode(
                    String.format("[[\"supplier\",\"=\",\"%s\"]]", supplierFilter),
                    StandardCharsets.UTF_8
            );
        }

        ResponseEntity<ErpListResponse<Quotation>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<ErpListResponse<Quotation>>() {}
        );

        return response.getBody().getData();
    }

    private HttpHeaders createAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String sessionToken = authService.getSessionToken();

        if (sessionToken == null) {
            throw new RuntimeException("No active session found. Please login first.");
        }

        headers.set("Cookie", "sid=" + sessionToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    // Ajout des getters pour la classe interne
    private static class ErpListResponse<T> {
        private List<T> data;

        public List<T> getData() {
            return data;
        }

        public void setData(List<T> data) {
            this.data = data;
        }
    }
}