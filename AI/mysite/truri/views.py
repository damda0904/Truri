from django.shortcuts import render

# Create your views here.

from django.http import HttpResponse
from . import crawling

def index(request, query):
    texts = crawling.crawling(query)
    for text in texts:
        print("test")

    return HttpResponse("Hello World! This is Django test")
