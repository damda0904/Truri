# Create your views here.

from django.http import HttpResponse
from .crawling.naver_crawling import naver_crwaling
from .image_text import detect_text
from .runModel import runModel
import time
import asyncio
import json

def index(request, query, page):
    start = time.time()
    items = naver_crwaling(query, page)

    #이미지 검사 수행
    loop = asyncio.new_event_loop()
    asyncio.set_event_loop(loop)
    tasks = []

    idx = 0
    for item in items:
        urls = item['last_images']
        for url in urls:
            if url == "": continue
            else : tasks.append(detect_text(loop, url, idx))
        idx += 1

    # image_result = loop.run_until_complete(asyncio.gather(*tasks))
    #
    # for i in image_result:
    #     if 'detect_result' not in items[i[0]]:
    #         items[i[0]]['detect_result'] = [i[1]]
    #     else: items[i[0]]['detect_result'].append([i[1][0]])

    #모듈 수행
    # loop = asyncio.new_event_loop()
    # asyncio.set_event_loop(loop)
    # print("=======================loop existed")
    # tasks = []
    # for item in items:
    #     tasks.append(runModel(item['content']))
    #
    # result = loop.run_until_complete(asyncio.gather(*tasks))
    # loop.close()
    # print(result)
    result = []
    for item in items:
        if('detect_result' in item):
            if(True in item['detect_result']):
                result.append(0.0)
                continue

        result.append(runModel(item['content']))


    # score_result = runModel(contents)

    size = len(items)
    for i in range(0, size):
        items[i]['score'] = result[i]
        items[i].pop('content')
        items[i].pop('last_images')

    response = json.dumps(items)

    end = time.time()

    print(f"\n총 걸린 시간은 {end - start} 초 입니다")
    #현재 크롤링 3초, ai분석 35초정도 소요됨.

    return HttpResponse(response)


def test(request):
    return HttpResponse("test success")