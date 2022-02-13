# Create your views here.

from django.http import HttpResponse
from .crawling.scrapy_crawling import scrapy_crawling
from .runModel import runModel
import time

def index(request, query, page):
    start = time.time()
    items = scrapy_crawling(query, page)

    #return HttpResponse(len(items))

    #본문 목록만 뽑기
    contents = []
    for item in items:
        contents.append(item['content'])

    score_result = runModel(contents)

    #print(result)

    response = ""

    idx = 0
    #score 추가
    for item in items:

        newItem = "{'link': '" +  item.get('link') + "', 'title': '" + item.get("title") + "', 'date': '" + item.get("date") + "', 'preview': '" + item.get('preview') + "', 'score': '" + str(score_result[idx]) + "'}"
        if(len(response) == 0) : response = newItem
        else: response = response + ", " + newItem
        idx += 1

    #items = "{'link':'http://', 'title': 'iphone', 'date': '21.10.10', 'preview':'test dummy data', 'score': 84.0}, {'link':'http://', 'title': 'iphone', 'date': '21.10.10', 'preview':'test dummy data', 'score': 37.0}, {'link':'http://', 'title': 'iphone', 'date': '21.10.10', 'preview':'test dummy data', 'score': 60.0}"
    #print(response)

    end = time.time()

    print(f"\n총 걸린 시간은 {end - start} 초 입니다")
    #현재 크롤링 3초, ai분석 47초정도 소요됨.

    return HttpResponse(response)


def test(request):
    return HttpResponse("test success")