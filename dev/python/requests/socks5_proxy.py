import requests

url = "https://www.baidu.com"
headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36'
}

# 定义代理(******:****** -> 用户名:密码)
proxies = {
    "http": "socks5://proxyUserName:proxyPassword@127.0.0.1:8080",
    "https": "socks5://proxyUserName:proxyPassword@127.0.0.1:8080",
}

# 使用代理发送请求
res = requests.get(url, proxies=proxies, headers=headers)
print(res.text)
