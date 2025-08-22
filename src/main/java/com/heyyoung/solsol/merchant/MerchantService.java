package com.heyyoung.solsol.merchant;

import com.heyyoung.solsol.menu.MenuService;
import com.heyyoung.solsol.merchant.dto.GetMenusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MerchantService {
    private final MenuService menuService;

    public GetMenusResponse getMenuList(long merchantId) {
        return new GetMenusResponse(menuService.getMenus(merchantId));
    }
}
