package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.controller.user.ShoppingCartController;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;//注入菜品
    @Autowired
    private SetmealMapper setmealMapper;//注入套餐

    /**
     * 查看购物车列表
     * @return
     */
    @Override
    public List<ShoppingCart> showShoppingCart() {
        //获取到当前微信用户的id
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart=ShoppingCart.builder()
                .userId(userId)
                .build();//构造一个购物车对象
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        return list;
    }

    /**
     * 添加购物车
     *
     * @param shoppingCartDTO
     */
    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        //判断当前加入到购物车中的商品是否已经存在了
        ShoppingCart shoppingCart = new ShoppingCart();//空的
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);//把DTO的属性拷给购物车对象
        Long userId = BaseContext.getCurrentId();//通过拦截器拿到这个微信用户的id
        shoppingCart.setUserId(userId);//拿到设置进去

        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);//返回结果

        //如果已经存在了，只需要将数量加一
        if (list != null && list.size() > 0) {
            ShoppingCart cart = list.get(0);
            cart.setNumber(cart.getNumber() + 1);//update shopping_cart set number=number+1 where id=cart.getId()
            shoppingCartMapper.updateNumberById(cart);
        } else {
            //如果不存在，则需要将数据插入到购物车表

            //判断本次添加到购物车的是菜品还是套餐
            Long dishId = shoppingCartDTO.getDishId();//获取菜品id
            if (dishId != null) {
                //本次添加到购物车的是菜品
                Dish dish = dishMapper.getById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());//购物车用的amount，dish用的price

            } else {
                //本次添加到购物车的是套餐 dish是空的 setmeal就不是空的
                Long setmealId = shoppingCartDTO.getSetmealId();

                Setmeal setmeal = setmealMapper.getById(setmealId);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());//购物车用的amount

            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);

        }
    }
}