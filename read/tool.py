
import logging
import re

import re


def parse_word(word):
    # 正则表达式模式：匹配圆括号前的内容、圆括号及其中内容，以及之后的内容
    pattern = r'^(.*?)\s*\((.*?)\)\s*(.*)$'

    # 应用正则表达式
    match = re.search(pattern, word)

    if match:
        # 提取各个部分并去除多余空白
        before_parentheses = match.group(1).strip()
        parentheses_content = match.group(2).strip()
        after_parentheses = match.group(3).strip()

        return {
            'original_phrase': word,
            'before_parentheses': before_parentheses,  # 圆括号之前的短句
            'parentheses_content': parentheses_content,  # 圆括号内的内容
            'after_parentheses': after_parentheses  # 圆括号之后的内容
        }
    else:
        # 如果没有匹配到圆括号，只返回原始短语和圆括号前的内容（即整个短语）
        return {
            'original_phrase': word,
            'before_parentheses': word.strip(),
            'parentheses_content': None,
            'after_parentheses': None
        }


# 测试示例
if __name__ == "__main__":
    test_cases = [
        # "blow sb up (infml 口) reprimand sb severely 训斥某人: She got blown up by her boss for being late. 她因迟到而受到老板严厉训斥.",
        # "example (abbr. 缩写) some explanation 一些解释",
        # "no parentheses here 没有括号的情况"
        "boric acid (also boracic acid / bE9rsIk 5sId; bE9rAsIk `AsId/)"
    ]

    for case in test_cases:
        result = parse_word(case)
        print("原始短语:", result['original_phrase'])
        print("圆括号之前的短句:", result['before_parentheses'])
        print("圆括号内容:", result['parentheses_content'])
        print("圆括号之后内容:", result['after_parentheses'])
        print("---")

# 测试示例
# if __name__ == "__main__":
#     test_cases = [
#         "blow sb up (infml 口) reprimand sb severely 训斥某人: She got blown up by her boss for being late. 她因迟到而受到老板严厉训斥.",
#         "example (abbr. 缩写) some explanation 一些解释",
#         "no parentheses here 没有括号的情况"
#     ]
#     # 先配置日志（首次使用前配置一次）
#     logging.basicConfig(level=logging.INFO, format='%(message)s')
#
#     for case in test_cases:
#         result = parse_word(case)
#         logging.info(f"原始短语: {result['original_phrase']}")
#         logging.info(f"圆括号内容: {result['parentheses_content'] or '无'}")  # 处理 None
#         logging.info(f"圆括号之后内容: {result['after_parentheses'] or '无'}")
#         logging.info("---")
