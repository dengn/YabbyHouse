package com.melbournestore.db;

import java.util.ArrayList;

public class SuburbDataComposer {
    public static final String TAG = SuburbDataComposer.class.getSimpleName();

    String[] titles = {"City", "北", "东北", "西", "东南"};

    String[][] stringss = {
            {
                    new String("Cross Roads"),
                    new String("Jiefang Bei"),
                    new String("abc"),
                    new String("bvc"),
                    new String("dgr"),
                    new String("rgg"),
                    new String("asd"),
                    new String("asd"),
                    new String("hyr"),
                    new String("vvr"),
                    new String("adc"),
                    new String("sde"),

            },
            {
                    new String("Airport"),
                    new String("Two River"),
                    new String("yeu"),
                    new String("uio"),
                    new String("ttt"),
                    new String("jjk"),
                    new String("gfh"),
                    new String("llj"),
                    new String("hgd"),
                    new String("vvr"),
                    new String("ccf"),
                    new String("rgf"),

            },
            {
                    new String("Northeast Area"),
                    new String("ery"),
                    new String("qwe"),
                    new String("Frederick the Great"),
                    new String("John Stanley"),
                    new String("Luise Adelgunda Gottsched"),
                    new String("Johann Ludwig Krebs"),
                    new String("Carl Philipp Emanuel Bach"),
                    new String("Christoph Willibald Gluck"),
                    new String("Gottfried August Homilius"),
            },
            {
                    new String("University Town"),
                    new String("Ludwig van Beethoven"),
                    new String("Fernando Sor"),
                    new String("Johann Strauss I"),
            },
            {
                    new String("South Area"),
                    new String("Ludwig van Beethoven"),
                    new String("Fernando Sor"),
                    new String("Johann Strauss I"),
            },
    };

    public String[][] getAllData() {


        return stringss;
    }

    public String[][] getDataByFilter(String query) {

        ArrayList<String> list_center = new ArrayList<String>();
        ArrayList<String> list_north = new ArrayList<String>();
        ArrayList<String> list_northeast = new ArrayList<String>();
        ArrayList<String> list_west = new ArrayList<String>();
        ArrayList<String> list_southeast = new ArrayList<String>();

        int array_len = 0;

        for (int i = 0; i < stringss[0].length; i++) {
            if (stringss[0][i].contains(query)) {
                list_center.add(stringss[0][i]);

            }
        }

        for (int i = 0; i < stringss[1].length; i++) {
            if (stringss[1][i].contains(query)) {
                list_north.add(stringss[1][i]);

            }

        }

        for (int i = 0; i < stringss[2].length; i++) {
            if (stringss[2][i].contains(query)) {
                list_northeast.add(stringss[2][i]);
            }
        }

        for (int i = 0; i < stringss[3].length; i++) {
            if (stringss[3][i].contains(query)) {
                list_west.add(stringss[3][i]);
            }
        }

        for (int i = 0; i < stringss[4].length; i++) {
            if (stringss[4][i].contains(query)) {
                list_southeast.add(stringss[4][i]);
            }
        }

        ArrayList<ArrayList<String>> list_string = new ArrayList<ArrayList<String>>();

        if (list_center.size() > 0) {
            list_string.add(list_center);
        }
        if (list_north.size() > 0) {
            list_string.add(list_north);
        }
        if (list_northeast.size() > 0) {
            list_string.add(list_northeast);
        }
        if (list_west.size() > 0) {
            list_string.add(list_west);
        }
        if (list_southeast.size() > 0) {
            list_string.add(list_southeast);
        }

        String[][] foo = new String[list_string.size()][];
        for (int i = 0; i < list_string.size(); i++) {
            foo[i] = list_string.get(i).toArray(new String[list_string.get(i).size()]);
        }
        return foo;


    }

}
