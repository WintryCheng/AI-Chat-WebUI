from nacos import NacosClient
import requests

# 初始化Nacos客户端
nacos_client = NacosClient('127.0.0.1:8848', namespace='public', username='nacos', password='nacos')

# 服务名称和分组名
service_name = 'model-service'
group_name = 'DEFAULT_GROUP'

# 从Nacos获取服务实例列表
instance_list = nacos_client.list_naming_instance(service_name=service_name, group_name=group_name)
print("instance_list: ", instance_list)

if instance_list:
    # 假设我们选择第一个可用的实例
    instance = instance_list[0]
    ip = instance['ip']
    port = instance['port']

    # 构造请求URL
    url = f'http://{ip}:{port}/get_aaa'

    try:
        # 发起GET请求
        response = requests.get(url)
        response.raise_for_status()  # 检查响应状态码
        print("Response:", response.text)  # 输出返回的内容
    except requests.exceptions.RequestException as e:
        print(f"An error occurred: {e}")
else:
    print("No available instances found.")