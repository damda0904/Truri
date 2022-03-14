# -*- coding: utf-8 -*-
import ast
import sys
from werkzeug.serving import WSGIRequestHandler

from flask import Flask, request
import time
import asyncio
import subprocess

from detect_text import detect_text
from check_text import check_text
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

    # 크롤링
    result = subprocess.check_output([sys.executable, 'naver_crawling.py', query, str(page)], shell=True, encoding='utf-8')
    result = result.split("\n")

    # 결과를 dict로 변환해 삽입
    items = []
    for item in result :
        if len(item) == 0 : continue
        tmp = ast.literal_eval(item)
        print(tmp)
        items.append(tmp)

    print("subprocess finished")

    # 텍스트 검색
    for item in items:
        item['check_text'] = check_text(item['content'])


    # 이미지 검사 수행
    loop = asyncio.new_event_loop()
    asyncio.set_event_loop(loop)
    tasks = []

    idx = 0
    for item in items:
        urls = item['last_images']
        for url in urls:    # 이미지가 있다면 이미지 검사 수행
            if url == "":
                continue
            elif item['check_text'] == True :   # 이미 텍스트에서 판명이 났다면 검사 패스
                continue
            else:
                tasks.append(detect_text(loop, url, idx))
        idx += 1

    image_result = loop.run_until_complete(asyncio.gather(*tasks)) # 멀티 스레드 수행
    # image_result : (인덱스, 결과)

    for i in image_result:
        if 'detect_result' not in items[i[0]]:
            items[i[0]]['detect_result'] = [i[1]]
        else: items[i[0]]['detect_result'].append(i[1])



    # 모듈 수행
    result=[]
    content_list = []
    for item in items:

        # 이미지 확인 후 이미 광고로 판명이 났다면 모듈 수행x
        if ('detect_result' in item):
            if (True in item['detect_result']):
                result.append(100)
                continue

        # 텍스트 검사 후 이미 광고로 판명이 났다면 모듈 수행x
        if(item['check_text'] == True) :
            result.append(100)
            continue

        result.append(-1.0)
        content_list.append(item['content'])
        print("set model")

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
        items[i].pop('check_text')

        item = items[i]
        response[str(i)] = item

    end = time.time()

    print(response)

    print(f"\n총 걸린 시간은 {end - start} 초 입니다")
    # 30개 단위 기준 크롤링 3초, ai분석 35초정도 소요
    # 10개로 쪼갠 기준 첫 로드 11초 두번째 로드 5초정도 소요(이미지 응답 제외)
    # 이미지 응답포함시 1초 가량 증가

    return json.dumps(response)

if __name__ == '__main__':
    app.run(debug=True)