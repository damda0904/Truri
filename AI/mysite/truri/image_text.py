import io
import os

from google.cloud import vision
from mysite.truri.checkText import checkText

# Set environment variable
os.environ["GOOGLE_APPLICATION_CREDENTIALS"] = "C:\DOWNLOAD\\automatic-opus-332907-8ec3f6a577b7.json"

async def detect_text(uri):
    client = vision.ImageAnnotatorClient()
    image = vision.Image()
    image.source.image_uri = uri

    response = client.text_detection(image=image)
    texts = response.text_annotations

    return checkText(texts[0].description)


