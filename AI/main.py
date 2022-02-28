from mysite.truri.runModel import runModel
from mysite.truri.image_text import detect_text
from mysite.truri.crawling.naver_crawling import naver_crwaling
from mysite.truri.crawling.tistory_crawling import tistory_crawling

import time
import asyncio
import threading

loop = asyncio.get_event_loop()

def test():
    print("Let's start!")

    start = time.time()

    # 크롤링 테스트 코드
    # crawlingTest()
    # result = tistory_crawling("맛집", 1)
    # print(result)

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

async def runningModel(content):
    result = await loop.run_in_executor(None, runModel, content)
    return result


def modelTest() :
    # 해당 모델을 테스트할 때, 모델 내 주소는 ../주소 가 아닌 ./주소 로 바꿔야 동작한다.
    content = ['test content', 'test content', 'test content', 'test content', 'test content', 'test content', 'test content', 'test content', 'test content', 'test content']

    start = time.time()

    tasks = []
    for c in content:
        tasks.append(runningModel(c))

    result = loop.run_until_complete(asyncio.gather(*tasks))
    print(result)
    loop.close()

    end = time.time()
    print(f'>>> 비동기 처리 총 소요 시간: {end - start}')

def OCRTest() :
    uri = [
        "https://postfiles.pstatic.net/MjAyMjAxMjVfMTE1/MDAxNjQzMDY0MzU5MjIz.RgVAxil5jRSw70a-vz_1c8LqXTAbfKKuLedsU62SnG8g.IATk5CJrO5RvLpDm_IZ6u7ahTPDzv4P34DMTTeZD46Qg.JPEG.nadiatour/SE-16801df9-07e0-4a71-9693-49588cb4bc0f.jpg?type=w80_blur"
        ]

    loop = asyncio.get_event_loop()
    tasks = []

    for u in uri:
        tasks.append(detect_text(loop, u, 0))

    result = loop.run_until_complete(asyncio.gather(*tasks))
    print(result)

async def naver(loop, query, page):
    return await loop.run_in_executor(None, naver_crwaling, query, page)

async def tistory(loop, query, page):
    return await loop.run_in_executor(None, tistory_crawling, query, page)

def crawlingTest():
    query = "맛집"
    page = 1

    loop = asyncio.get_event_loop()
    result = loop.run_until_complete(asyncio.gather(naver(loop, query, page), tistory(loop, query, page)))
    print(len(result))

# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    #test()
    test = [False]
    test.append([False])
    print(test)




# See PyCharm help at https://www.jetbrains.com/help/pycharm/
