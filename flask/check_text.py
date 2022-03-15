def check_text(text):
    ads_filter = ["지원받고", "지원 받고", "지원받아", "지원 받아", "지원받았으나",
                  "지원 받았으나", "지원 받았지만", "지원받았지만", "지원받을 수", "지원 받았습니다",
                  "제공받고", "제공 받고", "제공받아",  "제공 받아", "제공받았으나",
                  "제공 받았으나", "제공 받았지만", "제공받았지만", "제공받을 수", "제공 받았습니다",
                  "서포터즈", "파트너스", "소정의", "수수료", "고료", "협찬", "대가", "일정의", "지원금", "원고료"]

    non_ads_filter = ["받지 않고", "받지않고", "내돈 내산", "내돈내산"]

    # 특정 글자 검사
    for filter in non_ads_filter:
        if(text.find(filter) > -1) :
            return 0

    for filter in ads_filter:
        if (text.find(filter) > -1):
            return 1

    return -1
