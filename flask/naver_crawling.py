import math
import os
import sys

import scrapy
from scrapy.crawler import CrawlerProcess
# 검색 url
url = []

## 네이버 용도
# 본문
content = []
# 프리뷰 이미지
preview_image = []
# 신뢰도 평가용 이미지(마지막 2장)
last_images = []

# 크롤링된 데이터
result = []

#검색할 페이지
search_page = []

#네이버 spider
class NaverSpider(scrapy.Spider):
    name = 'naver'
    custom_settings = {'DOWNLOD_DELAY': 10}

    def start_requests(self):
        start_url = url[0]

        yield scrapy.Request(url=start_url, callback=self.parse)


    def parse(self, response):

        items = []

        #링크 크롤링
        link_data = response.css('.api_txt_lines').xpath("@href").extract()

        #제목 크롤링
        title_data = response.css('.total_area').xpath("string(./a)").extract()

        #프리뷰 크롤링
        preview_data = response.css(".total_dsc").xpath("string(./div)").getall()

        #날짜 크롤링
        date_data = response.css(".sub_time::text").extract()

        #서치할 영역 정하기
        if (search_page[0]-1) % 3 == 0:
            start = 0
            end = 10
        elif (search_page[0]-1) % 3 == 1:
            start = 10
            end = 20
        else:
            start = 20
            end = 30

        #하나의 아이템으로 묶어 저장하기
        search_url = []
        for idx in range(start, end):         #기존 : for idx in range(0, 30)
            item = {}
            item["link"] = link_data[idx]
            item["title"] = title_data[idx]
            item["preview"] = preview_data[idx]
            item["date"] = date_data[idx]
            items.append(item)
            search_url.append(link_data[idx])

        # 테스트용
        # yield scrapy.Request(url=link_data[0], callback=self.parse_detail)

        # 기존 : for url in link_data
        for i in range(0, len(search_url)):
            url = search_url[i]
            item = items[i]
            yield scrapy.Request(url=url, callback=self.parse_detail, cb_kwargs=dict(item=item))

    def parse_detail(self, response, item):

        # iframe 링크 크롤링
        iframe = response.css('#mainFrame::attr(src)').extract()
        if(len(iframe) > 0) :
            yield scrapy.Request(url="https://blog.naver.com/" + iframe[0], callback=self.parse_iframe,
                                 cb_kwargs=dict(item=item))
        # iframe이 없으면 result에 저장
        else :
            item['content'] = ""
            item['preview_image'] = ""
            item['last_images'] = ["", ""]
            result.append(item)

    def parse_iframe(self, response, item):
        image = response.css(".se-image-resource").xpath("@src").extract();
        sticker = response.css(".se-sticker-image").xpath("@src").extract()

        # 이미지가 있을 경우/ 없을 경우
        if len(image) != 0:
            item['preview_image'] = image[0]
        else :
            item['preview_image'] = ""

        if len(sticker) != 0:
            item['last_images'] = [sticker[len(sticker) - 2], sticker[len(sticker) - 1]]
        else:
            item['last_images'] = ["", ""]

        # 본문 크롤링
        content_data = response.css(".se-main-container").xpath(".//span//text()").getall()
        one_content = ""
        for line in content_data:
            if line != "\u200b" :
                one_content = one_content + line + " "

        item['content'] = one_content
        result.append(item)

def naver_crawling(query, page):

    #naver_page = (int(page) - 1) * 30 + 1
    naver_page = math.trunc((((int(page)+2)/3) -1) * 30 + 1)

    search_page.append(int(page))

    # 네이버 블로그 검색결과 url
    url.append(
        "https://s.search.naver.com/p/blog/search.naver?where=blog&sm=tab_pge&api_type=0&query=" + query + "&rev=44&start=" + str(
            naver_page) +
        "&dup_remove=1&post_blogurl=&post_blogurl_without=&nso=&nlu_query=%7B%22r_category%22%3A%2229%22%7D&dkey=0&source_query=&nx_search_query="
        + query + "&spq=0&_callback=viewMoreContents")

    # configure_logging({'LOG_FORMAT': '%(levelname)s: %(message)s'})
    # runner = CrawlerRunner()
    # runner.crawl(NaverSpider)
    # crawler = runner.join()
    # crawler.addBoth(lambda _: reactor.stop())
    # reactor.run()  # the script will block here until the crawling is finished

    process = CrawlerProcess()
    process.crawl(NaverSpider)
    process.start()

    return result

if __name__ == '__main__':
    result = naver_crawling(sys.argv[1], sys.argv[2])

    for item in result:
        print(item)
