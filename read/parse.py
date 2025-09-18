import json
import os
import re
import sys
import tool
import chardet
import db


def parse_definitions_improved(definitions_str):
    """
    进一步改进的函数，用于解析释义字符串。
    它将数字前的部分作为单独的第一个元素，并正确解析后续的带编号释义。
    """
    # 查找第一个数字及其后面的文本，将其余部分作为前缀
    match = re.search(r'(\s\d+[\s\S]*)', definitions_str)

    if match:
        prefix = definitions_str[:match.start()].strip()
        numbered_part = definitions_str[match.start():].strip()

        # 使用先行断言(?=...)来分割，但同时保留数字，这是实现的关键
        parts = re.split(r'(?=\s\d+\s)', numbered_part)

        # 将分割后的结果列表化，并确保没有空字符串
        parsed_list = [p.strip() for p in parts if p.strip()]

        # 将前缀添加到列表的开头
        if prefix:
            parsed_list.insert(0, prefix)

        return parsed_list
    else:
        # 如果没有找到带编号的释义，则返回整个字符串作为一个元素
        return [definitions_str.strip()]


def parse_example(senses_data):
    # 初始化一个空列表，用于存储最终解析结果
    parsed_senses = []

    # 假设列表的第一个元素是词性（如 "v"）

    if senses_data and senses_data[0].strip():  # 先判断是否有有效内容
        parts = senses_data[0].split()
        if parts:  # 再判断分割后的列表是否非空
            main_sense = parts[0]
        else:
            return parsed_senses
    else:
        return parsed_senses
    # 初始化一个列表，用于存储所有具体的定义
    definitions_list = []

    # 遍历 senses_data 中从第二个元素开始的所有定义字符串
    for definition_str in senses_data[1:]:
        # 使用 re.split() 以第一个冒号 ":" 作为分隔符，将字符串分割成两部分：
        # parts[0] 是定义文本，parts[1] 是例子文本
        # maxsplit=1 确保只分割一次
        parts = re.split(r':\s*', definition_str, maxsplit=1)

        # 检查是否成功分割出定义和例子
        if len(parts) == 2:
            definition_text = parts[0]
            examples_str = parts[1]

            # 使用正则表达式匹配并提取定义类型，如 "[Tn, Tn.p]" 或 "(phr v)"
            # ^(\[.*?\]|\(.*\)) 匹配行首的 "[...]" 或 "(...)"
            type_match = re.search(r'^(\[.*?\]|\(.*\))', definition_text)
            # 如果匹配成功，获取匹配到的字符串；否则，设置为空
            definition_type = type_match.group(1) if type_match else main_sense

            # 从定义文本中移除类型部分，并去除首尾空格
            text_without_type = definition_text.replace(definition_type, "", 1).strip()

            # 以星号 "*" 分割例子字符串，并去除每个例子首尾的空格
            # 使用列表推导式过滤掉可能出现的空字符串
            examples = [ex.strip() for ex in examples_str.split('*') if ex.strip()]

            definitions_list.append({
                "type": definition_type,
                "mean": text_without_type,
                "examples": examples
            })
        else:
            # 如果字符串中没有冒号，则视为没有例子的情况
            # 将整个字符串作为文本，例子列表为空
            definitions_list.append({
                "type": main_sense,
                "mean": definition_str.strip(),
                "examples": []
            })
    if not definitions_list:
        definitions_list.append({
            "type": main_sense,
            "mean": ' '.join(senses_data[0].split()[1:]),
            "examples": []
        })
    # 将主词性 ("v") 和所有定义列表组合成一个最终的字典
    # parsed_senses.append({
    #     "type": main_sense,
    #     "definitions": definitions_list
    # })

    return definitions_list


def parse_file(file_content, enc):
    lines = file_content.strip().split('\n')
    parsed_words = []

    # Process the file in pairs of lines
    sys.stdout.reconfigure(encoding=enc)
    for i in range(0, len(lines), 2):
        if i + 1 < len(lines):
            english_word = lines[i].strip()
            word = tool.parse_word(english_word)



            details = lines[i + 1].strip()
            details = re.sub(r'/.+?/', '', details)
            senses = parse_definitions_improved(details)
            definitions_list = parse_example(senses)

            if word['after_parentheses']:
                english_word = word['before_parentheses']
                parentheses_content = word['parentheses_content']
                after_parentheses = word['after_parentheses']
                if parentheses_content or after_parentheses:
                    definitions_list.append({
                        "type": "",
                        "mean": parentheses_content,
                        "examples": [after_parentheses]
                    })



            parsed_words.append({
                "word": english_word,
                "definitions_list": definitions_list
            })
    db.save_word_to_db(parsed_words)
    #print(json.dumps(parsed_words, indent=2, ensure_ascii=False))


if __name__ == "__main__":

    base_dir = "DICT"  # 词典的根目录
    for root, dirs, files in os.walk(base_dir):
        for file in files:
            if file.endswith(".txt"):  # 只处理 txt 文件
                file_path = os.path.join(root, file)

                with open(file_path, "rb") as f:
                    raw = f.read(20000)
                enc = chardet.detect(raw)["encoding"] or "utf-8"
                # print(file + "检测编码: " + enc)

                with open(file_path, "r", encoding=enc, errors="ignore") as f:
                    parse_file(f.read(), enc)

    # with open("./DICT/A/A-b.txt", "rb") as f:
    #     raw = f.read(20000)
    # enc = chardet.detect(raw)["encoding"] or "utf-8"
    # # print(f"{file} 检测编码: {enc}")
    #
    # with open("./DICT/A/A-b.txt", "r", encoding=enc, errors="ignore") as f:
    #     parse_file(f.read(), enc)
