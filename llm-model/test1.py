from fastapi import FastAPI
from fastapi.responses import PlainTextResponse
from nacos import NacosClient

app = FastAPI()

# 初始化Nacos客户端
nacos_client = NacosClient('localhost:8848', namespace='public', username='nacos', password='nacos')

# 服务注册信息
service_name = 'fastapi-service'
group_name = 'DEFAULT_GROUP'
ip = 'localhost'  # 服务实例的IP地址
port = 8000  # 服务实例的端口


@app.on_event("startup")
async def startup_event():
    # 在应用启动时注册服务到Nacos
    nacos_client.add_naming_instance(service_name, ip, port, group_name=group_name)


@app.on_event("shutdown")
async def shutdown_event():
    # 在应用关闭时从Nacos中注销服务
    nacos_client.remove_naming_instance(service_name, ip, port, group_name=group_name)


@app.get("/get_aaa", response_class=PlainTextResponse)
async def get_aaa():
    return "aaa"


if __name__ == "__main__":
    import uvicorn

    uvicorn.run('test1:app', reload=True)
