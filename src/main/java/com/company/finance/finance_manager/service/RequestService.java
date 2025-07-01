package com.company.finance.finance_manager.service;

import com.company.finance.finance_manager.dto.RequestDTO;
import com.company.finance.finance_manager.dto.RequestPagedDataDTO;
import com.company.finance.finance_manager.dto.RequestPaginatedDTO;
import com.company.finance.finance_manager.dto.RequestUpdateDTO;
import com.company.finance.finance_manager.entity.*;
import com.company.finance.finance_manager.enums.ERequestType;
import com.company.finance.finance_manager.enums.EResponse;
import com.company.finance.finance_manager.enums.EStatus;
import com.company.finance.finance_manager.exception.ResourceNotFoundException;
import com.company.finance.finance_manager.repository.InvoiceRepository;
import com.company.finance.finance_manager.repository.RequestRepository;
import com.company.finance.finance_manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceStatusAuditService invoiceStatusAuditService;

    public Page<Request> getAllRequests(Pageable pageable) {
        return requestRepository.findAll(pageable);
    }

    public Page<RequestPaginatedDTO> getAllRequestsPaginated(Pageable pageable) {
        Page<Request> requests = requestRepository.findAll(pageable);
        return requests.map(RequestPaginatedDTO::new);
    }

    public RequestPagedDataDTO getRequestById(Integer id) {
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found with id: " + id));
        return new RequestPagedDataDTO(request);
    }

    public Request createRequest(RequestDTO requestDTO) {
        // Get authenticated username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Load the User entity from DB
        User user = userRepository.findByUsername(username);

        // Load the Invoice entity from DB
        Invoice invoice = invoiceRepository.findById(requestDTO.getInvoiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with ID: " + requestDTO.getInvoiceId()));

        Request request = new Request();
        request.setInvoice(invoice);
        request.setRequestType(requestDTO.getRequestType());
        request.setStatus(EStatus.PENDING);
        request.setCreated_by(user);

        return requestRepository.save(request);
    }

    public Request updateRequest(Integer requestId, RequestUpdateDTO requestDTO) {
        // Load the Invoice entity from DB
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with ID: " + requestDTO.getId()));

        String currentUser = getCurrentUsername(); // Fetch from SecurityContext or Auth

        if (requestDTO.getResponse() == EResponse.ACCEPT) {
            Invoice invoice = invoiceRepository.findById(requestDTO.getInvoiceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with ID: " + requestDTO.getInvoiceId()));

            if (requestDTO.getRequestType() == ERequestType.FG_REQUEST) {
                invoiceStatusAuditService.saveStatusAudit(
                        invoice.getInvoiceNumber(),
                        "fgsStatus",
                        String.valueOf(invoice.getFgsStatus()),
                        String.valueOf(EStatus.PENDING),
                        currentUser
                );
                invoice.setFgsStatus(EStatus.PENDING);
            } else if (requestDTO.getRequestType() == ERequestType.FINANCE_REQUEST) {
                invoiceStatusAuditService.saveStatusAudit(
                        invoice.getInvoiceNumber(),
                        "financeStatus",
                        String.valueOf(invoice.getFgsStatus()),
                        String.valueOf(EStatus.PENDING),
                        currentUser
                );
                invoice.setFinanceStatus(EStatus.PENDING);
            }

            invoiceRepository.save(invoice);
        }

        request.setStatus(EStatus.COMPLETED);

        return requestRepository.save(request);
    }

    private String getCurrentUsername() {
        // Example using Spring Security
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : "SYSTEM";
    }
}
