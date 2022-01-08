from django.urls import path
from . import views

#장고 설치 : python -m pip install Django
#터미널 : python manage.py runserver
#링크 : http://localhost:8000/truri/search/검색어/
urlpatterns = [
    path('search/<query>/<page>', views.index, name='search'),
]