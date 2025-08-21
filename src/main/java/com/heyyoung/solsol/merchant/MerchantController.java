package com.heyyoung.solsol.merchant;

import com.heyyoung.solsol.merchant.dto.GetMenusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MerchantController {
    private final MerchantService merchantService;

    @GetMapping("/v1/merchants/{merchantId}/menu")
    public ResponseEntity<GetMenusResponse> getMenus(@PathVariable long merchantId) {
        GetMenusResponse response = merchantService.getMenuList(merchantId);
        return ResponseEntity.ok(response);
    }
}
