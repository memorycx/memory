
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
            INSERT INTO Oxford_english_dictionary (word, part_of_speech)
            VALUES (%s, %s)
            """
            cursor.execute(sql_word, (word_data["word"], word_data["part_of_speech"]))
            word_id = cursor.lastrowid

            # 2. 插入义项
            for sense in word_data.get("senses", []):
                sql_sense = """
                INSERT INTO Oxford_meaning (word_id, definition_en, definition_cn)
                VALUES (%s, %s, %s)
                """
                cursor.execute(sql_sense, (word_id, sense.get("definition_en", ""), sense.get("definition_cn", "")))
                sense_id = cursor.lastrowid

                # 3. 插入例句
                for ex in sense.get("examples", []):
                    sql_example = """
                    INSERT INTO Oxford_example (sense_id, example)
                    VALUES (%s, %s)
                    """
                    cursor.execute(sql_example, (sense_id, ex))

        conn.commit()
        print(f"插入成功: ")

    except Exception as e:
        conn.rollback()
        print(f" 出错: {e}")

    finally:
        cursor.close()
        conn.close()
