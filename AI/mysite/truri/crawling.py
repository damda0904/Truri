from selenium import webdriver
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions as EC
import json

def crawling(query) :
    URL = 'https://search.naver.com/search.naver?query=' + query + '&nso=&where=blog&sm=tab_opt'

    # webdirver옵션에서 headless기능을 사용
    webdriver_options = webdriver.ChromeOptions()
    webdriver_options.add_argument('headless')

    #크롬드라이브 링크(경로에 한글 불가)
    driver = webdriver.Chrome(executable_path='C:\Truri\AI\chromedriver.exe', options=webdriver_options)
    driver.get(url=URL)

    try:

        WebDriverWait(driver, 5).until(
            EC.presence_of_element_located((By.CLASS_NAME, 'thumb_single'))
        )

        #링크 크롤링
        result = driver.find_elements(By.CLASS_NAME, 'thumb_single')
        date_object = driver.find_elements(By.CLASS_NAME, 'total_sub')
        preview_object = driver.find_elements(By.CLASS_NAME, 'total_group')

        # 모든 날짜를 저장할 리스트
        dates = []
        for object in date_object:
            dates.append(object.text.split("\n")[0])

        # 모든 프리뷰를 저장할 리스트
        preview = []
        for object in preview_object:
            preview.append(object.text)

        #모든 본문을 저장할 리스트
        texts = []

        #모든 링크를 저장할 리스트
        links = []


        for element in result:
            try:
                links.append(element.get_attribute("href"))
            except Exception as e:
                print("exception During link crawling")
                print(e)


        # 모든 링크 내 정보들을 크롤링해 저장할 리스트
        items = []
        idx = 0
        for link in links:

            if(idx > 1) : break

            print("----------------------")
            try:
                # 링크 저장
                item = {}
                item['link'] = link

                print(0)

                driver.get(link)  # 해당 url로 이동

                mainFrame = driver.find_element(By.ID, 'mainFrame')
                driver.switch_to.frame(mainFrame)

                print(1)

                #제목 크롤링
                title = driver.find_element(By.CLASS_NAME, "pcol1").text
                item['title'] = title

                print(2)

                #날짜 크롤링
                item['date'] = dates[idx]

                #프리뷰 크롤링
                item['preview'] = preview[idx]
                idx += 1

                print(3)

                # 본문크롤링
                overlays = ".se-component.se-text.se-l-default"  # 내용 크롤링
                contents = driver.find_elements(By.CSS_SELECTOR, overlays)
                text = ""
                for content in contents:
                    text = text + content.text

                print(4)

                # texts.append(text)
                item['content'] = text
                # print(item)
                # item_json = json.dumps(item, ensure_ascii=False)
                # #print(item_json)
                items.append(item)

                # for text in texts:
                #     print(1)
            except Exception as e:
                print("crawling detail fail")
                print(e)
                break


        return items
        driver.quit()
    except:
        print("error")


