# -*- coding: utf-8 -*-
import asyncio

import uvicorn
from fastapi import FastAPI, Request
from fastapi.middleware.cors import CORSMiddleware
from sse_starlette.sse import EventSourceResponse

app = FastAPI()
# 跨域设置，因为测试需要前端访问，所以允许所有域访问
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


response_data = ["I", "am", "a", "handsome", "boy", "."]

@app.get('/stream')
async def stream(request: Request):
    async def event_generator():
        for item in response_data:
            if await request.is_disconnected():
                break
            yield {'data': item.lower() + " "}
            print(item)
            await asyncio.sleep(0.25)

    return EventSourceResponse(event_generator())


if __name__ == '__main__':
    uvicorn.run('fastapi_sse:app', reload=True)