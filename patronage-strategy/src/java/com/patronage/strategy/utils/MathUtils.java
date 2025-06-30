package src.java.com.patronage.strategy.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 数学工具类
 */
public class MathUtils {

    /**
     * 计算平均值
     */
    public static BigDecimal average(List<BigDecimal> values) {
        if (values == null || values.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal sum = values.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.divide(BigDecimal.valueOf(values.size()), 4, RoundingMode.HALF_UP);
    }

    /**
     * 计算标准差
     */
    public static BigDecimal standardDeviation(List<BigDecimal> values) {
        if (values == null || values.size() < 2) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal mean = average(values);
        BigDecimal sumSquaredDiff = values.stream()
                .map(value -> value.subtract(mean).pow(2))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal variance = sumSquaredDiff.divide(BigDecimal.valueOf(values.size() - 1), 8, RoundingMode.HALF_UP);
        return sqrt(variance);
    }

    /**
     * 计算平方根
     */
    public static BigDecimal sqrt(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal x = value;
        BigDecimal y = BigDecimal.ZERO;
        BigDecimal epsilon = new BigDecimal("0.00000001");
        
        while (x.subtract(y).abs().compareTo(epsilon) > 0) {
            y = x;
            x = value.divide(x, 8, RoundingMode.HALF_UP).add(x).divide(BigDecimal.valueOf(2), 8, RoundingMode.HALF_UP);
        }
        
        return x.setScale(4, RoundingMode.HALF_UP);
    }

    /**
     * 计算夏普比率
     */
    public static BigDecimal sharpeRatio(List<BigDecimal> returns, BigDecimal riskFreeRate) {
        if (returns == null || returns.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal avgReturn = average(returns);
        BigDecimal stdDev = standardDeviation(returns);
        
        if (stdDev.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        
        return avgReturn.subtract(riskFreeRate).divide(stdDev, 4, RoundingMode.HALF_UP);
    }

    /**
     * 计算最大回撤
     */
    public static BigDecimal maxDrawdown(List<BigDecimal> values) {
        if (values == null || values.size() < 2) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal maxDrawdown = BigDecimal.ZERO;
        BigDecimal peak = values.get(0);
        
        for (BigDecimal value : values) {
            if (value.compareTo(peak) > 0) {
                peak = value;
            } else {
                BigDecimal drawdown = peak.subtract(value).divide(peak, 4, RoundingMode.HALF_UP);
                if (drawdown.compareTo(maxDrawdown) > 0) {
                    maxDrawdown = drawdown;
                }
            }
        }
        
        return maxDrawdown;
    }

    /**
     * 计算胜率
     */
    public static BigDecimal winRate(List<BigDecimal> returns) {
        if (returns == null || returns.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        long winCount = returns.stream()
                .filter(return_ -> return_.compareTo(BigDecimal.ZERO) > 0)
                .count();
        
        return BigDecimal.valueOf(winCount)
                .divide(BigDecimal.valueOf(returns.size()), 4, RoundingMode.HALF_UP);
    }

    /**
     * 四舍五入到指定小数位
     */
    public static BigDecimal round(BigDecimal value, int scale) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        return value.setScale(scale, RoundingMode.HALF_UP);
    }

    /**
     * 百分比转换
     */
    public static BigDecimal toPercentage(BigDecimal value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        return value.multiply(BigDecimal.valueOf(100));
    }

    /**
     * 百分比转换（字符串）
     */
    public static String toPercentageString(BigDecimal value) {
        return toPercentage(value) + "%";
    }
}