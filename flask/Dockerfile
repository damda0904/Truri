FROM python:3.7-slim

COPY . /app

RUN pip3 install flask konlpy tensorflow scrapy

RUN pip install --upgrade google-cloud-vision

WORKDIR /app

CMD ["python3", "-m", "flask", "run", "--host=0.0.0.0"]