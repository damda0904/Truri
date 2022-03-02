import io
import os

from google.cloud import vision
from check_text import check_text

# Set environment variable
os.environ["GOOGLE_APPLICATION_CREDENTIALS"] = "C:\DOWNLOAD\\automatic-opus-332907-8ec3f6a577b7.json"

async def detect_text(loop, uri, num):
    print("detecting-------------------------------------")
    client = vision.ImageAnnotatorClient()
    image = vision.Image()
    image.source.image_uri = uri

    response = await loop.run_in_executor(None, client.text_detection, image)
    texts = response.text_annotations
    #print(len(texts))
    if len(texts) > 0 : result = check_text(texts[0].description)
    else : result = False

    return (num, result)


