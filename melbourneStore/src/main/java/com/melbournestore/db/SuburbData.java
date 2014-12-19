package com.melbournestore.db;

public class SuburbData {
//    public static final String TAG = SuburbData.class.getSimpleName();
//
//    public static List<Pair<String, List<String>>> getAllData() {
//        List<Pair<String, List<String>>> res = new ArrayList<Pair<String, List<String>>>();
//
//        for (int i = 0; i < 5; i++) {
//            res.add(getOneSection(i));
//        }
//
//        return res;
//    }
//
//    public static List<Pair<String, List<String>>> getData(String typedText) {
//        List<Pair<String, List<String>>> res = new ArrayList<Pair<String, List<String>>>();
//
//
//        String[] titles = {"City", "北", "东北", "西", "东南"};
//
//        int num_city = 0;
//        int num_north = 0;
//        int num_northeast = 0;
//        int num_west = 0;
//        int num_southeast = 0;
//
//        ArrayList<String> names_city = new ArrayList<String>();
//        ArrayList<String> names_north = new ArrayList<String>();
//        ArrayList<String> names_northeast = new ArrayList<String>();
//        ArrayList<String> names_west = new ArrayList<String>();
//        ArrayList<String> names_southeast = new ArrayList<String>();
//
//        for (int i = 0; i < DataResourceUtils.names_center.length; i++) {
//            if (DataResourceUtils.names_center[i].contains(typedText)) {
//                num_city++;
//                names_city.add(DataResourceUtils.names_center[i]);
//            }
//        }
//        for (int i = 0; i < DataResourceUtils.names_north.length; i++) {
//            if (DataResourceUtils.names_north[i].contains(typedText)) {
//                num_north++;
//                names_north.add(DataResourceUtils.names_north[i]);
//            }
//        }
//        for (int i = 0; i < DataResourceUtils.names_northeast.length; i++) {
//            if (DataResourceUtils.names_northeast[i].contains(typedText)) {
//                num_northeast++;
//                names_northeast.add(DataResourceUtils.names_northeast[i]);
//            }
//        }
//        for (int i = 0; i < DataResourceUtils.names_west.length; i++) {
//            if (DataResourceUtils.names_west[i].contains(typedText)) {
//                num_west++;
//                names_west.add(DataResourceUtils.names_west[i]);
//            }
//        }
//        for (int i = 0; i < DataResourceUtils.names_southeast.length; i++) {
//            if (DataResourceUtils.names_southeast[i].contains(typedText)) {
//                num_southeast++;
//                names_southeast.add(DataResourceUtils.names_southeast[i]);
//            }
//        }
//
//
//        if (num_city > 0) {
//            Pair city = new Pair<String, List<String>>(titles[0], names_city);
//            res.add(city);
//            Log.d("DEBUG", "city found");
//        }
//        if (num_north > 0) {
//            Pair north = new Pair<String, List<String>>(titles[1], names_north);
//            res.add(north);
//            Log.d("DEBUG", "north found");
//        }
//        if (num_northeast > 0) {
//            Pair northeast = new Pair<String, List<String>>(titles[2], names_northeast);
//            res.add(northeast);
//            Log.d("DEBUG", "northeast found");
//        }
//        if (num_west > 0) {
//            Pair west = new Pair<String, List<String>>(titles[3], names_west);
//            res.add(west);
//            Log.d("DEBUG", "west found");
//        }
//        if (num_southeast > 0) {
//            Pair southeast = new Pair<String, List<String>>(titles[4], names_southeast);
//            res.add(southeast);
//            Log.d("DEBUG", "southeast found");
//        }
//
//
//        return res;
//    }
//
//    public static List<String> getFlattenedData() {
//        List<String> res = new ArrayList<String>();
//
//        for (int i = 0; i < 5; i++) {
//            res.addAll(getOneSection(i).second);
//        }
//
//        return res;
//    }
//
//    public static Pair<Boolean, List<String>> getRows(int page) {
//        List<String> flattenedData = getFlattenedData();
//        if (page == 1) {
//            return new Pair<Boolean, List<String>>(true, flattenedData.subList(0, 5));
//        } else {
//            SystemClock.sleep(2000); // simulate loading
//            return new Pair<Boolean, List<String>>(page * 5 < flattenedData.size(), flattenedData.subList((page - 1) * 5, Math.min(page * 5, flattenedData.size())));
//        }
//    }
//
//    public static Pair<String, List<String>> getOneSection(int index) {
//        String[] titles = {"City", "北", "东北", "西", "东南"};
//        String[][] stringss = {
//                {
//                        new String("Cross Roads"),
//                        new String("Jiefang Bei"),
//                        new String("abc"),
//                        new String("bvc"),
//                        new String("dgr"),
//                        new String("rgg"),
//                        new String("asd"),
//                        new String("asd"),
//                        new String("hyr"),
//                        new String("vvr"),
//                        new String("adc"),
//                        new String("sde"),
//
//                },
//                {
//                        new String("Airport"),
//                        new String("Two River"),
//                        new String("yeu"),
//                        new String("uio"),
//                        new String("ttt"),
//                        new String("jjk"),
//                        new String("gfh"),
//                        new String("llj"),
//                        new String("hgd"),
//                        new String("vvr"),
//                        new String("ccf"),
//                        new String("rgf"),
//
//                },
//                {
//                        new String("Northeast Area"),
//                        new String("ery"),
//                        new String("qwe"),
//                        new String("Frederick the Great"),
//                        new String("John Stanley"),
//                        new String("Luise Adelgunda Gottsched"),
//                        new String("Johann Ludwig Krebs"),
//                        new String("Carl Philipp Emanuel Bach"),
//                        new String("Christoph Willibald Gluck"),
//                        new String("Gottfried August Homilius"),
//                },
//                {
//                        new String("University Town"),
//                        new String("Ludwig van Beethoven"),
//                        new String("Fernando Sor"),
//                        new String("Johann Strauss I"),
//                },
//                {
//                        new String("South Area"),
//                        new String("Ludwig van Beethoven"),
//                        new String("Fernando Sor"),
//                        new String("Johann Strauss I"),
//                },
//        };
//        return new Pair<String, List<String>>(titles[index], Arrays.asList(stringss[index]));
//    }
}
