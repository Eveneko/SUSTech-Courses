# encoding:utf-8
import asyncio
import os
import mimetypes
from urllib import parse

response = {
    # 200: [b'HTTP/1.0 200 OK\r\n',  # 正常的response
    #       b'Connection: close\r\n',
    #       b'Content-Type:text/html; charset=utf-8\r\n',
    #       b'\r\n'],
    404: [b'HTTP/1.0 404 Not Found\r\n',  # 请求文件不存在的response
          b'Connection: close\r\n',
          b'Content-Type:text/html; charset=utf-8\r\n',
          b'\r\n',
          b'<html><body>404 Not Found<body></html>\r\n',
          b'\r\n'],
    405: [b'HTTP/1.0 405 Method Not Allowed\r\n',  # 请求为GET/HEAD之外的request时的response
          b'Connection: close\r\n',
          b'Content-Type:text/html; charset=utf-8\r\n',
          b'\r\n',
          b'<html><body>405 Method Not Allowed<body></html>\r\n',
          b'\r\n'],
    416: [b'HTTP/1.0 416 Requested Range Not Satisfiable\r\n',  # Range Header error
          b'Connection: close\r\n',
          b'Content-Type:text/html; charset=utf-8\r\n',
          b'\r\n',
          b'<html><body>416 Requested Range Not Satisfiable<body></html>\r\n',
          b'\r\n']
}


# get mime by mimetypes.guess_type
def get_mime(path):
    mime = mimetypes.guess_type(path)[0]  # 返回文件类型，由浏览器决定怎么打开，或者下载
    if mime is None:  # 如果浏览器不支持打开，就下载
        mime = 'application/octet-stream'
    return mime


# seperate the raw cookie info to get the location
def get_cookie(raw_cookie):
    for content in raw_cookie:
        cookie = content.strip('\r\n').split(' ')
        for sub_cookie in cookie:
            if 'loc=' in sub_cookie:
                return sub_cookie.strip(';').replace('path=/', '')


async def dispatch(reader, writer):
    header = {}
    while True:
        data = await reader.readline()
        if data == b'\r\n':
            break
        if data == b'':
            break
        message = data.decode().split(' ')
        # seperate the header and store in the dictionary
        if message[0] == 'GET' or message[0] == 'HEAD':
            header['METHOD'] = message[0]
            header['PATH'] = message[1]
        if message[0] == 'Range:':
            header['RANGE'] = message[1]
        if message[0] == 'Cookie:':
            header['COOKIE'] = message
        if message[0] == 'Referer:':
            header['REFERER'] = message[1]
        if message[0] == 'Host:':
            header['HOST'] = message[1]

    """test start"""
    print('----------header')
    print(header)
    print('----------header')
    """test end"""
    # Handle the header
    r_head = []
    r = []
    if 'METHOD' not in header:
        # if the request is not GET or HEAD
        writer.writelines(response[405])
        await writer.drain()
        writer.close()
        return
    cookie = ''
    if 'COOKIE' in header:
        # get the location
        cookie = get_cookie(header['COOKIE'])
        """test start"""
        # print('----------cookie')
        # print(cookie)
        # print('----------cookie')
        """test end"""
    # set http status
    if 'RANGE' in header:
        r_head.append(b'HTTP/1.0 206 Partial Content\r\n')
    else:
        if header['PATH'] == '/' and 'REFERER' not in header and 'COOKIE' in header and \
                'loc=' in cookie and cookie != 'loc=/':
            r_head.append(b'HTTP/1.0 302 Found\r\n')
        else:
            r_head.append(b'HTTP/1.0 200 OK\r\n')
    # make the 302 header
    if header['PATH'] == '/' and 'REFERER' not in header and 'COOKIE' in header and \
            'loc=' in cookie and cookie != 'loc=/':
        cookie_loc = cookie[4:]
        header['HOST'] = header['HOST'].strip('\r\n')
        url = 'http://' + header['HOST'] + cookie_loc
        """test start"""
        print('----------url')
        print(url)
        print('----------url')
        """test end"""
        r_head.append('Location: {}\r\n\r\n'.format(url).encode('utf-8'))
        # set max-age for a day
        r_head.append('Cache-control: private; max-age={}\r\n\r\n'.format(86400).encode('utf-8'))
        print(r_head)
        writer.writelines(r_head)
        await writer.drain()
        writer.close()
        return

    # if header['PATH'] == 'favicon.ico':  # Chrome会多发一个这样的包，忽略
    #     pass
    # else:
    path = './' + header['PATH']
    try:  # url解码
        path = parse.unquote(path, errors='surrogatepass')
    except UnicodeDecodeError:
        path = parse.unquote(path)
    if os.path.isfile(path):  # 判断是否为文件
        file_size = int(os.path.getsize(path))
        start_index = 0
        end_index = file_size - 1
        length = file_size
        if 'RANGE' in header:
            # divide the piece of file
            start_index, end_index = header['RANGE'].strip('bytes=').split('-')
            # -
            if start_index == '' and end_index == '' or end_index == '\r\n':
                start_index, end_index = 0, file_size-1
            # x-
            elif end_index == '' or end_index == '\r\n':
                start_index, end_index = int(start_index), file_size-1
            # -x
            elif start_index == '':
                end_index = int(end_index)
                start_index = file_size - end_index
                end_index = file_size - 1
            # x-x
            start_index = int(start_index)
            end_index = int(end_index)
            length = end_index - start_index + 1
            if start_index < 0 or end_index >= file_size or start_index > end_index:
                writer.writelines(response[416])
                await writer.drain()
                writer.close()
                return
            r_head.append(
                'Content-Range: bytes {}-{}/{}\r\n'.format(start_index, end_index, file_size).encode('utf-8'))
        # guess the type
        mime = get_mime(path)
        r_head.append('Content-Type: {}\r\n'.format(mime).encode('utf-8'))
        r_head.append('Content-Length: {}\r\n'.format(length).encode('utf-8'))
        r_head.append(b'Connection: close\r\n')
        r_head.append(b'\r\n')
        writer.writelines(r_head)
        if header['METHOD'] == 'GET':
            file = open(path, 'rb')
            file.seek(start_index)
            writer.write(file.read(length))
            file.close()
    elif os.path.isdir(path):  # 判断是否为文件夹
        r_head.append(b'Connection: close\r\n')
        r_head.append(b'Content-Type:text/html; charset=utf-8\r\n')
        r_head.append('Set-Cookie: loc={};path=/\r\n'.format(header['PATH']).encode('utf-8'))
        r_head.append(b'\r\n')
        if header['METHOD'] == 'HEAD':
            writer.writelines(r_head)
        elif header['METHOD'] == 'GET':
            writer.writelines(r_head)
            file_list = os.listdir(path)  # 获取文件夹内文件名
            r.append(b'<html>')
            r.append(b'<head><title>Index of %s</title></head>' %
                     (path.encode('utf-8')))
            r.append(b'<body bgcolor="white">')
            r.append(b'<h1>Index of %s</h1><hr>' %
                     (path.encode('utf-8')))
            r.append(b'<ul>')
            if path != './':
                r.append(b'<li><a href=".."> ../ </a></li>')
            for name in file_list:
                if os.path.isdir(path + name + '/'):
                    name = name + '/'
                r.append(b'<li><a href="%s"> %s </a></li>' %
                         (name.encode('utf-8'), name.encode('utf-8')))
            r.append(b'</ul>')
            r.append(b'</body>')
            r.append(b'</html>')
            writer.writelines(r)
    else:
        writer.writelines(response[404])
    await writer.drain()
    writer.close()


if __name__ == '__main__':
    loop = asyncio.get_event_loop()  # 创建事件循环
    coro = asyncio.start_server(
        dispatch, '127.0.0.1', 8080, loop=loop)  # 开启一个新的协程
    server = loop.run_until_complete(coro)  # 将协程注册到事件循环
    # Serve requests until Ctrl+C is pressed
    print('Serving on {}'.format(server.sockets[0].getsockname()))
    try:
        loop.run_forever()
    except KeyboardInterrupt:
        pass
    # Close the server
    server.close()  # 关闭服务
    # 保持等待，直到数据流关闭。保持等待，直到底层连接被关闭，应该在close()后调用此方法。
    loop.run_until_complete(server.wait_closed())
    loop.close()
