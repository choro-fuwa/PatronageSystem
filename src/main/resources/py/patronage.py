import pandas as pd
import pymysql

df = pd.read_csv('raw_factor_data.csv')

df['factor_value'] = df['factor_value'].fillna(df['factor_value'].mean())

df['zscore'] = (df['factor_value'] - df['factor_value'].mean()) / df['factor_value'].std()

df = df[df['zscore'].abs() <= 3]

conn = pymysql.connect(
    host='localhost',
    user='root',
    password='243417',
    database='Patronage_system',
    charset='utf8mb4'
)
cursor = conn.cursor()

for _, row in df.iterrows():
    sql = """
    INSERT INTO factor_data (factor_id, fund_code, trade_date, factor_value, rank_value, percentile)
    VALUES (%s, %s, %s, %s, %s, %s)
    ON DUPLICATE KEY UPDATE
        factor_value=VALUES(factor_value),
        rank_value=VALUES(rank_value),
        percentile=VALUES(percentile)
    """
    cursor.execute(sql, (
        int(row['factor_id']),
        row['fund_code'],
        row['trade_date'],
        float(row['factor_value']),
        float(row.get('rank_value', 0)),
        float(row.get('percentile', 0))
    ))

conn.commit()
cursor.close()
conn.close()

print("数据加工并写入数据库完成！")