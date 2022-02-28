from flask import Flask
import time
import asyncio
import json

from .naver_crawling import naver_crwaling
from .detect_text import detect_text

app = Flask(__name__)

@app.route('/test')
def test():
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

    result = []
    for item in items:
        if ('detect_result' in item):
            if (True in item['detect_result']):
                result.append(0.0)
                continue

        result.append(runModel(item['content']))

    size = len(items)
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