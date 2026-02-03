package com.atun.brain.infrastructure.ai.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 中文数字转换工具
 * 将"五元"、"三十五块"等中文数字表达转换为阿拉伯数字
 *
 * @author lij
 * @date 2026/02/03
 */
public class ChineseNumberConverter {
    
    private static final Map<Character, Integer> CHINESE_NUM_MAP = new HashMap<>();
    private static final Map<Character, Integer> CHINESE_UNIT_MAP = new HashMap<>();
    
    static {
        // 基础数字
        CHINESE_NUM_MAP.put('零', 0);
        CHINESE_NUM_MAP.put('一', 1);
        CHINESE_NUM_MAP.put('二', 2);
        CHINESE_NUM_MAP.put('两', 2);
        CHINESE_NUM_MAP.put('三', 3);
        CHINESE_NUM_MAP.put('四', 4);
        CHINESE_NUM_MAP.put('五', 5);
        CHINESE_NUM_MAP.put('六', 6);
        CHINESE_NUM_MAP.put('七', 7);
        CHINESE_NUM_MAP.put('八', 8);
        CHINESE_NUM_MAP.put('九', 9);
        
        // 单位
        CHINESE_UNIT_MAP.put('十', 10);
        CHINESE_UNIT_MAP.put('拾', 10);
        CHINESE_UNIT_MAP.put('百', 100);
        CHINESE_UNIT_MAP.put('佰', 100);
        CHINESE_UNIT_MAP.put('千', 1000);
        CHINESE_UNIT_MAP.put('仟', 1000);
        CHINESE_UNIT_MAP.put('万', 10000);
    }
    
    /**
     * 转换中文金额表达为阿拉伯数字
     * 示例：
     * "花了五元" -> "花了5元"
     * "买了三十五块钱的东西" -> "买了35块钱的东西"
     * "一百二十块" -> "120块"
     */
    public static String convertChineseAmount(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        
        // 匹配中文数字 + 金额单位（元/块/毛/分/角）的模式
        // 例如：五元、三十五块、一百二十块钱、两毛五
        Pattern pattern = Pattern.compile(
            "([零一二两三四五六七八九十拾百佰千仟万]+)\\s*([元块毛分角]|块钱|元钱)"
        );
        
        Matcher matcher = pattern.matcher(text);
        StringBuffer result = new StringBuffer();
        
        while (matcher.find()) {
            String chineseNumber = matcher.group(1);
            String unit = matcher.group(2);
            
            try {
                int arabicNumber = chineseToArabic(chineseNumber);
                matcher.appendReplacement(result, arabicNumber + unit);
            } catch (Exception e) {
                // 转换失败则保持原样
                matcher.appendReplacement(result, matcher.group(0));
            }
        }
        matcher.appendTail(result);
        
        return result.toString();
    }
    
    /**
     * 中文数字转阿拉伯数字
     * 支持：一、五、十、二十、三十五、一百、一千、五千八百
     */
    private static int chineseToArabic(String chinese) {
        if (chinese == null || chinese.isEmpty()) {
            return 0;
        }
        
        int result = 0;
        int temp = 0;
        int currentNum = 0;
        
        for (int i = 0; i < chinese.length(); i++) {
            char c = chinese.charAt(i);
            
            if (CHINESE_NUM_MAP.containsKey(c)) {
                currentNum = CHINESE_NUM_MAP.get(c);
            } else if (CHINESE_UNIT_MAP.containsKey(c)) {
                int unit = CHINESE_UNIT_MAP.get(c);
                
                if (unit == 10000) { // 万
                    result = (temp + currentNum) * unit;
                    temp = 0;
                    currentNum = 0;
                } else {
                    // 处理"十"开头的情况，如"十五"应理解为15
                    if (currentNum == 0 && i == 0 && unit == 10) {
                        currentNum = 1;
                    }
                    temp += currentNum * unit;
                    currentNum = 0;
                }
            }
        }
        
        result += temp + currentNum;
        return result;
    }

}
