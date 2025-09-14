import os
import re

import chardet
import tool

#
#
def parse_file(text):
    entries = []
    # 匹配模式：单词 + 释义
    pattern = re.compile(r"^([a-zA-Z\-]+)\s*\n?(/[^/]+/)?\s*(.*)", re.MULTILINE)

    for match in pattern.finditer(text):
        word = match.group(1).strip()
        # pronunciation = match.group(2).strip() if match.group(2) else ""
        meaning = match.group(3).strip()
        entries.append({"word": word, "meaning": meaning})

    return entries


def parse_meaning_text(text):
    result = {
        "part_of_speech": "",
        "senses": []
    }

    if not text or text.strip() == "":
        return result

    # 提取词性（开头的 v, n, adj 等）
    pos_match = re.match(r"^([a-zA-Z\-\.]+)", text.strip())
    if pos_match:
        result["part_of_speech"] = pos_match.group(1)

    # 去掉开头的词性部分
    text = re.sub(r"^[a-zA-Z\-\.]+\s*", "", text.strip())

    # 按照义项编号拆分  (1, 2, 3, 或 1 (a), 1 (b))
    senses = re.split(r"\s(?=\d+\s|\d+\s*\([a-z]\))", text)

    for s in senses:
        s = s.strip()
        if not s:
            continue

        # 提取编号 (原本的编号暂时忽略)
        s = re.sub(r"^\d+\s*(\([a-z]\))?", "", s).strip()

        # 中英文拆分：第一个中文字符作为分界点
        # zh_split = re.split(r"([一-龥].*)", s, maxsplit=1)
        # if len(zh_split) > 1:
        #     definition_en = zh_split[0].strip(" :")
        #     rest_cn = zh_split[1]
        # else:
        #     definition_en = s
        #     rest_cn = ""

        # 先匹配所有 [] 内的内容，暂时替换成占位符
        placeholders = []

        def replace_brackets(match):
            placeholders.append(match.group(0))
            return f"__PLACEHOLDER_{len(placeholders) - 1}__"

        s_temp = re.sub(r"\[.*?\]", replace_brackets, s)

        # 找到第一个中文字符作为分界
        match = re.search(r"[一-龥]", s_temp)
        if match:
            idx = match.start()
            definition_en = s_temp[:idx].strip(" :")
            rest_cn = s_temp[idx:].strip()
        else:
            definition_en = s.strip()
            rest_cn = ""

        # 把占位符替换回原来的 [] 内容
        for i, ph in enumerate(placeholders):
            definition_en = definition_en.replace(f"__PLACEHOLDER_{i}__", ph)
            rest_cn = rest_cn.replace(f"__PLACEHOLDER_{i}__", ph)

        # 中文部分再拆成 “释义 + 例句”
        if ":" in rest_cn:
            parts = rest_cn.split(":", 1)
            definition_cn = parts[0].strip()
            examples = parts[1].strip()
        else:
            definition_cn = rest_cn.strip()
            examples = ""

        # 例句按 * 拆分
        example_list = [e.strip() for e in examples.split("*") if e.strip()]

        result["senses"].append({
            "id": "",  # 先空着，后面统一编号
            "definition_en": definition_en,
            "definition_cn": definition_cn,
            "examples": example_list
        })

    # 自动给 senses 编号，从 1 开始
    for idx, sense in enumerate(result["senses"], 1):
        sense["id"] = str(idx)

    return result


def parse_meaning(entries):
    """
    解析整个词典 entries (list of dict)，返回结构化结果
    """
    parsed_entries = []

    for entry in entries:
        word = entry.get("word", "").strip()
        meaning = entry.get("meaning", "").strip()

        meaning_parsed = parse_meaning_text(meaning)

        parsed_entries.append({
            "word": word,
            "part_of_speech": meaning_parsed["part_of_speech"],
            "senses": meaning_parsed["senses"]
        })

    return parsed_entries


def read_file(text, enc):
    # 解析文件，获得一个数组
    data = parse_file(text)
    end = parse_meaning(data)
    tool.save_word_to_db(end)


if __name__ == "__main__":

    base_dir = "DICT"  # 词典的根目录

    for root, dirs, files in os.walk(base_dir):
        for file in files:
            if file.endswith(".txt"):  # 只处理 txt 文件
                file_path = os.path.join(root, file)

                with open(file_path, "rb") as f:
                    raw = f.read(20000)
                enc = chardet.detect(raw)["encoding"] or "utf-8"
                print(f"{file} 检测编码: {enc}")

                with open(file_path, "r", encoding=enc, errors="ignore") as f:
                    content = f.read()
                    read_file(content, enc)
