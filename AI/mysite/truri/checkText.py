def checkText(text):
    ads_filter = ["지원받고", "지원 받고", "지원받아", "지원 받아", "지원받았으나", "제공 받았습니다",
                  "지원 받았으나", "지원 받았지만", "지원받았지만", "지원받을 수", "지원 받았습니다",
                  "서포터즈", "파트너스", "소정의", "수수료", "고료", "협찬", "대가", "일정의", "지원금"]

    # 특정 글자 검사
    result = False
    for text in ads_filter:
        if (text.find(text) > -1):
            result = True
            break

    return result