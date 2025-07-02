import pandas as pd

def calculate_derived_factor(factor_a: pd.Series, factor_b: pd.Series, weight_a: float, weight_b: float) -> pd.Series:
    """计算衍生因子：weight_a * factor_a + weight_b * factor_b"""
    if not (abs(weight_a + weight_b - 1.0) < 1e-6):
        raise ValueError("权重之和必须等于1")

    return factor_a * weight_a + factor_b * weight_b

def validate_formula(weights: list) -> bool:
    """验证公式权重是否合法"""
    return abs(sum(weights) - 1.0) < 1e-6