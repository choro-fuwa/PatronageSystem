package src.java.com.patronage.strategy.utils;

import java.math.BigDecimal;
import java.util.Collection;

/**
 * 验证工具类
 */
public class ValidationUtils {

    /**
     * 验证字符串是否为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 验证字符串是否不为空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 验证集合是否为空
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 验证集合是否不为空
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 验证对象是否为空
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String) {
            return isEmpty((String) obj);
        }
        if (obj instanceof Collection) {
            return isEmpty((Collection<?>) obj);
        }
        return false;
    }

    /**
     * 验证对象是否不为空
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * 验证数字是否为正数
     */
    public static boolean isPositive(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * 验证数字是否为非负数
     */
    public static boolean isNonNegative(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) >= 0;
    }

    /**
     * 验证数字是否在指定范围内
     */
    public static boolean isInRange(BigDecimal value, BigDecimal min, BigDecimal max) {
        if (value == null) {
            return false;
        }
        return value.compareTo(min) >= 0 && value.compareTo(max) <= 0;
    }

    /**
     * 验证整数是否在指定范围内
     */
    public static boolean isInRange(Integer value, Integer min, Integer max) {
        if (value == null) {
            return false;
        }
        return value >= min && value <= max;
    }

    /**
     * 验证邮箱格式
     */
    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) {
            return false;
        }
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    /**
     * 验证手机号格式
     */
    public static boolean isValidPhone(String phone) {
        if (isEmpty(phone)) {
            return false;
        }
        String phoneRegex = "^1[3-9]\\d{9}$";
        return phone.matches(phoneRegex);
    }

    /**
     * 验证身份证号格式
     */
    public static boolean isValidIdCard(String idCard) {
        if (isEmpty(idCard)) {
            return false;
        }
        String idCardRegex = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
        return idCard.matches(idCardRegex);
    }

    /**
     * 验证股票代码格式
     */
    public static boolean isValidStockCode(String stockCode) {
        if (isEmpty(stockCode)) {
            return false;
        }
        // 沪市：600xxx, 601xxx, 603xxx, 605xxx
        // 深市：000xxx, 002xxx, 300xxx
        // 创业板：300xxx
        // 科创板：688xxx
        String stockCodeRegex = "^(600|601|603|605|000|002|300|688)\\d{3}$";
        return stockCode.matches(stockCodeRegex);
    }

    /**
     * 验证策略名称
     */
    public static boolean isValidStrategyName(String strategyName) {
        if (isEmpty(strategyName)) {
            return false;
        }
        // 策略名称长度1-100，不能包含特殊字符
        return strategyName.length() >= 1 && strategyName.length() <= 100 
               && strategyName.matches("^[\\u4e00-\\u9fa5a-zA-Z0-9\\s_-]+$");
    }

    /**
     * 验证策略参数
     */
    public static boolean isValidStrategyParameters(String parameters) {
        if (isEmpty(parameters)) {
            return true; // 参数可以为空
        }
        // 验证是否为有效的JSON格式
        try {
            com.alibaba.fastjson2.JSON.parse(parameters);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
} 