package com.heyyoung.solsol.menu.dto;

import com.heyyoung.solsol.menu.MenuEntity;

import java.math.BigDecimal;

public record GetMenuResponse(int menuId, String name, BigDecimal price, String image) {
    public static GetMenuResponse from(MenuEntity menu) {
        return new GetMenuResponse(menu.getMenuId(), menu.getName(), menu.getPrice(), menu.getImage());
    }
}
