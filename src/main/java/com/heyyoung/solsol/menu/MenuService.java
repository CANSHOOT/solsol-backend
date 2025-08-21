package com.heyyoung.solsol.menu;

import com.heyyoung.solsol.menu.dto.GetMenuResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;

    public List<GetMenuResponse> getMenus(long merchantId) {
        return menuRepository.findByMerchantMerchantId(merchantId)
                .stream()
                .map(GetMenuResponse::from)
                .toList();
    }
}
