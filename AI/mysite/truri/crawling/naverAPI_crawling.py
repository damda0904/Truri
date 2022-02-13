import urllib.request
import json

#네이버 검색 API
client_id = "*"
client_secret = "*"

#본문을 가져오지 못해서 폐기

def naver_crawling(query, page) :

    print("naver_start!-------------------")
    start = (page - 1) * 30 + 1
    encText = urllib.parse.quote(query)
    url = "https://openapi.naver.com/v1/search/blog?query=" + encText + "&start=" + str(start) + "&display=30"
    request = urllib.request.Request(url)
    request.add_header("X-Naver-Client-Id", client_id)
    request.add_header("X-Naver-Client-Secret", client_secret)
    response = urllib.request.urlopen(request)
    rescode = response.getcode()
    if (rescode == 200):
        response_body = response.read()
        decoded = json.loads(response_body.decode('utf-8'))
        items = decoded['items']
        return items

    else:
        print("Error Code:" + rescode)