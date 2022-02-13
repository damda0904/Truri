# This is a sample Python script.

# Press Shift+F10 to execute it or replace it with your code.
# Press Double Shift to search everywhere for classes, files, tool windows, actions, and settings.
#from mysite.truri.crawling import crawling
from mysite.truri.crawling.scrapy_crawling import scrapy_crawling
import time


def print_hi():
    # Use a breakpoint in the code line below to debug your script.
    print("Let's start!")  # Press Ctrl+F8 to toggle the breakpoint.


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    print_hi()

start = time.time()

# 크롤링 테스트 코드
scrapy_result = scrapy_crawling("대전 맛집", 1)

# print(scrapy_result[0])
# print(len(scrapy_result))


# for idx in range(0, 30) :
#     item = naver_result[idx]
#     item["preview"] = scrapy_result["preview"][idx]
#     item["preview_image"] = scrapy_result["preview_image"][idx]
#     item["last_image"] = scrapy_result["last_images"][idx]
#     naver_result[idx] = item
#
# print("result---------------------------")
# print(naver_result[0]["description"])
end = time.time()

print(f"\n총 걸린 시간은 {end - start} 초 입니다")


# See PyCharm help at https://www.jetbrains.com/help/pycharm/
