# -*- coding: utf-8 -*-
import ast
import sys
from werkzeug.serving import WSGIRequestHandler

from flask import Flask, request
import time
import asyncio
import subprocess

from naver_crawling import naver_crawling
from detect_text import detect_text
from runModel import runModel
import json

WSGIRequestHandler.protocol_version = "HTTP/1.1"
app = Flask(__name__)

@app.route('/test', methods=['GET', 'POST'])
def test():
    if(request.method == 'POST'):
        print(request.get_json())
        return 'post connected!'
    return 'connected!'

# @app.route('/search/<query>/<page>')
def dummy(query, page):
    dummyData = {'0': {'link': 'https://blog.naver.com/metalingus58?Redirect=Log&logNo=222611264935', 'title': '방콕에서 Test&go 2차 pcr 검사 받은 후기', 'preview': '최근까지 태국 무격리 입국 프로그램인 Test&go 가 있었다. 얼마 전에 없앴는데... Test & go 라고 되어있는 방으로 들어가서 여권이랑 핑크페이퍼를 내밀면 1차적인... ', 'date': '2022.01.02.', 'preview_image': 'https://postfiles.pstatic.net/MjAyMTEyMjRfMjg0/MDAxNjQwMzI1OTYxMDI3.TagqbnDtNpQFIk3_7HZQaxlb2sdJZQHpQ70Pr2pTj8Eg._5w1_8qf3HXxXA02D0Cu6OBLR_HA-PUhmI0SBlejfq4g.JPEG.gameav/20211223_072755_HDR.jpg?type=w80_blur', 'score': 30},
                 '1': {'link': 'https://blog.naver.com/metalingus58?Redirect=Log&logNo=222611264935', 'title': '방콕에서 Test&go 2차 pcr 검사 받은 후기', 'preview': '최근까지 태국 무격리 입국 프로그램인 Test&go 가 있었다. 얼마 전에 없앴는데... Test & go 라고 되어있는 방으로 들어가서 여권이랑 핑크페이퍼를 내밀면 1차적인... ', 'date': '2022.01.02.', 'preview_image': 'https://postfiles.pstatic.net/MjAyMTEyMjRfMjg0/MDAxNjQwMzI1OTYxMDI3.TagqbnDtNpQFIk3_7HZQaxlb2sdJZQHpQ70Pr2pTj8Eg._5w1_8qf3HXxXA02D0Cu6OBLR_HA-PUhmI0SBlejfq4g.JPEG.gameav/20211223_072755_HDR.jpg?type=w80_blur', 'score': 60},
                 '2': {'link': 'https://blog.naver.com/metalingus58?Redirect=Log&logNo=222611264935', 'title': '방콕에서 Test&go 2차 pcr 검사 받은 후기', 'preview': '최근까지 태국 무격리 입국 프로그램인 Test&go 가 있었다. 얼마 전에 없앴는데... Test & go 라고 되어있는 방으로 들어가서 여권이랑 핑크페이퍼를 내밀면 1차적인... ', 'date': '2022.01.02.', 'preview_image': 'https://postfiles.pstatic.net/MjAyMTEyMjRfMjg0/MDAxNjQwMzI1OTYxMDI3.TagqbnDtNpQFIk3_7HZQaxlb2sdJZQHpQ70Pr2pTj8Eg._5w1_8qf3HXxXA02D0Cu6OBLR_HA-PUhmI0SBlejfq4g.JPEG.gameav/20211223_072755_HDR.jpg?type=w80_blur', 'score': 100},}

    print("dummy")

    return json.dumps(dummyData)

@app.route('/search/<query>/<page>')
def search(query, page):
    start = time.time()

    result = subprocess.check_output([sys.executable, 'naver_crawling.py', query, str(page)], shell=True, encoding='utf-8')
    result = result.split("\n")

    items = []
    for item in result :
        if len(item) == 0 : continue
        tmp = ast.literal_eval(item)
        print(type(tmp))
        items.append(tmp)

    print("subprocess finished")

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
        # if ('detect_result' in item):
        #     if (True in item['detect_result']):
        #         result.append(100.0)
        #         continue

        result.append(-1.0)
        content_list.append(item['content'])

    scores = runModel(content_list)

    print("scores------------------")
    print(scores)

    idx = 0
    size = len(result)
    # result 중 -1인 경우, 멀티 스레드를 수행했다는 뜻으로 결과값으로 바꿔 저장
    for i in range(0, size):
        if(result[i] < 0) :
            result[i] = scores[idx]
            idx += 1

    # items에 score 결과값 저장 및 response 용으로 정리
    response = {}
    for i in range(0, size):
        items[i]['score'] = result[i]
        items[i].pop('content')
        items[i].pop('last_images')

        item = items[i]
        response[str(i)] = item

    end = time.time()

    print(f"\n총 걸린 시간은 {end - start} 초 입니다")
    # 현재 크롤링 3초, ai분석 35초정도 소요됨.

    return json.dumps(response)

if __name__ == '__main__':
    app.run(debug=True)