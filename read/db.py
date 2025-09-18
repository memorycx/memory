import io
import sys

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


def save_word_to_db(all_word_data):
    # 插入一页的单词

    conn, cursor = get_db_connection()
    try:
        for word_data in all_word_data:
            # 1. 插入单词
            sql_word = """
            INSERT INTO Oxford_english_dictionary (word)
            VALUES (%s)
            """
            word = word_data['word']
            if len(word) > 50 or len(word) <= 0:  # 假设字段最大长度是100，根据实际情况调整
                logging.error(f"警告：单词 '{word}' 长度为 {len(word)}，可能超过数据库限制")
                continue

            cursor.execute(sql_word, (word,))
            word_id = cursor.lastrowid

            definitions = word_data["definitions_list"]
            # 2. 插入义项
            for definition in definitions:
                sql_sense = """
                INSERT INTO Oxford_meaning (word_id,type,mean)
                VALUES (%s, %s, %s)
                """
                cursor.execute(sql_sense, (word_id, definition["type"], definition.get("mean", ""),))
                mean_id = cursor.lastrowid

                # 3. 插入例句
                for ex in definition.get("examples", []):
                    sql_example = """
                    INSERT INTO Oxford_example (mean_id, example)
                    VALUES (%s, %s)
                    """
                    cursor.execute(sql_example, (mean_id, ex))

        conn.commit()
        # print(f"插入成功: ")

    except Exception as e:
        conn.rollback()
        logging.error(f"单词 '{word}'错误 {str(e)}")

    finally:
        cursor.close()
        conn.close()

# def save_word_to_db(all_word_data):
#     # 插入一页的单词
#     conn, cursor = get_db_connection()
#     try:
#         for word_data in all_word_data:
#             # 1. 插入单词
#             sql_word = """
#             INSERT INTO Oxford_english_dictionary (word)
#             VALUES (%s)
#             """
#             cursor.execute(sql_word, (word_data["word"],))
#             word_id = cursor.lastrowid
#
#             # 2. 插入义项
#             for sense in word_data.get("senses", []):
#                 for definition in sense.get("definitions", []):
#                     sql_sense = """
#                     INSERT INTO Oxford_meaning (word_id,type,mean)
#                     VALUES (%s, %s, %s)
#                     """
#                     cursor.execute(sql_sense, (word_id, definition.get("type", ""), definition.get("mean", "")))
#                     mean_id = cursor.lastrowid
#
#                     # 3. 插入例句
#                     for ex in definition.get("examples", []):
#                         sql_example = """
#                         INSERT INTO Oxford_example (mean_id, example)
#                         VALUES (%s, %s)
#                         """
#                         cursor.execute(sql_example, (mean_id, ex))
#
#         conn.commit()
#         print("插入成功")
#
#     except Exception as e:
#         conn.rollback()
#         # print(f" 出错: {e}")
#
#     finally:
#         cursor.close()
#         conn.close()
