import pdfplumber
from pathlib import Path
import tool
import re


def read_pdf_text(pdf_path, target, two_column_layout=True):
    """使用pdfplumber读取PDF文件并返回文本内容（支持双栏布局）"""

    try:
        if not Path(pdf_path).exists():
            raise FileNotFoundError(f"PDF文件不存在: {pdf_path}")

        with pdfplumber.open(pdf_path) as pdf:
            for page_num, page in enumerate(pdf.pages, 1):
                page_text = []

                if two_column_layout:
                    # 获取页面宽度和高度
                    width = page.width
                    height = page.height

                    # 计算中间分割线（可根据实际情况调整偏移量）
                    middle = width / 2

                    # 定义左右两栏的区域
                    left_bbox = (0, 0, middle, height)  # (x0, top, x1, bottom)
                    right_bbox = (middle, 0, width, height)

                    # 分别提取左右两栏文本
                    left_text = page.crop(left_bbox).extract_text() or ""
                    right_text = page.crop(right_bbox).extract_text() or ""
                    page_text = left_text.split("\n") + right_text.split("\n")

                    for line in page_text:
                        if line:
                            phonetic, rest = split_word_entry(line)
                            word, definition = split_word_and_definition(rest)
                            print(f"{page_num}: {word}, {definition}")
                            if word and definition:
                                tem = {
                                    "word": word,
                                    "definition": definition,
                                }
                                target.append(tem)
                # else:
                # 单栏模式
                # text += page.extract_text() + "\n\n"

                # ... existing table extraction code ...
    except Exception as e:
        return f"读取PDF时出错: {str(e)}"


def split_word_entry(text):
    """使用正则表达式分割单词条目
    返回: (中括号内容, 剩余部分) 的元组，匹配失败则返回 (None, 原始文本)
    """
    # 正则表达式模式：匹配中括号内容和剩余部分
    pattern = r'^(.*?)\[(.*?)\](.*)$'
    match = re.match(pattern, text.strip())

    if match:
        # 提取中括号内部分和剩余部分
        bracket_content = match.group(2).strip()
        remaining_part = (match.group(1) + match.group(3)).strip()
        return bracket_content, remaining_part
    return None, text


def split_word_and_definition(text):
    """使用正则表达式分割单词和中文意思
    返回: (单词部分, 中文意思部分) 的元组，匹配失败则返回 (原始文本, None)
    """
    # 正则表达式模式：匹配英文单词部分和中文释义部分
    # 支持包含空格的词性标注（如 vt. vi.）
    pattern = r'^([A-Za-z]+)\s*([a-zA-Z. ]*?[\u4e00-\u9fa5].*)$'
    match = re.match(pattern, text.strip())

    if match:
        word_part = match.group(1).strip()
        definition_part = match.group(2).strip()
        return word_part, definition_part
    return text, None


# 使用示例
if __name__ == "__main__":
    # 替换为你的PDF文件路径

    target = []
    pdf_file_path = "static\\CET4.pdf"
    pdf_text = read_pdf_text(pdf_file_path, target)
    print(len(target))
    tool.insert_word(target)
