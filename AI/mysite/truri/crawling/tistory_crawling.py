import scrapy
from scrapy.crawler import CrawlerRunner
from scrapy.linkextractors import LinkExtractor
from scrapy.utils.log import configure_logging
from twisted.internet import reactor
import asyncio

# 검색 rl
url = []

# 크롤링된 데이터
result = []

#티스토리 spider
class TistorySpider(scrapy.Spider):
    name = 'tistory'
    custom_settings = {'DOWNLOD_DELAY': 1}

    def start_requests(self):
        start_url = url[0]

        yield scrapy.Request(url=start_url, callback=self.parse)


    def parse(self, response):
        #print("Tistory : response!")

        # 링크 크롤링
        extractor = LinkExtractor()
        link_data = extractor.extract_links(response)

        # 테스트용
        yield scrapy.Request(url="https://fn3995.tistory.com/134", callback=self.parse_detail)

        # for link in link_data:
        #     if "tistory.com/" in link.url:
        #         print(link)
        #         yield scrapy.Request(url=link.url, callback=self.parse_detail)

    def parse_detail(self, response):
        print("Tistory : parse_detail!")
        print(response.text)

        # 제목 크롤링
        title_data = response.css(".tit_post").xpath("./a/text()").get()
        print("title:")
        print(title_data)

        # 날짜 크롤링
        date_data = response.css(".text_detail").xpath('./ul/li[2]/span/text()').get()
        date_data = date_data.replace("\n", "")
        date_data = date_data.replace("\t", "")
        print(date_data)

        # 이미지 크롤링
        image = response.css(".jb-article").xpath('.//img/@src').getall()

        # 본문 크롤링
        content_data = response.css(".jb-article").xpath("string(./div)").getall()
        content_data[0] = content_data[0].replace("\n", "")
        content_data[0] = content_data[0].replace("\t", "")
        content_data[0] = content_data[0].replace("\xa0", "")
        content_data[0] = content_data[0].replace("\r", "")
        content_data[0] = content_data[0].replace('(adsbygoogle = window.adsbygoogle || []).push({});', "")

        #데이터 묶기
        item = {}
        item["link"] = response.url
        item["title"] = title_data
        item["date"] = date_data
        item["content"] = content_data

        # 이미지가 있을 경우/ 없을 경우
        if len(image) != 0:
            item['preview_image'] = image[0]

            item['last_images'] = [image[len(image) - 2], image[len(image) - 1]]
        else:
            item['preview_image'] = ""
            item['last_images'] = ["", ""]

        # 본문으로 프리뷰 만들기
        item['preview'] = content_data[0][:51]

        result.append(item)
        print(result)


def tistory_crawling(query, page) :

    print("tistory start!")

    Google_page = (int(page) - 1) * 10

    # 티스토리 검색결과 url(10개씩)
    url.append("https://www.google.com/search?q=" + "site:tistory.com " + query +
               "&newwindow=1&rlz=1C1AVFC_koKR881KR881&ei=uQwJYsqzFcOm1e8PgJOw4As&start=" + str(Google_page) +
               "&sa=N&ved=2ahUKEwiK9Jyz6fz1AhVDU_UHHYAJDLwQ8tMDegQIARA-&biw=823&bih=722&dpr=1.25")

    configure_logging({'LOG_FORMAT': '%(levelname)s: %(message)s'})
    runner = CrawlerRunner()
    runner.crawl(TistorySpider)
    crawler = runner.join()
    crawler.addBoth(lambda _: reactor.stop())
    reactor.run()  # the script will block here until the crawling is finished

    print("tistory fisish---------------------------------")

    print(result)

    return result
