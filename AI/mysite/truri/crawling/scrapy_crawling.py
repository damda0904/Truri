import scrapy
from scrapy.crawler import CrawlerRunner
from scrapy.linkextractors import LinkExtractor
from scrapy.utils.log import configure_logging
from twisted.internet import reactor

###구글은 프리뷰를 가져오지 못하는 문제 발생

#네이버 spider
class NaverSpider(scrapy.Spider):
    name = 'naver'
    custom_settings = {'DOWNLOD_DELAY': 10}

    def start_requests(self):
        start_url = url[0]

        yield scrapy.Request(url=start_url, callback=self.parse)


    def parse(self, response):
        #print("Naver : response!")

        #링크 크롤링
        link_data = response.css('.api_txt_lines').xpath("@href").extract()

        #제목 크롤링
        title_data = response.css('.total_area').xpath("string(./a)").extract()
        # print(title_data[0])

        #프리뷰 크롤링
        preview_data = response.css(".total_dsc").xpath("string(./div)").getall()
        # print(preview_data[0])

        #날짜 크롤링
        date_data = response.css(".sub_time::text").extract()
        # print(date_data[0])

        for idx in range(0, 30):
            item = {}
            item["link"] = link_data[idx]
            item["title"] = title_data[idx]
            item["preview"] = preview_data[idx]
            item["date"] = date_data[idx]

            result_naver.append(item)


        # 테스트용
        # yield scrapy.Request(url=link_data[0], callback=self.parse_detail)

        for url in link_data:
            yield scrapy.Request(url=url, callback=self.parse_detail)

    def parse_detail(self, response):
        #print("Naver : parse_detail!")

        iframe = response.css('#mainFrame::attr(src)').extract()
        yield scrapy.Request(url="https://blog.naver.com/" + iframe[0], callback=self.parse_iframe)

    def parse_iframe(self, response):
        #print("Blog : in iframe")
        image = response.css(".se-image-resource").xpath("@src").extract()

        # 이미지가 있을 경우/ 없을 경우
        if len(image) != 0:
            preview_image.append(image[0])

            last_images.append([image[len(image) - 2], image[len(image) - 1]])
        else :
            preview_image.append("")
            last_images.append("")
            last_images.append("")

        # 본문 크롤링
        content_data = response.css(".se-main-container").xpath(".//span//text()").getall()
        one_content = ""
        for line in content_data:
            if line != "\u200b" :
                one_content = one_content + line + " "
        content.append(one_content)


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

        # 프리뷰 크롤링
        # preview_data = response.css(".total_dsc").xpath("string(./div)").getall()
        # # print(preview_data[0])

        # 테스트용
        # yield scrapy.Request(url=link_data[0], callback=self.parse_detail)

        for link in link_data:
            if "tistory" in link.url:
                yield scrapy.Request(url=link.url, callback=self.parse_detail)

    def parse_detail(self, response):
        #print("Tistory : parse_detail!")

        # 제목 크롤링
        title_data = response.css(".jb-content-title").xpath('./h2/a/text()').get()
        # print(title_data)

        #날짜 크롤링
        date_data = response.css(".jb-article-information").xpath('./ul/li[2]/span/text()').get()
        date_data = date_data.replace("\n", "")
        date_data = date_data.replace("\t", "")
        #print(date_data)

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



        result_t.append(item)

# 검색 rl
url = []

## 네이버 용도
# 본문
content = []
# 프리뷰 이미지
preview_image = []
# 신뢰도 평가용 이미지(마지막 2장)
last_images = []

# 크롤링된 데이터(네이버)
result_naver = []
# 크롤링된 데이터(티스토리)
result_t = []

def scrapy_crawling(query, page) :
    #print("scrapy start---------------------")

    naver_page = (int(page) - 1) * 30 + 1

    # 네이버 블로그 검색결과 url
    url.append("https://s.search.naver.com/p/blog/search.naver?where=blog&sm=tab_pge&api_type=0&query=" + query +"&rev=44&start=" + str(naver_page) +
               "&dup_remove=1&post_blogurl=&post_blogurl_without=&nso=&nlu_query=%7B%22r_category%22%3A%2229%22%7D&dkey=0&source_query=&nx_search_query="
               + query + "&spq=0&_callback=viewMoreContents")

    Google_page = (int(page) - 1) * 10

    # 티스토리 검색결과 url(10개씩)
    url.append("https://www.google.com/search?q=" + "site:tistory.com " + query +
               "&newwindow=1&rlz=1C1AVFC_koKR881KR881&ei=uQwJYsqzFcOm1e8PgJOw4As&start=" + str(Google_page) +
               "&sa=N&ved=2ahUKEwiK9Jyz6fz1AhVDU_UHHYAJDLwQ8tMDegQIARA-&biw=823&bih=722&dpr=1.25")

    configure_logging({'LOG_FORMAT': '%(levelname)s: %(message)s'})
    runner = CrawlerRunner()
    runner.crawl(NaverSpider)
    runner.crawl(TistorySpider)
    crawler = runner.join()
    crawler.addBoth(lambda _: reactor.stop())
    reactor.run()  # the script will block here until the crawling is finished

    # print(len(content))
    #
    # 네이버 아이템 별로 데이터 정리
    for idx in range(0, 30):
        item = result_naver[idx]

        item["content"] = content[idx]
        item["preview_image"] = preview_image[idx]
        item["last_images"] = last_images[idx]

        result_naver[idx] = item

    result = result_naver + result_t

    return result



