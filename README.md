# AI-Chat-WebUI 项目简介

部署一个聊天前端页面，在后端调用本地部署的通义千问 AI 大模型进行问答

## 文件夹介绍

- llm-backend：Java 后端，接受前端请求，向模型端发送请求，将模型响应发送到前端
- llm-frontend：Vue 前端，用于和用户进行交互
- llm-model：模型端，部署大模型回答用户提问，使用 FastAPI 和 Java 后端交互

## 下载依赖

- 前端依赖：`npm install`
- 模型文件：`python chat.py`

## 启动项目

- 前端启动：`npm run dev`
- 后端启动：`直接运行 springboot 主类`
- vLLM 启动：`python -m vllm.entrypoints.openai.api_server --model /home/chengwch/Code/llm_project/llm-model/models/Qwen/Qwen2-0___5B-Instruct --port 8082`
- fastAPI 启动：`python fastapi_sse_localmodel.py`（如果本地无法启动大模型，可以使用 `python fastapi_sse.py`或`python fastapi_sse_num.py` 代替，作为模拟输出）

## 注意

vLLM 只能在 Linux 上面运行，最低支持 cuda 版本为 11.8，推荐使用 cuda12.1，参考：[https://docs.vllm.ai/en/latest/getting_started/installation.html](https://docs.vllm.ai/en/latest/getting_started/installation.html)
