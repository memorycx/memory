
import pymysql
from pymysql import OperationalError
import logging


def get_db_connection():
    """建立数据库连接
    返回: 数据库连接对象和游标对象
    """
    try:
        connection = pymysql.connect(
            host='localhost',  # 替换为您的MySQL主机
            user='root',  # 替换为您的用户名
            password='cx050416',  # 替换为您的密码
            database='memory',  # 目标数据库
            charset='utf8mb4',
        )
        return connection, connection.cursor()
    except OperationalError as e:
        logging.error(f"数据库连接失败: {e}")
        raise


def insert_word(a):
    """插入CET4词汇数据到数据库
    参数: word_dict - 格式为{'world': 'xxx', 'mean': 'xxx'}的字典
    """

    connection, cursor = get_db_connection()
    try:
        for word_dict in a:

            # 插入SQL语句 (使用参数化查询防止注入)
            sql = "INSERT INTO ct6 (word, mean) VALUES (%s, %s)"
            # 提取字典数据
            data = (word_dict['word'], word_dict['definition'])
            # 执行插入
            cursor.execute(sql, data)
            connection.commit()
            logging.info(f"成功插入数据: {word_dict['word']}")
            # return cursor.lastrowid

    except OperationalError as e:
        if connection: connection.rollback()
        logging.error(f"数据库操作失败: {e}")
        raise
    finally:
        if connection: connection.close()
