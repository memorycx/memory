
import tool
from docx import Document

# 打开 Word 文档
doc = Document("static\\CET6.docx")

target = []
# 遍历文档中的表格
for table in doc.tables:
    # 遍历表格中的行
    for row in table.rows:
        # 遍历行中的单元格
        cells = row.cells
        # 假设表格有四列：序号、单词、音标、释义
        if len(cells) >= 4:
            serial_number = cells[0].text
            word = cells[1].text
            phonetic = cells[2].text
            definition = cells[3].text
            if word == "单词":
                continue
            if len(definition) > 50:
                definition = definition[:50]
            tem = {
                "word": word,
                "definition": definition,
            }
            target.append(tem)

tool.insert_word(target)

