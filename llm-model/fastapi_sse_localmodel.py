# -*- coding: utf-8 -*-
import asyncio

import uvicorn
from fastapi import FastAPI, Request
from fastapi.middleware.cors import CORSMiddleware
from sse_starlette.sse import EventSourceResponse
from openai import OpenAI


app = FastAPI()
# 跨域设置，因为测试需要前端访问，所以允许所有域访问
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


# Set OpenAI's API key and API base to use vLLM's API server.
openai_api_key = "EMPTY"
openai_api_base = "http://localhost:8082/v1"

client = OpenAI(
    api_key=openai_api_key,
    base_url=openai_api_base,
)

# @app.get('/stream')
# # async def stream(request: Request):
# async def stream(prompt: str=""):
#     print("模型提示词为: ", prompt)
#     chat_response = client.chat.completions.create(
#         # model="Qwen/Qwen2-0.5B-Instruct",
#         # model="Qwen/Qwen2-0___5B-Instruct",
#         model="/home/chengwch/Code/llm_project/llm-model/models/Qwen/Qwen2-0___5B-Instruct",
#         messages=[

#             {"role": "system", "content": "You are a helpful assistant."},
#             # {"role": "user", "content": "Tell me something about large language models."},
#             {"role": "user", "content": prompt},

#         ],
#         temperature=0.7,
#         top_p=0.8,
#         max_tokens=512,
#         extra_body={
#             "repetition_penalty": 1.05,
#         },
#         stream=True,
#     )
#     return EventSourceResponse(chat_response)


# async def stream(request: Request):
def generage_response(prompt: str=""):
    print("模型提示词为: ", prompt)
    chat_response = client.chat.completions.create(
        # model="Qwen/Qwen2-0.5B-Instruct",
        # model="Qwen/Qwen2-0___5B-Instruct",
        model="/home/chengwch/Code/llm_project/llm-model/models/Qwen/Qwen2-0___5B-Instruct",
        messages=[

            {"role": "system", "content": "You are a helpful assistant."},
            # {"role": "user", "content": "Tell me something about large language models."},
            {"role": "user", "content": prompt},

        ],
        temperature=0.7,
        top_p=0.8,
        max_tokens=512,
        extra_body={
            "repetition_penalty": 1.05,
        },
        stream=True,
    )
    return chat_response
def data_generator(response):
    for item in response:
        yield item.choices[0].delta.content
    print("response: ", response)

@app.get('/stream')
async def stream(prompt: str=""):
    return EventSourceResponse(data_generator(generage_response(prompt)))

if __name__ == '__main__':
    uvicorn.run('fastapi_sse_localmodel:app', reload=True)