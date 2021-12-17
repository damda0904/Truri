from django.shortcuts import render

# Create your views here.

from django.http import HttpResponse
from . import crawling
import json

from .runModel import runModel


def index(request, query):
    items = crawling.crawling(query)

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
    print(response)

    return HttpResponse(response)
