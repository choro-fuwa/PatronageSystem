#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
策略回测引擎
用于Java通过JPype调用的Python回测模块
"""

import pandas as pd
import numpy as np
import json
from datetime import datetime, timedelta
import logging

# 配置日志
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

class BacktestEngine:
    """回测引擎主类"""
    
    def __init__(self):
        self.data = None
        self.positions = []
        self.cash = 0
        self.initial_capital = 0
        self.current_capital = 0
        self.trades = []
        self.equity_curve = []
        
    def load_data(self, data_path):
        """加载历史数据"""
        try:
            self.data = pd.read_csv(data_path)
            self.data['date'] = pd.to_datetime(self.data['date'])
            self.data = self.data.sort_values('date').reset_index(drop=True)
            logger.info(f"数据加载成功，共{len(self.data)}条记录")
            return True
        except Exception as e:
            logger.error(f"数据加载失败: {e}")
            return False
    
    def run_backtest(self, strategy_type, parameters, initial_capital):
        """运行回测"""
        try:
            self.initial_capital = initial_capital
            self.cash = initial_capital
            self.current_capital = initial_capital
            self.positions = []
            self.trades = []
            self.equity_curve = []
            
            # 解析策略参数
            if isinstance(parameters, str):
                params = json.loads(parameters)
            else:
                params = parameters
            
            # 根据策略类型选择策略
            if strategy_type == "ma_strategy":
                self._run_ma_strategy(params)
            elif strategy_type == "mean_reversion":
                self._run_mean_reversion_strategy(params)
            else:
                raise ValueError(f"不支持的策略类型: {strategy_type}")
            
            # 计算回测结果
            result = self._calculate_results()
            return result
            
        except Exception as e:
            logger.error(f"回测执行失败: {e}")
            return None
    
    def _run_ma_strategy(self, params):
        """双均线策略"""
        short_period = params.get('short_period', 5)
        long_period = params.get('long_period', 20)
        
        # 计算移动平均线
        self.data['ma_short'] = self.data['close'].rolling(window=short_period).mean()
        self.data['ma_long'] = self.data['close'].rolling(window=long_period).mean()
        
        # 生成信号
        self.data['signal'] = 0
        self.data.loc[self.data['ma_short'] > self.data['ma_long'], 'signal'] = 1  # 买入信号
        self.data.loc[self.data['ma_short'] < self.data['ma_long'], 'signal'] = -1  # 卖出信号
        
        # 执行交易
        for i in range(1, len(self.data)):
            current_signal = self.data.iloc[i]['signal']
            prev_signal = self.data.iloc[i-1]['signal']
            
            if current_signal != prev_signal and current_signal != 0:
                if current_signal == 1:  # 买入
                    self._buy(self.data.iloc[i]['date'], self.data.iloc[i]['close'])
                elif current_signal == -1:  # 卖出
                    self._sell(self.data.iloc[i]['date'], self.data.iloc[i]['close'])
            
            # 记录权益曲线
            self._update_equity_curve(self.data.iloc[i]['date'], self.data.iloc[i]['close'])
    
    def _run_mean_reversion_strategy(self, params):
        """均值回归策略"""
        lookback_period = params.get('lookback_period', 20)
        std_multiplier = params.get('std_multiplier', 2)
        
        # 计算移动平均和标准差
        self.data['ma'] = self.data['close'].rolling(window=lookback_period).mean()
        self.data['std'] = self.data['close'].rolling(window=lookback_period).std()
        
        # 生成信号
        self.data['signal'] = 0
        upper_band = self.data['ma'] + std_multiplier * self.data['std']
        lower_band = self.data['ma'] - std_multiplier * self.data['std']
        
        self.data.loc[self.data['close'] < lower_band, 'signal'] = 1  # 买入信号
        self.data.loc[self.data['close'] > upper_band, 'signal'] = -1  # 卖出信号
        
        # 执行交易
        for i in range(1, len(self.data)):
            current_signal = self.data.iloc[i]['signal']
            prev_signal = self.data.iloc[i-1]['signal']
            
            if current_signal != prev_signal and current_signal != 0:
                if current_signal == 1:  # 买入
                    self._buy(self.data.iloc[i]['date'], self.data.iloc[i]['close'])
                elif current_signal == -1:  # 卖出
                    self._sell(self.data.iloc[i]['date'], self.data.iloc[i]['close'])
            
            # 记录权益曲线
            self._update_equity_curve(self.data.iloc[i]['date'], self.data.iloc[i]['close'])
    
    def _buy(self, date, price):
        """买入操作"""
        if self.cash > 0:
            shares = int(self.cash / price)
            if shares > 0:
                cost = shares * price
                self.cash -= cost
                self.positions.append({
                    'date': date,
                    'shares': shares,
                    'price': price,
                    'type': 'buy'
                })
                self.trades.append({
                    'date': date,
                    'type': 'buy',
                    'shares': shares,
                    'price': price,
                    'value': cost
                })
                logger.info(f"买入: {date}, 价格: {price}, 数量: {shares}")
    
    def _sell(self, date, price):
        """卖出操作"""
        if self.positions:
            total_shares = sum(pos['shares'] for pos in self.positions)
            if total_shares > 0:
                proceeds = total_shares * price
                self.cash += proceeds
                self.positions = []  # 清空持仓
                self.trades.append({
                    'date': date,
                    'type': 'sell',
                    'shares': total_shares,
                    'price': price,
                    'value': proceeds
                })
                logger.info(f"卖出: {date}, 价格: {price}, 数量: {total_shares}")
    
    def _update_equity_curve(self, date, price):
        """更新权益曲线"""
        position_value = sum(pos['shares'] for pos in self.positions) * price
        self.current_capital = self.cash + position_value
        self.equity_curve.append({
            'date': date,
            'equity': self.current_capital,
            'cash': self.cash,
            'position_value': position_value
        })
    
    def _calculate_results(self):
        """计算回测结果"""
        if not self.equity_curve:
            return None
        
        equity_df = pd.DataFrame(self.equity_curve)
        trades_df = pd.DataFrame(self.trades)
        
        # 计算基本指标
        initial_equity = self.initial_capital
        final_equity = equity_df['equity'].iloc[-1]
        total_return = (final_equity - initial_equity) / initial_equity
        
        # 计算年化收益率
        start_date = equity_df['date'].iloc[0]
        end_date = equity_df['date'].iloc[-1]
        days = (end_date - start_date).days
        annual_return = (1 + total_return) ** (365 / days) - 1 if days > 0 else 0
        
        # 计算最大回撤
        equity_df['peak'] = equity_df['equity'].expanding().max()
        equity_df['drawdown'] = (equity_df['equity'] - equity_df['peak']) / equity_df['peak']
        max_drawdown = equity_df['drawdown'].min()
        
        # 计算夏普比率
        equity_df['daily_return'] = equity_df['equity'].pct_change()
        sharpe_ratio = equity_df['daily_return'].mean() / equity_df['daily_return'].std() * np.sqrt(252) if equity_df['daily_return'].std() > 0 else 0
        
        # 计算胜率
        if len(trades_df) > 0:
            winning_trades = len(trades_df[trades_df['type'] == 'sell'])
            total_trades = len(trades_df[trades_df['type'] == 'sell'])
            win_rate = winning_trades / total_trades if total_trades > 0 else 0
        else:
            win_rate = 0
            total_trades = 0
            winning_trades = 0
        
        result = {
            'initial_capital': initial_equity,
            'final_capital': final_equity,
            'total_return': total_return,
            'annual_return': annual_return,
            'max_drawdown': max_drawdown,
            'sharpe_ratio': sharpe_ratio,
            'win_rate': win_rate,
            'total_trades': total_trades,
            'winning_trades': winning_trades,
            'equity_curve': equity_df.to_dict('records'),
            'trades': trades_df.to_dict('records')
        }
        
        return result

# 全局回测引擎实例
_engine = BacktestEngine()

def run_backtest(strategy_type, parameters, data_path, initial_capital):
    """运行回测的全局函数，供Java调用"""
    try:
        # 加载数据
        if not _engine.load_data(data_path):
            return None
        
        # 运行回测
        result = _engine.run_backtest(strategy_type, parameters, initial_capital)
        return json.dumps(result) if result else None
        
    except Exception as e:
        logger.error(f"回测执行失败: {e}")
        return None

def get_backtest_progress(backtest_id):
    """获取回测进度"""
    return {
        'status': 'completed',
        'progress': 100
    }

# 测试代码
if __name__ == "__main__":
    # 生成测试数据
    dates = pd.date_range('2023-01-01', '2023-12-31', freq='D')
    np.random.seed(42)
    prices = 100 + np.cumsum(np.random.randn(len(dates)) * 0.5)
    
    test_data = pd.DataFrame({
        'date': dates,
        'open': prices * 0.99,
        'high': prices * 1.02,
        'low': prices * 0.98,
        'close': prices,
        'volume': np.random.randint(1000, 10000, len(dates))
    })
    
    # 保存测试数据
    test_data.to_csv('test_data.csv', index=False)
    
    # 运行测试回测
    result = run_backtest(
        strategy_type="ma_strategy",
        parameters='{"short_period": 5, "long_period": 20}',
        data_path="test_data.csv",
        initial_capital=100000
    )
    
    print("回测结果:")
    print(result) 