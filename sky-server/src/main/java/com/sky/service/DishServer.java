package com.sky.service;

import com.sky.dto.DishDTO;

public interface DishServer {

    /*
    * 新增菜品
    * */
    void saveWithFlavor(DishDTO dishDTO);
}
