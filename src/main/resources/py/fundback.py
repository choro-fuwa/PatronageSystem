import pandas as pd
import os
import numpy as np
import requests
import json
from string import Template
import random
import time
from math import floor
import matplotlib.pyplot as plt
import seaborn as sns

# 不支持中文的解决办法
plt.rcParams['font.sans-serif'] = ['Arial Unicode MS']  # 用来正常显示中文标签


class FundBT():
    def __init__(self, fundsList, loseStatusAssignFundCode, eachAdditionalCaptital: int, onRebalance: bool = True,
                 inititalCaptital: int = 0, rebalanceRate: float = 0.05, startDate: str = "2018-01-01",
                 endDate: str = "2023-12-01"):
        self.totalCaptital = 0
        self.totalAdditionalCaptital = 0
        self.totalFee = 0
        self.inititalCaptital = inititalCaptital
        self.eachAdditionalCaptital = eachAdditionalCaptital
        self.startDate = startDate
        self.endDate = endDate
        self.maxDrawdown = 0
        self.rebalanceRate = rebalanceRate
        self.onRebalance = onRebalance

        self.rebalanceCaptitalPool = 0

        # 记录缺失数据的基金，用于计算初始资金的时候跳过
        self.ignore_funds = []
        # 记录缺失数据的基金，将其比例汇总，后期归到_loseStatusAssignFundCode基金的比例中
        self.ignore_funds_rate = 0

        # 用于控制当前计算的日期
        self._currentDate = 0

        # 记录最大的时间，如果超过则代表计算结束
        self._maxDate = 0

        # 当其他基金是后期加入或者在定投日没有数据的时候，将会把没有数据的基金份额增加到该基金上去
        # 该基金时间范围必须为最长。
        self._loseStatusAssignFundCode = loseStatusAssignFundCode

        # 最后一个交易日
        self._last_exchange_day = 1

        # 初始化基础数据和格式
        self.fundsList = {}

        # 用于计算整个资金情况
        self.totalCaptitalData = None

        # 已购买基金，列表形式，避免因为基金本身的特殊原因，比如境外、暂停等原因导致没有数据造成计算错误
        self.purchasedFunds = set()

        for fund in fundsList:
            fundFormat = {
                "name": fund["name"],
                "code": fund["code"],
                "maxDrawdown": 0,
                "rate": fund["rate"],
                "purchaseFee": fund["purchaseFee"],
                "sellFee": fund["sellFee"],
                "lastMonth": 0,  # 用于控制当前基金是否要定投
                "data": None
            }
            self.fundsList[fund["code"]] = fundFormat

    def getFundData(self, code):
        page = 1
        resultData = None

        # 设置请求头 - 模拟浏览器访问
        headers = {
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36',
            'Referer': f'http://fundf10.eastmoney.com/jjjz_{code}.html',
            'Host': 'api.fund.eastmoney.com'
        }

        # 设置URL模板 - 天天基金API
        url_Template = Template(
            "http://api.fund.eastmoney.com/f10/lsjz?"
            "fundCode=${code}&"
            "pageIndex=${page}&"
            "pageSize=200&"  # 每页200条记录
            "startDate=${startDate}&"
            "endDate=${endDate}&"
            "_=${timestamp}"  # 时间戳防止缓存
        )

        while True:
            # 生成当前时间戳
            timestamp = int(time.time() * 1000)

            # 构建URL
            url = url_Template.substitute(
                code=code,
                page=page,
                startDate=self.startDate,
                endDate=self.endDate,
                timestamp=timestamp
            )

            try:
                response = requests.get(url, headers=headers, timeout=10)
                response.raise_for_status()  # 检查HTTP错误

                # 解析JSON响应
                json_data = response.json()

                # 检查返回数据是否有效
                if json_data["Data"] is None:
                    print(f"基金 {code} 无数据，错误信息: {json_data.get('ErrMsg', '未知错误')}")
                    break

                data_list = json_data["Data"]["LSJZList"]

                # 如果没有数据则结束
                if not data_list:
                    break

                # 创建临时DataFrame
                temp_df = pd.DataFrame(data_list)

                # 合并数据
                if resultData is None:
                    resultData = temp_df
                else:
                    resultData = pd.concat([resultData, temp_df])

                # 检查是否还有更多数据
                total_count = json_data["TotalCount"]
                current_count = page * 200
                if current_count >= total_count:
                    break

                page += 1

                # 随机休眠防止封IP
                sleep_time = random.uniform(1, 3)
                time.sleep(sleep_time)

            except requests.exceptions.RequestException as e:
                print(f"请求基金 {code} 数据失败: {e}")
                break
            except json.JSONDecodeError as e:
                print(f"解析基金 {code} JSON 数据失败: {e}")
                break
            except KeyError as e:
                print(f"解析基金 {code} 数据结构错误: {e}")
                break

        return resultData

    def checkFundData(self):
        willGetDataFundList = []  # 存放没有本地数据，需要网络获取的数据
        baseDir = './data/'

        # 确保数据目录存在
        if not os.path.exists(baseDir):
            os.makedirs(baseDir)

        for code in self.fundsList.keys():
            code = self.fundsList[code]["code"]
            data = None
            filenameXLSX = baseDir + code + '-' + self.startDate + '-' + self.endDate + '.xlsx'
            filenameCSV = baseDir + code + '-' + self.startDate + '-' + self.endDate + '.csv'

            # 根据类型进行读取数据
            if os.path.exists(filenameXLSX):
                data = pd.read_excel(filenameXLSX)
            elif os.path.exists(filenameCSV):
                data = pd.read_csv(filenameCSV)
            else:
                # 如果不存在，则把当前循环的基金代码存入待获取列表中，并且跳过当前循环
                willGetDataFundList.append(code)
                continue

            self.fundsList[code]["data"] = data[["FSRQ", "DWJZ"]].copy(deep=True)

            # 显性转换类型
            self.fundsList[code]["data"]["FSRQ"] = pd.to_datetime(self.fundsList[code]["data"]["FSRQ"])
            self.fundsList[code]["data"]["DWJZ"] = pd.to_numeric(self.fundsList[code]["data"]["DWJZ"])

            self.fundsList[code]["data"]["captital"] = 0.0
            self.fundsList[code]["data"]["share"] = 0.0
            self.fundsList[code]["data"]["Drawdown"] = 0.0
            self.fundsList[code]["data"]["purchaseFee"] = 0.0
            self.fundsList[code]["data"]["sellFee"] = 0.0
            self.fundsList[code]["data"]["additionalCaptital"] = 0.0
            self.fundsList[code]["data"]["rebalance"] = 0.0

            # 按时间排序，从小到大
            # self.fundsList[fundCodeKey]["data"].sort_values(by="FSRQ", inplace=True)

        # 开始获取新数据
        for code in willGetDataFundList:
            print(f"正在获取基金 {code} 数据...")
            data = self.getFundData(code)

            if data is None or len(data) == 0:
                print(f"基金 {code} 数据获取失败，跳过处理")
                continue

            # 将获取的数据写入本地
            data.to_excel(baseDir + code + '-' + self.startDate + '-' + self.endDate + '.xlsx', index=False)

            # 将获取的数据传递给基金
            self.fundsList[code]["data"] = data[["FSRQ", "DWJZ"]].copy(deep=True)

            # 显性转换类型
            self.fundsList[code]["data"]["FSRQ"] = pd.to_datetime(self.fundsList[code]["data"]["FSRQ"])
            self.fundsList[code]["data"]["DWJZ"] = pd.to_numeric(self.fundsList[code]["data"]["DWJZ"])

            self.fundsList[code]["data"]["captital"] = 0.0
            self.fundsList[code]["data"]["share"] = 0.0
            self.fundsList[code]["data"]["Drawdown"] = 0.0
            self.fundsList[code]["data"]["purchaseFee"] = 0.0
            self.fundsList[code]["data"]["sellFee"] = 0.0
            self.fundsList[code]["data"]["additionalCaptital"] = 0.0
            self.fundsList[code]["data"]["rebalance"] = 0.0

        # 数据排序，按照日期大小，并设置日期为索引
        for code in self.fundsList.keys():
            if self.fundsList[code]["data"] is not None:
                self.fundsList[code]["data"].sort_values(by='FSRQ', inplace=True)
                self.fundsList[code]["data"].set_index("FSRQ", inplace=True)

        # 生成总资金df表
        if self._loseStatusAssignFundCode in self.fundsList and self.fundsList[self._loseStatusAssignFundCode][
            "data"] is not None:
            self.captitalData = pd.DataFrame(index=self.fundsList[self._loseStatusAssignFundCode]["data"].index,
                                             columns=['captital', 'drawdown'])
            self.captitalData.fillna(0.0, inplace=True)
        else:
            print(f"主基金 {self._loseStatusAssignFundCode} 数据无效")
            self.captitalData = pd.DataFrame(columns=['captital', 'drawdown'])

    def outData(self):

        _totalCaptital = 0.0
        _totalAddCaptital = 0.0
        _startDate = pd.to_datetime(self.startDate) + pd.Timedelta(days=1)
        # _lastDate = pd.to_datetime(self.endDate)  -  pd.Timedelta(days=1)
        _lastDate = pd.to_datetime(self.endDate)
        _totalSellFee = 0.0
        _totalPurchaseFee = 0.0

        self.totalCaptitalData = pd.DataFrame(
            columns=['基金名字', '最早数据时间', '最晚数据时间', '总投入资金', '账户资金', '盈亏资金', '盈亏率',
                     '最大回撤', '买入手续费合计', '卖出手续费合计'])

        for code in self.fundsList.keys():
            _currentFund = self.fundsList[code]

            # 检查基金数据是否有效
            if _currentFund["data"] is None:
                print(f"基金 {code} 数据无效，跳过输出")
                continue

            try:
                _currentCaptital = _currentFund["data"].loc[_lastDate, 'captital']
            except KeyError:
                # 如果结束日期没有数据，使用最后一条有效数据
                last_valid_idx = _currentFund["data"].index.max()
                if last_valid_idx < _lastDate:
                    _lastDate = last_valid_idx
                _currentCaptital = _currentFund["data"].loc[last_valid_idx, 'captital']

            _totalCaptital += _currentCaptital
            _currentAddCaptital = _currentFund["data"]['additionalCaptital'].sum()
            _totalAddCaptital += _currentAddCaptital

            _currentSellFee = _currentFund["data"]['sellFee'].sum()
            _currentPurchaseFee = _currentFund["data"]['purchaseFee'].sum()

            _totalSellFee += _currentSellFee
            _totalPurchaseFee += _currentPurchaseFee

            # 计算最大回撤
            try:
                maxDWJZID = _currentFund['data']['DWJZ'].idxmax()
                maxDWJZ = _currentFund['data'].loc[maxDWJZID, 'DWJZ']
                minDWJZID = _currentFund['data']['DWJZ'].idxmin()
                minDWJZ = _currentFund['data'].loc[minDWJZID, 'DWJZ']
                maxDrawdown = (maxDWJZ - minDWJZ) / maxDWJZ * 100
            except:
                maxDrawdown = 0
                print(f"计算基金 {code} 最大回撤时出错")

            # 获取最早和最晚日期
            try:
                earliest_date = _currentFund['data'].index.min().strftime('%Y-%m-%d')
                latest_date = _currentFund['data'].index.max().strftime('%Y-%m-%d')
            except:
                earliest_date = "N/A"
                latest_date = "N/A"

            data = {
                '基金名字': _currentFund['name'],
                '最早数据时间': earliest_date,
                '最晚数据时间': latest_date,
                '总投入资金': _currentAddCaptital,
                '账户资金': _currentCaptital,
                '盈亏资金': _currentCaptital - _currentAddCaptital,
                '盈亏率': ((_currentCaptital / _currentAddCaptital) - 1) * 100 if _currentAddCaptital > 0 else 0,
                '最大回撤': maxDrawdown,
                '买入手续费合计': _currentPurchaseFee,
                '卖出手续费合计': _currentSellFee
            }

            self.totalCaptitalData = pd.concat([self.totalCaptitalData, pd.DataFrame([data], index=[code])],
                                               ignore_index=True)

        print('''
    """""""""""""""""""""""""""""""""""""""""""""""""""
    所有资金汇总

    总投入资金：{}， 账户资金：{}， 盈亏资金：{}， 盈亏率：{:.2f}%
    买入手续费合计：{}，卖出手续费合计：{}
    """""""""""""""""""""""""""""""""""""""""""""""""""
            '''.format(
            _totalAddCaptital,
            _totalCaptital,
            _totalCaptital - _totalAddCaptital,
            ((_totalCaptital / _totalAddCaptital) - 1) * 100 if _totalAddCaptital > 0 else 0,
            _totalPurchaseFee,
            _totalSellFee
        )
        )

        print(self.totalCaptitalData)

    def simulationsCalc(self):
        # 检查主基金数据是否有效
        if self._loseStatusAssignFundCode not in self.fundsList or self.fundsList[self._loseStatusAssignFundCode][
            "data"] is None:
            print(f"主基金 {self._loseStatusAssignFundCode} 数据无效，无法进行计算")
            return

        # 将_loseStatusAssignFundCode基金开始时间当作初始的时间指针
        self._currentDate = self.fundsList[self._loseStatusAssignFundCode]['data'].index[0]

        # 将_loseStatusAssignFundCode基金结束时间当作结束的时间指针
        self._maxDate = self.fundsList[self._loseStatusAssignFundCode]['data'].index[-1]

        while self._currentDate <= self._maxDate:

            # 每次日期变更则重制缺失的基金列表和缺失基金的比例
            self.ignore_funds = []
            self.ignore_funds_rate = 0

            # 开始循环当前日期的每个基金是否有不存在数据的
            for code in self.fundsList.keys():
                # 跳过无效基金
                if self.fundsList[code]["data"] is None:
                    continue

                _currentFund = self.fundsList[code]

                # 检查基金是否有当前日期的数据
                if self._currentDate not in _currentFund["data"].index:
                    self.ignore_funds.append(code)
                    self.ignore_funds_rate += _currentFund["rate"]

            # 开始循环当前日期的每个基金
            for code in self.fundsList.keys():
                # 跳过无效基金
                if self.fundsList[code]["data"] is None:
                    continue

                # 跳过当前日期没有数据的基金
                if code in self.ignore_funds:
                    continue

                _currentFund = self.fundsList[code]

                # 查找当前日期当前基金的上个工作日
                self._last_exchange_day = 1

                # 确保日期索引是datetime类型
                if not isinstance(_currentFund["data"].index, pd.DatetimeIndex):
                    continue

                while (self._currentDate - pd.Timedelta(days=self._last_exchange_day)) not in _currentFund[
                    "data"].index:
                    # 如果检查到基金第一条数据的日期和当前日期相等，则说明为新增基金
                    if _currentFund["data"].iloc[0].name == self._currentDate:
                        self._last_exchange_day = 0
                        break

                    self._last_exchange_day += 1

                    # 防止无限循环
                    if self._last_exchange_day > 30:
                        print(f"基金 {code} 在日期 {self._currentDate} 找不到前一个交易日")
                        self._last_exchange_day = 1
                        break

                # 检查是不是第一次计算
                if self._currentDate == self.fundsList[self._loseStatusAssignFundCode]['data'].index[0]:
                    self.calcInitCaptital(_currentFund=_currentFund)
                    self.purchasedFunds.add(code)
                    _currentFund['lastMonth'] = self._currentDate.month

                # 如果检查到月份变化，则进行定投
                elif self._currentDate.month != _currentFund['lastMonth']:
                    self.calcAdditionalCaptital(_currentFund=_currentFund)
                    self.purchasedFunds.add(code)
                    _currentFund['lastMonth'] = self._currentDate.month

                # 非上述两种情况，则只计算每日盈亏情况
                else:
                    self.calcIncome(_currentFund=_currentFund)

            # 再平衡检查
            if self.onRebalance:
                self.calcRebalance()

            # 当前日期的所有基金计算执行完成后，将当前日期加1
            self._currentDate += pd.Timedelta(days=1)

    def calcRebalance(self):
        # 再平衡函数

        # 用于储存当前日期下的所有基金中的资金总和
        _totalCaptital = 0

        # 用于储存基金超过阈值，需要再平衡的基金
        _willRebalanceFunds = []
        _willPurchaseFunds = []

        # 单独获取需要调整的基金比例
        _willFundRateAdjust = 0.0

        # 计算出当前日期的总体资金情况
        for code in self.purchasedFunds:
            # 跳过无效基金
            if code not in self.fundsList or self.fundsList[code]["data"] is None:
                continue

            # 跳过当前日期没有数据的基金
            if code in self.ignore_funds:
                continue

            _currentFund = self.fundsList[code]
            _totalCaptital += _currentFund["data"].loc[self._currentDate, 'captital']

        # 循环原始基金列表
        for code_sub in self.fundsList:
            # 将原始基金列表中还没有购买的基金比例加入到指定基金中
            if code_sub not in self.purchasedFunds:
                _willFundRateAdjust += self.fundsList[code_sub]["rate"]

        # 计算目前基金占比情况
        for code in self.purchasedFunds:
            # 跳过无效基金
            if code not in self.fundsList or self.fundsList[code]["data"] is None:
                continue

            # 跳过当前日期没有数据的基金
            if code in self.ignore_funds:
                continue

            _currentFund = self.fundsList[code]
            _fundRate = _currentFund["rate"]

            if code == self._loseStatusAssignFundCode:
                _fundRateAdjust = _fundRate + _willFundRateAdjust
            else:
                _fundRateAdjust = _fundRate

            # 得到当前日期基金的占总体资金的比例
            try:
                _currentFundRate = _currentFund["data"].loc[self._currentDate, 'captital'] / _totalCaptital
            except:
                # 如果分母为0，跳过计算
                continue

            # 计算比例差
            _currentFundRateAdjustDiff = _currentFundRate - _fundRateAdjust

            # 收集再平衡数据
            rebalanceStatus = [
                code,
                _fundRate,
                _fundRateAdjust,
                _currentFundRate,
                _currentFundRateAdjustDiff,
                _currentFund["data"].loc[self._currentDate, 'DWJZ'],
                _currentFund["data"].loc[self._currentDate, 'share'],
                _currentFund["data"].loc[self._currentDate, 'captital'],
                _totalCaptital * _currentFundRateAdjustDiff
            ]

            # 记录超过再平衡阈值的基金
            if _currentFundRateAdjustDiff >= self.rebalanceRate:
                _willRebalanceFunds.append(rebalanceStatus)

            # 记录低于目标比例的基金
            if _currentFundRateAdjustDiff < 0:
                _willPurchaseFunds.append(rebalanceStatus)

        # 记录卖出的基金后资金临时存放的池子
        captitalPool = 0

        # 开始处理需要再平衡卖出的基金
        for rebalanceData in _willRebalanceFunds:
            code = rebalanceData[0]
            _currentFund = self.fundsList[code]

            currentShare = _currentFund["data"].loc[self._currentDate, 'share']
            currentCaptital = _currentFund["data"].loc[self._currentDate, 'captital']
            currentSellFeeRate = _currentFund["sellFee"]
            currentDWJZ = _currentFund["data"].loc[self._currentDate, 'DWJZ']
            currentAppendCaptital = _currentFund["data"].loc[self._currentDate, 'additionalCaptital']

            # 计算原始卖出资金
            willSellCaptital = rebalanceData[8]

            # 计算需要卖出的资金手续费
            willSellFee = willSellCaptital * currentSellFeeRate

            # 计算实际卖出资金，扣除卖出手续费
            willSellActualCaptital = willSellCaptital - willSellFee

            # 计算实际卖出份额
            willSellShare = willSellActualCaptital / currentDWJZ

            # 设置卖掉后新的基金份额
            _currentFund['data'].loc[self._currentDate, 'share'] = currentShare - willSellShare

            # 设置卖掉后新的基金金额
            _currentFund['data'].loc[self._currentDate, 'captital'] = currentCaptital - willSellCaptital

            # 设置当前追加的资金
            _currentFund['data'].loc[
                self._currentDate, 'additionalCaptital'] = currentAppendCaptital - willSellCaptital

            # 设置卖出手续费
            _currentFund['data'].loc[self._currentDate, 'sellFee'] = willSellFee

            # 设置再平衡标签
            _currentFund['data'].loc[self._currentDate, 'rebalance'] = 1

            # 卖掉的基金进入资金池
            captitalPool = captitalPool + willSellActualCaptital

        # 开始处理需要再平衡买的基金
        for rebalanceData in _willPurchaseFunds:
            if captitalPool == 0:
                break

            code = rebalanceData[0]
            _currentFund = self.fundsList[code]

            currentShare = _currentFund["data"].loc[self._currentDate, 'share']
            currentCaptital = _currentFund["data"].loc[self._currentDate, 'captital']
            currentPurchaseFeeRate = _currentFund['purchaseFee']
            currentPurchase = _currentFund["data"].loc[self._currentDate, 'purchaseFee']
            currentDWJZ = _currentFund["data"].loc[self._currentDate, 'DWJZ']
            currentAppendCaptital = _currentFund["data"].loc[self._currentDate, 'additionalCaptital']

            # 计算需要买入的资金
            if captitalPool <= abs(rebalanceData[8]):
                willPurchaseCaptital = captitalPool
            else:
                willPurchaseCaptital = abs(rebalanceData[8])

            # 计算需要买入的资金手续费
            willPurchaseFee = willPurchaseCaptital * currentPurchaseFeeRate

            # 计算扣除手续费后实际买入资金
            willPurchaseActualCaptital = willPurchaseCaptital - willPurchaseFee

            # 计算需要买入的份额
            willPurchaseShare = willPurchaseActualCaptital / currentDWJZ

            # 设置买入后新的基金份额
            _currentFund['data'].loc[self._currentDate, 'share'] = currentShare + willPurchaseShare

            # 设置买入后新的基金金额
            _currentFund['data'].loc[self._currentDate, 'captital'] = currentCaptital + willPurchaseActualCaptital

            # 设置追加的资金
            _currentFund['data'].loc[
                self._currentDate, 'additionalCaptital'] = currentAppendCaptital + willPurchaseCaptital

            # 设置买入手续费
            _currentFund['data'].loc[self._currentDate, 'purchaseFee'] = currentPurchase + willPurchaseFee

            # 设置再平衡标签
            _currentFund['data'].loc[self._currentDate, 'rebalance'] = 1

            # 基金资金池，减去已购买费用
            captitalPool = captitalPool - willPurchaseCaptital

    def calcInitCaptital(self, _currentFund):
        # 开始计算初始资金

        # 如果检查到是最长时间基金，则将起没有数据的基金投资比例合并一起
        if self._loseStatusAssignFundCode == _currentFund["code"]:
            _currentCaptital = self.inititalCaptital * (_currentFund["rate"] + self.ignore_funds_rate)

        # 计算当前基金投入比例
        else:
            _currentCaptital = self.inititalCaptital * _currentFund["rate"]

        _currentPurcaseFee = _currentCaptital * _currentFund["purchaseFee"]  # 计算当前基金买入手续费
        _currentActualCaptital = _currentCaptital - _currentPurcaseFee  # 计算当前基金买入扣除手续费后，实际投入资金

        _currentFund["data"].loc[self._currentDate, "additionalCaptital"] = _currentCaptital  # 记录当前基金本次实际投入资本，包含手续费
        _currentFund["data"].loc[self._currentDate, "purchaseFee"] = _currentPurcaseFee  # 记录当前基金本次手续费
        _currentFund["data"].loc[self._currentDate, "captital"] = _currentActualCaptital  # 记录当前基金实际投入资金，扣除了手续费
        _currentFund["data"].loc[self._currentDate, "share"] = _currentActualCaptital / _currentFund["data"].loc[
            self._currentDate, "DWJZ"]  # 记录当前购买份额

    def calcIncome(self, _currentFund):
        # 收益计算

        _currentFund["data"].loc[self._currentDate, "share"] = _currentFund["data"].loc[
            self._currentDate - pd.Timedelta(days=self._last_exchange_day), "share"]

        _currentFund["data"].loc[self._currentDate, "captital"] = _currentFund["data"].loc[self._currentDate, "DWJZ"] * \
                                                                  _currentFund["data"].loc[self._currentDate, "share"]

        # 计算到当天的回撤值
        try:
            prev_DWJZ = _currentFund["data"].loc[
                self._currentDate - pd.Timedelta(days=self._last_exchange_day), "DWJZ"]
            _currentFund["data"].loc[self._currentDate, "Drawdown"] = (_currentFund["data"].loc[
                                                                           self._currentDate, "DWJZ"] - prev_DWJZ) / \
                                                                      _currentFund["data"].loc[
                                                                          self._currentDate, "DWJZ"]
        except:
            # 如果无法计算回撤，设为0
            _currentFund["data"].loc[self._currentDate, "Drawdown"] = 0

    def calcAdditionalCaptital(self, _currentFund):
        # 每月定投函数

        # 获取上期份额
        _lastShare = _currentFund["data"].loc[
            self._currentDate - pd.Timedelta(days=self._last_exchange_day), "share"]

        # 获取上期资金
        _lastCaptital = _currentFund["data"].loc[
            self._currentDate - pd.Timedelta(days=self._last_exchange_day), "captital"]

        # 如果检查到是最长时间基金，则将起没有数据的基金投资比例合并一起
        if self._loseStatusAssignFundCode == _currentFund["code"]:
            _currentCaptital = self.eachAdditionalCaptital * (_currentFund["rate"] + self.ignore_funds_rate)
        # 计算当前基金投入比例
        else:
            _currentCaptital = self.eachAdditionalCaptital * _currentFund["rate"]

        # 计算当期基金买入手续费
        _currentPurcaseFee = _currentCaptital * _currentFund["purchaseFee"]

        # 计算当期基金买入扣除手续费后，实际投入资金
        _currentActualCaptital = _currentCaptital - _currentPurcaseFee

        # 计算当期份额
        _currentShare = _currentActualCaptital / _currentFund["data"].loc[self._currentDate, "DWJZ"]

        # 将当期数据写入数据表
        _currentFund["data"].loc[self._currentDate, "share"] = _lastShare + _currentShare
        _currentFund["data"].loc[self._currentDate, "captital"] = _lastCaptital + _currentActualCaptital
        _currentFund["data"].loc[self._currentDate, "purchaseFee"] = _currentPurcaseFee
        _currentFund["data"].loc[self._currentDate, "additionalCaptital"] = _currentCaptital

        # 计算到当天的回撤值
        try:
            prev_DWJZ = _currentFund["data"].loc[
                self._currentDate - pd.Timedelta(days=self._last_exchange_day), "DWJZ"]
            _currentFund["data"].loc[self._currentDate, "Drawdown"] = (_currentFund["data"].loc[
                                                                           self._currentDate, "DWJZ"] - prev_DWJZ) / \
                                                                      _currentFund["data"].loc[
                                                                          self._currentDate, "DWJZ"]
        except:
            _currentFund["data"].loc[self._currentDate, "Drawdown"] = 0


fundsList = [
    {"code": "163407", "name": "兴全沪深300增强A", "rate": 0.6, "purchaseFee": 0.0012, "sellFee": 0.005},
    {"code": "160119", "name": "南方中证500ETF联接(LOF)A", "rate": 0.4, "purchaseFee": 0.0012, "sellFee": 0.005}
]

# 创建回测实例
fundBT = FundBT(fundsList=fundsList, loseStatusAssignFundCode="163407", eachAdditionalCaptital=5000, rebalanceRate=0.05,
                inititalCaptital=10000, startDate="2020-01-01", endDate="2024-12-01")

# 获取基金数据（使用爬虫）
fundBT.checkFundData()

# 执行回测计算
fundBT.simulationsCalc()

# 输出结果
fundBT.outData()

fundBT.totalCaptitalData

# 生成净值走势图
if "163407" in fundBT.fundsList and fundBT.fundsList["163407"]["data"] is not None:
    allData = pd.DataFrame(index=fundBT.fundsList['163407']['data'].index, columns=fundBT.fundsList.keys())

    for code in fundBT.fundsList.keys():
        if fundBT.fundsList[code]["data"] is not None:
            allData[code] = fundBT.fundsList[code]['data']['DWJZ']

    allData.fillna(0, inplace=True)

    plt.figure(figsize=(30, 15))
    for i, column in enumerate(allData.columns):
        if column in fundBT.fundsList:
            plt.plot(allData.index, allData[column], label=f"{column}")

    plt.title('Fund Net Value Trend Chart', fontsize=20)
    plt.xlabel('FSRQ', fontsize=16)
    plt.ylabel('DWJZ', fontsize=16)
    plt.grid(True, linestyle='--', alpha=0.7)
    plt.legend(loc='upper left', bbox_to_anchor=(1, 1), fontsize=12)
    plt.xticks(rotation=45)
    plt.tight_layout()

    # 获取项目根目录
    project_root = os.path.abspath(os.path.join(os.path.dirname(__file__), '../../../..'))

    # 拼接图片输出路径
    output_path = os.path.join(project_root, 'doc', 'fund_performance.png')

    plt.savefig(output_path, dpi=150, bbox_inches='tight')

    # 保存到前端
    project_root = os.path.abspath(os.path.join(os.path.dirname(__file__), '../../../../patronage-vue/public'))
    output_path = os.path.join(project_root, 'doc', 'fund_performance.png')
    plt.savefig(output_path, dpi=150, bbox_inches='tight')

else:
    print("主基金数据无效，无法生成净值走势图")


# 保存到前端
current_script_path = os.path.abspath(__file__)
project_root = os.path.abspath(os.path.join(os.path.dirname(current_script_path), '../../../../patronage-vue/public'))
doc_dir = os.path.join(project_root, 'doc')

# 确保doc目录存在
if not os.path.exists(doc_dir):
    os.makedirs(doc_dir)
    
# 定义输出文件路径
output_file = os.path.join(doc_dir, 'result.csv')


if fundBT.totalCaptitalData is not None:
    fundBT.totalCaptitalData.to_csv(output_file, index=False, encoding='utf-8-sig')
    print(f"结果已保存至: {os.path.abspath(output_file)}")
else:
    print("没有结果数据可以保存")


current_script_path = os.path.abspath(__file__)
project_root = os.path.abspath(os.path.join(os.path.dirname(current_script_path), '../../../..'))
doc_dir = os.path.join(project_root, 'doc')

# 确保doc目录存在
if not os.path.exists(doc_dir):
    os.makedirs(doc_dir)
    
# 定义输出文件路径
output_file = os.path.join(doc_dir, 'result.csv')