# -*- coding: utf-8 -*-
import random
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


@app.get('/stream')
async def stream(request: Request):
    def new_count():
        return random.randint(1, 100)

    async def event_generator():
        index = 0
        while True:
            index += 1
            if await request.is_disconnected():
                break
            # 测试取随机数据，每次取一个随机数
            if count := new_count():
                yield {'data': count}
                print(count)

            await asyncio.sleep(0.1)

    return EventSourceResponse(event_generator())


if __name__ == '__main__':
    uvicorn.run('fastapi_sse_num:app', reload=True)