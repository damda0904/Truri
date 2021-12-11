from django.shortcuts import render

# Create your views here.

from django.http import HttpResponse
from . import crawling
from . import runModel

def index(request, query):
    contents = crawling.crawling(query)
    result = runModel.runModel(contents)

    print(result)

    return HttpResponse("Hello World! This is Django test")
