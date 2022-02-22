# This is a sample Python script.

# Press Shift+F10 to execute it or replace it with your code.
# Press Double Shift to search everywhere for classes, files, tool windows, actions, and settings.
#from mysite.truri.crawling import crawling
from mysite.truri.crawling.scrapy_crawling import scrapy_crawling
from mysite.truri.runModel import runModel
from mysite.truri.image_text import detect_text

import time
import asyncio
import threading

def test():
    print("Let's start!")

    start = time.time()

    # 크롤링 테스트 코드
    # scrapy_result = scrapy_crawling("대전 맛집", 1)

    # 모델 멀티스레드 테스트코드
    # modelTest()

    # 구글 OCR 테스트
    OCRTest()

    end = time.time()

    print(f"\n총 걸린 시간은 {end - start} 초 입니다")

async def find_users_async(n):
    for i in range(1, n + 1):
        print(f'{n}명 중 {i}번 째 사용자 조회 중 ...')
        await asyncio.sleep(1)
    print(f'> 총 {n} 명 사용자 동기 조회 완료!')

def modelTest() :
    # 해당 모델을 테스트할 때, 모델 내 주소는 ../주소 가 아닌 ./주소 로 바꿔야 동작한다.
    content = ['test content', 'test content', 'test content', 'test content', 'test content']
    # for url in urls:
    #     runModel(url)

    # futures = [asyncio.ensure_future(runModel(url)) for url in urls]
    # result = await asyncio.gather(*futures)

    # start1 = time.time()
    #
    # for c in content:
    #     runModel(c)
    #
    # end1 = time.time()
    #
    # print(f'>>> 비동기 처리 총 소요 시간: {end1 - start1}')

    start = time.time()

    loop = asyncio.get_event_loop()
    tasks = [
        loop.create_task(runModel(content)),
        loop.create_task(runModel(content)),
        loop.create_task(runModel(content)),
        loop.create_task(runModel(content)),
        loop.create_task(runModel(content)),
    ]

    # tasks = [
    #     loop.create_task(find_users_async(3)),
    #     loop.create_task(find_users_async(2)),
    #     loop.create_task(find_users_async(1)),
    # ]
    result = loop.run_until_complete(asyncio.gather(*tasks))
    print(result)
    loop.close()

    end = time.time()
    print(f'>>> 비동기 처리 총 소요 시간: {end - start}')

def OCRTest() :
    uri = [
        "https://storep-phinf.pstatic.net/ogq_5a558da2e2a83/original_15.png?type=p50_50"
        ]


    loop = asyncio.get_event_loop()
    tasks = []

    nums = [3, 2, 1]
    for u in uri:
        tasks.append(detect_text(u))


    # tasks = [
    #     loop.create_task(find_users_async(3)),
    #     loop.create_task(find_users_async(2)),
    #     loop.create_task(find_users_async(1)),
    # ]
    result = loop.run_until_complete(asyncio.gather(*tasks))
    print(result)

# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    test()




# See PyCharm help at https://www.jetbrains.com/help/pycharm/
