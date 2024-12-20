# -*- coding: utf-8 -*-
import asyncio
from contextlib import asynccontextmanager

import uvicorn
from fastapi import FastAPI, Request
from fastapi.middleware.cors import CORSMiddleware
from nacos import NacosClient
from sse_starlette.sse import EventSourceResponse

# 初始化Nacos客户端
nacos_client = NacosClient('localhost:8848', namespace='public', username='nacos', password='nacos')

# 服务注册信息
service_name = 'fastapi-service'
group_name = 'DEFAULT_GROUP'
ip = 'localhost'  # 服务实例的IP地址
port = 8000  # 服务实例的端口


# fastapi启动时将服务注册到Nacos
@asynccontextmanager
async def lifespan(app: FastAPI):
    # 在应用启动时注册服务到Nacos
    nacos_client.add_naming_instance(service_name, ip, port, group_name=group_name)
    yield
    # 在应用关闭时从Nacos中注销服务
    nacos_client.remove_naming_instance(service_name, ip, port, group_name=group_name)


app = FastAPI(lifespan=lifespan)

# 跨域设置
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# 自定义响应数据
response_data = ["I", "am", "a", "handsome", "boy", "."]


@app.get('/stream', response_class=EventSourceResponse)
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
