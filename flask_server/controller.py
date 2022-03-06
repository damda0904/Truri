from flask import Flask, request
import time
import asyncio
import json
import requests

from naver_crawling import naver_crwaling
from detect_text import detect_text
from runModel import runModel

app = Flask(__name__)

@app.route('/test', methods=['GET', 'POST'])
def test():
    if(request.method == 'POST'):
        print(request.get_json())
        return 'post connected!'
    return 'connected!'


@app.route('/search/<query>/<page>')
def search(query, page):
    start = time.time()
    items = naver_crwaling(query, page)

    # 이미지 검사 수행
    loop = asyncio.new_event_loop()
    asyncio.set_event_loop(loop)
    tasks = []

    idx = 0
    for item in items:
        urls = item['last_images']
        for url in urls:
            if url == "":
                continue
            else:
                tasks.append(detect_text(loop, url, idx))
        idx += 1

    # image_result = loop.run_until_complete(asyncio.gather(*tasks)) # 멀티 스레드 수행
    #
    # for i in image_result:
    #     if 'detect_result' not in items[i[0]]:
    #         items[i[0]]['detect_result'] = [i[1]]
    #     else: items[i[0]]['detect_result'].append(i[1])

    # 모듈 수행
    result=[]
    content_list = []
    for item in items:

        # 이미지 확인 후 이미 광고로 판명이 났다면 모듈 수행x
        if ('detect_result' in item):
            if (True in item['detect_result']):
                result.append(100.0)
                continue

        result.append(-1.0)
        content_list.append(item['content'])

    scores = runModel(content_list)

    print(scores)

    return "complete"

    # idx = 0
    size = len(result)
    # # result 중 -1인 경우, 멀티 스레드를 수행했다는 뜻으로 결과값으로 바꿔 저장
    # for i in range(0, size):
    #     if(result[i] < 0) :
    #         result[i] = task_result[idx]
    #         idx += 1

    # items에 score 결과값 저장 및 response 용으로 정리
    for i in range(0, size):
        items[i]['score'] = result[i]
        items[i].pop('content')
        items[i].pop('last_images')

    response = json.dumps(items)

    end = time.time()

    print(f"\n총 걸린 시간은 {end - start} 초 입니다")
    # 현재 크롤링 3초, ai분석 35초정도 소요됨.

    return response

if __name__ == '__main__':
    app.run(debug=True)