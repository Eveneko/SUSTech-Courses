# encoding:utf-8
import asyncio
import os
import mimetypes
from urllib import parse

ok200 = [b'HTTP/1.0 200 OK\r\n',  # 正常的response
         b'Connection: close\r\n'
         b'Content-Type:text/html; charset=utf-8\r\n',
         b'\r\n']
err404 = [b'HTTP/1.0 404 Not Found\r\n',  # 请求文件不存在的response
          b'Connection: close\r\n'
          b'Content-Type:text/html; charset=utf-8\r\n',
          b'\r\n',
          b'<html><body>404 Not Found<body></html>\r\n',
          b'\r\n']
err405 = [b'HTTP/1.0 405 Method Not Allowed\r\n',  # 请求为GET/HEAD之外的request时的response
          b'Connection: close\r\n'
          b'Content-Type:text/html; charset=utf-8\r\n',
          b'\r\n',
          b'<html><body>405 Method Not Allowed<body></html>\r\n',
          b'\r\n']


async def dispatch(reader, writer):
    method = '' # 请求类型
    path = ''   # 请求内容
    while True:
        data = await reader.readline()
        message = data.decode().split(' ')
        print('message:%s' % message)
        if path == '' and len(message) >= 2:    # 记录请求信息
            path = message[1]
            method = message[0]
        if message == ['']:
            break
        if data == b'\r\n':
            break

    print('method:%s' % method)
    print('path:%s' % path)
    if path == 'favicon.ico':   # Chrome会多发一个这样的包，忽略
        pass
    else:
        if method != 'GET' and method != 'HEAD':    # 如果不是GET或者HEAD，返回405
            writer.writelines(err405)
        else:
            r = []  # HEAD
            path = '.' + path   # 必须在前面加'.'，否则错误
            try:    # url解码
                path = parse.unquote(path, errors='surrogatepass')
            except UnicodeDecodeError:
                path = parse.unquote(path)
            if os.path.isfile(path):    # 判断是否为文件
                r.append(b'HTTP/1.0 200 OK\r\n')
                r.append(b'Connection: close\r\n')
                size = os.path.getsize(path)    # 返回文件大小
                r.append(b'Content-Length: %s\r\n' % (str(size).encode('utf-8')))
                filetype = mimetypes.guess_type(path)[0]    # 返回文件类型，由浏览器决定怎么打开，或者下载
                if filetype is None:    # 如果浏览器不支持打开，就下载
                    filetype = 'application/octet-stream'
                r.append(b'Content-Type: %s; charset=utf-8\r\n' % (filetype.encode('utf-8')))
                r.append(b'\r\n')
                print(r)
                writer.writelines(r)
                file = open(path, 'rb')
                if method == 'GET': # 如果是GET，response文件内容
                    file_content = file.readlines()
                    writer.writelines(file_content)
            elif os.path.isdir(path):   # 判断是否为文件夹
                file_list = os.listdir(path)    # 获取文件夹内文件名
                if method == 'HEAD':
                    writer.writelines(ok200)
                elif method == 'GET':   # 如果是GET，返回文件夹内容
                    writer.writelines(ok200)
                    r.append(b'<html>')
                    r.append(b'<head><title>Index of %s</title></head>' % (path.encode('utf-8')))
                    r.append(b'<body bgcolor="white">')
                    r.append(b'<h1>Index of %s</h1><hr>' % (path.encode('utf-8')))
                    r.append(b'<ul>')
                    for name in file_list:
                        if os.path.isdir(path + name + '/'):
                            name = name + '/'
                        r.append(b'<li><a href="%s"> %s </a></li>' % (name.encode('utf-8'), name.encode('utf-8')))
                    r.append(b'</ul>')
                    r.append(b'</body>')
                    r.append(b'</html>')
                    writer.writelines(r)
            else:
                writer.writelines(err404)   # 请求未找到，返回404
    await writer.drain()
    writer.close()


if __name__ == '__main__':
    loop = asyncio.get_event_loop() # 创建事件循环
    coro = asyncio.start_server(dispatch, '127.0.0.1', 8080, loop=loop) # 开启一个新的协程
    server = loop.run_until_complete(coro)  # 将协程注册到事件循环
    # Serve requests until Ctrl+C is pressed
    print('Serving on {}'.format(server.sockets[0].getsockname()))
    try:
        loop.run_forever()
    except KeyboardInterrupt:
        pass
    # Close the server
    server.close()  # 关闭服务
    loop.run_until_complete(server.wait_closed())    # 保持等待，直到数据流关闭。保持等待，直到底层连接被关闭，应该在close()后调用此方法。
    loop.close()
