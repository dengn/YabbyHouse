package com.melbournestore.db;

import com.melbournestore.activities.R;

public class DataResourceUtils {

    // For the main page

    public static final String[] drawerItemsTitles = {"美食餐厅", "我的订单", "位置定位",
            "买家秀", "精品推荐"};

    public static final int[] drawerItemsImages = {
            R.drawable.sidebar_icon_restaurant, R.drawable.sidebar_icon_order,
            R.drawable.sidebar_icon_location,
            R.drawable.sidebar_icon_photoshow, R.drawable.sidebar_icon_aboutus};

    public static final String[] shopItems = {"龙虾叔叔", "黑丸嫩仙草", "水饺妞妞"};

    public static final int[] shopItemsImages = {R.drawable.pic1,
            R.drawable.pic2, R.drawable.pic3};

    public static final String[][] plateNames = {
            {"麻辣小龙虾", "蒜泥小龙虾", "泡椒小龙虾", "咖喱小龙虾", "小龙虾炒年糕"},
            {"A套餐", "B套餐", "C套餐", "D套餐", "E套餐"},
            {"水饺", "煎饺", "猪肉水饺", "芹菜水饺", "白菜水饺"}};

    public static final int[][] platePrices = {{55, 55, 58, 58, 55},
            {20, 42, 45, 70, 63}, {41, 12, 45, 45, 16}};

    public static final int[][] plateNumbers = {{0, 0, 1, 2, 2},
            {0, 0, 1, 2, 2}, {0, 0, 1, 2, 2}};

    public static final int[][] plateStockMax = {{20, 20, 20, 10, 10},
            {20, 20, 20, 10, 10}, {20, 20, 20, 10, 10}};

    public static final int[][] plateLikeNumbers = {
            {100, 101, 458, 258, 254}, {100, 101, 458, 258, 254},
            {100, 101, 458, 258, 254}};

    public static final int[][] plateImages = {
            {R.drawable.plate1, R.drawable.plate2, R.drawable.plate3,
                    R.drawable.plate4, R.drawable.plate5},
            {R.drawable.plate6, R.drawable.plate7, R.drawable.plate8,
                    R.drawable.plate9, R.drawable.plate10},
            {R.drawable.plate11, R.drawable.plate12, R.drawable.plate13,
                    R.drawable.plate14, R.drawable.plate15}};


    public static final String[] names_center = {"Cross Roads", "Jiefang Bei"};
    public static final String[] names_southeast = {"South Area"};
    public static final String[] names_north = {"Airport", "Two River"};
    public static final String[] names_west = {"University Town"};
    public static final String[] names_northeast = {"Northeast Area"};


    public static final String post_center = "3001";
    public static final String post_southeast = "3002";
    public static final String post_north = "3003";
    public static final String post_west = "3004";
    public static final String post_northeast = "3005";

}
