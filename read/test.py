import re


# def parse_definitions_improved(definitions_str):
#     """
#     ��һ���Ľ��ĺ��������ڽ��������ַ�����
#     ��������ǰ�Ĳ�����Ϊ�����ĵ�һ��Ԫ�أ�����ȷ���������Ĵ�������塣
#     """
#     # ���ҵ�һ�����ּ��������ı��������ಿ����Ϊǰ׺
#     match = re.search(r'(\s\d+[\s\S]*)', definitions_str)
#
#     if match:
#         prefix = definitions_str[:match.start()].strip()
#         numbered_part = definitions_str[match.start():].strip()
#
#         # ʹ�����ж���(?=...)���ָ��ͬʱ�������֣�����ʵ�ֵĹؼ�
#         parts = re.split(r'(?=\s\d+\s)', numbered_part)
#
#         # ���ָ��Ľ���б�����ȷ��û�п��ַ���
#         parsed_list = [p.strip() for p in parts if p.strip()]
#
#         # ��ǰ׺��ӵ��б�Ŀ�ͷ
#         if prefix:
#             parsed_list.insert(0, prefix)
#
#         return parsed_list
#     else:
#         # ���û���ҵ�����ŵ����壬�򷵻������ַ�����Ϊһ��Ԫ��
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
    �����ʵ��ı�����ȡ���ʡ����ԡ����������
    :param text: ԭʼ�ʵ��ı�
    :return: �ṹ���Ĵʵ������б�
    """
    # ƥ�䵥�ʵ�������ʽ��������ͨ���ʺʹ����ַ��ĵ��ʣ���A-bomb��a la carte��
    # ƥ������Էǿհ��ַ���ͷ�����ܰ������ַ�/�ո�/б�ܣ�������ԣ��� / adj; / n; �ȣ�
    word_pattern = re.compile(
        r'^([A-Za-z\-\/\s]+?)\s*\/\s*([A-Za-z]+(?:; [A-Za-z]+)?);',
        re.MULTILINE | re.VERBOSE
    )

    # �洢���е��ʵĽṹ������
    dictionary_data = []

    # ��������ƥ�䵽�ĵ���
    for match in word_pattern.finditer(text):
        word = match.group(1).strip()  # ��ȡ���ʣ�ȥ��ǰ��հף�
        pos = match.group(2).strip()  # ��ȡ���ԣ��� adj, n, adv��

        # ��������������� "adj; n"��
        pos_list = [p.strip() for p in pos.split(';')]

        # ��ȡ��ǰ���ʵ����������ı����ӵ���ƥ�����λ�õ���һ�����ʿ�ʼǰ��
        end_pos = match.end()
        # �ҵ���һ�����ʵĿ�ʼλ�ã���Ϊ��ǰ��������Ľ����߽磩
        next_match = word_pattern.search(text, end_pos)
        if next_match:
            word_content = text[end_pos:next_match.start()].strip()
        else:
            word_content = text[end_pos:].strip()  # ���һ�����ʴ���

        # ����ÿ�����Ե����壨һ�����ʿ����ж�����ԣ���adj��n��
        senses = []
        for pos_item in pos_list:
            # ƥ�䵱ǰ�����µ��������壨��ʽ��1 ... 2 ... ���������䣩
            # ����������ֿ�ͷ�����Ӣ������+�������壬���ܰ������ű�ע����(infml ��)��
            definition_pattern = re.compile(
                r'(\d+)\s*(\([^)]*\)\s*)?(.*?)(?=\s*\d+|$)',
                re.DOTALL | re.MULTILINE
            )
            def_matches = definition_pattern.findall(word_content)

            definitions = []
            for def_match in def_matches:
                seq_num = def_match[0]  # ������ţ�1, 2, 3...��
                note = def_match[1].strip() if def_match[1] else ""  # ��ע����(infml ��)��
                def_text = def_match[2].strip()  # �����ı���Ӣ��+���ģ�

                # ���������壨��������ƥ�������
                if not def_text:
                    continue

                # ������������ı������+��ע+���壩
                full_def_text = f"{seq_num} {note}{def_text}".strip()

                # ��ȡ��ǰ�����µ����䣨��ʽ��* �����ı� ���ķ��룩
                example_pattern = re.compile(
                    rf'{seq_num}\s*.*?\s*(\* .*?)(?=\s*\*|$)',
                    re.DOTALL | re.MULTILINE
                )
                example_matches = example_pattern.findall(word_content)
                examples = [ex.strip() for ex in example_matches if ex.strip()]

                # �ṹ����ǰ����
                definitions.append({
                    "type": pos_item,
                    "text": full_def_text,
                    "examples": examples
                })

            # �ṹ����ǰ���Եĺ��壨senses��
            if definitions:
                senses.append({
                    "type": pos_item,
                    "definitions": definitions
                })

        # ���������Ч����ĵ���
        if senses:
            dictionary_data.append({
                "word": word,
                "senses": senses
            })

    return dictionary_data



with open("./DICT/A/A-b.txt", "rb") as f:
    raw = f.read(20000)
enc = chardet.detect(raw)["encoding"] or "utf-8"
# ��ȡԭʼ�ı��ļ����뽫�ļ�·���滻Ϊ���ʵ��·����
with open("./DICT/A/A-b.txt", "r", encoding=enc) as f:
    raw_text = f.read()

# ִ�н���
parsed_data = parse_dictionary_text(raw_text)

# ���������ΪJSON�ļ�������ʽ����
with open("parsed_dictionary.json", "w", encoding=enc) as f:
    json.dump(parsed_data, f, ensure_ascii=False, indent=4)

# ��ӡǰ2�����ʵĽ��������ʾ�������
print("�������ʾ����ǰ2�����ʣ���")
print(json.dumps(parsed_data[:2], ensure_ascii=False, indent=4))
