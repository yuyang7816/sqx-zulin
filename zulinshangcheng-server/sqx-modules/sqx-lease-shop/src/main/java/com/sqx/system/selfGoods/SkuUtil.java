package com.sqx.system.selfGoods;

import java.util.ArrayList;
import java.util.List;

public class SkuUtil {

    /**
     * @param inputList 所有数组的列表
     * */
    public  static List<List<String>> skuSort(List<List<String>> inputList) {
        List<List<String>> result = new ArrayList<>();
        List<Integer> combination = new ArrayList<Integer>();
        int n=inputList.size();
        for (int i = 0; i < n; i++) {
            combination.add(0);
        }
        int i=0;
        boolean isContinue=false;
        do{
            List<String> temp = new ArrayList<>();
            //打印一次循环生成的组合
            for (int j = 0; j < n; j++) {
                temp.add(inputList.get(j).get(combination.get(j)));
            }
            result.add(temp);
            i++;
            combination.set(n-1, i);
            for (int j = n-1; j >= 0; j--) {
                if (combination.get(j)>=inputList.get(j).size()) {
                    combination.set(j, 0);
                    i=0;
                    if (j-1>=0) {
                        combination.set(j-1, combination.get(j-1)+1);
                    }
                }
            }
            isContinue=false;
            for (Integer integer : combination) {
                if (integer != 0) {
                    isContinue=true;
                }
            }
        }while (isContinue);
        return result;
    }

}
