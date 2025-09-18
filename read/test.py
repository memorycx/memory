import re


# def parse_definitions_improved(definitions_str):
#     """
#     进一步改进的函数，用于解析释义字符串。
#     它将数字前的部分作为单独的第一个元素，并正确解析后续的带编号释义。
#     """
#     # 查找第一个数字及其后面的文本，将其余部分作为前缀
#     match = re.search(r'(\s\d+[\s\S]*)', definitions_str)
#
#     if match:
#         prefix = definitions_str[:match.start()].strip()
#         numbered_part = definitions_str[match.start():].strip()
#
#         # 使用先行断言(?=...)来分割，但同时保留数字，这是实现的关键
#         parts = re.split(r'(?=\s\d+\s)', numbered_part)
#
#         # 将分割后的结果列表化，并确保没有空字符串
#         parsed_list = [p.strip() for p in parts if p.strip()]
#
#         # 将前缀添加到列表的开头
#         if prefix:
#             parsed_list.insert(0, prefix)
#
#         return parsed_list
#     else:
#         # 如果没有找到带编号的释义，则返回整个字符串作为一个元素
#         return [definitions_str.strip()]
#
#
# def parse_file(file_content):
#     lines = file_content.strip().split('\n')
#     parsed_words = []
#
#     # Process the file in pairs of lines
#     for i in range(0, len(lines), 2):
#         if i + 1 < len(lines):
#             english_word = lines[i].strip()
#             details = lines[i + 1].strip()
#             senses = parse_definitions_improved(details)
#             parsed_words.append({
#                 "word": english_word,
#                 "senses": senses
#             })
#
#     return parsed_words


import re
import json

import chardet


def parse_dictionary_text(text):
    """
    解析词典文本，提取单词、词性、释义和例句
    :param text: 原始词典文本
    :return: 结构化的词典数据列表
    """
    # 匹配单词的正则表达式（处理普通单词和带连字符的单词，如A-bomb、a la carte）
    # 匹配规则：以非空白字符开头，可能包含连字符/空格/斜杠，后跟词性（如 / adj; / n; 等）
    word_pattern = re.compile(
        r'^([A-Za-z\-\/\s]+?)\s*\/\s*([A-Za-z]+(?:; [A-Za-z]+)?);',
        re.MULTILINE | re.VERBOSE
    )

    # 存储所有单词的结构化数据
    dictionary_data = []

    # 遍历所有匹配到的单词
    for match in word_pattern.finditer(text):
        word = match.group(1).strip()  # 提取单词（去除前后空白）
        pos = match.group(2).strip()  # 提取词性（如 adj, n, adv）

        # 处理多词性情况（如 "adj; n"）
        pos_list = [p.strip() for p in pos.split(';')]

        # 提取当前单词的完整释义文本（从单词匹配结束位置到下一个单词开始前）
        end_pos = match.end()
        # 找到下一个单词的开始位置（作为当前单词释义的结束边界）
        next_match = word_pattern.search(text, end_pos)
        if next_match:
            word_content = text[end_pos:next_match.start()].strip()
        else:
            word_content = text[end_pos:].strip()  # 最后一个单词处理

        # 处理每个词性的释义（一个单词可能有多个词性，如adj和n）
        senses = []
        for pos_item in pos_list:
            # 匹配当前词性下的所有释义（格式：1 ... 2 ... ，包含例句）
            # 释义规则：数字开头，后跟英文释义+中文释义，可能包含括号标注（如(infml 口)）
            definition_pattern = re.compile(
                r'(\d+)\s*(\([^)]*\)\s*)?(.*?)(?=\s*\d+|$)',
                re.DOTALL | re.MULTILINE
            )
            def_matches = definition_pattern.findall(word_content)

            definitions = []
            for def_match in def_matches:
                seq_num = def_match[0]  # 释义序号（1, 2, 3...）
                note = def_match[1].strip() if def_match[1] else ""  # 标注（如(infml 口)）
                def_text = def_match[2].strip()  # 释义文本（英文+中文）

                # 跳过空释义（避免正则匹配残留）
                if not def_text:
                    continue

                # 组合完整释义文本（序号+标注+释义）
                full_def_text = f"{seq_num} {note}{def_text}".strip()

                # 提取当前释义下的例句（格式：* 例句文本 中文翻译）
                example_pattern = re.compile(
                    rf'{seq_num}\s*.*?\s*(\* .*?)(?=\s*\*|$)',
                    re.DOTALL | re.MULTILINE
                )
                example_matches = example_pattern.findall(word_content)
                examples = [ex.strip() for ex in example_matches if ex.strip()]

                # 结构化当前释义
                definitions.append({
                    "type": pos_item,
                    "text": full_def_text,
                    "examples": examples
                })

            # 结构化当前词性的含义（senses）
            if definitions:
                senses.append({
                    "type": pos_item,
                    "definitions": definitions
                })

        # 仅添加有有效释义的单词
        if senses:
            dictionary_data.append({
                "word": word,
                "senses": senses
            })

    return dictionary_data



with open("./DICT/A/A-b.txt", "rb") as f:
    raw = f.read(20000)
enc = chardet.detect(raw)["encoding"] or "utf-8"
# 读取原始文本文件（请将文件路径替换为你的实际路径）
with open("./DICT/A/A-b.txt", "r", encoding=enc) as f:
    raw_text = f.read()

# 执行解析
parsed_data = parse_dictionary_text(raw_text)

# 将结果保存为JSON文件（带格式化）
with open("parsed_dictionary.json", "w", encoding=enc) as f:
    json.dump(parsed_data, f, ensure_ascii=False, indent=4)

# 打印前2个单词的解析结果（示例输出）
print("解析结果示例（前2个单词）：")
print(json.dumps(parsed_data[:2], ensure_ascii=False, indent=4))
