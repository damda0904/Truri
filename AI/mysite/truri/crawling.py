import selenium
from selenium import webdriver
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions as EC

def crawling(query) :
    URL = 'https://search.naver.com/search.naver?query=' + query + '&nso=&where=blog&sm=tab_opt'

    #크롬드라이브 링크(경로에 한글 불가)
    driver = webdriver.Chrome(executable_path='C:\Truri\AI\chromedriver.exe')
    driver.get(url=URL)

    try:
        WebDriverWait(driver, 5).until(
            EC.presence_of_element_located((By.CLASS_NAME, 'thumb_single'))
        )

        #링크 크롤링
        result = driver.find_elements_by_class_name('thumb_single')


        #모든 본문을 저장할 리스트
        texts = []

        for element in result:
                driver.get(element.get_attribute("href"))  # 해당 url로 이동
                try:
                    mainFraime = driver.find_element_by_id('mainFrame')
                    driver.switch_to.frame('mainFrame')
                    # 본문크롤링
                    overlays = ".se-component.se-text.se-l-default"  # 내용 크롤링
                    contents = driver.find_elements_by_css_selector(overlays)
                    tmp = ""
                    for content in contents:
                        tmp = tmp + content.text
                        texts.append(tmp)
                    return texts
                    # for text in texts:
                    #     print(1)
                except:
                    print("crawling detail fail")

        driver.quit()
    except:
        print("error")


