from selenium import webdriver
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions as EC
from time import sleep
import time

def crawling(query, page) :

    #시간 측정
    start = time.time()

    URL = 'https://search.naver.com/search.naver?query=' + query + '&nso=&where=blog&sm=tab_opt'

    # webdirver옵션에서 headless기능을 사용
    webdriver_options = webdriver.ChromeOptions()
    webdriver_options.add_argument('headless')

    #크롬드라이브 링크(경로에 한글 불가)
    driver = webdriver.Chrome(executable_path='C:\Truri\AI\chromedriver.exe', options=webdriver_options)
    driver.get(url=URL)

    try:

        #검색 결과가 나올 때까지 대기
        WebDriverWait(driver, 5).until(
            EC.presence_of_element_located((By.CLASS_NAME, 'thumb_single'))
        )


        #1페이지 이외라면 추가 스크롤 및 대기
        if page > 1 :
            for i in range(page - 1):
                try :
                    driver.execute_script("window.scrollTo(0, document.body.scrollHeight)")
                    sleep(1)
                    #그냥 물리적으로 1초 기다림
                    #implicitly wait - 화면이 이미 떴기 때문에 사용해봤자 기다리지 않고 바로 다음 코드 진행
                    #explicitly wait - n번째 요소가 떴는지 기다리는 코드는 없어서 사용하지 않음
                except Exception as e:
                    print("스크롤 에러")
                    print(e)


        # 1) 링크 크롤링
        result = driver.find_elements(By.CLASS_NAME, 'thumb_single')

        # 2) 날짜 및 프리뷰 저장
        date_object = driver.find_elements(By.CLASS_NAME, 'total_sub')
        preview_object = driver.find_elements(By.CLASS_NAME, 'total_group')


        #모든 링크를 저장할 리스트
        links = []

        # 모든 날짜를 저장할 리스트
        dates = []

        # 모든 프리뷰를 저장할 리스트
        preview = []

        for idx in range(len(result)):
            #가져오고자 하는 검색결과 범위만큼 크롤링
            #네이버는 한번에 30개씩 보여준다. 스크롤시 30개씩 추가되는 방식.
            if idx > ((page - 1) * 30 - 1) :

                element = result[idx]
                objectD = date_object[idx]
                objectP = preview_object[idx]

                try:
                    links.append(element.get_attribute("href"))
                    dates.append(objectD.text.split("\n")[0])
                    preview.append(objectP.text)
                except Exception as e:
                    print("exception During link crawling")
                    print(e)

        # print("\nlinks ----------------")
        # print(links)
        # print("\ndates ----------------")
        # print(dates)
        # print("\npreview ----------------")
        # print(preview)


        # 3) 모든 링크 내 정보들을 크롤링해 저장할 리스트
        items = []
        idx = 0
        for link in links:

            try:
                # 링크 저장
                item = {}
                item['link'] = link

                driver.get(link)  # 해당 url로 이동

                mainFrame = driver.find_element(By.ID, 'mainFrame')
                driver.switch_to.frame(mainFrame)

                #제목 크롤링
                title = driver.find_element(By.CLASS_NAME, "pcol1").text
                item['title'] = title

                #날짜 크롤링
                item['date'] = dates[idx]

                #프리뷰 크롤링
                item['preview'] = preview[idx]
                idx += 1

                # 본문크롤링
                overlays = ".se-component.se-text.se-l-default"  # 내용 크롤링
                contents = driver.find_elements(By.CSS_SELECTOR, overlays)
                text = ""
                for content in contents:
                    text = text + content.text

                item['content'] = text
                items.append(item)

            except Exception as e:
                print("crawling detail fail")
                print(e)
                break

        print("items--------------------")
        print(len(items))

        end = time.time()

        print(f"\n총 걸린 시간은 {end - start} 초 입니다")

        return items
        driver.quit()
    except:
        print("error")


