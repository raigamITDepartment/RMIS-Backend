package com.company.finance.finance_manager.controller;

import com.company.finance.finance_manager.dto.*;
import com.company.finance.finance_manager.entity.Request;
import com.company.finance.finance_manager.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/requests")
public class RequestController {

    @Autowired
    private RequestService requestService;

    @GetMapping
    public ResponseEntity<PaginatedResponse<RequestPaginatedDTO>> getAllRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        Sort sortOrder = Sort.by(Sort.Direction.fromString(direction), sort);
        Pageable pageable = PageRequest.of(page, size, sortOrder);

        Page<RequestPaginatedDTO> requestPage = requestService.getAllRequestsPaginated(pageable);

        PaginatedResponse<RequestPaginatedDTO> response = new PaginatedResponse<>(requestPage);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRequestById(@PathVariable Integer id) {
        RequestPagedDataDTO requestPagedDataDTO = requestService.getRequestById(id);

        return ResponseEntity.ok(requestPagedDataDTO);
    }

    @PostMapping
    public ResponseEntity<Request> createRequest(@RequestBody RequestDTO requestDTO) {
        Request request = requestService.createRequest(requestDTO);
        URI location = URI.create("/requests/" + request.getId()); // assuming course has getSlug()
        return ResponseEntity.created(location).body(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Request> updateRequest(@PathVariable Integer id, @RequestBody RequestUpdateDTO updateDto) {
        Request response = requestService.updateRequest(id, updateDto);
        return ResponseEntity.ok(response);
    }
}
