#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
策略回测引擎
"""

import pandas as pd
import numpy as np
import json
import sys
from datetime import datetime, timedelta
from typing import Dict, List, Any

class BacktestEngine:
    """回测引擎类"""
    
    def __init__(self, initial_capital: float = 100000):
        self.initial_capital = initial_capital
        self.current_capital = initial_capital
        self.positions = {}
        self.trades = []
        self.daily_returns = []
        
    def run_backtest(self, strategy_params: Dict[str, Any], 
                    start_date: str, end_date: str) -> Dict[str, Any]:
        """
        执行回测
        
        Args:
            strategy_params: 策略参数
            start_date: 开始日期
            end_date: 结束日期
            
        Returns:
            回测结果字典
        """
        try:
            # 模拟股票数据（实际应用中应该从数据库或API获取）
            stock_data = self._generate_mock_data(start_date, end_date)
            
            # 执行策略
            self._execute_strategy(stock_data, strategy_params)
            
            # 计算回测指标
            results = self._calculate_metrics()
            
            return results
            
        except Exception as e:
            return {
                "error": str(e),
                "success": False
            }
    
    def _generate_mock_data(self, start_date: str, end_date: str) -> pd.DataFrame:
        """生成模拟股票数据"""
        start = datetime.strptime(start_date, "%Y-%m-%d")
        end = datetime.strptime(end_date, "%Y-%m-%d")
        
        # 生成日期序列
        date_range = pd.date_range(start=start, end=end, freq='D')
        
        # 模拟股票数据
        np.random.seed(42)  # 固定随机种子，确保结果可重现
        returns = np.random.normal(0.0005, 0.02, len(date_range))  # 日收益率
        
        # 计算价格序列
        prices = [100]  # 初始价格
        for ret in returns[1:]:
            prices.append(prices[-1] * (1 + ret))
        
        # 创建DataFrame
        data = pd.DataFrame({
            'date': date_range,
            'close': prices,
            'volume': np.random.randint(1000000, 10000000, len(date_range))
        })
        
        return data
    
    def _execute_strategy(self, data: pd.DataFrame, params: Dict[str, Any]):
        """执行策略逻辑"""
        strategy_type = params.get('strategy_type', 'trend_following')
        
        if strategy_type == 'trend_following':
            self._trend_following_strategy(data, params)
        elif strategy_type == 'mean_reversion':
            self._mean_reversion_strategy(data, params)
        else:
            # 默认简单买入持有策略
            self._buy_and_hold_strategy(data)
    
    def _trend_following_strategy(self, data: pd.DataFrame, params: Dict[str, Any]):
        """趋势跟踪策略"""
        lookback = params.get('lookback_period', 20)
        
        for i in range(lookback, len(data)):
            # 计算移动平均
            ma_short = data['close'].iloc[i-lookback:i].mean()
            ma_long = data['close'].iloc[i-lookback*2:i].mean()
            
            current_price = data['close'].iloc[i]
            
            # 金叉买入，死叉卖出
            if ma_short > ma_long and 'STOCK' not in self.positions:
                # 买入
                shares = int(self.current_capital * 0.95 / current_price)
                if shares > 0:
                    self.positions['STOCK'] = shares
                    self.current_capital -= shares * current_price
                    self.trades.append({
                        'date': data['date'].iloc[i],
                        'action': 'BUY',
                        'price': current_price,
                        'shares': shares
                    })
            
            elif ma_short < ma_long and 'STOCK' in self.positions:
                # 卖出
                shares = self.positions['STOCK']
                self.current_capital += shares * current_price
                del self.positions['STOCK']
                self.trades.append({
                    'date': data['date'].iloc[i],
                    'action': 'SELL',
                    'price': current_price,
                    'shares': shares
                })
    
    def _mean_reversion_strategy(self, data: pd.DataFrame, params: Dict[str, Any]):
        """均值回归策略"""
        lookback = params.get('lookback_period', 20)
        std_multiplier = params.get('std_multiplier', 2.0)
        
        for i in range(lookback, len(data)):
            # 计算布林带
            window = data['close'].iloc[i-lookback:i]
            ma = window.mean()
            std = window.std()
            
            upper_band = ma + std_multiplier * std
            lower_band = ma - std_multiplier * std
            
            current_price = data['close'].iloc[i]
            
            # 价格突破下轨买入，突破上轨卖出
            if current_price < lower_band and 'STOCK' not in self.positions:
                shares = int(self.current_capital * 0.95 / current_price)
                if shares > 0:
                    self.positions['STOCK'] = shares
                    self.current_capital -= shares * current_price
                    self.trades.append({
                        'date': data['date'].iloc[i],
                        'action': 'BUY',
                        'price': current_price,
                        'shares': shares
                    })
            
            elif current_price > upper_band and 'STOCK' in self.positions:
                shares = self.positions['STOCK']
                self.current_capital += shares * current_price
                del self.positions['STOCK']
                self.trades.append({
                    'date': data['date'].iloc[i],
                    'action': 'SELL',
                    'price': current_price,
                    'shares': shares
                })
    
    def _buy_and_hold_strategy(self, data: pd.DataFrame):
        """买入持有策略"""
        if not self.trades:  # 只在第一天买入
            current_price = data['close'].iloc[0]
            shares = int(self.current_capital * 0.95 / current_price)
            if shares > 0:
                self.positions['STOCK'] = shares
                self.current_capital -= shares * current_price
                self.trades.append({
                    'date': data['date'].iloc[0],
                    'action': 'BUY',
                    'price': current_price,
                    'shares': shares
                })
    
    def _calculate_metrics(self) -> Dict[str, Any]:
        """计算回测指标"""
        # 计算最终资金
        final_capital = self.current_capital
        for stock, shares in self.positions.items():
            # 假设最后以100元卖出
            final_capital += shares * 100
        
        # 计算收益率
        total_return = (final_capital - self.initial_capital) / self.initial_capital
        
        # 计算年化收益率（假设回测期为一年）
        annual_return = (1 + total_return) ** (252 / len(self.daily_returns)) - 1 if self.daily_returns else total_return
        
        # 计算最大回撤
        max_drawdown = self._calculate_max_drawdown()
        
        # 计算夏普比率
        sharpe_ratio = self._calculate_sharpe_ratio()
        
        # 计算胜率
        win_rate = self._calculate_win_rate()
        
        return {
            "success": True,
            "initial_capital": self.initial_capital,
            "final_capital": final_capital,
            "total_return": total_return,
            "annual_return": annual_return,
            "max_drawdown": max_drawdown,
            "sharpe_ratio": sharpe_ratio,
            "win_rate": win_rate,
            "trade_count": len(self.trades),
            "trades": self.trades,
            "daily_returns": self.daily_returns
        }
    
    def _calculate_max_drawdown(self) -> float:
        """计算最大回撤"""
        if not self.daily_returns:
            return 0.0
        
        cumulative = np.cumprod(1 + np.array(self.daily_returns))
        running_max = np.maximum.accumulate(cumulative)
        drawdown = (cumulative - running_max) / running_max
        return float(np.min(drawdown))
    
    def _calculate_sharpe_ratio(self) -> float:
        """计算夏普比率"""
        if not self.daily_returns:
            return 0.0
        
        returns = np.array(self.daily_returns)
        if len(returns) < 2:
            return 0.0
        
        mean_return = np.mean(returns)
        std_return = np.std(returns)
        
        if std_return == 0:
            return 0.0
        
        # 假设无风险利率为0
        sharpe_ratio = mean_return / std_return * np.sqrt(252)
        return float(sharpe_ratio)
    
    def _calculate_win_rate(self) -> float:
        """计算胜率"""
        if not self.trades:
            return 0.0
        
        wins = 0
        for i in range(1, len(self.trades), 2):  # 每对买卖交易
            if i < len(self.trades):
                buy_price = self.trades[i-1]['price']
                sell_price = self.trades[i]['price']
                if sell_price > buy_price:
                    wins += 1
        
        return wins / (len(self.trades) // 2) if len(self.trades) > 1 else 0.0

def main():
    """主函数，用于命令行调用"""
    if len(sys.argv) < 2:
        print("Usage: python backtest_engine.py <json_params>")
        sys.exit(1)
    
    try:
        # 解析JSON参数
        params = json.loads(sys.argv[1])
        
        # 创建回测引擎
        engine = BacktestEngine(initial_capital=params.get('initial_capital', 100000))
        
        # 执行回测
        results = engine.run_backtest(
            strategy_params=params.get('strategy_params', {}),
            start_date=params.get('start_date', '2024-01-01'),
            end_date=params.get('end_date', '2024-12-31')
        )
        
        # 输出结果
        print(json.dumps(results, indent=2, default=str))
        
    except Exception as e:
        print(json.dumps({
            "error": str(e),
            "success": False
        }))

if __name__ == "__main__":
    main() 